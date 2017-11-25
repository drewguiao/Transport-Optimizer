package utility;

import java.util.Scanner;

import exceptions.UtilityError;
import model.SimplexMatrix;

public final class Utility {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void setUpDemand(float[] demand) {
		// TODO Auto-generated method stub
		int length = demand.length;
		for(int i = 0; i < length ; i++){
			System.out.println("Enter Demand on D["+(i+1)+"]: ");
			demand[i] = scanner.nextFloat();
		}
		
	}

	public static void setUpCapacity(float[] capacity) {
		// TODO Auto-generated method stub
		int length = capacity.length;
		for(int i = 0; i < length ; i++){
			System.out.println("Capacity on S["+(i+1)+"]: ");
			capacity[i] = scanner.nextFloat();
		}
		
	}

	public static void setUpCost(SimplexMatrix matrix) {
		int row = matrix.getRow();
		int column = matrix.getColumn();
		float cost = 0;
		for(int i = 0; i < row; i++){
			for(int j = 0 ; j < column; j++){
				System.out.print("COST on S["+(i+1)+"]D["+(j+1)+"]: ");
				cost = scanner.nextFloat();
				matrix.setCost(cost,i,j);
			}
		}
	}

	public static void validateCoefficients(SimplexMatrix matrix) throws UtilityError {
		// TODO Auto-generated method stub
		int row = matrix.getRow();
		int column = matrix.getColumn();
		float rowCapacity, rowCapacityTotal, columnDemandTotal, columnDemand;
		
		for(int i = 0; i < row; i++){
			rowCapacityTotal = matrix.getCapacityTotalOnRow(i);
			rowCapacity = matrix.getCapacity()[i];
			if(rowCapacityTotal > rowCapacity){
				throw new UtilityError("CAPACITY",rowCapacityTotal,rowCapacity);
			}
		}
		for(int j = 0 ; j < column ; j++){
			columnDemandTotal = matrix.getDemandTotalOnColumn(j);
			columnDemand = matrix.getDemand()[j];
			if(columnDemandTotal > columnDemand){
				throw new UtilityError("Demand",columnDemandTotal,columnDemand);
			}
		}
	}

	
}
