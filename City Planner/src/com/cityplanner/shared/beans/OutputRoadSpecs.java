package com.cityplanner.shared.beans;

public class OutputRoadSpecs {

	RoadWidth roadWidth = new RoadWidth();
	RoadDirectionality roadDirectionality = new RoadDirectionality();
	FeasibilityRequirements feasibilityRequirements = new FeasibilityRequirements();

	public RoadWidth getRoadWidth() {
		return roadWidth;
	}

	public void setRoadWidth(RoadWidth roadWidth) {
		this.roadWidth = roadWidth;
	}

	public RoadDirectionality getRoadDirectionality() {
		return roadDirectionality;
	}

	public void setRoadDirectionality(RoadDirectionality roadDirectionality) {
		this.roadDirectionality = roadDirectionality;
	}

	public FeasibilityRequirements getFeasibilityRequirements() {
		return feasibilityRequirements;
	}

	public void setFeasibilityRequirements(FeasibilityRequirements feasibilityRequirements) {
		this.feasibilityRequirements = feasibilityRequirements;
	}

}
