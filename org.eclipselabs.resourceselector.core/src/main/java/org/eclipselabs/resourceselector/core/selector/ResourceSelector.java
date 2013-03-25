package org.eclipselabs.resourceselector.core.selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipselabs.resourceselector.core.Activator;
import org.eclipselabs.resourceselector.core.filters.Filter;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;
import org.eclipselabs.resourceselector.core.processor.SearchStatus;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * Resource Selector Dialog Box
 * 
 * @author mvanbesien TODO To be documented
 */
public class ResourceSelector {

	private Display display;

	private Shell shell;

	private boolean keepOpen = true;

	private Label patternFieldDescription;

	private Text patternField;

	private Label resultsListDescription;

	private Label resultsAmountDescription;

	private Table resultsTable;

	private Button okButton;

	private Button cancelButton;

	private Button filterEnablementButton;

	private TableViewer resultsTableViewer;

	private ResourceInfo savedResult;

	private boolean isOpenable = true;

	private IProject project;

	private Group resourceInfoDescriptionGroup;

	private Label resourceInfoDescription;

	private Label resourceInfoDescriptionImageContainer;

	private Filter filter;

	private Image dialogImage;

	private ResourceProcessorFactory[] processorFactories;

	private String title = Messages.DIALOG_TITLE;

	private static long resourceProcessorThreadIndex = 0;

	private static long getResourceProcessorThreadIndex() {
		long index = ResourceSelector.resourceProcessorThreadIndex;
		ResourceSelector.resourceProcessorThreadIndex++;
		return index;
	}

	private static long listLoaderThreadIndex = 0;

	private static long getListLoaderThreadIndex() {
		long index = ResourceSelector.listLoaderThreadIndex;
		ResourceSelector.listLoaderThreadIndex++;
		return index;
	}

	public ResourceSelector(ResourceProcessorFactory[] processorFactories, IProject project) {
		this.processorFactories = processorFactories;
		this.project = project;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
		this.updateFilterButton();
	}

	public void run() {
		if (this.isOpenable) {
			this.initializeDialog();
			this.addListenersToDialog();
			this.initializeDialogParts();
			this.addLayoutToDialogParts();
			this.setListenersToDialogParts();
			this.open();
		}
	}

	public void setDialogImage(Image dialogImage) {
		this.dialogImage = dialogImage;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void initializeDialog() {
		this.display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
		this.shell = new Shell(this.display.getActiveShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE
				| SWT.MAX);
		this.shell.setText(this.title);
		if (this.dialogImage == null) {
			ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
					"/icons/ResourceSelectorDialogDefaultIcon.gif");
			if (descriptor != null)
				this.dialogImage = descriptor.createImage();
		}
		if (this.dialogImage != null)
			this.shell.setImage(this.dialogImage);
		this.shell.setLayout(new FormLayout());
	}

	private void addListenersToDialog() {

		this.shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
					ResourceSelector.this.close();
				}

			}
		});
		this.shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				e.doit = false;
				ResourceSelector.this.close();
			}
		});

		this.shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					ISelection selection = ResourceSelector.this.resultsTableViewer.getSelection();
					if (selection instanceof StructuredSelection) {
						Object element = ((StructuredSelection) selection).getFirstElement();
						if (element != null) {
							ResourceSelector.this.saveResult();
							ResourceSelector.this.close();
						}
					}
				}
			}
		});
	}

	private void open() {

		this.shell.update();
		this.shell.pack();
		
		// Center dialog according to IDE location (to handle dual screen config)
		Rectangle bounds;
		try {
			bounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell().getBounds();
		} catch (NullPointerException npe) {
			bounds = this.shell.getDisplay().getPrimaryMonitor().getBounds();
		}
		Rectangle rect = this.shell.getBounds();
		this.shell.setLocation(bounds.x + (bounds.width - rect.width) / 2,
				bounds.y + (bounds.height - rect.height) / 2);
		this.shell.open();
		this.isOpenable = false;
		while (this.keepOpen)
			if (!this.display.readAndDispatch())
				this.display.sleep();

		if (!this.shell.isDisposed())
			this.shell.dispose();
	}

	private void close() {
		this.keepOpen = false;
		if (this.dialogImage != null)
			this.dialogImage.dispose();
	}

	private void saveResult() {
		ISelection selection = this.resultsTableViewer.getSelection();
		if (selection instanceof StructuredSelection) {
			Object element = ((StructuredSelection) selection).getFirstElement();
			if (element instanceof ResourceInfo)
				this.savedResult = (ResourceInfo) element;
		}

	}

	private void initializeDialogParts() {
		this.patternFieldDescription = new Label(this.shell, SWT.NONE);
		this.patternFieldDescription.setText(Messages.PATTERN_FIELD_DESCRIPTION);

		this.patternField = new Text(this.shell, SWT.BORDER);

		this.resultsListDescription = new Label(this.shell, SWT.NONE);
		this.resultsListDescription.setText(Messages.RESULTS_LIST_DESCRIPTION);

		this.resultsAmountDescription = new Label(this.shell, SWT.RIGHT);

		this.resultsTable = new Table(this.shell, SWT.BORDER);
		this.resultsTable.setLinesVisible(false);
		this.resultsTable.setHeaderVisible(false);
		this.resultsTableViewer = new TableViewer(this.resultsTable);

		this.resultsTableViewer.setContentProvider(new ResourceSelectorResultsContentProvider());
		this.resultsTableViewer.setLabelProvider(new ResourceSelectorResultsLabelProvider());
		this.resultsTableViewer.setSorter(new ResourceSelectorResultsSorter());

		this.resourceInfoDescriptionGroup = new Group(this.shell, SWT.NONE);
		this.resourceInfoDescriptionGroup.setLayout(new FormLayout());

		this.resourceInfoDescription = new Label(this.resourceInfoDescriptionGroup, SWT.NONE);
		this.resourceInfoDescriptionImageContainer = new Label(this.resourceInfoDescriptionGroup, SWT.NONE);
		this.resourceInfoDescriptionImageContainer.setSize(20, 20);

		this.okButton = new Button(this.shell, SWT.PUSH);
		this.okButton.setText(Messages.OK_BUTTON);

		this.cancelButton = new Button(this.shell, SWT.PUSH);
		this.cancelButton.setText(Messages.CANCEL_BUTTON);

		this.filterEnablementButton = new Button(this.shell, SWT.CHECK);
		this.updateFilterButton();

	}

	private void updateFilterButton() {
		if (this.filterEnablementButton != null) {
			if (this.filter != null) {
				this.filterEnablementButton.setEnabled(this.filter.canDisable());
				this.filterEnablementButton.setText(Messages.FILTER_BUTTON + " (" + this.filter.getName() + ")");
				this.filterEnablementButton.setSelection(!this.filter.canDisable() ? true : ResourcePreferences
						.getInstance().isFilterEnabled());
			} else {
				this.filterEnablementButton.setEnabled(false);
				this.filterEnablementButton.setText("");
				this.filterEnablementButton.setSelection(false);
			}
			this.filterEnablementButton.update();
		}
	}

	private void addLayoutToDialogParts() {
		FormData fd;

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(0, 5);
		this.patternFieldDescription.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.width = 350;
		fd.top = new FormAttachment(this.patternFieldDescription, 5);
		this.patternField.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(this.patternField, 5);
		this.resultsListDescription.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(this.resultsListDescription, 5);
		fd.right = new FormAttachment(100, -5);
		fd.top = new FormAttachment(this.patternField, 5);
		this.resultsAmountDescription.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.top = new FormAttachment(this.resultsListDescription, 5);
		fd.bottom = new FormAttachment(this.resourceInfoDescriptionGroup, -5);
		fd.height = 13 * 15 + 5;
		fd.width = 350;
		this.resultsTable.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(this.cancelButton, -5);
		fd.width = 350;
		fd.height = 20;
		this.resourceInfoDescriptionGroup.setLayoutData(fd);

		fd = new FormData();
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		fd.width = 80;
		fd.height = 25;
		this.cancelButton.setLayoutData(fd);

		fd = new FormData();
		fd.right = new FormAttachment(this.cancelButton, -5);
		fd.bottom = new FormAttachment(100, -5);
		fd.width = 80;
		fd.height = 25;
		this.okButton.setLayoutData(fd);

		fd = new FormData();
		fd.right = new FormAttachment(this.okButton, -5);
		fd.left = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -5);
		fd.width = 80;
		fd.height = 25;
		this.filterEnablementButton.setLayoutData(fd);

		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(0, 0);
		fd.width = 16;
		fd.height = 16;
		this.resourceInfoDescriptionImageContainer.setLayoutData(fd);

		fd = new FormData();
		fd.right = new FormAttachment(100, -5);
		fd.left = new FormAttachment(this.resourceInfoDescriptionImageContainer, 5);
		fd.top = new FormAttachment(0, 0);
		this.resourceInfoDescription.setLayoutData(fd);

	}

	private void setListenersToDialogParts() {
		this.okButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ResourceSelector.this.saveResult();
				ResourceSelector.this.close();
			}

		});
		this.cancelButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ResourceSelector.this.close();
			}

		});

		this.resultsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = ResourceSelector.this.resultsTableViewer.getSelection();
				if (selection instanceof StructuredSelection) {
					Object element = ((StructuredSelection) selection).getFirstElement();
					if (element != null && element instanceof ResourceInfo) {
						try {
							ResourceInfo t = (ResourceInfo) element;
							String text = t.getContainerName()
									+ (t.getAdditionalDescription() != null ? " - " + t.getAdditionalDescription() : "");
							ResourceSelector.this.resourceInfoDescription.setText(text);
							ResourceSelector.this.resourceInfoDescription.setToolTipText(text);
							ResourceSelector.this.resourceInfoDescriptionImageContainer.setImage(t.getContainerImage());
						} catch (Exception e) {
							Activator.getDefault().logError("Error while element selection in Resource Selector", e);
						}
						return;
					}
				}
				ResourceSelector.this.resourceInfoDescription.setText(Messages.EMPTY_STR);
				ResourceSelector.this.resourceInfoDescription.setToolTipText(Messages.EMPTY_STR);
				ResourceSelector.this.resourceInfoDescriptionImageContainer.setImage(null);
			}

		});

		this.resultsTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				ResourceSelector.this.saveResult();
				ResourceSelector.this.close();
			}

		});

		this.patternField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (e.getSource() instanceof Text) {
					final Text t = (Text) e.getSource();
					if (t.getText() != null && t.getText().length() > 0)
						ResourceSelector.this.loadList(t.getText());
					else
						ResourceSelector.this.clearList();
				}
			}
		});

		this.filterEnablementButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ResourcePreferences.getInstance().setFilterEnabled(((Button) e.getSource()).getSelection());
				String pattern = ResourceSelector.this.patternField.getText();
				if (pattern != null && pattern.length() > 0)
					ResourceSelector.this.loadList(pattern);
				else
					ResourceSelector.this.clearList();
			}

		});

	}

	private void clearList() {
		this.resultsTableViewer.setInput(Collections.EMPTY_LIST);
		this.resultsAmountDescription.setText(Messages.getResultAmountDescription(0));
		this.resultsAmountDescription.update();

	}

	private ListLoader current = null;

	private void loadList(final String pattern) {
		if (this.current != null)
			this.current.kill();
		this.current = new ListLoader(pattern);
		Thread thread = new Thread(this.current);
		thread.setName("TSListLoader-" + ResourceSelector.getListLoaderThreadIndex());
		thread.start();

	}

	public ResourceInfo getSavedResourceInfo() {
		return this.savedResult;
	}

	public static class ResourceSelectorResultsContentProvider implements IStructuredContentProvider {

		public ResourceSelectorResultsContentProvider() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				List<ResourceInfo> elements = (List<ResourceInfo>) inputElement;
				return elements.toArray();
			}
			return null;
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	public static class ResourceSelectorResultsLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			if (!(element instanceof ResourceInfo))
				return null;
			ResourceInfo info = (ResourceInfo) element;
			return info.getImage();
		}

		public String getColumnText(Object element, int columnIndex) {
			if (!(element instanceof ResourceInfo))
				return null;
			ResourceInfo info = (ResourceInfo) element;
			return info.getElementName() + " - " + info.getContainerName();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}

	public static class ResourceSelectorResultsSorter extends ViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof ResourceInfo && e2 instanceof ResourceInfo) {
				ResourceInfo resourceInfo1 = (ResourceInfo) e1;
				ResourceInfo resourceInfo2 = (ResourceInfo) e2;
				if (resourceInfo1.getElementName().equalsIgnoreCase(resourceInfo2.getElementName()))
					if (resourceInfo1.getPackageName() == null || resourceInfo2.getPackageName() == null)
						return resourceInfo1.getElementName().compareToIgnoreCase(resourceInfo2.getElementName());
					else if (resourceInfo1.getPackageName() == null)
						return -1;
					else if (resourceInfo2.getPackageName() == null)
						return 1;
					else
						return resourceInfo1.getPackageName().compareToIgnoreCase(resourceInfo2.getPackageName());
				else
					return resourceInfo1.getElementName().compareToIgnoreCase(resourceInfo2.getElementName());
			} else
				return super.compare(viewer, e1, e2);
		}
	}

	private class ListLoader implements Runnable {

		private String pattern;

		private boolean isActive = true;

		public ListLoader(String pattern) {
			this.pattern = pattern;
		}

		public void kill() {
			this.isActive = false;
		}

		public void run() {

			ResourceSelector.this.display.syncExec(new Runnable() {
				public void run() {
					ResourceSelector.this.resultsTableViewer.setInput(Collections.EMPTY_LIST);
					ResourceSelector.this.resultsAmountDescription.setText(Messages.SEARCH_IN_PROGRESS);
					ResourceSelector.this.resultsAmountDescription.update();
				}
			});
			int resultsFound = 0;
			for (ResourceProcessorFactory factory : ResourceSelector.this.processorFactories) {
				final ResourceProcessor tp = factory.createResourceProcessor(ResourceSelector.this.project,
						this.pattern);
				tp.setFilter(ResourceSelector.this.filter);
				Thread thread = new Thread(tp);
				thread.setName("TSTypeProcessor-" + ResourceSelector.getResourceProcessorThreadIndex());
				thread.start();

				while (this.isActive && tp.getSearchStatus() != SearchStatus.FINISHED)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Activator.getDefault().logError("Error while waiting for processor to terminate.", e);
					}

				if (this.isActive && !ResourceSelector.this.shell.isDisposed()) {
					int index = 0;
					final int maxSize = tp.getSearchResults().size();
					final Iterator<ResourceInfo> iterator = tp.getSearchResults().iterator();
					while (iterator.hasNext() && this.isActive) {
						final int indexf = index;
						ResourceSelector.this.display.syncExec(new Runnable() {
							public void run() {
								if (maxSize < 100 || indexf % (maxSize / 100) == 0) {
									ResourceSelector.this.resultsAmountDescription.setText(Messages
											.getPopulatingDescription(100 * indexf / maxSize));
									ResourceSelector.this.resultsAmountDescription.update();
								}
								ResourceSelector.this.resultsTableViewer.add(iterator.next());
							}
						});
						index++;
					}
				}
				if (!this.isActive)
					tp.cancel();
				else if (tp.getSearchResults() != null)
					resultsFound += tp.getSearchResults().size();
			}
			final int resultsDescription = resultsFound;
			if (this.isActive && !ResourceSelector.this.shell.isDisposed())
				ResourceSelector.this.display.syncExec(new Runnable() {
					public void run() {
						if ((ResourceSelector.this.resultsTableViewer.getSelection() == null || ((StructuredSelection) ResourceSelector.this.resultsTableViewer
								.getSelection()).size() == 0)
								&& ResourceSelector.this.resultsTableViewer.getElementAt(0) != null)
							ResourceSelector.this.resultsTableViewer.setSelection(new StructuredSelection(
									ResourceSelector.this.resultsTableViewer.getElementAt(0)));

						ResourceSelector.this.resultsAmountDescription.setText(Messages
								.getResultAmountDescription(resultsDescription));
						ResourceSelector.this.resultsAmountDescription.update();

					}
				});
		}

	}

}
