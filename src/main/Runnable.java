package main;


import utility.Utility;
import utility.Constants;
import java.util.Scanner;

import exceptions.ExceedsLimitException;
import model.Optimizer;
import model.SimplexMatrix;

public class Runnable extends Constants{
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws ExceedsLimitException{
		int numOfSources;
		int numOfDestinations;
		int state = 99;
		
		while(state!=0){
			System.out.println("[1] Ultimate-Optimizer");
			System.out.println("[2] Transport-Optimizer");
			System.out.println("[3] Exit");
			System.out.print("Choice: ");
			state = scanner.nextInt();
			switch(state){
			case ULTIMATE_OPTIMIZER:

				System.out.print("Enter number of constraints:");
				int constraints = scanner.nextInt();
				System.out.print("Enter number of variables:");
				int variables = scanner.nextInt();
				
				Optimizer ultimateOptimizer = new Optimizer(constraints,variables);
				
				break;
			case TRANSPORT_OPTIMIZER:
				System.out.print("Enter # sources: ");
				numOfSources = scanner.nextInt();
				
				System.out.print("Enter # destinations:");
				numOfDestinations = scanner.nextInt();
				
				SimplexMatrix matrix = new SimplexMatrix(numOfSources,numOfDestinations);
				
				Utility.setUpObjectiveFunction(matrix);
				matrix.viewObjectiveFunction();
				Utility.setUpCost(matrix);
				Utility.setUpCapacity(matrix.getCapacity());
				Utility.setUpDemand(matrix.getDemand());
				matrix.setUpInitialTableau();
				matrix.viewInitialTableau();
				matrix.applyOptimization();

				break;
			case EXIT: System.exit(1);
				break;
			}
		}

		
	}


}
