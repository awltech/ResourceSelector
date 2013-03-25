package org.eclipselabs.resourceselector.processor.java.primitives;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;

/**
 * Extension of the TypeProcessorFactory for the Primitive Types
 * 
 * @author mvanbesien
 * 
 */
public class PrimitiveTypeProcessorFactory implements ResourceProcessorFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipselabs.resourceselector.core.processor.TypeProcessorFactory#
	 * createTypeProcessor(org.eclipse.core.resources.IProject,
	 * java.lang.String)
	 */
	public ResourceProcessor createResourceProcessor(IProject project, String pattern) {
		return new PrimitiveTypeProcessor(project, pattern);
	}

}
