package org.eclipselabs.resourceselector.processor.uml;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Static helping class, that extracts all UMLTypeInfos from UML Resource
 * 
 * @author mvanbesien
 * 
 */
public class UMLResourceProcessor {

	/**
	 * Collection of Valid EClasses. These EClasses are defining which UML
	 * Objects are valid as UML Types.
	 */
	protected static Collection<EClass> validEClasses = null;

	/**
	 * Extracts UMLTypesInfos from Resource.
	 * 
	 * @param resource
	 *            : UML Resource
	 * @return collection of UMLTypesInfos
	 */
	public Collection<UMLTypeInfo> lookForTypesInResource(Resource resource) {
		if (UMLResourceProcessor.validEClasses == null)
			UMLResourceProcessor.instanciateValidEClasses();

		Collection<UMLTypeInfo> typeInfos = new ArrayList<UMLTypeInfo>();
		for (EObject eObject : resource.getContents())
			if (eObject instanceof PackageableElement)
				this.managePackageableElement((PackageableElement) eObject,
						typeInfos);
		return typeInfos;
	}

	protected synchronized static void instanciateValidEClasses() {
		if (UMLResourceProcessor.validEClasses == null) {
			UMLResourceProcessor.validEClasses = new ArrayList<EClass>();
			UMLResourceProcessor.validEClasses.add(UMLPackage.eINSTANCE
					.getClass_());
			UMLResourceProcessor.validEClasses.add(UMLPackage.eINSTANCE
					.getInterface());
			UMLResourceProcessor.validEClasses.add(UMLPackage.eINSTANCE
					.getDataType());
			UMLResourceProcessor.validEClasses.add(UMLPackage.eINSTANCE
					.getEnumeration());
		}
	}

	/**
	 * Check if package element is a valid element, and goes recursively among
	 * its children
	 * 
	 * @param element
	 *            Element
	 * @param typeInfos
	 *            Collection of valid elements
	 */
	protected void managePackageableElement(PackageableElement element,
			Collection<UMLTypeInfo> typeInfos) {
		String[] enclosingElements = new String[0];

		if (element == null)
			return;

		if (UMLResourceProcessor.validEClasses.contains(element.eClass())) {

			if (!element.eContainer().equals(element.getNearestPackage())) {
				Collection<String> elements = new ArrayList<String>();
				EObject temp = element.eContainer();
				while (temp.equals(element.getNearestPackage()))
					if (temp instanceof NamedElement)
						elements.add(((NamedElement) temp).getName());
					else
						elements.add("");
				enclosingElements = elements
						.toArray(new String[elements.size()]);
			}
			String fullPackageName = "";
			EObject temp = element.getNearestPackage();
			while (temp != null && temp instanceof Package
					&& !(temp instanceof Model)) {
				fullPackageName = ((Package) temp).getName()
						+ (fullPackageName.length() > 0 ? "." : "")
						+ fullPackageName;
				temp = temp.eContainer();
			}
			UMLTypeInfo umlTypeInfo = new UMLTypeInfo(element.getName(),
					fullPackageName, enclosingElements, UMLType.create(element
							.eClass()), element.eResource().getURI().toString());
			umlTypeInfo.setEmfURL(element.eResource().getURI() + "#"
					+ element.eResource().getURIFragment(element));
			typeInfos.add(umlTypeInfo);
		}
		for (Element subElement : element.getOwnedElements())
			if (subElement instanceof PackageableElement)
				this.managePackageableElement((PackageableElement) subElement,
						typeInfos);
			else if (subElement instanceof ElementImport) {
				Element importedElement = ((ElementImport) subElement)
						.getImportedElement();
				if (importedElement instanceof PackageableElement)
					managePackageableElement(
							(PackageableElement) importedElement, typeInfos);
			} else if (subElement instanceof PackageImport) {
				managePackageableElement(
						((PackageImport) subElement).getImportedPackage(),
						typeInfos);
			}
	}
}
