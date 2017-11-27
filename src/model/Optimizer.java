package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import utility.Constants;

public class Optimizer extends Constants{
	private double[][] tableau;
	private double[] objectiveFunction;
	private double[] basicSolution;
	private int numOfRows, numOfColumns, numOfVariables,answerColumnIndex;
	private Scanner console = new Scanner(System.in);
	
	public Optimizer(int numOfConstraints, int numOfVariables){
		this.numOfRows = numOfConstraints + OBJECTIVE_FUNCTION_ROW;
		this.numOfColumns = numOfConstraints + numOfVariables + Z_COLUMN + ANSWER_COLUMN;
		this.numOfVariables = numOfVariables;
		this.answerColumnIndex = numOfColumns - 1;
		
		this.tableau = new double[numOfRows][numOfColumns];
		
		this.setUpObjectiveFunction();
		this.setUpOptimizer();
		this.appendSlackVariables();
		this.appendObjectiveFunctionCoeff();
		this.viewTableau();
		this.updateBasicSolution();
		this.optimize();
	}
	private void updateBasicSolution() {
		basicSolution = new double[numOfColumns];
		// TODO Auto-generated method stub
		for(int i = 0; i < numOfColumns ; i++){
			if(isColumnBasic(i)){
				for(int j=0;j<numOfRows;j++){
					if(tableau[j][i] == 1.00){
						basicSolution[i] = tableau[j][numOfColumns-1];
					}
				}
			}
		}
		viewBasicSolution();
		
	}
	private void viewBasicSolution() {
		// TODO Auto-generated method stub
		System.out.print("Basic Solution: ");
		for(int i=0;i<basicSolution.length;i++){
			System.out.print(+basicSolution[i]+" ");
		}
		System.out.println("");
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
	
	private void optimize() {
		// TODO Auto-generated method stub
		while(bottomRowHasNegative(tableau)){
			int pivotColumnIndex = getNegativeWithHighestMagnitude(tableau);
			int pivotElementIndex = getLowestTestRatio(tableau,pivotColumnIndex);
			normalizePivotElementRow(tableau,pivotElementIndex,pivotColumnIndex);
			applyGaussJordan(tableau,pivotElementIndex,pivotColumnIndex);
			updateBasicSolution();
			viewTableau();
		}
		
	}
	
	private void viewTableau() {
		// TODO Auto-generated method stub
		for(int i=0;i<numOfRows;i++){
			for(int j = 0;j<numOfColumns;j++){
				System.out.print(tableau[i][j]+" ");
			}
			System.out.println("");
		}
		
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
		for(int i = 0; i < numOfRows-1;i++){
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
	
	private void appendSlackVariables(){
		for(int i = 0; i < numOfRows ; i++){
			for(int j = numOfVariables; j < numOfColumns - (Z_COLUMN + ANSWER_COLUMN); j++){
				if(i+numOfVariables == j){
					tableau[i][j] = SLACK_VARIABLE_COEFFICIENT;
				}
			}
		}
	}

	private void appendObjectiveFunctionCoeff() {
		// TODO Auto-generated method stub
		for(int i = 0; i < numOfVariables; i++){
			tableau[numOfRows-1][i] = -objectiveFunction[i];
		}
		tableau[numOfRows-1][numOfColumns-2] = Z_COEFFICIENT;
	}
}
