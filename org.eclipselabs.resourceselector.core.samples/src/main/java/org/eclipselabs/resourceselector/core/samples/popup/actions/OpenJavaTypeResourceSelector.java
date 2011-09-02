package org.eclipselabs.resourceselector.core.samples.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;
import org.eclipselabs.resourceselector.core.selector.ResourceSelector;
import org.eclipselabs.resourceselector.processor.java.JavaTypeFilters;
import org.eclipselabs.resourceselector.processor.java.JavaTypeProcessorFactory;
import org.eclipselabs.resourceselector.processor.java.primitives.PrimitiveTypeProcessorFactory;

/**
 * Popup action used to open the Resource Selector
 */
public class OpenJavaTypeResourceSelector implements IObjectActionDelegate {

	/**
	 * Input project instance
	 */
	private IProject project;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		ResourceProcessorFactory[] factories = new ResourceProcessorFactory[] {
				new PrimitiveTypeProcessorFactory(),
				new JavaTypeProcessorFactory() };
		ResourceSelector selector = new ResourceSelector(factories,
				this.project);
		selector.setFilter(JavaTypeFilters.getMostUsedJavaTypesFilter());
		selector.run();
		ResourceInfo savedTypeInfo = selector.getSavedResourceInfo();
		if (savedTypeInfo != null)
			System.out.println("Returned: " + savedTypeInfo.toString());
		else
			System.out.println("Returned: <No Object>");
		// Clean resourceSet
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.project = null;
		if (selection instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) selection)
					.getFirstElement();
			if (selected instanceof IProject)
				this.project = (IProject) selected;
			else if (selected instanceof IJavaProject)
				this.project = ((IJavaProject) selected).getProject();
		}
		action.setEnabled(this.project != null);
	}

}
