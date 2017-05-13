package com.cityplanner.shared.beans;

public class InputFactors {

	MonetaryFactors monetaryFactors = new MonetaryFactors();
	TimeFactors timeFactors = new TimeFactors();
	RoadSpecificFactors roadSpecificFactors = new RoadSpecificFactors();
	AuxiliaryFactors auxiliaryFactors = new AuxiliaryFactors();

	public MonetaryFactors getMonetaryFactors() {
		return monetaryFactors;
	}

	public void setMonetaryFactors(MonetaryFactors monetaryFactors) {
		this.monetaryFactors = monetaryFactors;
	}

	public TimeFactors getTimeFactors() {
		return timeFactors;
	}

	public void setTimeFactors(TimeFactors timeFactors) {
		this.timeFactors = timeFactors;
	}

	public RoadSpecificFactors getRoadSpecificFactors() {
		return roadSpecificFactors;
	}

	public void setRoadSpecificFactors(RoadSpecificFactors roadSpecificFactors) {
		this.roadSpecificFactors = roadSpecificFactors;
	}

	public AuxiliaryFactors getAuxiliaryFactors() {
		return auxiliaryFactors;
	}

	public void setAuxiliaryFactors(AuxiliaryFactors auxiliaryFactors) {
		this.auxiliaryFactors = auxiliaryFactors;
	}

}
