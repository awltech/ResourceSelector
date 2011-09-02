package org.eclipselabs.resourceselector.core.resources;

import org.eclipse.swt.graphics.Image;

/**
 * Abstract implementation of a definition of a Resource. This class has to be
 * subclassed by modules defining a specific Resource Processor.
 * 
 * @author mvanbesien
 */
public abstract class ResourceInfo {

	/**
	 * Name of the Element
	 */
	protected String elementName;

	/**
	 * Package name of the element
	 */
	protected String packageName;

	/**
	 * names of the elements' Enclosing Element
	 */
	protected String[] enclosingNames;

	/**
	 * Container name of the element;
	 */
	protected String containerName;

	/**
	 * Boolean telling if element is an inner Element;
	 */
	protected boolean isInnerElement;

	/**
	 * In case some additional information to display is required
	 */
	protected String additionalDescription;

	/**
	 * Image corresponding to the ResourceInfo, to be displayed in the Resource
	 * Selector
	 */
	protected Image image;

	/**
	 * Image corresponding to the ResourceInfo's container, to be displayed in the
	 * Resource Selector
	 */
	protected Image containerImage;

	/**
	 * @return the element name
	 */
	public String getElementName() {
		return this.elementName;
	}

	/**
	 * @return the name of the package containing the element
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the name of the element's container
	 */
	public String getContainerName() {
		return this.containerName;
	}

	/**
	 * @return some additional description
	 */
	public String getAdditionalDescription() {
		return this.additionalDescription;
	}

	/**
	 * @return the image corresponding to the ResourceInfo, to be displayed in the
	 *         Resource Selector
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * @return the image corresponding to the ResourceInfo's container, to be
	 *         displayed in the Resource Selector
	 */
	public Image getContainerImage() {
		return this.containerImage;
	}

	/**
	 * Creates a new ResourceInfo instance
	 * 
	 * @param elementName
	 *            : name of element
	 * @param packageName
	 *            : name of the package containing the element
	 * @param containerName
	 *            : name of the resource containing the element
	 */
	public ResourceInfo(String elementName, String packageName, String containerName) {
		this(elementName, packageName, containerName, new String[0]);
	}

	/**
	 * Creates a new ResourceInfo instance
	 * 
	 * @param elementName
	 *            : name of element
	 * @param packageName
	 *            : name of the package containing the element
	 * @param containerName
	 *            : name of the resource containing the element
	 * @param enclosingName
	 *            : name of the element in which this element is enclosed
	 */
	public ResourceInfo(String elementName, String packageName, String containerName, String enclosingName) {
		this(elementName, packageName, containerName, new String[] { enclosingName });
	}

	/**
	 * Creates a new ResourceInfo instance
	 * 
	 * @param elementName
	 *            : name of element
	 * @param packageName
	 *            : name of the package containing the element
	 * @param containerName
	 *            : name of the resource containing the element
	 * @param enclosingNames
	 *            : name of the elements in which this element are enclosed
	 */
	public ResourceInfo(String elementName, String packageName, String containerName, String[] enclosingNames) {
		super();
		this.elementName = elementName;
		this.packageName = packageName;
		this.containerName = containerName;
		this.enclosingNames = enclosingNames;
		this.isInnerElement = enclosingNames != null && enclosingNames.length > 0;
	}

	/**
	 * @return True is ResourceInfo is inner element
	 */
	public boolean isInnerElement() {
		return this.isInnerElement;
	}

	/**
	 * Sets whether is ResourceInfo is an inner element
	 * 
	 * @param isInnerElement
	 */
	public void setInnerElement(boolean isInnerElement) {
		this.isInnerElement = isInnerElement;
	}

	/**
	 * @return names of the elements' Enclosing Element
	 */
	public String[] getEnclosingNames() {
		return this.enclosingNames;
	}

	/**
	 * Sets names of the elements' Enclosing Element
	 * 
	 * @param enclosingNames
	 */
	public void setEnclosingNames(String[] enclosingNames) {
		this.enclosingNames = enclosingNames;
	}
}
