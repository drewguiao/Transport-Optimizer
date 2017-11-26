package main;


import utility.Utility;
import java.util.Scanner;

import exceptions.ExceedsLimitException;
import model.SimplexMatrix;

public class Runnable {
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws ExceedsLimitException{
		int numOfSources;
		int numOfDestinations;
		float[][] initialTableau;
		
		System.out.print("Enter # sources: ");
		numOfSources = scanner.nextInt();
		
		System.out.print("Enter # destinations:");
		numOfDestinations = scanner.nextInt();
		
		//initializeMatrix
		SimplexMatrix matrix = new SimplexMatrix(numOfSources,numOfDestinations);
		
		Utility.setUpObjectiveFunction(matrix);
		
		System.out.println(matrix.viewObjectiveFunction());
		
		
		
//		Utility.setUpCost(matrix);
//		Utility.setUpCapacity(matrix.getCapacity());
//		Utility.setUpDemand(matrix.getDemand());
//		Utility.validateCoefficients(matrix);
//		
//		initialTableau = matrix.setUpInitialTableau();
//		
//		for(int i = 0; i < numOfSources+1;i++){
//			for(int j = 0; j < numOfDestinations+1; j++){
//				System.out.print(initialTableau[i][j]+ " ");;
//			}
//			System.out.println("");
//		}
		
		
		System.out.println(matrix.toString());
		
	}


}
