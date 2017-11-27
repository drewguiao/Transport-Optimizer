package utility;

import java.util.Scanner;

import exceptions.ExceedsLimitException;
import model.OptimizerUi;
import model.SimplexMatrix;

public final class Utility {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void setUpDemand(double[] demand) {
		// TODO Auto-generated method stub
		int length = demand.length;
		for(int i = 0; i < length ; i++){
			System.out.print("DEM on D["+i+"]: ");
			demand[i] = scanner.nextDouble();
			validateNonNegativity(demand[i]);
		}
		
	}

	public static void setUpCapacity(double[] capacity) {
		// TODO Auto-generated method stub
		int length = capacity.length;
		for(int i = 0; i < length ; i++){
			System.out.print("CAP on S["+i+"]: ");
			capacity[i] = scanner.nextDouble();
			validateNonNegativity(capacity[i]);
		}
		
	}

	public static void setUpCost(SimplexMatrix matrix) {
		int row = matrix.getRow();
		int column = matrix.getColumn();
		double cost = 0;
		for(int i = 0; i < row; i++){
			for(int j = 0 ; j < column; j++){
				System.out.print("COST on S["+i+"]D["+j+"]: ");
				cost = scanner.nextDouble();
				validateNonNegativity(cost);
				matrix.setCost(cost,i,j);
			}
		}
	}

	public static void validateCoefficients(SimplexMatrix matrix) throws ExceedsLimitException{
		// TODO Auto-generated method stub
		int row = matrix.getRow();
		int column = matrix.getColumn();
		double rowCapacity, rowCapacityTotal, columnDemandTotal, columnDemand;
		
		for(int i = 0; i < row; i++){
			rowCapacityTotal = matrix.getCapacityTotalOnRow(i);
			rowCapacity = matrix.getCapacity()[i];
			if(rowCapacityTotal > rowCapacity){
				throw new RuntimeException("Capacity Total:"+rowCapacityTotal+" > Capacity:"+rowCapacity);
			}
		}
		for(int j = 0 ; j < column ; j++){
			columnDemandTotal = matrix.getDemandTotalOnColumn(j);
			columnDemand = matrix.getDemand()[j];
			if(columnDemandTotal > columnDemand){
				throw new RuntimeException("Demand Total > Demand");
			}
		}
	}
	
	private static void validateNonNegativity(double value){
		if(value < 0){
			throw new RuntimeException("Value should not be negative!");
		}
	}

	public static void setUpObjectiveFunction(SimplexMatrix matrix) {
		// TODO Auto-generated method stub
		System.out.println("===SETTING UP OBJECTIVE FUNCTION===");
		int length = matrix.getColumn();
		matrix.initializeObjectiveFunction(length);
		for(int i=0;i<length;i++){
			System.out.print("Enter coefficient for x["+i+"]: ");
			matrix.setObjectiveFunctionCoefficients(scanner.nextDouble(), i);
			
		}
	}

	public static void initializeUI() {
		// TODO Auto-generated method stub
		OptimizerUi ui = new OptimizerUi();
	}

	
}
