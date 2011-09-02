package org.eclipselabs.resourceselector.core.filters;

/**
 * Filter instance dedicated to Resource Processors
 * 
 * @author mvanbesien
 * 
 */
public class Filter {

	/**
	 * Name of the current Filter
	 */
	private String filterName;

	/**
	 * property telling if filter can be disabled
	 */
	private boolean canDisable;

	/**
	 * Filter constructor
	 * 
	 * @param name
	 *            : Filter name
	 * @param canDisable
	 *            : true if filter can be disabled by user, false otherwise
	 */
	public Filter(String name, boolean canDisable) {
		this.filterName = name;
		this.canDisable = canDisable;
	}

	/**
	 * Returns this Resource Filter's name
	 * 
	 * @return name
	 */
	public String getName() {
		return this.filterName;
	}

	/**
	 * Returns whether the user will be able to disable this filter or not.
	 * 
	 * @return user
	 */
	public boolean canDisable() {
		return this.canDisable;
	}
}
