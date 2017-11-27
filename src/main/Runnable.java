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
		
		System.out.print("Enter # sources: ");
		numOfSources = scanner.nextInt();
		
		System.out.print("Enter # destinations:");
		numOfDestinations = scanner.nextInt();
		
		//initializeMatrix
		SimplexMatrix matrix = new SimplexMatrix(numOfSources,numOfDestinations);
		
		Utility.setUpObjectiveFunction(matrix);
		matrix.viewObjectiveFunction();
		Utility.setUpCost(matrix);
		Utility.setUpCapacity(matrix.getCapacity());
		Utility.setUpDemand(matrix.getDemand());
		matrix.setUpInitialTableau();
		matrix.viewInitialTableau();
		matrix.applyOptimization();
//		System.out.println(matrix.toString());
		
	}


}
