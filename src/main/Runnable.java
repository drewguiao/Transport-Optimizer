package main;

import java.util.Scanner;

import model.SimplexMatrix;

public class Runnable {
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args){
		int numOfSources;
		int numOfDestinations;
			
		System.out.println("Enter # sources: ");
		numOfSources = scanner.nextInt();
		
		System.out.println("Enter # destinations:");
		numOfDestinations = scanner.nextInt();
		
		//initializeMatrix
		SimplexMatrix matrix = new SimplexMatrix(numOfSources,numOfDestinations);
		
		setUpCost(matrix);
		setUpCapacity(matrix.getCapacity());
		setUpDemand(matrix.getDemand());
		
	}

	private static void setUpDemand(float[] demand) {
		// TODO Auto-generated method stub
		
	}

	private static void setUpCapacity(float[] capacity) {
		// TODO Auto-generated method stub
		
	}

	private static void setUpCost(SimplexMatrix matrix) {
		int row = matrix.getRow();
		int column = matrix.getColumn();
		float cost = 0;
		for(int i = 0; i < row; i++){
			for(int j = 0 ; j < column; j++){
				System.out.println("COST on Source");
				cost = scanner.nextFloat();
				matrix.setCost(cost,i,j);
			}
		}
	}

	
}
