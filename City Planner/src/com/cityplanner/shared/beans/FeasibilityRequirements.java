package com.cityplanner.shared.beans;

public class FeasibilityRequirements {

	RoadRequirements roadRequirements = new RoadRequirements();
	MonetaryRequirements monetaryRequirements = new MonetaryRequirements();
	TimeRequirements timeRequirements = new TimeRequirements();

	public RoadRequirements getRoadRequirements() {
		return roadRequirements;
	}

	public void setRoadRequirements(RoadRequirements roadRequirements) {
		this.roadRequirements = roadRequirements;
	}

	public MonetaryRequirements getMonetaryRequirements() {
		return monetaryRequirements;
	}

	public void setMonetaryRequirements(MonetaryRequirements monetaryRequirements) {
		this.monetaryRequirements = monetaryRequirements;
	}

	public TimeRequirements getTimeRequirements() {
		return timeRequirements;
	}

	public void setTimeRequirements(TimeRequirements timeRequirements) {
		this.timeRequirements = timeRequirements;
	}

}
