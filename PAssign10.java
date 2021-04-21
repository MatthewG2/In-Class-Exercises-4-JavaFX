/**
 * File: PAssign10.java
 * Package: ch16
 * @author Matthew Goetz
 * Created on: Apr 20, 2021
 * Last Modified: Apr 21, 2021
 * Description: simple MPG or KPL with the choice by comboBox that changing textboxs
 */
package ch14;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PAssign10 extends Application {
	
	//create counter
	public static int counter = 0;
	
	// default values/strings
	private double txtWidth = 125.0;
	private String defaultCalc = String.format("%.2f", 0.00);
	private String defaultEntry = String.format("%.2f", 0.00);
	private String defaultMileage = "Miles";
	private String defaultCapacity = "Gallons";
	private String defaultResult = "MPG";
	private String altMileage = "Kilometers";
	private String altCapacity = "Liters";
	private String altResult = "L/100KM";

	// create UI components split by type
	private Button btnCalc = new Button("Calculate");
	private Button btnReset = new Button("Reset");

	private Label lblDistance = new Label(defaultMileage);
	private Label lblCapacity = new Label(defaultCapacity);
	private Label lblResult = new Label(defaultResult);
	private Label lblEffType = new Label("Efficiency Type");

	private TextField tfDistance = new TextField(defaultEntry);
	private TextField tfCapacity = new TextField(defaultEntry);
	private TextField tfResult = new TextField(defaultCalc);

	//create list with already created strings
	ObservableList<String> results = FXCollections.observableArrayList(defaultResult,altResult);

	//populate the combobox
	ComboBox<String> cmbresults =	new ComboBox<>(results);


	private GridPane mainPane = new GridPane();

	public void start(Stage primaryStage) {   	

		// set preferences for UI components
		tfDistance.setMaxWidth(txtWidth);
		tfCapacity.setMaxWidth(txtWidth);
		tfResult.setMaxWidth(txtWidth);
		tfResult.setEditable(false);

		// create a main grid pane to hold items
		mainPane.setPadding(new Insets(10.0));
		mainPane.setHgap(txtWidth/2.0);
		mainPane.setVgap(txtWidth/12.0);

		// add items to mainPane
		mainPane.add(lblEffType, 0, 0);
		mainPane.add(cmbresults, 0, 1);
		mainPane.add(lblDistance, 0, 2);
		mainPane.add(tfDistance, 1, 2);
		mainPane.add(lblCapacity, 0, 3);
		mainPane.add(tfCapacity, 1, 3);
		mainPane.add(lblResult, 0, 4);
		mainPane.add(tfResult, 1, 4);
		mainPane.add(btnReset, 0, 5);
		mainPane.add(btnCalc, 1, 5);

		// register action handlers
		btnCalc.setOnAction(e -> calcMileage());
		tfDistance.setOnAction(e -> calcMileage());
		tfCapacity.setOnAction(e -> calcMileage());
		tfResult.setOnAction(e -> calcMileage());
		cmbresults.setOnAction(e -> changeLabels());
		btnReset.setOnAction(e -> resetForm());

		cmbresults.setPromptText(defaultResult);



		// create a scene and place it in the stage
		Scene scene = new Scene(mainPane); 

		// set and show stage
		primaryStage.setTitle("Mileage Calculator"); 
		primaryStage.setScene(scene); 
		primaryStage.show();      

		// stick default focus in first field for usability
		tfDistance.requestFocus();
	}

	/**
	 * Convert existing figures and recalculate
	 * This needs to be separate to avoid converting when
	 * the conversion is not necessary
	 */
	private void changeLabels() {
		
		//declare variables
		double distance = 0, capacity = 0, results = 0;
		String newDistance, newCapacity;
		
		//update counter
		counter++;


		//Make the default MPG
		if(cmbresults.getValue() == null) {
			cmbresults.getValue().equals(defaultResult);
		}

		//make sure to get numeric values only
		if (tfCapacity.getText() != null && !tfCapacity.getText().isEmpty()
				&& tfDistance.getText() != null && !tfDistance.getText().isEmpty()) {
			distance = Double.parseDouble(tfDistance.getText());
			capacity = Double.parseDouble(tfCapacity.getText());
			results = Double.parseDouble(tfResult.getText());
		}

		// distinguish between L/100KM and MPG. Also changes the textboxs
		if (cmbresults.getValue().equals(altResult) && lblCapacity.getText().equals(defaultCapacity)) {
			// update labels
			lblCapacity.setText(altCapacity);
			lblDistance.setText(altMileage);
			lblResult.setText(altResult);
			
			//convert values from miles to kms
			distance = distance * 1.609;
			newDistance = String.format("%,.2f", distance);
			tfDistance.setText(newDistance);
			
			//convert values from gallons to liters
			capacity = capacity * 3.785;
			newCapacity = String.format("%,.2f", capacity);
			tfCapacity.setText(newCapacity);
			if (results != 0) {
				calcMileage();
		}



		} else {
			// update labels and textboxs
			lblCapacity.setText(defaultCapacity);
			lblDistance.setText(defaultMileage);
			lblResult.setText(defaultResult);
			
			//convert values from kms to miles and make sure the labels have been change atleast once, so it doesnt change values
			//without needing to.
			if (counter > 1) {
			distance = distance / 1.609;
			newDistance = String.format("%,.2f", distance);
			tfDistance.setText(newDistance);

			//convert values from liters to gallons
			capacity = capacity / 3.785;
			newCapacity = String.format("%,.2f", capacity);
			tfCapacity.setText(newCapacity);
			}
			if (results != 0) {
				calcMileage();
			}
		}
	}


	/**
	 * Calculate expenses based on entered figures
	 */
	private void calcMileage() {   

		//Make the default MPG
		if(cmbresults.getValue() == null) {
			cmbresults.setValue(defaultResult);
		}

		// set default values
		double distance = 0.0, capacity = 0.0;

		// make sure to get numeric values only
		if (tfCapacity.getText() != null && !tfCapacity.getText().isEmpty()
				&& tfDistance.getText() != null && !tfDistance.getText().isEmpty()) {
			distance = Double.parseDouble(tfDistance.getText());
			capacity = Double.parseDouble(tfCapacity.getText());
		}

		// check for type of calculation
		double result = 0.0;
		if (cmbresults.getValue().equals(altResult)) {
			// liters / 100KM
			result = (distance != 0) ? capacity/(distance/100.0) : 0;
		} else {
			// MPG
			result = (capacity != 0) ? distance/capacity : 0;       	
		}

		// update calculation fields with currency formatting
		tfResult.setText(String.format("%.2f", result));
	}

	/**
	 * Reset all values in the application
	 */
	private void resetForm() {
		// reset all form fields
		cmbresults.setValue(defaultResult);
		tfDistance.setText(defaultEntry);
		tfCapacity.setText(defaultEntry);
		tfResult.setText(defaultCalc);
		lblCapacity.setText(defaultCapacity);
		lblDistance.setText(defaultMileage);
		lblResult.setText(defaultResult);
	}


	public static void main(String[] args) {
		launch(args);
	}
}