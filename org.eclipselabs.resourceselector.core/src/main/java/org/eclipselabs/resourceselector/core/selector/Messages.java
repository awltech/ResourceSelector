package org.eclipselabs.resourceselector.core.selector;

/**
 * Messages for the Resource Selector
 * 
 * @author mvanbesien
 * 
 */
public class Messages {

	/**
	 * Resource Selector Dialog Box Title
	 */
	static final String DIALOG_TITLE = "Resource Selector";

	/**
	 * Pattern Field Description
	 */
	static final String PATTERN_FIELD_DESCRIPTION = "Choose a Resource:";

	/**
	 * Results List Description
	 */
	static final String RESULTS_LIST_DESCRIPTION = "Matching Resources:";

	/**
	 * OK Button Description
	 */
	static final String OK_BUTTON = "OK";

	/**
	 * Cancel Button Description
	 */
	static final String CANCEL_BUTTON = "Cancel";

	/**
	 * Filter Checkbox button description
	 */
	static final String FILTER_BUTTON = "Enable filter";

	/**
	 * Empty String
	 */
	static final String EMPTY_STR = "";

	/**
	 * Searching Label Description.
	 */
	static final String SEARCH_IN_PROGRESS = "(Searching...)";

	/**
	 * Returns formatted String for Results Amount Description
	 * 
	 * @param amount
	 *            : Amount
	 * @param time
	 *            : Time
	 * @return String
	 */
	static final String getResultAmountDescription(int amount) {
		return String.format("(Found %d result%s.)", amount, amount > 1 ? "s" : "");

	}

	/**
	 * Returns formatted String for Populating Description
	 * 
	 * @param amount
	 *            : Amount
	 * @param time
	 *            : Time
	 * @return String
	 */
	static final String getPopulatingDescription(int pct) {
		return String.format("(Populating list... %d%%)", pct);

	}

}
