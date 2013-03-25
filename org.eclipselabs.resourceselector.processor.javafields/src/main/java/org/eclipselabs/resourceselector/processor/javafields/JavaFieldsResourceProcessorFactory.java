package org.eclipselabs.resourceselector.processor.javafields;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;

/**
 * Resource Processor Factory for Java Methods
 * @author mvanbesien
 *
 */
public class JavaFieldsResourceProcessorFactory implements ResourceProcessorFactory {

	/*
	 * (non-Javadoc)
	 * @see org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory#createResourceProcessor(org.eclipse.core.resources.IProject, java.lang.String)
	 */
	public ResourceProcessor createResourceProcessor(IProject project, String pattern) {
		return new JavaFieldsResourceProcessor(project, pattern);
	}

}
