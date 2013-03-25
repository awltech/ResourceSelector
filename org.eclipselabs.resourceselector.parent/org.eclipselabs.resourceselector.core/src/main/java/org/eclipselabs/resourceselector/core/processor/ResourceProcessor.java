package org.eclipselabs.resourceselector.core.processor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.filters.Filter;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * Abstract implementation of a Resource Processor. A Resource processor is a Runnable
 * implementation, which role is to retrieve valid Resources from an input IProject,
 * an input filter and an input pattern.
 * 
 * @author mvanbesien
 * 
 */
public abstract class ResourceProcessor implements Runnable {

	/**
	 * Constant
	 */
	protected static final String DOT = ".";

	/**
	 * Input IProject
	 */
	protected IProject iProject;

	/**
	 * Input pattern
	 */
	protected String pattern;

	/**
	 * Search-process results
	 */
	protected List<ResourceInfo> searchResults = new ArrayList<ResourceInfo>();

	/**
	 * Input filter
	 */
	protected Filter filter;

	/**
	 * Current search Status
	 */
	private SearchStatus searchStatus = SearchStatus.NOT_STARTED;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this.searchStatus = SearchStatus.IN_PROGRESS;
		this.process();
		this.searchStatus = SearchStatus.FINISHED;
	}

	/**
	 * Method to implement. This method will contain the searching process from
	 * the different input objects. At process end, all valid results are stored
	 * in the searchResults list.
	 */
	protected abstract void process();

	/**
	 * Specifies a Filter to this Resource Processor
	 * 
	 * @param filter
	 *            Filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Returns all the valid search results, fetched be this processor.
	 * 
	 * @return List<ResourceInfo>
	 */
	public List<ResourceInfo> getSearchResults() {
		return this.searchResults;
	}

	/**
	 * Returns the current Resource Processor Status
	 * 
	 * @return SearchStatus
	 */
	public SearchStatus getSearchStatus() {
		return this.searchStatus;
	}

	public void cancel() {
	}

}
