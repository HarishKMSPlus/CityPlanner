/**
 * The system starts out by taking the following parameters as input (in sequence):
 * (These are the hard constraints of time & money.)
 * 1. Total Budget
 * 2. Reserved Budget (See Note 1 Below)
 * 3. Deadline
 * 4. Cost per Square Meter of Road (See Note 2 Below)
 * 5. Time per Square Meter of Road
 * 6. Length of the Road
 * 7. Available Land, i.e., avg. width of the strip of land, to be used for the road.
 * 
 * Note 1: Reserved budget is the money reserved for traffic signals, speed breakers, road signs, planting trees, etc.
 * Note 2: Cost includes cost of material, labour charge, costs incurred due to road design (under / over bridges, crossings), etc.
 * 
 * The system would provide an output like this: "The road should be M meters (L lanes) wide & 1/2 way."
 * 
 * Using the above inputs, we calculate the road width as:
 * Width 1 = (Total Budget - Reserved Budget) / (Length of the Road x Cost per Square Meter of Road)
 * Width 2 = Total Time Available / (Length of the Road x Time per Square Meter of Road)
 * 
 * The system now finds the minimum of Width 1, Width 2 & Available Land & considers that as the final road width.
 * Road width divided by std. lane width (3.7 meters) gives us the no. of lanes.
 * If no. of lanes < 2, the system recommends a 1 way.
 * All further inputs are used to adjust this value (road width).
 * 
 * Further inputs:
 * 1. Reserved Lanes - Reserved for buses, carpool, bicycle, etc.
 * 	(Subtracted from road width.)
 * 2. Types of Vehicles - Bikes, Cars, Buses, Etc.
 * 	(Restricts minimum road width.)
 * 3. Traffic Density - 4 wheelers / minute.
 * 	Adds the time & money clause to the system's output.
 * 	The output would look something like this:
 * 	A 1/2 way road, M meters (L lanes) wide, is feasible with the given parameters.
 * 	However, a road N meters wide is required to handle the traffic density.
 * 	A budget of R rupees & a build time of D days is required to accomplish this.
 */

package com.cityplanner.client;

import java.util.Date;

import com.cityplanner.client.handlers.ResultUpdateHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CityPlanner implements EntryPoint {

	private TextBox totalBudgetInRupeesTextBox = new TextBox();
	private TextBox reservedBudgetInRupeesTextBox = new TextBox();
	private TextBox rupeesPerSqMeterTextBox = new TextBox();
	private DatePicker deadlineDatePicker = new DatePicker();
	private TextBox timePerSqMeterTextBox = new TextBox();
	private TextBox lengthTextBox = new TextBox();
	private TextBox availableLandTextBox = new TextBox();
	private ListBox reservedLanesListBox = new ListBox();
	private ListBox widestVehicleListBox = new ListBox();
	private TextBox trafficDensityTextBox = new TextBox();

	public TextBox getTotalBudgetTextBox() {
		return totalBudgetInRupeesTextBox;
	}

	public TextBox getReservedBudgetTextBox() {
		return reservedBudgetInRupeesTextBox;
	}

	public TextBox getCostPerSqMeterTextBox() {
		return rupeesPerSqMeterTextBox;
	}

	public DatePicker getDeadlineDatePicker() {
		return deadlineDatePicker;
	}

	public TextBox getTimePerSqMeterTextBox() {
		return timePerSqMeterTextBox;
	}

	public TextBox getLengthTextBox() {
		return lengthTextBox;
	}

	public TextBox getAvailableLandTextBox() {
		return availableLandTextBox;
	}

	public ListBox getReservedLanesListBox() {
		return reservedLanesListBox;
	}

	public ListBox getWidestVehicleListBox() {
		return widestVehicleListBox;
	}

	public TextBox getTrafficDensityTextBox() {
		return trafficDensityTextBox;
	}

	private ResultUpdateHandler resultUpdateHandler = new ResultUpdateHandler(this);

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable throwable) {
				GWT.log(throwable.getMessage(), throwable);
			}
		});

		FlexTable mainTable = new FlexTable();

		mainTable.setWidget(0, 0, new Label("Total Budget (₹)"));
		styleAsMandatory(totalBudgetInRupeesTextBox);
		totalBudgetInRupeesTextBox.getElement().getStyle().setMarginRight(73, Unit.PX);
		totalBudgetInRupeesTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(0, 1, totalBudgetInRupeesTextBox);

		mainTable.setWidget(1, 0, new Label("Reserved Budget (₹)"));
		reservedBudgetInRupeesTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(1, 1, reservedBudgetInRupeesTextBox);

		mainTable.setWidget(0, 2, new HTML("₹/m<sup>2</sup>"));
		styleAsMandatory(rupeesPerSqMeterTextBox);
		rupeesPerSqMeterTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(0, 3, rupeesPerSqMeterTextBox);

		mainTable.setWidget(2, 0, new Label("Deadline"));
		deadlineDatePicker.addValueChangeHandler(resultUpdateHandler);
		mainTable.setWidget(2, 1, deadlineDatePicker);

		mainTable.setWidget(2, 2, new HTML("Hours/m<sup>2</sup>"));
		styleAsMandatory(timePerSqMeterTextBox);
		timePerSqMeterTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(2, 3, timePerSqMeterTextBox);

		mainTable.setWidget(3, 0, new Label("Road Length (km)"));
		styleAsMandatory(lengthTextBox);
		lengthTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(3, 1, lengthTextBox);

		mainTable.setWidget(3, 2, new Label("Available Land Width (m)"));
		styleAsMandatory(availableLandTextBox);
		availableLandTextBox.addKeyUpHandler(resultUpdateHandler);
		mainTable.setWidget(3, 3, availableLandTextBox);

		mainTable.setWidget(4, 0, new Label("Reserved Lanes"));
		reservedLanesListBox.addItem("0");
		reservedLanesListBox.addItem("1");
		reservedLanesListBox.addItem("2");
		reservedLanesListBox.addItem("3");
		reservedLanesListBox.addItem("4");
		reservedLanesListBox.addItem("5");
		reservedLanesListBox.addItem("6");
		reservedLanesListBox.addItem("7");
		reservedLanesListBox.addItem("8");
		reservedLanesListBox.addItem("9");
		reservedLanesListBox.addChangeHandler(resultUpdateHandler);
		mainTable.setWidget(4, 1, reservedLanesListBox);

		mainTable.setWidget(4, 2, new Label("Widest Vehicle"));
		widestVehicleListBox.addItem("Bike");
		widestVehicleListBox.addItem("Car");
		widestVehicleListBox.addItem("Bus");
		widestVehicleListBox.addChangeHandler(resultUpdateHandler);
		mainTable.setWidget(4, 3, widestVehicleListBox);

//		mainTable.setWidget(5, 0, new Label("Traffic Density"));
//		trafficDensityTextBox.addKeyUpHandler(resultUpdateHandler);
//		mainTable.setWidget(5, 1, trafficDensityTextBox);

		RootPanel.get("mainTable").add(mainTable);

		Label resultLabel = new Label();
		resultLabel.addStyleName("result");
		RootPanel.get("result").add(resultLabel);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				setDefaultValues();
				totalBudgetInRupeesTextBox.setFocus(true);
				totalBudgetInRupeesTextBox.setSelectionRange(0, totalBudgetInRupeesTextBox.getText().length());
				resultUpdateHandler.updateResult();
			}
		});
	}

	private void setDefaultValues() {
		totalBudgetInRupeesTextBox.setText("1000000");
		reservedBudgetInRupeesTextBox.setText("10000");
		rupeesPerSqMeterTextBox.setText("10");

		Date date = new Date();
		CalendarUtil.addMonthsToDate(date, 12);
		deadlineDatePicker.setCurrentMonth(date);
		deadlineDatePicker.setValue(date);

		timePerSqMeterTextBox.setText("0.1");
		lengthTextBox.setText("10");
		availableLandTextBox.setText("10");
	}

	private void styleAsMandatory(UIObject uiObject) {
		uiObject.getElement().getStyle().setBorderColor("red");
	}

}
