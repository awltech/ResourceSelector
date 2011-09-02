package org.eclipselabs.resourceselector.processor.javafields.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;
import org.eclipselabs.resourceselector.core.selector.ResourceSelector;
import org.eclipselabs.resourceselector.processor.javafields.JavaFieldsResourceProcessorFactory;

public class JavaFieldsResourceProcessorTestAction implements IObjectActionDelegate {

	private IProject project;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// Does nothing.
	}

	public void run(IAction action) {

		// Initialize an array of Resource Processor Factories
		ResourceProcessorFactory[] factories = new ResourceProcessorFactory[1];
		factories[0] = new JavaFieldsResourceProcessorFactory();

		// Then we initialize the Resource Selector with the factories and the
		// project
		ResourceSelector resourceSelector = new ResourceSelector(factories, this.project);
		resourceSelector.setTitle("My Java Fields Processor");

		// Now we open the Resource Selector to let the user make his choice
		resourceSelector.run();

		// And finally, we get and display the user selection.
		ResourceInfo savedResourceInfo = resourceSelector.getSavedResourceInfo();
		if (savedResourceInfo != null)
			MessageDialog.openInformation(new Shell(Display.getDefault()), "My Java Fields Processor Results",
					savedResourceInfo.getElementName());
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// Retrieve the project from the user selection
		this.project = null;
		if (selection instanceof IStructuredSelection) {
			Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
			if (selectedObject instanceof IProject)
				this.project = (IProject) selectedObject;
		}
		action.setEnabled(this.project != null);
	}

}
