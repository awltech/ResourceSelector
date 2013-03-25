package org.eclipselabs.resourceselector.processor.uml;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipselabs.resourceselector.core.filters.DefaultTypeFilter;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;
import org.eclipselabs.resourceselector.core.selector.ResourcePreferences;

/**
 * Type processor extension for UML Models
 * 
 * if the UML Resource field is set, this file only will be processed. Unless,
 * the entry project will be processed to retrieve all contained UML Resources,
 * to finally process them.
 * 
 * @author mvanbesien
 * 
 */
public class UMLTypeProcessor extends ResourceProcessor {

	/**
	 * Model file extension value
	 */
	public static String fileExtension = "uml";

	/**
	 * 
	 */
	protected ResourceSet resourceSet;

	/**
	 * Creates new UML Type Processor with input parameters
	 * 
	 * @param resourceSet
	 * 
	 * @param project
	 *            : input IProject
	 * @param pattern
	 *            : input pattern
	 */
	public UMLTypeProcessor(ResourceSet resourceSet, IProject project,
			String pattern) {
		this.resourceSet = resourceSet;
		this.iProject = project;
		this.pattern = pattern;
	}

	/**
	 * Sets the file extension used to filter resources in project
	 * 
	 * @param fileExtension
	 *            : file extension
	 */
	public static void setFileExtension(String fileExtension) {
		if (fileExtension != null)
			UMLTypeProcessor.fileExtension = fileExtension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipselabs.resourceselector.core.processor.TypeProcessor#process()
	 */
	@Override
	protected void process() {

		Collection<UMLTypeInfo> typeInfos = null;

		if (this.resourceSet == null) {
			this.resourceSet = new ResourceSetImpl();
			typeInfos = this.processProject();
		} else
			typeInfos = this.processResourceSet(this.resourceSet);
		if (typeInfos == null)
			return;

		UMLMatcher matcher = new UMLMatcher(this.pattern);
		DefaultTypeFilter filter = null;
		if (this.filter instanceof DefaultTypeFilter)
			filter = (DefaultTypeFilter) this.filter;
		for (ResourceInfo typeInfo : typeInfos)
			if (filter == null
					|| !ResourcePreferences.getInstance().isFilterEnabled()
					|| filter.matches(typeInfo.getPackageName() + "."
							+ typeInfo.getElementName()))
				if (matcher.match(typeInfo.getPackageName(), typeInfo
						.getElementName()))
					this.searchResults.add(typeInfo);
	}

	/**
	 * Processes a single UML resource
	 * 
	 * @param umlResource
	 *            UML Resource
	 * @return Collection of UML TypeInfos
	 */
	protected Collection<UMLTypeInfo> processResourceSet(ResourceSet resourceSet) {
		Collection<UMLTypeInfo> umlTypeInfos = new ArrayList<UMLTypeInfo>();
		Collection<Resource> resources = new ArrayList<Resource>();
		resources.addAll(resourceSet.getResources());
		for (Resource resource : resources)
			if (resource instanceof UMLResource
					&& !resource.getURI().scheme().equals("pathmap"))
				umlTypeInfos.addAll(new UMLResourceProcessor()
						.lookForTypesInResource(resource));
		return umlTypeInfos;
	}

	/**
	 * Processes the input IProject to look for UML Resources, to finally
	 * process them.
	 * 
	 * @return Collection of UML TypeInfos
	 */
	protected Collection<UMLTypeInfo> processProject() {
		Collection<UMLTypeInfo> umlTypeInfos = new ArrayList<UMLTypeInfo>();
		this.processContainer(this.iProject, umlTypeInfos);
		return umlTypeInfos;
	}

	/**
	 * Processes a container. This method looks in the container's children for
	 * files and folders. If folder is found, recursive process is done. If file
	 * is found and is an UML Resource, it is processed.
	 * 
	 * @param iContainer
	 *            IContainer
	 * @param umlTypeInfos
	 *            Collection of UMLTypeInfos
	 */
	protected void processContainer(IContainer iContainer,
			Collection<UMLTypeInfo> umlTypeInfos) {
		try {
			for (IResource sub : iContainer.members())
				if (sub instanceof IContainer)
					this.processContainer((IContainer) sub, umlTypeInfos);
				else if (sub instanceof IFile) {
					IFile file = (IFile) sub;
					if (file.getFileExtension() != null
							&& UMLTypeProcessor.fileExtension.equals(file
									.getFileExtension()))
						umlTypeInfos.addAll(UMLModelsCache.getInstance()
								.getTypeInfosForModel(file, this.resourceSet));
				}
		} catch (CoreException e) {
			Activator.getDefault().logError(
					"An Exception has been thrown while "
							+ "listing IResources in IContainer", e);
		}
	}
}
