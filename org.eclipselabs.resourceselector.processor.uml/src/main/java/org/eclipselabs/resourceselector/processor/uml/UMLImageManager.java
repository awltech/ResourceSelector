package org.eclipselabs.resourceselector.processor.uml;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Image;
import org.eclipse.uml2.uml.edit.UMLEditPlugin;

/**
 * Class which role is to manage the UML Images associated with UML TypeInfos.
 * 
 * @author mvanbesien
 * 
 */
public class UMLImageManager {

	/**
	 * Private default Constructor
	 */
	private UMLImageManager() {
	}

	/**
	 * Constant, defining the URL prefix to UML images in the UML edit plugin
	 */
	private static final String IMG_PATH_PREFIX = "/icons/full/obj16/";

	/**
	 * Constant, defining the file extension of UML images
	 */
	private static final String IMG_EXTENSION = ".gif";

	/**
	 * Returns the UML Image associated with UML EClass
	 * 
	 * @param eClass
	 *            : EClass
	 * @return UML Image instance
	 */
	public static Image getUMLImage(EClass eClass) {

		String pluginID = UMLEditPlugin.INSTANCE.getSymbolicName();
		String imagePath = UMLImageManager.IMG_PATH_PREFIX + eClass.getName()
				+ UMLImageManager.IMG_EXTENSION;
		return Activator.getDefault().getImage(pluginID, imagePath);

	}

}
