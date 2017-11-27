package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import utility.Constants;

public class SimplexMatrix extends Constants{
	private int length, row, column;
	private int initialTableauRow;
	private int initialTableauColumn;
	private double[][] matrix;
	private double[][] initialTableau;
	private double[] capacity, demand;
	private double[] objectiveFunction;
	
	
	public SimplexMatrix(int numOfSources, int numOfDestinations) {
		this.matrix = new double[numOfSources][numOfDestinations];
		this.length = numOfSources * numOfDestinations;
		this.row = numOfSources;
		this.column = numOfDestinations;
		this.capacity = new double[row];
		this.demand = new double[column];
	}
	
	public int getLength(){
		return this.length;
	}
	
	public double[][] getMatrix(){
		return this.matrix;
	}
	
	public int getRow(){
		return this.row;
	}
	
	public int getColumn(){
		return this.column;
	}
	
	public double[] getCapacity(){
		return this.capacity;
	}
	
	public double[] getDemand(){
		return this.demand;
	}
	
	public void setCost(double cost, int i, int j){
		this.matrix[i][j] = cost;
	}
	
	public void setCapacity(double capacity, int i){
		this.capacity[i] = capacity;
	};
	
	public void setDemand(double demand, int i){
		this.demand[i] = demand;
	}
	
	public void setObjectiveFunctionCoefficients(double value, int index){
		this.objectiveFunction[index] = value;
	}
	
	public void viewObjectiveFunction(){
		List<String> listOfVariables = new ArrayList<String>();
		for(int i = 0; i < objectiveFunction.length;i++){
			listOfVariables.add(""+objectiveFunction[i]+"x["+i+"] ");
		}
		
		 System.out.println(joinVariables(listOfVariables));
	}
	
	private String joinVariables(List<String> listOfVariables) {
		// TODO Auto-generated method stub
		String tokensWithPlusSign = String.join("+ ", listOfVariables);
		tokensWithPlusSign+= Z_VARIABLE;
		return tokensWithPlusSign;
	}

	public double getCapacityTotalOnRow(int index){
		double total = 0;
		for(int i = 0; i < index; i++){
			total = total + matrix[index][i];
		}
		return total;
	}
	
	public double getDemandTotalOnColumn(int index){
		double total = 0;
		for(int i = 0; i < index; i++){
			total = total + matrix[i][index];
		}
		return total;
	}
	
	public void setUpInitialTableau(){
		int numOfSlackColumns = row + column;
		int numOfDemandColumns = row * column;
		initialTableauRow = row + column + OBJECTIVE_FUNCTION_ROW;
		initialTableauColumn = column + numOfDemandColumns + numOfSlackColumns + Z_COLUMN + ANSWER_COLUMN;
		initialTableau = new double[initialTableauRow][initialTableauColumn];
		
		int answerColumnIndex = initialTableauColumn - 1;
		int zColumnIndex = answerColumnIndex - 1;
		
		//appendCostConstraints
		for(int i = 0 ; i < row;i++){
			for(int j = 0 ; j < column ; j++){
				initialTableau[i][j] = matrix[i][j];
			}
		}
		//append Cost total
		for(int i = 0 ; i < capacity.length ; i++){
			initialTableau[i][answerColumnIndex] = capacity[i];
		}
		
		//appendDemandConstraints
		int displacement = 0;
		for(int i = row; i < column + row ; i++){
			
			for(int j = column ; j < numOfDemandColumns ; j++){
				initialTableau[i][j+displacement] = 1;
			}
			displacement = displacement + (row*column)/column;
		}
		//append demand total
		for(int i = row ; i < demand.length + row; i++){
			initialTableau[i][answerColumnIndex] = demand[i-row];
		}
		//appendObjectiveFunction
		for(int i = 0; i < objectiveFunction.length; i++){
			initialTableau[row + column][i] = -objectiveFunction[i];
		}
		initialTableau[initialTableauRow - 1][zColumnIndex] = 1;
		
		
		//append slack variables
		for(int i = 0; i < numOfSlackColumns;i++){
			for(int j = column + numOfDemandColumns; j < column + numOfDemandColumns + numOfSlackColumns;j++){
				if(i+(column + numOfDemandColumns) == j){
					initialTableau[i][j] = SLACK_VARIABLE_COEFFICIENT;
				}
				
			}
		}
	}
	
	public void viewInitialTableau(){
		for(int i = 0 ; i < initialTableauRow ; i++){
			for(int j = 0; j < initialTableauColumn; j++){
				System.out.print(initialTableau[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	

	@Override
	public String toString(){
		int i,j;
		String retval = "";
		for(i = 0; i < row; i++){
			retval += "S"+(i+1)+" ";
			for(j = 0; j < column; j++){
				retval += matrix[i][j] + " ";
			}
			retval += capacity[i] + " ";
			retval += "\n";
		}
		retval += "   ";
		for(i = 0; i < column; i++){
			retval += demand[i] + " ";
		}
		
		return retval;
	}

	public void initializeObjectiveFunction(int length) {
		// TODO Auto-generated method stub
		this.objectiveFunction = new double[length];
		
	}

	public void applyOptimization() {
		// TODO Auto-generated method stub
		double[][] mat = initialTableau;
		while(bottomRowHasNegative(mat)){
			int pivotColumnIndex = getNegativeWithHighestAbsoluteValue(mat);
			int pivotElementIndex = getLowestTestRatio(mat,pivotColumnIndex);
			
			normalizePivotElementRow(mat,pivotElementIndex,pivotColumnIndex);
			viewMatrix(mat);
			applyGaussJordan(mat,pivotElementIndex, pivotColumnIndex);
			viewMatrix(mat);
		}
		
	}
	public void viewMatrix(double[][] mat){
		for(int i = 0 ; i < initialTableauRow ; i++){
			for(int j = 0; j < initialTableauColumn; j++){
				System.out.print(mat[i][j]+" ");
			}
			System.out.println("");
		}
	}

	private void applyGaussJordan(double[][] mat, int pivotElementIndex, int pivotColumnIndex) {
		// TODO Auto-generated method stub
		for(int i = 0; i < initialTableauRow;i++){
			if(i != pivotElementIndex){
				double valueToZero = initialTableau[i][pivotColumnIndex];
				for(int j = pivotColumnIndex; j < initialTableauColumn;j++){
					double pivotRowElement = mat[pivotElementIndex][j];
					System.out.println("MAT[i][j] = " +mat[i][j]+ "- ("+valueToZero+"*"+pivotRowElement);
					mat[i][j] = mat[i][j] - (valueToZero * pivotRowElement);						
				}
			}
		}
		
	}

	private void normalizePivotElementRow(double[][] mat, int pivotElementIndex,int pivotColumnIndex) {
		// TODO Auto-generated method stub
		double pivotElement = mat[pivotElementIndex][pivotColumnIndex];
		System.out.println("PIVOT ELEMENT:" + pivotElement);
		for(int i = 0; i < initialTableauColumn;i++){
			mat[pivotElementIndex][i] = mat[pivotElementIndex][i] / pivotElement;
		}
	}

	private int getLowestTestRatio(double[][] mat, int pivotColumnIndex) {
		// TODO Auto-generated method stub
		List<BigDecimal> testRatioList = new ArrayList<>();
		Map<BigDecimal,Integer> ratioIndexMap = new HashMap<>();
		int answerColumnIndex = initialTableauColumn - 1;
		for(int i = 0 ; i < initialTableauRow; i++){
			if(mat[i][pivotColumnIndex] > 0 ){
				BigDecimal testRatio = BigDecimal.valueOf(initialTableau[i][answerColumnIndex]/initialTableau[i][pivotColumnIndex]);
				testRatioList.add(testRatio);
				ratioIndexMap.put(testRatio, i);
			}
		}
		Collections.sort(testRatioList);
		
		return 	ratioIndexMap.get(testRatioList.get(0));
	}

	private int getNegativeWithHighestAbsoluteValue(double[][] mat) {
		// TODO Auto-generated method stub
		List<BigDecimal> negativeList = new ArrayList<>();
		for(int i = 0; i < initialTableauColumn - 1; i++){
			if(mat[initialTableauRow-1][i] < 0){
				negativeList.add(BigDecimal.valueOf(mat[initialTableauRow-1][i]));
			}
		}
		Collections.sort(negativeList);
		
		for(int i=0; i < initialTableauColumn - 1; i++){
			if(negativeList.get(0).doubleValue() == initialTableau[initialTableauRow-1][i]){
				return i;
			}
		}
		return 0;
		
	}

	private boolean bottomRowHasNegative(double[][] mat) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < initialTableauColumn-1;i++){
			if(mat[initialTableauRow-1][i] < 0 ){
				return true;
			}
		}
		return false;
	}

	
}
