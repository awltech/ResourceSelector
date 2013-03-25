package org.eclipselabs.resourceselector.processor.uml;

import java.util.regex.Pattern;

/**
 * Tool class to perform pattern matching on UML Elements
 * 
 * @author mvanbesien
 * 
 */
public class UMLMatcher {

	/**
	 * Initial Patterns as String
	 */
	private String pattern;

	/**
	 * Pattern dedicated to Type names.
	 */
	private Pattern typePattern;

	/**
	 * Pattern dedicated to Types' Package names
	 */
	private Pattern packagePattern;

	/**
	 * Creates a new Instance of UML Matcher from the pattern passed as
	 * parameter
	 * 
	 * @param pattern
	 *            : Pattern as String
	 */
	public UMLMatcher(String pattern) {
		this.pattern = pattern;
		this.init();
	}

	/**
	 * Initializes the UML Matcher by parsing the String pattern
	 * 
	 */
	private void init() {
		String typePatternAsString = this.pattern;
		String packagePatternAsString = null;

		if (typePatternAsString.contains(".")) {
			packagePatternAsString = typePatternAsString.substring(0,
					typePatternAsString.lastIndexOf("."));
			typePatternAsString = typePatternAsString
					.substring(typePatternAsString.lastIndexOf(".") + 1);
		}

		if (packagePatternAsString != null) {
			packagePatternAsString = packagePatternAsString.replace(".", "[.]");
			packagePatternAsString = packagePatternAsString.replace("*", ".*");
			packagePatternAsString = packagePatternAsString
					.replace("..*", ".*");
		}
		if (!typePatternAsString.endsWith("*"))
			typePatternAsString += "*";
		typePatternAsString = typePatternAsString.replace("*", ".*");
		typePatternAsString = typePatternAsString.replace("..*", ".*");

		this.typePattern = Pattern.compile(typePatternAsString,
				Pattern.CASE_INSENSITIVE);
		if (packagePatternAsString != null)
			this.packagePattern = Pattern.compile(packagePatternAsString,
					Pattern.CASE_INSENSITIVE);

	}

	/**
	 * Tests if the names passed as parameters match with the internal Patterns.
	 * 
	 * @param packageName
	 *            : Type's Package name
	 * @param typeName
	 *            : Type name
	 * @return true if both names match, false otherwise.
	 */
	public boolean match(String packageName, String typeName) {
		if (packageName != null && this.packagePattern != null)
			if (!this.packagePattern.matcher(packageName).matches())
				return false;
		if (typeName != null && this.typePattern != null)
			if (!this.typePattern.matcher(typeName).matches())
				return false;
		return true;
	}

}