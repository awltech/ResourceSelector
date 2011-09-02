package org.eclipselabs.resourceselector.processor.java;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;

/**
 * Java extension of TypeProcessorFactory
 * 
 * @author A125788
 * 
 */
public class JavaTypeProcessorFactory implements ResourceProcessorFactory {

	public ResourceProcessor createResourceProcessor(IProject project, String pattern) {
		return new JavaTypeProcessor(project, pattern);
	}

}
