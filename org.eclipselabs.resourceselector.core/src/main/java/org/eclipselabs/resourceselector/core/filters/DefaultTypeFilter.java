package org.eclipselabs.resourceselector.core.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Filter dedicated to Java Types
 * 
 * @author mvanbesien
 */
public class DefaultTypeFilter extends Filter {

	/**
	 * Enumeration to determine the scope of the Filter.
	 * 
	 * @author mvanbesien
	 * 
	 */
	public static enum TypeFilterScope {
		TYPES_ONLY, PACKAGE_ONLY, TYPES_AND_PACKAGES
	}

	private static final String DOT = ".";

	/**
	 * List of compiled patterns to filter package names.
	 */
	protected List<Pattern> packagePatterns;

	/**
	 * List of compiled patterns to filter type names.
	 */
	protected List<Pattern> typePatterns;

	/**
	 * Scope of the current Filter
	 */
	private TypeFilterScope scope;

	/**
	 * @return current filter scope
	 */
	public TypeFilterScope getScope() {
		return this.scope;
	}

	/**
	 * Adds a Package filter Pattern, from pattern as String
	 * 
	 * @param patternAsString
	 *            : Pattern as String (Regular Expression)
	 */
	public void addPackagePattern(String patternAsString) {
		boolean found = false;
		for (Iterator<Pattern> patterns = this.packagePatterns.iterator(); patterns.hasNext() && !found;)
			found = patternAsString.equals(patterns.next().pattern());
		if (!found)
			this.packagePatterns.add(Pattern.compile(patternAsString, Pattern.CASE_INSENSITIVE));
	}

	/**
	 * Adds a Type filter Pattern, from pattern as String
	 * 
	 * @param patternAsString
	 *            : Pattern as String (Regular Expression)
	 */
	public void addTypePattern(String patternAsString) {
		boolean found = false;
		for (Iterator<Pattern> patterns = this.typePatterns.iterator(); patterns.hasNext() && !found;)
			found = patternAsString.equals(patterns.next().pattern());
		if (!found)
			this.typePatterns.add(Pattern.compile(patternAsString, Pattern.CASE_INSENSITIVE));
	}

	/**
	 * Removes a Package filter Pattern, from pattern as String
	 * 
	 * @param patternAsString
	 *            : Pattern as String (Regular Expression)
	 */
	public void removePackagePattern(String patternAsString) {
		boolean found = false;
		for (Iterator<Pattern> patterns = this.packagePatterns.iterator(); patterns.hasNext() && !found;) {
			Pattern pattern = patterns.next();
			found = patternAsString.equals(pattern.pattern());
			if (found)
				this.packagePatterns.remove(pattern);
		}
	}

	/**
	 * Removes a Type filter Pattern, from pattern as String
	 * 
	 * @param patternAsString
	 *            : Pattern as String (Regular Expression)
	 */
	public void removeTypePattern(String patternAsString) {
		boolean found = false;
		for (Iterator<Pattern> patterns = this.typePatterns.iterator(); patterns.hasNext() && !found;) {
			Pattern pattern = patterns.next();
			found = patternAsString.equals(pattern.pattern());
			if (found)
				this.typePatterns.remove(pattern);
		}
	}

	/**
	 * Creates a new Type Filter instance
	 * 
	 * @param name
	 *            : Name of the Type Filter instance.
	 */
	public DefaultTypeFilter(String name, TypeFilterScope scope) {
		super(name, true);
		this.scope = scope;
		this.packagePatterns = new ArrayList<Pattern>();
		this.typePatterns = new ArrayList<Pattern>();
	}

	/**
	 * Checks is the Package name passed as parameter matches the Patterns
	 * dedicated to Packages, contained in this Filter.
	 * 
	 * @param packageName
	 *            : Package Name
	 * @return true if name matches, false otherwise
	 */
	public boolean packageMatches(String packageName) {
		if (this.packagePatterns.size() == 0 || TypeFilterScope.TYPES_ONLY.equals(this.scope))
			return true;
		for (Pattern pattern : this.packagePatterns)
			if (pattern.matcher(packageName).matches())
				return true;
		return false;
	}

	/**
	 * Checks is the Type name passed as parameter matches the Patterns
	 * dedicated to Types, contained in this Filter.
	 * 
	 * @param typeName
	 *            : Types Name
	 * @return true if name matches, false otherwise
	 */
	public boolean typeMatches(String typeName) {
		if (this.typePatterns.size() == 0 || TypeFilterScope.PACKAGE_ONLY.equals(this.scope))
			return true;
		for (Pattern pattern : this.typePatterns)
			if (pattern.matcher(typeName).matches())
				return true;
		return false;
	}

	/**
	 * Checks is the Fully Qualified Name passed as parameter matches the
	 * Patterns dedicated to Packages and Types, contained in this Filter.
	 * 
	 * @param fullyQualifiedName
	 *            : Type Fully Qualified Name
	 * @return true if name matches, false otherwise
	 */
	public boolean matches(String fullyQualifiedName) {
		String packagePattern = null;
		String typePattern = fullyQualifiedName;
		if (fullyQualifiedName.contains(DefaultTypeFilter.DOT)) {
			packagePattern = typePattern.substring(0, typePattern.lastIndexOf(DefaultTypeFilter.DOT));
			typePattern = typePattern.substring(typePattern.lastIndexOf(DefaultTypeFilter.DOT) + 1);
		}
		if (packagePattern == null)
			return this.typeMatches(typePattern);
		else
			return this.packageMatches(packagePattern) && this.typeMatches(typePattern);
	}

}
