package com.cityplanner.client.handlers;

import java.util.Date;

import com.cityplanner.client.CityPlanner;
import com.cityplanner.client.templates.ResultTemplates;
import com.cityplanner.client.utils.CityPlannerUtils;
import com.cityplanner.shared.beans.AuxiliaryFactors;
import com.cityplanner.shared.beans.InputFactors;
import com.cityplanner.shared.beans.MonetaryFactors;
import com.cityplanner.shared.beans.OutputRoadSpecs;
import com.cityplanner.shared.beans.RoadDirectionality;
import com.cityplanner.shared.beans.RoadSpecificFactors;
import com.cityplanner.shared.beans.RoadWidth;
import com.cityplanner.shared.beans.TimeFactors;
import com.cityplanner.shared.enums.Vehicle;
import com.cityplanner.shared.exceptions.RoadNotFeasibleException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ResultUpdateHandler implements KeyUpHandler, ChangeHandler, ValueChangeHandler<Date> {

	private CityPlanner cityPlanner = null;

	public ResultUpdateHandler(CityPlanner cityPlanner) {
		this.cityPlanner = cityPlanner;
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		updateResult();
	}

	@Override
	public void onChange(ChangeEvent event) {
		updateResult();
	}

	@Override
	public void onValueChange(ValueChangeEvent<Date> event) {
		updateResult();
	}

	public void updateResult() {
		setResult("");
		if (isEmpty(cityPlanner.getTotalBudgetTextBox()) ||
				isEmpty(cityPlanner.getCostPerSqMeterTextBox()) ||
				isEmpty(cityPlanner.getLengthTextBox())) {
			return;
		}

		try {
			InputFactors inputFactors = getInputFactors();
			OutputRoadSpecs outputRoadSpecs = new OutputRoadSpecs();
			CityPlannerUtils.getRoadWidth(inputFactors, outputRoadSpecs);
			CityPlannerUtils.accomodateReservedLanes(inputFactors, outputRoadSpecs);
			CityPlannerUtils.checkRoadSuitabilityForWidestVehicle(inputFactors, outputRoadSpecs);
			setCommonResult(outputRoadSpecs);
		} catch (RoadNotFeasibleException roadNotFeasibleException) {
			setResult("A road is not feasible with the given parameters.");
		}
	}

	private void setResult(String result) {
		((Label) RootPanel.get("result").getWidget(0)).setText(result);
	}

	public static final ResultTemplates RESULT_TEMPLATES = GWT.create(ResultTemplates.class);

	private void setCommonResult(OutputRoadSpecs outputRoadSpecs) {
		RoadWidth roadWidth = outputRoadSpecs.getRoadWidth();
		RoadDirectionality roadDirectionality = outputRoadSpecs.getRoadDirectionality();

		SafeHtml result = RESULT_TEMPLATES.commonResult(getFormattedNumber(roadWidth.getRoadWidthInMeters()),
			getFormattedNumber(roadWidth.getRoadWidthInLanes()), roadDirectionality.isOneWay() ? "1" : "2");

		((Label) RootPanel.get("result").getWidget(0)).setText(result.asString());
	}

	private InputFactors getInputFactors() {
		InputFactors inputFactors = new InputFactors();

		MonetaryFactors monetaryFactors = inputFactors.getMonetaryFactors();
		monetaryFactors.setTotalBudgetInRs(getDouble(getText(cityPlanner.getTotalBudgetTextBox())));
		monetaryFactors.setReservedBudgetInRs(getDouble(getText(cityPlanner.getReservedBudgetTextBox())));
		monetaryFactors.setRupeesPerSqMeter(getDouble(getText(cityPlanner.getCostPerSqMeterTextBox())));

		TimeFactors timeFactors = inputFactors.getTimeFactors();
		timeFactors.setDeadline(cityPlanner.getDeadlineDatePicker().getValue());
		timeFactors.setHoursPerSqMeter(getDouble(getText(cityPlanner.getTimePerSqMeterTextBox())));

		RoadSpecificFactors roadSpecificFactors = inputFactors.getRoadSpecificFactors();
		roadSpecificFactors.setLengthOfRoadInKm(getDouble(getText(cityPlanner.getLengthTextBox())));

		AuxiliaryFactors auxiliaryFactors = inputFactors.getAuxiliaryFactors();
		auxiliaryFactors.setAvailableLand(getDouble(getText(cityPlanner.getAvailableLandTextBox())));
		auxiliaryFactors.setNoOfReservedLanes(getDouble(cityPlanner.getReservedLanesListBox().getSelectedItemText()));
		auxiliaryFactors.setWidestVehicleAllowed(Vehicle.valueOf(cityPlanner.getWidestVehicleListBox().getSelectedItemText().toUpperCase()));
		auxiliaryFactors.setFourWheelersPerMinute(getDouble(getText(cityPlanner.getTrafficDensityTextBox())));

		return inputFactors;
	}

	private boolean isEmpty(TextBox textBox) {
		return textBox.getText().trim().isEmpty();
	}

	private String getText(TextBox textBox) {
		return textBox.getText().trim();
	}

	private double getDouble(String text) {
		if (text == null || text.trim().isEmpty()) {
			return 0.0;
		}

		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException numberFormatException) {
			return 0.0;
		}
	}

	private String getFormattedNumber(Number number) {
		return NumberFormat.getFormat("#0.#").format(number);
	}

}
