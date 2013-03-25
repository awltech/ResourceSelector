package org.eclipselabs.resourceselector.processor.java.hierarchy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo.JavaTypeVisibility;

/**
 * Type Processor Extension for Java Hierarchy management
 * 
 * @author mvanbesien
 * 
 */
public class JavaHierarchyTypeProcessor extends ResourceProcessor {

	/**
	 * IJava project corresponding to input IProject
	 */
	private IJavaProject javaProject;
	private Class<?> topHierarchyClass;

	/**
	 * Creates new Java Hierarchy Type Processor with input parameters
	 * 
	 * @param project
	 *            input IProject
	 * @param pattern
	 *            pattern
	 */
	public JavaHierarchyTypeProcessor(IProject project, String pattern) {
		this.iProject = project;
		this.javaProject = JavaCore.create(this.iProject);
		this.pattern = pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipselabs.resourceselector.core.processor.TypeProcessor#process()
	 */
	@Override
	protected void process() {

		IType type = null;
		IJavaSearchScope javaSearchScope = null;
		if (this.topHierarchyClass == null)
			return;
		try {
			type = this.javaProject.findType(this.topHierarchyClass.getCanonicalName());
			javaSearchScope = HierarchyScopesCache.getInstance().getScope(type);
		} catch (JavaModelException e1) {
			Activator.getDefault().logError("Error while Creating Java Hierarchy", e1);
		}

		if (type == null || javaSearchScope == null)
			return;

		TypeNameRequestor typeNameRequestor = new TypeNameRequestor() {

			@Override
			public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName,
					char[][] enclosingTypeNames, String path) {

				String packageNameAsString = new String(packageName);
				String simpleTypeNameAsString = new String(simpleTypeName);

				JavaTypeInfo JavaTypeInfo = new JavaTypeInfo(simpleTypeNameAsString, packageNameAsString, path, null,
						modifiers);
				if (this.isJavaTypeInfoValid(JavaTypeInfo))
					JavaHierarchyTypeProcessor.this.addResult(JavaTypeInfo);
			}

			/**
			 * Filter on JavaTypeInfos, to remove not valid classes (e.g.
			 * private inner classes)
			 * 
			 * @param javaTypeInfo
			 *            JavaTypeInfo
			 * @return true if valid, false otherwise
			 */
			private boolean isJavaTypeInfoValid(JavaTypeInfo javaTypeInfo) {
				if (javaTypeInfo.isInnerElement()
						&& JavaTypeVisibility.PRIVATE.equals(javaTypeInfo.getTypeVisibility()))
					return false;
				// TODO Deal with package-visible classes
				return true;
			}
		};

		SearchEngine searchEngine = new SearchEngine();

		String packagePattern = null;
		String typePattern = this.pattern;
		if (!typePattern.endsWith("*"))
			typePattern += "*";
		if (typePattern.contains(ResourceProcessor.DOT)) {
			packagePattern = typePattern.substring(0, typePattern.lastIndexOf(ResourceProcessor.DOT));
			typePattern = typePattern.substring(typePattern.lastIndexOf(ResourceProcessor.DOT) + 1);
		}
		try {
			searchEngine.searchAllTypeNames(packagePattern == null ? null : packagePattern.toCharArray(),
					SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH, typePattern.toCharArray(),
					SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH,
					IJavaSearchConstants.CLASS_AND_INTERFACE, javaSearchScope, typeNameRequestor,
					IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, new NullProgressMonitor());
		} catch (JavaModelException e) {
			Activator.getDefault().logError("Error while processing Jave Types in Hierarchy.", e);
		}
	}

	/**
	 * Adds new result to search results list
	 * 
	 * @param JavaTypeInfo
	 *            new result
	 */
	protected void addResult(JavaTypeInfo javaTypeInfo) {
		this.searchResults.add(javaTypeInfo);
	}

	@Override
	public void cancel() {
	}

	/**
	 * @param Top Hierarchy Class of this Hierarchy Processor
	 */
	public void setTopHierarchyClass(Class<?> clazz) {
		this.topHierarchyClass = clazz;
	}
	
	/**
	 * @return Top Hierarchy Class of this Hierarchy Processor
	 */
	public Class<?> getTopHierarchyClass() {
		return topHierarchyClass;
	}
}
