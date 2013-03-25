package org.eclipselabs.resourceselector.core.processor;

import org.eclipse.core.resources.IProject;

/**
 * ResourceProcessorFactory interface. The role of the implementations of this class
 * is to create new Resource Processors of a given kind.
 * 
 * @author mvanbesien
 * 
 */
public interface ResourceProcessorFactory {

	/**
	 * Creates a new Resource Processor instance, with input parameters
	 * 
	 * @param project
	 *            Input IProject
	 * @param pattern
	 *            Input pattern
	 * @return new Resource Processor Instance
	 */
	public ResourceProcessor createResourceProcessor(IProject project, String pattern);

}
