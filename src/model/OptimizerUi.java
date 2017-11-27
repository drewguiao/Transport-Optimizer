package model;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OptimizerUi extends JFrame{
	JFrame uiFrame;
	JPanel optimizationPanel,initializationPanel, objectiveFunctionPanel,constraintsPanel,constraintsTitlePanel;
	JTextField constraintsCount,variablesCount;
	JButton okButton,maximizeButton,minimizeButton;
	
	List<JTextField> objFunctionCoeff = new ArrayList<>();
	List<JTextField> constCoeffList = new ArrayList<>();
	
	int numOfVariables;
	int numOfConstraints;
	
	public OptimizerUi(){
		uiFrame = new JFrame("Ultimate Optimizer");
		uiFrame.setLayout(new GridLayout(0,1));
		
		optimizationPanel = new JPanel();
		initializationPanel = new JPanel();
		objectiveFunctionPanel = new JPanel();
		constraintsPanel = new JPanel();
		constraintsTitlePanel = new JPanel();
		
		JLabel constraintsLabel = new JLabel("Constraints:");
		constraintsCount = new JTextField("1");
		constraintsCount.setColumns(3);
		
		JLabel variablesLabel = new JLabel("Variables:");
		variablesCount = new JTextField("1");
		variablesCount.setColumns(3);
		
		
		
		okButton = new JButton("Ok");
		 maximizeButton = new JButton("Maximize");
		 minimizeButton = new JButton("Minimize");
		
		okButton.addActionListener(addTextField);
		
		initializationPanel.add(constraintsLabel);
		initializationPanel.add(constraintsCount);
		initializationPanel.add(variablesLabel);
		initializationPanel.add(variablesCount);
		initializationPanel.add(okButton);
		
		maximizeButton.addActionListener(maximize);
		minimizeButton.addActionListener(minimize);
		
		optimizationPanel.add(maximizeButton);
		optimizationPanel.add(minimizeButton);
		
		
		uiFrame.add(initializationPanel);
		uiFrame.add(objectiveFunctionPanel);
		uiFrame.add(constraintsTitlePanel);
		uiFrame.add(constraintsPanel);
		uiFrame.add(optimizationPanel);
		
		uiFrame.setLocationRelativeTo(null);
		uiFrame.setSize(400, 400);
		uiFrame.setResizable(false);
		uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uiFrame.validate();
		uiFrame.setVisible(true);
	}
	
	private ActionListener minimize = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			final int MINIMIZE = 1;
			Optimizer uO = new Optimizer(numOfConstraints, numOfVariables);
			uO.getObjFuncCoeff(objFunctionCoeff);
			uO.getConstCoeff(constCoeffList);
			uO.appendSlackVariables();
			uO.appendObjectiveFunctionCoeff();
			uO.viewTableau();
			uO.updateBasicSolution();
			uO.optimize();
			uO.getMode(MINIMIZE);
		}
	};
	
	private ActionListener maximize = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			final int MAXIMIZE = 2;
			Optimizer uO = new Optimizer(numOfConstraints, numOfVariables);
			
			
			uO.getObjFuncCoeff(objFunctionCoeff);
			uO.getConstCoeff(constCoeffList);
			uO.appendSlackVariables();
			uO.appendObjectiveFunctionCoeff();
			uO.viewTableau();
			uO.updateBasicSolution();
			uO.optimize();
			uO.getMode(MAXIMIZE);
		}
	};
	
	
	private ActionListener addTextField = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
//			get Objective Function List
			objectiveFunctionPanel.removeAll();
			objFunctionCoeff.clear();
			refreshView();
			
			JLabel objFunctionLabel = new JLabel("Objective Function: ");
			objectiveFunctionPanel.add(objFunctionLabel);
			numOfConstraints = Integer.parseInt(constraintsCount.getText());
			numOfVariables = Integer.parseInt(variablesCount.getText());
			for(int i = 0; i < numOfVariables;i++){
				JLabel coeffLabel = new JLabel("x["+i+"]");
				JTextField coeffField = new JTextField();
				coeffField.setColumns(3);
				coeffField.setBounds(100,200,240,280);
				
				objFunctionCoeff.add(coeffField);
				objectiveFunctionPanel.add(coeffField);
				objectiveFunctionPanel.add(coeffLabel);
				
				JLabel plusLabel = new JLabel("+");
				objectiveFunctionPanel.add(plusLabel);
				
			}
			JLabel zLabel = new JLabel("= Z");
			objectiveFunctionPanel.add(zLabel);
			
			
//			CONSTRAINTS PANEL
			constraintsPanel.removeAll();
			constraintsPanel.setLayout(new GridLayout(numOfConstraints,numOfVariables));
			constraintsTitlePanel.removeAll();
			
			List<String> tokens = new ArrayList<>();
			for(int i=0;i<numOfVariables;i++){				
				tokens.add("x["+i+"]");
			}
			
			String constraintText = String.join("+", tokens);
			JLabel text = new JLabel(constraintText+" = Answer");
			constraintsTitlePanel.add(text);
			constCoeffList.clear();
			
			for(int i = 0; i < numOfConstraints;i++){
				for(int j=0;j<numOfVariables+1;j++){
					JTextField constCoeffField = new JTextField();
					constCoeffField.setColumns(3);
					
					constCoeffList.add(constCoeffField);
					constraintsPanel.add(constCoeffField);
						
				}
				
			}
			
			
			
			refreshView();
			
		}

		private void refreshView() {
			// TODO Auto-generated method stub
			uiFrame.pack();
			uiFrame.revalidate();
			uiFrame.repaint();
						
		}
		
	};
	
}
