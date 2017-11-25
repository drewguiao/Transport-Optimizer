package main;


import utility.Utility;
import java.util.Scanner;

import model.SimplexMatrix;

public class Runnable {
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args){
		int numOfSources;
		int numOfDestinations;
			
		System.out.print("Enter # sources: ");
		numOfSources = scanner.nextInt();
		
		System.out.print("Enter # destinations:");
		numOfDestinations = scanner.nextInt();
		
		//initializeMatrix
		SimplexMatrix matrix = new SimplexMatrix(numOfSources,numOfDestinations);
		
		Utility.setUpCost(matrix);
		Utility.setUpCapacity(matrix.getCapacity());
		Utility.setUpDemand(matrix.getDemand());
		Utility.validateCoefficients(matrix);
		
		System.out.println(matrix.toString());
		
	}


}
