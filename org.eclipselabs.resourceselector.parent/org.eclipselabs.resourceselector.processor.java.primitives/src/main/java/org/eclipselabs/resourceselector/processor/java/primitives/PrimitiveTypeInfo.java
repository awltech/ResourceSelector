package org.eclipselabs.resourceselector.processor.java.primitives;

import org.eclipse.swt.graphics.Image;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * TypeInfo extension for Primitive Types
 * 
 * @author mvanbesien
 */
public class PrimitiveTypeInfo extends ResourceInfo {

	private static final String IMAGE_PLUGINID = "org.eclipse.jdt.ui";
	private static final String IMAGE_FILEPATH = "/icons/full/obj16/typevariable_obj.gif";

	/**
	 * Container name constant for primitive Types
	 */
	private static final String CONTAINER_NAME = "Java Primitive Type";

	/**
	 * Creates new PrimitiveTypeInfo
	 * 
	 * @param elementName
	 *            : name of the element
	 */
	public PrimitiveTypeInfo(String elementName) {
		super(elementName, null, PrimitiveTypeInfo.CONTAINER_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipselabs.resourceselector.core.types.TypeInfo#getImage()
	 */
	@Override
	public Image getImage() {
		return Activator.getDefault().getImage(PrimitiveTypeInfo.IMAGE_PLUGINID, PrimitiveTypeInfo.IMAGE_FILEPATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipselabs.resourceselector.core.types.TypeInfo#getContainerImage()
	 */
	@Override
	public Image getContainerImage() {
		return Activator.getDefault().getImage(PrimitiveTypeInfo.IMAGE_PLUGINID, PrimitiveTypeInfo.IMAGE_FILEPATH);
	}

}
