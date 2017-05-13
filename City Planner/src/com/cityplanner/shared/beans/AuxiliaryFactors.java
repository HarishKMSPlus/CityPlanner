package com.cityplanner.shared.beans;

import com.cityplanner.shared.enums.Vehicle;

public class AuxiliaryFactors {

	double availableLand;
	double noOfReservedLanes;
	Vehicle widestVehicleAllowed;
	double fourWheelersPerMinute;

	public double getAvailableLand() {
		return availableLand;
	}

	public void setAvailableLand(double availableLand) {
		this.availableLand = availableLand;
	}

	public double getNoOfReservedLanes() {
		return noOfReservedLanes;
	}

	public void setNoOfReservedLanes(double noOfReservedLanes) {
		this.noOfReservedLanes = noOfReservedLanes;
	}

	public Vehicle getWidestVehicleAllowed() {
		return widestVehicleAllowed;
	}

	public void setWidestVehicleAllowed(Vehicle widestVehicleAllowed) {
		this.widestVehicleAllowed = widestVehicleAllowed;
	}

	public double getFourWheelersPerMinute() {
		return fourWheelersPerMinute;
	}

	public void setFourWheelersPerMinute(double fourWheelersPerMinute) {
		this.fourWheelersPerMinute = fourWheelersPerMinute;
	}

}
