package org.eclipselabs.resourceselector.processor.uml;

import org.eclipse.uml2.uml.UMLPackage;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * TypeInfo extension for UML Types
 * 
 * @author mvanbesien
 * 
 */
public class UMLTypeInfo extends ResourceInfo {

	/**
	 * Additional value, to store the uri to this EMF element in workspace.
	 */
	private String emfURL;

	/**
	 * UML Type of this TypeInfo
	 */
	private UMLType type;

	/**
	 * Creates new UMLTypeInfo
	 * 
	 * @param elementName
	 *            name of the element
	 * @param packageName
	 *            name of the package containing the element
	 * @param umlType
	 *            uml type if the element
	 * @param filePath
	 *            path to resource containing the element
	 */
	public UMLTypeInfo(String elementName, String packageName,
			String[] enclosingNames, UMLType umlType, String filePath) {
		super(elementName, packageName, packageName, enclosingNames);
		this.additionalDescription = filePath;
		this.type = umlType;
		this.image = this.type != null ? this.type.getImage() : null;
		this.containerImage = UMLImageManager.getUMLImage(UMLPackage.eINSTANCE
				.getPackage());
	}

	/**
	 * @return URL to this EMF element
	 */
	public String getEmfURL() {
		return this.emfURL;
	}

	/**
	 * @return UML Type of this element
	 */
	public UMLType getType() {
		return this.type;
	}

	/**
	 * Sets the URL to this EMF element
	 * 
	 * @param emfURL
	 *            URL to this EMF element
	 */
	public void setEmfURL(String emfURL) {
		this.emfURL = emfURL;
	}

}
