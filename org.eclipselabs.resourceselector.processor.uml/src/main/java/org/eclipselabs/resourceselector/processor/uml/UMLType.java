package org.eclipselabs.resourceselector.processor.uml;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Image;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Enumeration describing all possible UML Types.
 * 
 * @author mvanbesien
 * 
 */
public enum UMLType {
	CLASS, ENUM, INTERFACE, DATATYPE;

	/**
	 * Returns UMLType enumeration value from UML EClass
	 * 
	 * @param eClass
	 *            UML EClass
	 * @return UMLType enumeration value
	 */
	public static UMLType create(EClass eClass) {
		if (UMLPackage.eINSTANCE.getClass_().equals(eClass))
			return UMLType.CLASS;
		if (UMLPackage.eINSTANCE.getInterface().equals(eClass))
			return UMLType.INTERFACE;
		if (UMLPackage.eINSTANCE.getDataType().equals(eClass))
			return UMLType.DATATYPE;
		if (UMLPackage.eINSTANCE.getEnumeration().equals(eClass))
			return UMLType.ENUM;
		else
			return null;
	}

	/**
	 * @return EClass associated to this UMLType value
	 */
	public EClass getEClass() {
		switch (this) {
		case CLASS:
			return UMLPackage.eINSTANCE.getClass_();
		case INTERFACE:
			return UMLPackage.eINSTANCE.getInterface();
		case DATATYPE:
			return UMLPackage.eINSTANCE.getDataType();
		case ENUM:
			return UMLPackage.eINSTANCE.getEnumeration();
		default:
			return null;
		}
	}

	/**
	 * @return UML image associated to this UMLType value
	 */
	public Image getImage() {
		return UMLImageManager.getUMLImage(this.getEClass());
	}
}
