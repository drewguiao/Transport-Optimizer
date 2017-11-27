package model;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import utility.Constants;

public class Optimizer extends Constants{
	private double[][] tableau;
	private double[] objectiveFunction;
	private double[] basicSolution;
	private int numOfRows, numOfColumns, numOfVariables,answerColumnIndex,numOfConstraints;
	private Scanner console = new Scanner(System.in);
	private int optimizationStatus;
	
	public Optimizer(int numOfConstraints, int numOfVariables){
		this.numOfRows = numOfConstraints + OBJECTIVE_FUNCTION_ROW;
		this.numOfColumns = numOfConstraints + numOfVariables + Z_COLUMN + ANSWER_COLUMN;
		this.numOfVariables = numOfVariables;
		this.answerColumnIndex = numOfColumns - 1;
		this.numOfConstraints = numOfConstraints;
		this.tableau = new double[numOfRows][numOfColumns];
		

	}
	private void setOptimization() {
		// TODO Auto-generated method stub
		int choice = 0;
		System.out.println("[1] Minimize");
		System.out.println("[2] Maximize");
		System.out.print("Choice: ");
		choice = console.nextInt();
		switch(choice){
		case MINIMIZE:  this.optimizationStatus = MINIMIZE;
		
			break; 
		case MAXIMIZE: this.optimizationStatus = MAXIMIZE;
			break;
		}
	}
	void updateBasicSolution() {
		basicSolution = new double[numOfColumns];
		// TODO Auto-generated method stub
		for(int i = 0; i < numOfColumns-1 ; i++){
			if(isColumnBasic(i)){
				for(int j=0;j<numOfRows;j++){
					if(tableau[j][i] == 1.00){
						basicSolution[i] = tableau[j][numOfColumns-1];
					}
				}
			}
		}
		
	}
	private String viewBasicSolution() {
		// TODO Auto-generated method stub

		
		String retVal = "Basic Solution: ";
		
		
		for(int i=0;i< numOfVariables;i++){
			retVal+= "x["+i+"] ";
		}
		
		for(int i=0;i<numOfConstraints;i++){
			retVal+= "s["+i+"] ";
		}
		retVal += "z \n";
		for(int i=0;i<basicSolution.length-1;i++){
			retVal += +basicSolution[i]+" ";
		}
		retVal += "\n";
		return retVal;
	}
	private boolean isColumnBasic(int columnIndex) {
		// TODO Auto-generated method stub
		Set<Double> rowValues = new HashSet<>();
		for(int i = 0 ; i < numOfRows ; i++){
			rowValues.add(tableau[i][columnIndex]);
		}
		if(rowValues.size() == 2 && (rowValues.contains(Double.valueOf(0)) && rowValues.contains(Double.valueOf(1)))){
			
			return true;
		}
		return false;
	}
	
	void optimize() {
		String status = "";
		if(optimizationStatus == MAXIMIZE){
			status += "Maximize";
		}
		else{
			status+= "Minimize";
		}
		JFrame optimizedTableauFrame = new JFrame(status);
		JPanel tableauPanel = new JPanel();
		JTextArea tableauMatrix= new JTextArea();
		Dimension d = new Dimension(700, 700);
		tableauMatrix.setSize(d);
		tableauMatrix.setEditable(false);
		JScrollPane scroll = new JScrollPane(tableauMatrix);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// TODO Auto-generated method stub
		tableauMatrix.append(viewBasicSolution());
		tableauMatrix.append(viewTableau());
		while(bottomRowHasNegative(tableau)){
			int pivotColumnIndex = getNegativeWithHighestMagnitude(tableau);
			int pivotElementIndex = getLowestTestRatio(tableau,pivotColumnIndex);
			normalizePivotElementRow(tableau,pivotElementIndex,pivotColumnIndex);
			applyGaussJordan(tableau,pivotElementIndex,pivotColumnIndex);
			updateBasicSolution();
			tableauMatrix.append(viewBasicSolution());
			tableauMatrix.append(viewTableau());
		}
		tableauMatrix.append(viewAnswers());
		tableauPanel.add(tableauMatrix);
		tableauPanel.add(scroll);
		optimizedTableauFrame.add(tableauPanel);
		optimizedTableauFrame.setSize(800, 800);
		optimizedTableauFrame.setVisible(true);
		optimizedTableauFrame.pack();
		optimizedTableauFrame.validate();
	}
	
	private String viewAnswers() {
		// TODO Auto-generated method stub
		String retVal = "";
		for(int i = 0; i < basicSolution.length-1;i++){
			for(int j = 0; j < numOfVariables;j++){
				retVal+= "x["+i+"] = "+basicSolution[i]+ "\n";
				i = i+ 1;
			}
			for(int j = 0; j < numOfConstraints;j++){
				retVal+= "s["+i+"] = "+basicSolution[i]+ "\n";
				i = i+1;
			}
			retVal+= "z = "+basicSolution[i]; i=i+1;
		}
		
		return retVal;
	}
	String viewTableau() {
		// TODO Auto-generated method stub
		String retVal = "==\n";
		for(int i=0;i<numOfRows;i++){
			for(int j = 0;j<numOfColumns;j++){
				retVal+= tableau[i][j]+" ";
			}
			retVal+="\n";
		}
		return retVal;
		
	}
	
	
	private void applyGaussJordan(double[][] tableau, int pivotElementIndex, int pivotColumnIndex) {
		// TODO Auto-generated method stub
		for(int i = 0; i < numOfRows;i++){
			if(i != pivotElementIndex){
				double valueToZero = tableau[i][pivotColumnIndex];
				for(int j = pivotColumnIndex; j < numOfColumns;j++){
					double pivotRowElement = tableau[pivotElementIndex][j];
					tableau[i][j] = tableau[i][j] - (valueToZero * pivotRowElement);
					
				}
			}
		}
		
		
	}
	private void normalizePivotElementRow(double[][] mat, int pivotElementIndex, int pivotColumnIndex) {
		// TODO Auto-generated method stub
		double pivotElement = mat[pivotElementIndex][pivotColumnIndex];
		for(int i = 0; i < numOfColumns;i++){
			mat[pivotElementIndex][i] = mat[pivotElementIndex][i] / pivotElement;
		}
		
	}
	private int getLowestTestRatio(double[][] tableau, int pivotColumnIndex) {
		// TODO Auto-generated method stub
		List<BigDecimal> testRatioList = new ArrayList<>();
		Map<BigDecimal,Integer> ratioIndexMap = new HashMap<>();
		int answerColumnIndex = numOfColumns - 1;
		for(int i = 0 ; i < numOfRows; i++){
			if(tableau[i][pivotColumnIndex] > 0 ){
				BigDecimal testRatio = BigDecimal.valueOf(tableau[i][answerColumnIndex]/tableau[i][pivotColumnIndex]);
				testRatioList.add(testRatio);
				ratioIndexMap.put(testRatio, i);
			}
		}
		Collections.sort(testRatioList);
		
		return 	ratioIndexMap.get(testRatioList.get(0));
		
	}
	private int getNegativeWithHighestMagnitude(double[][] tableau) {
		// TODO Auto-generated method stub
		List<BigDecimal> negativeList = new ArrayList<>();
		for(int i = 0; i < numOfColumns - 1; i++){
			if(tableau[numOfRows-1][i] < 0){
				negativeList.add(BigDecimal.valueOf(tableau[numOfRows-1][i]));
			}
		}
		Collections.sort(negativeList);
		
		for(int i=0; i < numOfColumns - 1; i++){
			if(negativeList.get(0).doubleValue() == tableau[numOfRows-1][i]){
				return i;
			}
		}
		return 0;
	}
	private boolean bottomRowHasNegative(double[][] tableau2) {
		// TODO Auto-generated method stub
		for(int i=0; i < numOfColumns; i++){
			if(tableau[numOfRows-1][i] < 0){
				return true;
			}
		}
		return false;
	}
	
	private void setUpObjectiveFunction() {
		// TODO Auto-generated method stub
		this.objectiveFunction = new double[numOfVariables];
		System.out.println("SET-UP OBJECTIVE FUNCTION: ");
		for(int i = 0; i < numOfVariables;i++){
			System.out.print("Enter coefficient for X["+i+"]: ");
			double coefficient = console.nextDouble();
			objectiveFunction[i] = coefficient;
		}
		
		viewObjectiveFunction();
	}
	private void viewObjectiveFunction() {
		// TODO Auto-generated method stub
		List<String> objectiveFunctionCoeff = new ArrayList<String>();
		for(int i=0;i< numOfVariables;i++){
			objectiveFunctionCoeff.add(objectiveFunction[i]+"x["+i+"] ");
		}
		String objectiveFunction = String.join("+ ", objectiveFunctionCoeff);
		objectiveFunction+= Z_VARIABLE;
		System.out.println(objectiveFunction);
		
	}
	
	private void setUpOptimizer(){
		for(int i = 0; i < numOfRows -1; i++){
			for(int j = 0; j < numOfVariables;j++){
				System.out.print("Enter coefficient for S["+i+"]D["+j+"]: ");
				double coefficient = console.nextDouble();
				setValue(coefficient,i,j);
			}
			System.out.print("Enter value for Answer["+i+"]: ");
			double answer = console.nextDouble();
			setValue(answer,i,answerColumnIndex);
		}			
	}

	
	public void setValue(double value, int i, int j){
		this.tableau[i][j] = value;
	}
	
	void appendSlackVariables(){
		for(int i = 0; i < numOfRows ; i++){
			for(int j = numOfVariables; j < numOfColumns - (Z_COLUMN + ANSWER_COLUMN); j++){
				if(i+numOfVariables == j){
					tableau[i][j] = SLACK_VARIABLE_COEFFICIENT;
				}
			}
		}
	}

	void appendObjectiveFunctionCoeff() {
		// TODO Auto-generated method stub

		for(int i = 0; i < numOfVariables; i++){
			if(optimizationStatus == MINIMIZE){

				tableau[numOfRows-1][i] = objectiveFunction[i];
			}else{

				tableau[numOfRows-1][i] = (objectiveFunction[i]* -1);	
			}
			
		}
		tableau[numOfRows-1][numOfColumns-2] = Z_COEFFICIENT;

		
	}
	public void getObjFuncCoeff(List<JTextField> objFunctionCoeff) {
		// TODO Auto-generated method stub
		
		// TODO Auto-generated method stub
		this.objectiveFunction = new double[numOfVariables];
		
		for(int i=0;i<objFunctionCoeff.size();i++){
			objectiveFunction[i] = Double.parseDouble(objFunctionCoeff.get(i).getText());
		}
		
		viewObjectiveFunction();
		
	}
	public void getConstCoeff(List<JTextField> constCoeffList) {
		// TODO Auto-generated method stub
		int k = 0;
		for(int i = 0; i < numOfRows -1; i++){
			for(int j = 0; j < numOfVariables;j++){
				setValue(Double.parseDouble(constCoeffList.get(k).getText()),i,j);
				k++;
			}
			setValue(Double.parseDouble(constCoeffList.get(k).getText()),i,answerColumnIndex);
			k++;
		}	
	}
	public void getMode(int mode) {
		// TODO Auto-generated method stub
		switch(mode){
		case MAXIMIZE: this.optimizationStatus = MAXIMIZE; break;
		case MINIMIZE: this.optimizationStatus = MINIMIZE; break;
		}
		
	}
}
