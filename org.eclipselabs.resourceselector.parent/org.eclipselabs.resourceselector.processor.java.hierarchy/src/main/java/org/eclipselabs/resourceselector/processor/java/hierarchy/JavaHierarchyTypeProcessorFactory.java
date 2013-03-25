package org.eclipselabs.resourceselector.processor.java.hierarchy;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;

/**
 * Java Hierarchy extension of Type Processor Factory
 * 
 * @author mvanbesien
 * 
 */
public class JavaHierarchyTypeProcessorFactory implements ResourceProcessorFactory {

	/**
	 * Class that will be used to determine filtering in Scope
	 */
	private Class<?> clazz;

	/**
	 * Constructor that provides Class to filter on.
	 */
	public JavaHierarchyTypeProcessorFactory(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipselabs.resourceselector.core.processor.TypeProcessorFactory#
	 * createTypeProcessor(org.eclipse.core.resources.IProject,
	 * java.lang.String)
	 */
	public ResourceProcessor createResourceProcessor(IProject project, String pattern) {
		JavaHierarchyTypeProcessor javaHierarchyTypeProcessor = new JavaHierarchyTypeProcessor(project, pattern);
		javaHierarchyTypeProcessor.setTopHierarchyClass(this.clazz);
		return javaHierarchyTypeProcessor;
	}

}
