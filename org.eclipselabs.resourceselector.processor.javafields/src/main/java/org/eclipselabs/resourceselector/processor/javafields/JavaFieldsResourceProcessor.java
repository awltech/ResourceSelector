package org.eclipselabs.resourceselector.processor.javafields;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;

public class JavaFieldsResourceProcessor extends ResourceProcessor {

	public JavaFieldsResourceProcessor(IProject project, String pattern) {
		this.iProject = project;
		this.pattern = pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipselabs.resourceselector.core.processor.ResourceProcessor#process
	 * ()
	 */
	@Override
	protected void process() {
		// --- BEGINNING OF USER IMPLEMENTATION ---

		// First, retrieve the Java Project
		IJavaProject javaProject = JavaCore.create(this.iProject);

		try {
			// ... For each package fragment root of the Java Project...
			for (IPackageFragmentRoot packageFragmentRoot : javaProject.getAllPackageFragmentRoots()) {
				// ... For each child of the Package Fragment Root...
				for (IJavaElement javaElement : packageFragmentRoot.getChildren()) {
					// ... If is Package Fragment
					if (javaElement instanceof IPackageFragment)
						this.processPackageFragment((IPackageFragment) javaElement);

					// ... If is Compilation Unit
					else if (javaElement instanceof ICompilationUnit)
						this.processCompilationUnit((ICompilationUnit) javaElement);

				}
			}
		} catch (JavaModelException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An exception was encountered", e));
		}
		// --- END OF USER IMPLEMENTATION ---
	}

	/*
	 * Extract Compilation Units from Package Fragment
	 */
	private void processPackageFragment(IPackageFragment packageFragment) throws JavaModelException {
		for (ICompilationUnit compilationUnit : packageFragment.getCompilationUnits())
			processCompilationUnit(compilationUnit);

	}

	/*
	 * Extracts Methods from Compilation Units
	 */
	private void processCompilationUnit(ICompilationUnit compilationUnit) throws JavaModelException {
		// ... For each Type of the Compilation Unit...
		for (IType type : compilationUnit.getTypes()) {
			// ... For each Method of the Type
			for (IField field : type.getFields()) {
				String signature = getSignature(field);
				// We add the method in the Processor's results if pattern is matching !
				if (this.pattern == null 
						|| "*".equals(this.pattern) 
						|| signature.startsWith(this.pattern))
				this.searchResults.add(new JavaFieldsResourceInfo(signature, type
						.getElementName(), compilationUnit.getElementName()));
			}
		}
	}
	
	/*
	 * returns a sexier signature than the one provided by the field.getSignature()
	 */
	private static String getSignature(IField field) throws JavaModelException {
		String name = field.getElementName();
		String type= field.getTypeSignature();
		return name+" : "+type;
	}
}
