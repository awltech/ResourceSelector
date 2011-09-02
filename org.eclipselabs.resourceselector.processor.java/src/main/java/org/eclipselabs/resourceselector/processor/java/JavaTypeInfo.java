package org.eclipselabs.resourceselector.processor.java;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * Java extension of the TypeInfo object
 * 
 * @author mvanbesien
 * 
 */
public class JavaTypeInfo extends ResourceInfo {

	/**
	 * Enumeration defining the different Java Type kinds
	 * 
	 * @author mvanbesien
	 */
	public static enum JavaTypeKind {
		CLASS, INTERFACE, ENUM;

		@Override
		public String toString() {
			String name = super.toString();
			return name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
		}
	}

	/**
	 * Enumeration defining the different Java Type possible visibilities
	 * 
	 * @author mvanbesien
	 */
	public static enum JavaTypeVisibility {
		PUBLIC, PRIVATE, PROTECTED, PACKAGE;

		@Override
		public String toString() {
			if (this.equals(PACKAGE))
				return "";
			return super.toString().toLowerCase();
		}
	}

	/**
	 * Location Separator Constant
	 */
	public static final String LOCATION_SEPARATOR = "|";

	/**
	 * Path Separator Constant
	 */
	public static final String PATH_SEPARATOR = "/";

	/**
	 * Package Separator Constant
	 */
	public static final String PACKAGE_SEPARATOR = ".";

	/**
	 * Type Separator Constant
	 */
	public static final String TYPE_SEPARATOR = "$";

	/**
	 * Class File Extension Constant
	 */
	private static final String CLASS_EXTENSION = ".class";

	/**
	 * Modifiers of the Java TypeInfo
	 */
	private int modifiers;

	/**
	 * Created new Java TypeInfo
	 * 
	 * @param elementName
	 *            : name of element
	 * @param packageName
	 *            : name of the package containing the element
	 * @param location
	 *            : location of the resource containing the element
	 * @param modifiers
	 *            : element's modifiers
	 */
	public JavaTypeInfo(String elementName, String packageName, String location, String[] enclosingNames, int modifiers) {
		super(elementName, packageName, location, enclosingNames);
		this.modifiers = modifiers;
		if (location.indexOf(JavaTypeInfo.LOCATION_SEPARATOR) > -1) {
			this.additionalDescription = location.substring(0, location.indexOf(JavaTypeInfo.LOCATION_SEPARATOR));
			this.containerName = location.substring(location.indexOf(JavaTypeInfo.LOCATION_SEPARATOR) + 1).replace(
					JavaTypeInfo.PATH_SEPARATOR, JavaTypeInfo.PACKAGE_SEPARATOR);

		} else if (location.indexOf(JavaTypeInfo.PATH_SEPARATOR) > -1) {
			this.additionalDescription = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(location))
					.getProject().getName();
			this.containerName = this.packageName + JavaTypeInfo.PACKAGE_SEPARATOR + this.elementName;
		}

		if (this.containerName.indexOf(JavaTypeInfo.TYPE_SEPARATOR) > -1
				&& !this.containerName.endsWith(JavaTypeInfo.TYPE_SEPARATOR))
			this.containerName = this.containerName.substring(0, this.containerName
					.indexOf(JavaTypeInfo.TYPE_SEPARATOR));

		if (this.containerName.endsWith(JavaTypeInfo.CLASS_EXTENSION))
			this.containerName = this.containerName.substring(0, this.containerName.length()
					- JavaTypeInfo.CLASS_EXTENSION.length());

		if (!this.isInnerElement && this.containerName.contains(JavaTypeInfo.PACKAGE_SEPARATOR))
			this.containerName = this.containerName.substring(0, this.containerName
					.lastIndexOf(JavaTypeInfo.PACKAGE_SEPARATOR));

		String jreName = JREHelper.getInstance().getJREName(this.additionalDescription);

		if (jreName != null)
			this.additionalDescription = jreName;

		this.image = JavaImagesManager.getImage(this);
		this.containerImage = JavaImagesManager.getContainerImage(this);
	}

	/**
	 * @return Kind of this Type
	 */
	public JavaTypeKind getTypeKind() {
		if (Flags.isEnum(this.modifiers))
			return JavaTypeKind.ENUM;
		if (Flags.isInterface(this.modifiers))
			return JavaTypeKind.INTERFACE;
		return JavaTypeKind.CLASS;
	}

	/**
	 * @return Visibility of this Type
	 */
	public JavaTypeVisibility getTypeVisibility() {
		if (Flags.isPublic(this.modifiers))
			return JavaTypeVisibility.PUBLIC;
		if (Flags.isProtected(this.modifiers))
			return JavaTypeVisibility.PROTECTED;
		if (Flags.isPrivate(this.modifiers))
			return JavaTypeVisibility.PRIVATE;
		return JavaTypeVisibility.PACKAGE;
	}

}
