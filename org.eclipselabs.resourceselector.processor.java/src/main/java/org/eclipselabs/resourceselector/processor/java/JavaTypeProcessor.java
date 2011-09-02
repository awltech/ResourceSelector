package org.eclipselabs.resourceselector.processor.java;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipselabs.resourceselector.core.filters.DefaultTypeFilter;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.selector.ResourcePreferences;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo.JavaTypeVisibility;

/**
 * Java extension of Type Processor
 * 
 * @author mvanbesien
 * 
 */
public class JavaTypeProcessor extends ResourceProcessor {

	/**
	 * Creates a new Java Type Processor with input project and pattern
	 * 
	 * @param project
	 *            : input IProject
	 * @param pattern
	 *            : input pattern
	 */
	public JavaTypeProcessor(IProject project, String pattern) {
		this.iProject = project;
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
		IJavaSearchScope javaSearchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { JavaCore
				.create(this.iProject) });

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
					IJavaSearchConstants.CLASS_AND_INTERFACE, javaSearchScope, new JavaTypeNameRequestor(this),
					IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
			searchEngine.searchAllTypeNames(packagePattern == null ? null : packagePattern.toCharArray(),
					SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH, typePattern.toCharArray(),
					SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH,
					IJavaSearchConstants.ENUM, javaSearchScope, new JavaTypeNameRequestor(this),
					IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
		} catch (JavaModelException e) {
			Activator.getDefault().logError("Error while processing Java Types", e);
		}
	}

	/**
	 * Protected tool method, to be able to add new result from inner classes of
	 * this processor.
	 */
	protected void addResult(JavaTypeInfo javaTypeInfo) {
		this.searchResults.add(javaTypeInfo);
	}

	/**
	 * Inner class extending the JDT Type Name Requestor. This class is in
	 * charge of determining if a Type is valid or not, according to filter and
	 * pattern.
	 * 
	 * @author mvanbesien
	 * 
	 */
	private static class JavaTypeNameRequestor extends TypeNameRequestor {

		/**
		 * Type Filter
		 */
		private DefaultTypeFilter javaTypeFilter;

		/**
		 * true is filter is currently enabled, false otherwise
		 */
		private boolean isFilterEnabled;

		/**
		 * Current Java Type Processor
		 */
		private JavaTypeProcessor javaTypeProcessor;

		/**
		 * Creates a new Java TypeNameRequestor linked with current
		 * JavaTypeProcessor
		 * 
		 * @param javaTypeProcessor
		 *            JavaTypeProcessor
		 */
		public JavaTypeNameRequestor(JavaTypeProcessor javaTypeProcessor) {
			this.javaTypeProcessor = javaTypeProcessor;
			this.isFilterEnabled = ResourcePreferences.getInstance().isFilterEnabled();
			if (javaTypeProcessor.filter != null && javaTypeProcessor.filter instanceof DefaultTypeFilter)
				this.javaTypeFilter = (DefaultTypeFilter) javaTypeProcessor.filter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jdt.core.search.TypeNameRequestor#acceptType(int,
		 * char[], char[], char[][], java.lang.String)
		 */
		@Override
		public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
				String path) {

			String packageNameAsString = new String(packageName);
			String simpleTypeNameAsString = new String(simpleTypeName);
			String[] enclosingNames = new String[enclosingTypeNames.length];
			for (int i = 0; i < enclosingTypeNames.length; i++)
				enclosingNames[i] = new String(enclosingTypeNames[i]);

			if (this.isFilterEnabled && this.javaTypeFilter != null) {
				if (this.javaTypeFilter.packageMatches(packageNameAsString)
						&& this.javaTypeFilter.typeMatches(simpleTypeNameAsString)) {
					JavaTypeInfo javaTypeInfo = new JavaTypeInfo(simpleTypeNameAsString, packageNameAsString, path,
							enclosingNames, modifiers);
					if (this.isJavaTypeInfoValid(javaTypeInfo))
						this.javaTypeProcessor.addResult(javaTypeInfo);
				}
			} else {
				JavaTypeInfo javaTypeInfo = new JavaTypeInfo(simpleTypeNameAsString, packageNameAsString, path,
						enclosingNames, modifiers);
				if (this.isJavaTypeInfoValid(javaTypeInfo))
					this.javaTypeProcessor.addResult(javaTypeInfo);
			}
		}

		/**
		 * Filter on JavaTypeInfos, to remove not valid classes (e.g. private
		 * inner classes)
		 * 
		 * @param javaTypeInfo
		 *            JavaTypeInfo
		 * @return true if valid, false otherwise
		 */
		private boolean isJavaTypeInfoValid(JavaTypeInfo javaTypeInfo) {
			if (javaTypeInfo.isInnerElement() && JavaTypeVisibility.PRIVATE.equals(javaTypeInfo.getTypeVisibility()))
				return false;
			// TODO Deal with package-visible classes
			return true;
		}
	};
}
