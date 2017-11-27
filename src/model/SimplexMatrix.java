package model;

import java.util.ArrayList;
import java.util.List;

import utility.Constants;

public class SimplexMatrix extends Constants{
	private int length, row, column;
	private int initialTableauRow;
	private int initialTableauColumn;
	private float[][] matrix;
	private float[][] initialTableau;
	private float[] capacity, demand;
	private float[] objectiveFunction;
	
	
	public SimplexMatrix(int numOfSources, int numOfDestinations) {
		this.matrix = new float[numOfSources][numOfDestinations];
		this.length = numOfSources * numOfDestinations;
		this.row = numOfSources;
		this.column = numOfDestinations;
		this.capacity = new float[row];
		this.demand = new float[column];
	}
	
	public int getLength(){
		return this.length;
	}
	
	public float[][] getMatrix(){
		return this.matrix;
	}
	
	public int getRow(){
		return this.row;
	}
	
	public int getColumn(){
		return this.column;
	}
	
	public float[] getCapacity(){
		return this.capacity;
	}
	
	public float[] getDemand(){
		return this.demand;
	}
	
	public void setCost(float cost, int i, int j){
		this.matrix[i][j] = cost;
	}
	
	public void setCapacity(float capacity, int i){
		this.capacity[i] = capacity;
	};
	
	public void setDemand(float demand, int i){
		this.demand[i] = demand;
	}
	
	public void setObjectiveFunctionCoefficients(float value, int index){
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

	public float getCapacityTotalOnRow(int index){
		float total = 0;
		for(int i = 0; i < index; i++){
			total = total + matrix[index][i];
		}
		return total;
	}
	
	public float getDemandTotalOnColumn(int index){
		float total = 0;
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
		initialTableau = new float[initialTableauRow][initialTableauColumn];
		
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
		this.objectiveFunction = new float[length];
		
	}
}
