package org.eclipselabs.resourceselector.processor.java;

import org.eclipse.swt.graphics.Image;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo.JavaTypeKind;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo.JavaTypeVisibility;

/**
 * Manages Images for Java Types
 * 
 * @author mvanbesien
 * 
 */
public class JavaImagesManager {

	/**
	 * Location of JAVA Images in the Eclipse dedicated Bundle
	 */
	private static final String IMAGES_LOCATION_IN_BUNDLE = "/icons/full/obj16/";

	/**
	 * ID of the JAVA Images Eclipse dedicated bundle
	 */
	private static final String BUNDLE_ID = "org.eclipse.jdt.ui";

	/**
	 * Class Tag
	 */
	private static final String TAG_CLASS = "class";

	/**
	 * Interface Tag
	 */
	private static final String TAG_INTERFACE = "int";

	/**
	 * Inner Class Tag
	 */
	private static final String TAG_INNER_CLASS = "innerclass";

	/**
	 * Inner Interface Tag
	 */
	private static final String TAG_INNER_INTERFACE = "innerinterface";

	/**
	 * Enumeration Tag
	 */
	private static final String TAG_ENUM = "enum";

	/**
	 * Default visibility Tag
	 */
	private static final String TAG_DEFAULT = "default";

	/**
	 * Suffix Tag
	 */
	private static final String TAG_SUFFIX = "_obj.gif";

	/**
	 * Public visibility Tag
	 */
	private static final String TAG_PUBLIC = "public";

	/**
	 * Private visibility Tag
	 */
	private static final String TAG_PRIVATE = "private";

	/**
	 * Protected visibility Tag
	 */
	private static final String TAG_PROTECTED = "protected";

	/**
	 * Package visibility Tag
	 */
	private static final String TAG_PACKAGE = "package";

	/**
	 * Returns Image associated with the container of the TypeInfo,which
	 * innerType property is passed as parameter.
	 * 
	 * @param isInnerType
	 *            : boolean
	 * @return Image
	 */
	public static Image getContainerImage(boolean isInnerType) {

		StringBuffer location = new StringBuffer();
		location.append(JavaImagesManager.IMAGES_LOCATION_IN_BUNDLE);
		if (!isInnerType)
			location.append(JavaImagesManager.TAG_PACKAGE);
		else
			location.append(JavaImagesManager.TAG_CLASS);
		location.append(JavaImagesManager.TAG_SUFFIX);

		return Activator.getDefault().getImage(JavaImagesManager.BUNDLE_ID, location.toString());
	}

	/**
	 * Returns Image associated with the container of the TypeInfo, passed as
	 * parameter
	 * 
	 * @param info
	 *            : TypeInfo
	 * @return image
	 */
	static Image getContainerImage(JavaTypeInfo info) {
		return JavaImagesManager.getContainerImage(info.isInnerElement());
	}

	/**
	 * Returns Image associated with the TypeInfo, passed as parameter
	 * 
	 * @param info
	 *            : TypeInfo
	 * @return image
	 */
	private static Image getTypeImage(JavaTypeKind kind, JavaTypeVisibility visibility, boolean isInnerType) {

		StringBuffer imagePath = new StringBuffer();
		imagePath.append(JavaImagesManager.IMAGES_LOCATION_IN_BUNDLE);

		if (!isInnerType) {
			if (kind == JavaTypeKind.CLASS)
				imagePath.append(JavaImagesManager.TAG_CLASS);
			else if (kind == JavaTypeKind.INTERFACE)
				imagePath.append(JavaImagesManager.TAG_INTERFACE);
			else if (kind == JavaTypeKind.ENUM)
				imagePath.append(JavaImagesManager.TAG_ENUM);

			if (visibility == JavaTypeVisibility.PACKAGE)
				imagePath.append("_" + JavaImagesManager.TAG_DEFAULT);
		} else {
			if (kind == JavaTypeKind.CLASS)
				imagePath.append(JavaImagesManager.TAG_INNER_CLASS);
			else if (kind == JavaTypeKind.INTERFACE)
				imagePath.append(JavaImagesManager.TAG_INNER_INTERFACE);
			else if (kind == JavaTypeKind.ENUM)
				imagePath.append(JavaImagesManager.TAG_ENUM);

			if (visibility == JavaTypeVisibility.PACKAGE)
				imagePath.append("_" + JavaImagesManager.TAG_DEFAULT);
			if (visibility == JavaTypeVisibility.PUBLIC)
				imagePath.append("_" + JavaImagesManager.TAG_PUBLIC);
			if (visibility == JavaTypeVisibility.PROTECTED)
				imagePath.append("_" + JavaImagesManager.TAG_PROTECTED);
			if (visibility == JavaTypeVisibility.PRIVATE)
				imagePath.append("_" + JavaImagesManager.TAG_PRIVATE);
		}
		imagePath.append(JavaImagesManager.TAG_SUFFIX);

		return Activator.getDefault().getImage(JavaImagesManager.BUNDLE_ID, imagePath.toString());

	}

	/**
	 * Returns Image associated with the TypeInfo, which nature and innerType
	 * property is passed as parameter.
	 * 
	 * @param nature
	 *            : TypeNature
	 * @param isInnerType
	 *            : boolean
	 * @return Image
	 */
	public static Image getImage(JavaTypeInfo info) {
		return JavaImagesManager.getTypeImage(info.getTypeKind(), info.getTypeVisibility(), info.isInnerElement());
	}

}
