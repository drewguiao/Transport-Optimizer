package model;

public class SimplexMatrix {
	private int length, row, column;
	private float[][] matrix;
	private float[] capacity, demand;
	
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
	
	public float getCapacityTotalOnRow(int index){
		float total = 0;
		for(int i = 0; i < index; i++){
			total = total + this.capacity[i];
		}
		return total;
	}
	
	public float getDemandTotalOnColumn(int index){
		float total = 0;
		for(int i = 0; i < index; i++){
			total = total + this.demand[i];
		}
		return total;
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
	
	
	
}
