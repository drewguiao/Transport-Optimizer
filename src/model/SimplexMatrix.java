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
	
	public String viewObjectiveFunction(){
		List<String> listOfVariables = new ArrayList<String>();
		for(int i = 0; i < objectiveFunction.length;i++){
			listOfVariables.add(""+objectiveFunction[i]+"v["+i+"] ");
		}
		
		return joinVariables(listOfVariables);
	}
	
	private String joinVariables(List<String> listOfVariables) {
		// TODO Auto-generated method stub
		String tokensWithComma = String.join("+", listOfVariables);
		tokensWithComma+= "= Z";
		return tokensWithComma;
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
	

	public float[][] setUpInitialTableau() {
		// TODO Auto-generated method stub
		int numOfSlackVariables = row;
		this.initialTableauColumn = numOfSlackVariables + row + Z_COLUMN + ANSWER_COLUMN;
		this.initialTableauRow = row + OBJECTIVE_FUNCTION_COLUMN;
		
		initialTableau = new float[initialTableauRow][initialTableauColumn];
		
		for(int i =0; i < row; i++){
			for(int j = 0; j < column ; j++){
				initialTableau[i][j] = matrix[i][j];
				
				//set Up slack Variables
				if(i+column == j){
					initialTableau[i][j] = SLACK_VARIABLE_COEFFICIENT;
				}
			}
		}
		append(CAPACITY,capacity);
		append(DEMAND,demand);
		return initialTableau;
	}
	
	
	
	private void append(int tag, float[] list) {
		// TODO Auto-generated method stub
		length = list.length;
		int i = 0;
		int flag = 0;
		
		switch(tag){
		case CAPACITY:
			for(i = 0; i < length; i++){
				initialTableau[i][initialTableauRow] = list[i];
				flag = i;
			}
			break;
		case DEMAND:
			for(i = flag;i < length + flag; i++){

				initialTableau[i][initialTableauColumn] = list[i];
			}
			break;
		default:
			break;
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
