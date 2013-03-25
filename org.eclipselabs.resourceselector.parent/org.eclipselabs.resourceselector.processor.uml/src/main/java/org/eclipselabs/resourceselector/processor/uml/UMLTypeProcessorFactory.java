package org.eclipselabs.resourceselector.processor.uml;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;

/**
 * TypeProcessorFactory extension for UML Types.
 * 
 * If an additional resource is specified, it will be transmitted to created
 * TypeProcessors (See UMLTypeProcessor javadow to know why)
 * 
 * @author mvanbesien
 * 
 */
public class UMLTypeProcessorFactory implements ResourceProcessorFactory {

	/**
	 * ResourceSet to use while processing elements
	 */
	protected ResourceSet resourceSet;

	/**
	 * Creates a new Factory with UML Resource
	 * 
	 * @param resource
	 *            UML Resource
	 */
	public UMLTypeProcessorFactory(Resource resource) {
		this.resourceSet = resource.getResourceSet();
	}

	/**
	 * Creates new Factory
	 */
	public UMLTypeProcessorFactory(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipselabs.resourceselector.core.processor.TypeProcessorFactory#
	 * createTypeProcessor(org.eclipse.core.resources.IProject,
	 * java.lang.String)
	 */
	public ResourceProcessor createResourceProcessor(IProject project,
			String pattern) {
		UMLTypeProcessor processor = new UMLTypeProcessor(this.resourceSet,
				project, pattern);
		return processor;
	}

}
