package org.eclipselabs.resourceselector.core.selector;

/**
 * Instance of Class dedicated to static preferences for Resources management.
 * 
 * @author mvanbesien
 * 
 */
public class ResourcePreferences {

	/**
	 * Instance
	 */
	private static ResourcePreferences instance;

	/**
	 * @return Class Instance containing preferences for Resources management
	 */
	public static ResourcePreferences getInstance() {
		if (ResourcePreferences.instance == null)
			ResourcePreferences.instance = new ResourcePreferences();
		return ResourcePreferences.instance;
	}

	/**
	 * <code>Enable Filter</code> preference
	 */
	private boolean enableFilter = true;

	/**
	 * @return the <code>Enable Filter</code> preference
	 */
	public boolean isFilterEnabled() {
		return this.enableFilter;
	}

	/**
	 * Sets the <code>Enable Filter</code> preference
	 * 
	 * @param enableFilter
	 */
	public void setFilterEnabled(boolean enableFilter) {
		this.enableFilter = enableFilter;
	}

}
