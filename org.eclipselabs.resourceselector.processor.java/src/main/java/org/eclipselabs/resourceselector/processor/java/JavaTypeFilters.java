package org.eclipselabs.resourceselector.processor.java;

import org.eclipselabs.resourceselector.core.filters.DefaultTypeFilter;
import org.eclipselabs.resourceselector.core.filters.DefaultTypeFilter.TypeFilterScope;

/**
 * Static class to propose default Java Types Filters
 * 
 * @author mvanbesien
 */
public class JavaTypeFilters {

	/**
	 * Returns the "Most used JAVA Types" Filter.
	 * 
	 * This Filter will allow all types, directly contained in the
	 * <code>java.lang</code>, <code>java.util</code> and <code>java.io</code>
	 * packages
	 * 
	 * @return Most used JAVA Types Filter instance.
	 */
	public static DefaultTypeFilter getMostUsedJavaTypesFilter() {
		DefaultTypeFilter filter = new DefaultTypeFilter("Most used JAVA Types", TypeFilterScope.PACKAGE_ONLY);
		filter.addPackagePattern("^java[.](lang|util|sql|io|text)");
		return filter;
	}

}
