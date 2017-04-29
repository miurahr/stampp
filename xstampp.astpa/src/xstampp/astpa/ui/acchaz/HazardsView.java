/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.acchaz;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class HazardsView extends CommonTableView<IHazardViewDataModel> {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.steps.step1_3"; //$NON-NLS-1$

	// the hazard currently displayed in the text widget
	private Hazard displayedHazard;


	/**
	 * Create contents of the view part.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent
	 *            The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));

		this.createCommonTableView(parent, Messages.Hazards);

		this.getFilterTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				HazardsView.this.getFilter().setSearchText(
						HazardsView.this.getFilterTextField().getText());
				HazardsView.this.refreshView();
			}
		});

		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());

		

		Listener addHazardListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				HazardsView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				HazardsView.this.getFilterTextField().setText(""); //$NON-NLS-1$
//				HazardsView.this.refreshView();
				HazardsView.this.getDataInterface().addHazard(
						Messages.DoubleClickToEditTitle, Messages.DescriptionOfThisHazard);
				int newID = getTableViewer().getTable().getItemCount()-1;
				HazardsView.this.getTableViewer().setSelection(
						new StructuredSelection(HazardsView.this
								.getTableViewer().getElementAt(newID)), true);
				HazardsView.this
						.getTitleColumn()
						.getViewer()
						.editElement(
								HazardsView.this.getTableViewer().getElementAt(
										newID), 1);
			}
		};

		this.getAddNewItemButton()
				.addListener(SWT.Selection, addHazardListener);

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, addHazardListener);

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown)
						&& (event.keyCode == SWT.CR)
						&& (!HazardsView.this.getTableViewer().getSelection()
								.isEmpty())) {
					int indexFirstSelected = HazardsView.this.getTableViewer()
							.getTable().getSelectionIndices()[0];
					HazardsView.this
							.getTitleColumn()
							.getViewer()
							.editElement(
									HazardsView.this.getTableViewer()
											.getElementAt(indexFirstSelected),
									1);
				}
			}
		};

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, returnListener);

		// check if the description is default and delete it in that case
		this.getDescriptionWidget().addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				Text text = (Text) e.widget;
				String description = text.getText();
				if (description.compareTo(Messages.DescriptionOfThisHazard) == 0) {
					UUID id = HazardsView.this.displayedHazard.getId();
					HazardsView.this.getDataInterface().setHazardDescription(id, ""); //$NON-NLS-1$
					text.setText(""); //$NON-NLS-1$
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// intentionally empty

			}
		});

		// Listener for the Description
		this.getDescriptionWidget().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (HazardsView.this.displayedHazard != null) {
					Text text = (Text) e.widget;
					String description = text.getText();
					UUID id = HazardsView.this.displayedHazard.getId();
					HazardsView.this.getDataInterface().setHazardDescription(id,
							description);
				}
			}
		});

		// Listener for showing the description of the selected hazard
		HazardsView.this.getTableViewer().addSelectionChangedListener(
				new HazSelectionChangedListener());

		EditingSupport titleEditingSupport = new HazEditingSupport(
				HazardsView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);

		TableViewerColumn linksColumn;

		linksColumn = new TableViewerColumn(HazardsView.this.getTableViewer(),
				SWT.NONE);
		linksColumn.getColumn().setText(Messages.Links);
		linksColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				String linkString = ""; //$NON-NLS-1$
				List<ITableModel> links = HazardsView.this.getDataInterface()
						.getLinkedAccidents(((Hazard) element).getId());
				if (!(links == null)) {
					for (int i = 0; i < links.size(); i++) {
						linkString += ((Accident)links.get(i)).getIdString();
						if (i < (links.size() - 1)) {
							linkString += ", "; //$NON-NLS-1$
						}
					}
				}
				return linkString;
			}
		});

		final int linksWeight = 10;
		final int linksMinWidth = 50;
		this.getTableColumnLayout().setColumnData(linksColumn.getColumn(),
				new ColumnWeightData(linksWeight, linksMinWidth, false));

		// KeyListener for deleting hazards by selecting them and pressing the
		// delete key
		HazardsView.this.getTableViewer().getControl()
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(final KeyEvent e) {
						if ((e.keyCode == SWT.DEL)
								|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
							IStructuredSelection selection = (IStructuredSelection) HazardsView.this
									.getTableViewer().getSelection();
							if (selection.isEmpty()) {
								return;
							}
							HazardsView.this.deleteItems();
						}
					}
				});

		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(HazardsView.this.getTableViewer()
				.getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (HazardsView.this.getTableViewer().getSelection().isEmpty()) {
					return;
				}
				if (HazardsView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteHazard = new Action(Messages.DeleteHazards) {

						@Override
						public void run() {
							HazardsView.this.deleteItems();
						}
					};
					manager.add(deleteHazard);
				}
			}

		});

		menuMgr.setRemoveAllWhenShown(true);
		HazardsView.this.getTableViewer().getControl().setMenu(menu);

		this.updateTable();

	}

	@Override
	protected void deleteEntry(ATableModel model) {
    HazardsView.this.displayedHazard = null;
    this.getDataInterface().removeHazard(model.getId());
	}
	
	private class HazEditingSupport extends EditingSupport {

		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public HazEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(HazardsView.this.getTableViewer()
					.getTable());
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof Hazard) {
				// deleting the default title
				if ((((Hazard) element).getTitle()
						.compareTo(Messages.DoubleClickToEditTitle)) == 0) {
					((Hazard) element).setTitle(""); //$NON-NLS-1$
				}
				return ((Hazard) element).getTitle();
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof Hazard) {
				HazardsView.this.getDataInterface().setHazardTitle(((Hazard) element).getId(),
						String.valueOf(value));
				// Fill in the default title if the user left it blank
				if (String.valueOf(value).length() == 0) {
					HazardsView.this.getDataInterface().setHazardTitle(((Hazard) element).getId(),
							Messages.DoubleClickToEditTitle);
				}
			}
			HazardsView.this.refreshView();
		}
	}

	private class HazSelectionChangedListener implements
			ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// if the selection is empty clear the label
			if (event.getSelection().isEmpty()) {
				HazardsView.this.displayedHazard = null;
				HazardsView.this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				HazardsView.this.getDescriptionWidget().setEnabled(false);
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.getFirstElement() instanceof Hazard) {
					if (HazardsView.this.displayedHazard == null) {
						HazardsView.this.getDescriptionWidget()
								.setEnabled(true);
					} else {
						HazardsView.this.displayedHazard = null;
					}
					HazardsView.this.getDescriptionWidget().setText(
							((Hazard) selection.getFirstElement())
									.getDescription());
					HazardsView.this.displayedHazard = (Hazard) selection
							.getFirstElement();
				}
			}
		}
	}

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void updateTable() {
		HazardsView.this.getTableViewer().setInput(
				this.getDataInterface().getAllHazards());
	}

	@Override
	public String getId() {
		return HazardsView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.Hazards;
	}


	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	@Override
	public commonTableType getCommonTableType() {
		return commonTableType.HazardsView;
	}

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	@Override
	protected void moveEntry(UUID id, boolean moveUp) {
		getDataInterface().moveEntry(moveUp, id, ObserverValue.HAZARD);
	}
}
