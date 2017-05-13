/**
 * Method: getRoadWidthFromMonetaryFactors
 * Algorithm: Width = (Total Budget - Reserved Budget) / (Length of the Road x Cost per Square Meter of Road)
 * 
 * Method: getRoadWidthFromTimeFactors
 * Algorithm: Width = Total Time Available / (Length of the Road x Time per Square Meter of Road)
 * 
 * Method: getRoadWidthInLanesFromRoadWidthInMeters
 * Algorithm:
 * Road Width in Lanes = Road Width in Meters / 3.7
 * Is 1 Way = Road Width in Lanes < 2
 * 
 * Method: accomodateReservedLanes
 * Algorithm: 
 * New Road Width in Lanes = Old Road Width in Lanes - Reserved Lanes
 * New Road Width in Meters = New Road Width in Lanes x 3.7
 * 
 * Method: checkRoadSuitabilityForWidestVehicle
 * Algorithm: If Widest Vehicle <> BIKE & Road Width < 1 Lane, throw RoadNotFeasibleException
 * 
 * Method: accomodateTrafficDensity
 * Algorithm:
 * Required Road Width in Lanes = 4 Wheelers per Minute / 6
 * Required Road Width in Meters = Required Road Width in Lanes x 3.7
 * Required Budget = Length of the Road x Width of the Road x Cost per Square Meter of Road
 * Required Time = Length of the Road x Width of the Road x Time per Square Meter of Road
 */

package com.cityplanner.client.utils;

import java.util.Date;

import com.cityplanner.shared.beans.InputFactors;
import com.cityplanner.shared.beans.OutputRoadSpecs;
import com.cityplanner.shared.enums.Vehicle;
import com.cityplanner.shared.exceptions.RoadNotFeasibleException;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CityPlannerUtils {

	public static void getRoadWidth(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) throws RoadNotFeasibleException {
		getRoadWidthFromMonetaryFactors(inputFactors, outputRoadSpecs);
		double roadWidthFromMonetaryFactors = outputRoadSpecs.getRoadWidth().getRoadWidthInMeters();
		getRoadWidthFromTimeFactors(inputFactors, outputRoadSpecs);
		double roadWidthFromTimeFactors = outputRoadSpecs.getRoadWidth().getRoadWidthInMeters();

		double roadWidth = Math.min(roadWidthFromMonetaryFactors, roadWidthFromTimeFactors);
		roadWidth = Math.min(roadWidth, inputFactors.getAuxiliaryFactors().getAvailableLand());
		setRoadWidth(outputRoadSpecs, roadWidth);
	}

	public static void getRoadWidthFromMonetaryFactors(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) throws RoadNotFeasibleException {
		/*
		 * Width = (Total Budget - Reserved Budget) / (Length of the Road x Cost per Square Meter of Road)
		 */

		double totalBudgetInRs = inputFactors.getMonetaryFactors().getTotalBudgetInRs();
		double reservedBudgetInRs = inputFactors.getMonetaryFactors().getReservedBudgetInRs();
		double rupeesPerSqMeter = inputFactors.getMonetaryFactors().getRupeesPerSqMeter();

		double roadWidth = (totalBudgetInRs - reservedBudgetInRs) / (getRoadLength(inputFactors) * rupeesPerSqMeter);
		setRoadWidth(outputRoadSpecs, roadWidth);
	}

	public static void getRoadWidthFromTimeFactors(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) throws RoadNotFeasibleException {
		/*
		 * Width = Total Time Available / (Length of the Road x Time per Square Meter of Road)
		 */

		int daysAvailable = CalendarUtil.getDaysBetween(new Date(), inputFactors.getTimeFactors().getDeadline());
		if (daysAvailable <= 0) {
			throw new RoadNotFeasibleException();
		}
		int hoursAvailable = daysAvailable * 24;

		double hoursPerSqMeter = inputFactors.getTimeFactors().getHoursPerSqMeter();
		double roadWidth = hoursAvailable / (getRoadLength(inputFactors) * hoursPerSqMeter);
		setRoadWidth(outputRoadSpecs, roadWidth);
	}

	public static void accomodateReservedLanes(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) throws RoadNotFeasibleException {
		/*
		 * New Road Width in Meters = ((Old Road Width in Meters / 3.7) - Reserved Lanes) x 3.7
		 */

		double requiredWidthInMeters = ((outputRoadSpecs.getRoadWidth().getRoadWidthInMeters() / STANDARD_ROAD_LANE_WIDTH)
			- inputFactors.getAuxiliaryFactors().getNoOfReservedLanes()) * STANDARD_ROAD_LANE_WIDTH;

		setRoadWidth(outputRoadSpecs, requiredWidthInMeters);
	}

	public static void checkRoadSuitabilityForWidestVehicle(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) throws RoadNotFeasibleException {
		/*
		 * If Widest Vehicle <> BIKE & Road Width < 1 Lane, throw RoadNotFeasibleException
		 */
		if (inputFactors.getAuxiliaryFactors().getWidestVehicleAllowed() != Vehicle.BIKE &&
				outputRoadSpecs.getRoadWidth().getRoadWidthInLanes() < 1) {
			throw new RoadNotFeasibleException();
		}
	}

	public static void accomodateTrafficDensity(InputFactors inputFactors, OutputRoadSpecs outputRoadSpecs) {
		/*
		 * Required Road Width in Lanes = 4 Wheelers per Minute / 6
		 * Required Road Width in Meters = Required Road Width in Lanes x 3.7
		 * Required Budget = Length of the Road x Width of the Road x Cost per Square Meter of Road
		 * Required Time = Length of the Road x Width of the Road x Time per Square Meter of Road
		 */
	}

	private static double getRoadLength(InputFactors inputFactors) {
		double lengthOfRoadInKm = inputFactors.getRoadSpecificFactors().getLengthOfRoadInKm();
		double lengthOfRoadInMeters = lengthOfRoadInKm * 1000;
		return lengthOfRoadInMeters;
	}

	public static final double STANDARD_ROAD_LANE_WIDTH = 3.7; // meters.

	private static void setRoadWidth(OutputRoadSpecs outputRoadSpecs, double roadWidth) throws RoadNotFeasibleException {
		if (getFormattedNumber(roadWidth) <= 0) {
			throw new RoadNotFeasibleException();
		}
		outputRoadSpecs.getRoadWidth().setRoadWidthInMeters(roadWidth);

		double roadWidthInLanes = roadWidth / STANDARD_ROAD_LANE_WIDTH;
		if (getFormattedNumber(roadWidthInLanes) < 1) {
			roadWidthInLanes =1;
		}

		outputRoadSpecs.getRoadWidth().setRoadWidthInLanes(roadWidthInLanes);
		outputRoadSpecs.getRoadDirectionality().setOneWay(roadWidthInLanes <= 1);
	}

	private static double getFormattedNumber(Number number) {
		return Double.parseDouble(NumberFormat.getFormat("#0.#").format(number));
	}

}
