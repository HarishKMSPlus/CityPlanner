package com.cityplanner.client.templates;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface ResultTemplates extends SafeHtmlTemplates {

	@Template("The road should be {0} meters ({1} lanes) wide & {2} way.")
	SafeHtml commonResult(String meters, String lanes, String oneTwoWay);

	@Template("The road should be {0} meters ({1} lanes) wide & {2} way."
		+ "\nHowever, a road {3} meters wide is required to handle the traffic density."
		+ "\nA budget of {4} rupees & a build time of {5} days is required to accomplish this.")
	SafeHtml trafficDensityAdjustedResult(String meters, String lanes,
		String oneTwoWay, String requiredMeters, String rupees, String days);

}
