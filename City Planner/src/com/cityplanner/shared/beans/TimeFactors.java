package com.cityplanner.shared.beans;

import java.util.Date;

public class TimeFactors {

	Date deadline;
	double hoursPerSqMeter;

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public double getHoursPerSqMeter() {
		return hoursPerSqMeter;
	}

	public void setHoursPerSqMeter(double hoursPerSqMeter) {
		this.hoursPerSqMeter = hoursPerSqMeter;
	}

}
