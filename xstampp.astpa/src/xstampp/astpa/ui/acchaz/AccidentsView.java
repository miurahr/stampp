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

import xstampp.astpa.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class AccidentsView extends CommonTableView<IAccidentViewDataModel> {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.steps.step1_2"; //$NON-NLS-1$

	// the accident currently displayed in the text widget
	private Accident displayedAccident;


	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public AccidentsView() {

	}

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
		this.createCommonTableView(parent, Messages.Accidents);

		this.getFilterTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				AccidentsView.this.getFilter().setSearchText(
						AccidentsView.this.getFilterTextField().getText());
				AccidentsView.this.refreshView();
			}
		});

		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());

		Listener addAccidentListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				AccidentsView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				AccidentsView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				AccidentsView.this.refreshView();
				AccidentsView.this.getDataInterface().addAccident(
						Messages.DoubleClickToEditTitle, Messages.DescriptionOfThisAccident);
				int newID = AccidentsView.this.getDataInterface().getAllAccidents()
						.size() - 1;
				AccidentsView.this.updateTable();
				AccidentsView.this.refreshView();
				AccidentsView.this.getTableViewer().setSelection(
						new StructuredSelection(AccidentsView.this
								.getTableViewer().getElementAt(newID)), true);
				AccidentsView.this
						.getTitleColumn()
						.getViewer()
						.editElement(
								AccidentsView.this.getTableViewer()
										.getElementAt(newID), 1);
			}
		};

		this.getAddNewItemButton().addListener(SWT.Selection,
				addAccidentListener);

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, addAccidentListener);

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown)
						&& (event.keyCode == SWT.CR)
						&& (!AccidentsView.this.getTableViewer().getSelection()
								.isEmpty())) {
					int indexFirstSelected = AccidentsView.this
							.getTableViewer().getTable().getSelectionIndices()[0];
					AccidentsView.this
							.getTitleColumn()
							.getViewer()
							.editElement(
									AccidentsView.this.getTableViewer()
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
				if (description.compareTo(Messages.DescriptionOfThisAccident) == 0) {
					UUID id = AccidentsView.this.displayedAccident.getId();
					AccidentsView.this.getDataInterface().setAccidentDescription(id,
							""); //$NON-NLS-1$
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
				if (AccidentsView.this.displayedAccident != null) {
					Text text = (Text) e.widget;
					String description = text.getText();
					UUID id = AccidentsView.this.displayedAccident.getId();
					AccidentsView.this.getDataInterface().setAccidentDescription(id,
							description);
				}
			}
		});

		// Listener for showing the description of the selected accident
		AccidentsView.this.getTableViewer().addSelectionChangedListener(
				new AccSelectionChangedListener());

		final EditingSupport titleEditingSupport = new AccEditingSupport(
				AccidentsView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);

		TableViewerColumn linksColumn;
		linksColumn = new TableViewerColumn(
				AccidentsView.this.getTableViewer(), SWT.NONE);
		linksColumn.getColumn().setText(Messages.Links);
		linksColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				String linkString = ""; //$NON-NLS-1$
				List<ITableModel> links = AccidentsView.this.getDataInterface()
						.getLinkedHazards(((Accident) element).getId());
				if (!(links == null)) {
					for (int i = 0; i < links.size(); i++) {
						linkString += ((Hazard)links.get(i)).getIdString();
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

		// KeyListener for deleting accidents by selecting them and pressing the
		// delete key
		AccidentsView.this.getTableViewer().getControl()
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(final KeyEvent e) {
						if ((e.keyCode == SWT.DEL)
								|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
							IStructuredSelection selection = (IStructuredSelection) AccidentsView.this
									.getTableViewer().getSelection();
							if (selection.isEmpty()) {
								return;
							}
							AccidentsView.this.deleteItems();
						}
					}
				});

		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(AccidentsView.this
				.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (AccidentsView.this.getTableViewer().getSelection()
						.isEmpty()) {
					return;
				}
				if (AccidentsView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteAccident = new Action(
							Messages.Delete_accidents) {

						@Override
						public void run() {
							AccidentsView.this.deleteItems();
						}
					};
					manager.add(deleteAccident);
				}
			}
		});

		menuMgr.setRemoveAllWhenShown(true);
		AccidentsView.this.getTableViewer().getControl().setMenu(menu);

		this.updateTable();

	}


	@Override
	protected void deleteEntry(ATableModel model) {
    AccidentsView.this.displayedAccident = null;
    this.getDataInterface().removeAccident(model.getId());
	}
	private class AccEditingSupport extends EditingSupport {

		/**
		 * EditingSupport for the title column
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public AccEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(AccidentsView.this.getTableViewer()
					.getTable());
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof Accident) {
				// deleting the default title
				if ((((Accident) element).getTitle()
						.compareTo(Messages.DoubleClickToEditTitle)) == 0) {
					((Accident) element).setTitle(""); //$NON-NLS-1$
				}
				return ((Accident) element).getTitle();
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof Accident) {
				AccidentsView.this.getDataInterface().setAccidentTitle(((Accident) element).getId(),String.valueOf(value));
				// Fill in the default title if the user left it blank
				if (String.valueOf(value).length() == 0) {
					AccidentsView.this.getDataInterface().setAccidentTitle(((Accident) element).getId(),Messages.DoubleClickToEditTitle);
				}
			}
			AccidentsView.this.refreshView();
		}
	}

	private class AccSelectionChangedListener implements
			ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// if the selection is empty clear the label
			if (event.getSelection().isEmpty()) {
				AccidentsView.this.displayedAccident = null;
				AccidentsView.this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				AccidentsView.this.getDescriptionWidget().setEnabled(false);
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.getFirstElement() instanceof Accident) {
					if (AccidentsView.this.displayedAccident == null) {
						AccidentsView.this.getDescriptionWidget().setEnabled(
								true);
					} else {
						AccidentsView.this.displayedAccident = null;
					}
					AccidentsView.this.getDescriptionWidget().setText(
							((Accident) selection.getFirstElement())
									.getDescription());
					AccidentsView.this.displayedAccident = (Accident) selection
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
		AccidentsView.this.getTableViewer().setInput(
				this.getDataInterface().getAllAccidents());
	}

	@Override
	public String getId() {
		return AccidentsView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.Accidents;
	}

	

	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	@Override
	public commonTableType getCommonTableType() {
		return commonTableType.AccidentsView;
	}

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	@Override
	protected void moveEntry(UUID id, boolean moveUp) {
		getDataInterface().moveEntry(moveUp, id, ObserverValue.ACCIDENT);
	}

}
