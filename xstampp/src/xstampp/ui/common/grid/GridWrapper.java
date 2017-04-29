/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.common.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;

import messages.Messages;
import xstampp.Activator;

/**
 * A wrapper for nebula grid to simplify adding more complicated cells.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt, Lukas Balzer
 * 
 */
public class GridWrapper {

  private class ColumnResizeAdapter extends ControlAdapter{
    private int colIndex;

    public ColumnResizeAdapter(int colIndex) {
      this.colIndex = colIndex;
    }
    
    @Override
    public void controlResized(ControlEvent e) {
      actualGrid.getColumn(colIndex);
    }
  }
  
  private class GridFocusListener implements FocusListener {

    private GridWrapper grid;
    public GridFocusListener(GridWrapper grid) {
      this.grid = grid;
    }

    @Override
    public void focusGained(FocusEvent e) {
      // intentionally empty handled by mouse event.
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (this.grid.getFocusedCell() != null) {
        this.grid.getFocusedCell().onFocusLost();

        this.grid.setFocusedCell(null);
      }
    }
  }

  private class GridMouseListener implements MouseListener {

    private GridWrapper grid;

    public GridMouseListener(GridWrapper grid) {
      this.grid = grid;
    }

    private IGridCell getCellFromMouse(MouseEvent e) {
      return this.grid.getGridCellFromMouseCoordinate(new Point(e.x, e.y));
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
      // intentionally empty
    }

    @Override
    public void mouseDown(MouseEvent e) {
      IGridCell cell = this.getCellFromMouse(e);
      if (getEditClient() != null && (getEditClient() != cell)) {
        getEditClient().cleanUp();
        setEditClient(null);
      }
      Point mousePoint = new Point(e.x, e.y);
      Point cellCoord = this.grid.getGrid().getCell(mousePoint);

      Point relativeMouse = null;
      if ((cellCoord != null) && (cellCoord.y >= 0) && (cellCoord.y < this.grid.getGrid().getItemCount())) {
        GridItem item = this.grid.getGrid().getItem(cellCoord.y);
        Rectangle itemBounds = item.getBounds(cellCoord.x);

        relativeMouse = new Point(mousePoint.x - itemBounds.x, mousePoint.y - itemBounds.y);

        if (cell != null) {
          cell.onMouseDown(e, relativeMouse, itemBounds);
        }
      }

      Point focused = this.grid.actualGrid.getFocusCell();

      if (focused != null) {
        if (this.grid.getFocusedCell() != null) {
          this.grid.getFocusedCell().onFocusLost();
        }

        this.grid.setFocusedCell(this.grid.getGridCellFromCellCoordinate(focused));
        if (this.grid.getFocusedCell() != null) {
          this.grid.getFocusedCell().onFocusGained();
        }
      }
    }

    @Override
    public void mouseUp(MouseEvent e) {

      IGridCell cell = this.getCellFromMouse(e);
      IGridCell editClient = getEditClient();
      if (cell != null && editClient != null && (editClient != cell)) {
        cell.cleanUp();
        setEditClient(null);
      }
      if (cell != null) {
        cell.onMouseUp(e);
      }
    }
  }

  private class GridMouseMoveListener implements MouseMoveListener {

    private GridWrapper grid;

    public GridMouseMoveListener(GridWrapper grid) {
      this.grid = grid;
    }

    private IGridCell getCellFromMouse(MouseEvent e) {
      return this.grid.getGridCellFromMouseCoordinate(new Point(e.x, e.y));
    }

    @Override
    public void mouseMove(MouseEvent e) {
      IGridCell cell = this.getCellFromMouse(e);
      if(cell != null){
//        actualGrid.setToolTipText(cell.getToolTip(new Point(e.x, e.y)));
      }
      this.grid.setHoveredCell(cell);
    }
  }

  /**
   * Wrapper for a grid item, representing a row in the grid.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   */
  public class NebulaGridRowWrapper extends GridItem {

    private GridRow gridRow;
    private GridRow parentRow;
    private NebulaGridRowWrapper parent;
    
    @Override
    public boolean isVisible() {
      return super.isVisible();
    }
    /**
     * Ctor.
     * 
     * @author Patrick Wickenhaeuser, Lukas Balzer
     * 
     * @param parent
     *          the grid in which the row is added.
     * @param style
     *          swt style.
     * @param row
     *          the row to take the cells from.
     * @param parentRow
     *          the parent of the row. Can be null if it doesn't have a parent.
     */
    public NebulaGridRowWrapper(Grid parent, int style, GridRow row, GridRow parentRow) {
      super(parent, style);
      this.parent = null;
      this.gridRow = row;
      this.parentRow = parentRow;
    }
    protected int getParentDepth(int depth) {
      if(getParentGridRow() != null){
        return getParentWrapper().getParentDepth(depth+1);
      }
      return depth;
    }

    /**
     * Get the cell in a given column in the row.
     * 
     * @author Patrick Wickenhaeuser
     * 
     * @param column
     *          the column of the cell.
     * @return the cell.
     */
    public IGridCell getCell(int column) {
      int actualColumn = column;
      IGridCell cell = null;
      GridRow row = this.getGridRow();
      List<IGridCell> rowCells = row.getCells();

//      if (this.getParentGridRow() != null) {
//        // column = this.getColumn()
//        // - item.getParentGridRow().getCells().size();
//
//        // there is only one parent cell on the left of it at this point
//        actualColumn = column - getParentDepth(0);
//        
//      } else {
//        actualColumn = column;
//      }

      // check if the cell exists
      if ((actualColumn >= 0) && (rowCells.size() > actualColumn)) {
        cell = rowCells.get(actualColumn);
      }
      return cell;
    }

    /**
     * Get the row in the grid.
     * 
     * @author Patrick Wickenhaeuser
     * 
     * @return the row.
     */
    public GridRow getGridRow() {
      return this.gridRow;
    }

    @Override
    public boolean hasChildren() {
      return !this.gridRow.getChildren().isEmpty();
    }
    
    /**
     * Get the parent row.
     * 
     * @author Patrick Wickenhaeuser
     * 
     * @return the parent row.
     */
    public GridRow getParentGridRow() {
      return this.parentRow;
    }

    public NebulaGridRowWrapper getParentWrapper() {
      return parent;
    }

    @Override
    public void setHeight(int newHeight) {
      if (this.getHeight() != newHeight) {
        if (newHeight > getHeight()) {
          super.setHeight(newHeight);
        } else {
          for (IGridCell cell : getGridRow().getCells()) {
            if (cell.getPreferredHeight() > newHeight) {
              return;
            }
          }
          super.setHeight(newHeight);
        }
        if (getParentWrapper() != null) {
          int difference = newHeight - getHeight();
          getParentWrapper().setHeight(getParentWrapper().getHeight() + difference);
        }
      }
    }

    public void setParentWrapper(NebulaGridRowWrapper parent) {
      this.parent = parent;
    }

  }

  /**
   * The log4j logger.
   */
  private static final Logger LOGGER = Logger.getRootLogger();

  // private GridCellRenderer cellRenderer;

  private static final int DEFAULT_COLUMN_WIDTH = 200;
  private static final String DELETE_LINK_ICON_PATH_16 = "/icons/buttons/grid/DeleteButton_InCell_16.png"; //$NON-NLS-1$
  private static final String EDIT_LINK_ICON_PATH_16 = "/icons/buttons/grid/LinkButton_InCell_16.png"; //$NON-NLS-1$
  private static final String ADD_ICON_PATH_16 = "/icons/buttons/grid/AddButton_InCell_16.png"; //$NON-NLS-1$

  private static final String DELETE_LINK_ICON_PATH_32 = "/icons/buttons/grid/DeleteButton_InCell_32.png"; //$NON-NLS-1$

  private static final String EDIT_LINK_ICON_PATH_32 = "/icons/buttons/grid/LinkButton_InCell_32.png"; //$NON-NLS-1$

  private static final String ADD_ICON_PATH_32 = "/icons/buttons/grid/AddButton_InCell_32.png"; //$NON-NLS-1$

  private static final String EDIT_LINK_ICON_PATH_24 = "/icons/buttons/grid/LinkButton_InCell_24.png"; //$NON-NLS-1$

  private static Image deleteLinkImage16 = null;

  private static Image editLinkImage16 = null;
  private static Image addImage16 = null;
  private static Image deleteLinkImage32 = null;

  private static Image editLinkImage32 = null;
  private static Image addImage32 = null;
  private static Image editLinkImage24;

  private float[] columnratios;
  /**
   * Get the image for the add button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the add button.
   */
  public static final Image getAddButton16() {
    if (GridWrapper.addImage16 == null) {
      GridWrapper.addImage16 = Activator.getImageDescriptor(GridWrapper.ADD_ICON_PATH_16).createImage();
    }

    return GridWrapper.addImage16;
  }

  /**
   * Get the image for the add button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the add button.
   */
  public static final Image getAddButton32() {
    if (GridWrapper.addImage32 == null) {
      GridWrapper.addImage32 = Activator.getImageDescriptor(GridWrapper.ADD_ICON_PATH_32).createImage();
    }

    return GridWrapper.addImage32;
  }

  /**
   * Get the image for the delete button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the delete button.
   */
  public static final Image getDeleteButton16() {
    if (GridWrapper.deleteLinkImage16 == null) {
      GridWrapper.deleteLinkImage16 = Activator.getImageDescriptor(GridWrapper.DELETE_LINK_ICON_PATH_16).createImage();
    }

    return GridWrapper.deleteLinkImage16;
  }

  /**
   * Get the image for the delete button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the delete button.
   */
  public static final Image getDeleteButton32() {
    if (GridWrapper.deleteLinkImage32 == null) {
      GridWrapper.deleteLinkImage32 = Activator.getImageDescriptor(GridWrapper.DELETE_LINK_ICON_PATH_32).createImage();
    }

    return GridWrapper.deleteLinkImage32;
  }

  /**
   * Get the image for the link button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the add link.
   */
  public static final Image getLinkButton16() {
    if (GridWrapper.editLinkImage16 == null) {
      GridWrapper.editLinkImage16 = Activator.getImageDescriptor(GridWrapper.EDIT_LINK_ICON_PATH_16).createImage();
    }

    return GridWrapper.editLinkImage16;
  }

  /**
   * Get the image for the link button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the add link.
   */
  public static final Image getLinkButton24() {
    if (GridWrapper.editLinkImage24 == null) {
      GridWrapper.editLinkImage24 = Activator.getImageDescriptor(GridWrapper.EDIT_LINK_ICON_PATH_24).createImage();
    }

    return GridWrapper.editLinkImage24;
  }

  /**
   * Get the image for the link button.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the add link.
   */
  public static final Image getLinkButton32() {
    if (GridWrapper.editLinkImage32 == null) {
      GridWrapper.editLinkImage32 = Activator.getImageDescriptor(GridWrapper.EDIT_LINK_ICON_PATH_32).createImage();
    }

    return GridWrapper.editLinkImage32;
  }

  private Grid actualGrid;

  private IGridCell hoveredCell = null;

  private IGridCell focusedCell = null;

  private IGridCell editClient = null;

  private List<GridRow> rows;

  private List<NebulaGridRowWrapper> nebulaRows;

  private String[] columnLabels = null;

  private GridCellRenderer cellRenderer;

  private boolean selectRow = true;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   * @param parent
   *          the parent composite.
   * @param columnLabels
   *          the initial column labels. Can be empty.
   */
  public GridWrapper(Composite parent, String[] columnLabels) {
    this.rows = new ArrayList<GridRow>();
    this.nebulaRows = new ArrayList<NebulaGridRowWrapper>();
    this.hoveredCell = null;
    this.columnratios = new float[columnLabels.length];
    Arrays.fill(columnratios, 1.0f / columnLabels.length);
    this.actualGrid = new Grid(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
    this.actualGrid.setHeaderVisible(true);
    this.actualGrid.getHeaderHeight();
    this.actualGrid.setCellSelectionEnabled(true);
    this.actualGrid.setLinesVisible(false);
    this.actualGrid.setWordWrapHeader(true);
    this.actualGrid.addMouseListener(new GridMouseListener(this));
    this.actualGrid.addMouseMoveListener(new GridMouseMoveListener(this));
    this.actualGrid.addFocusListener(new GridFocusListener(this));
    this.cellRenderer = new GridCellRenderer(this);

    this.setColumnLabels(columnLabels);
    this.actualGrid.addDisposeListener(new DisposeListener() {
      
      @Override
      public void widgetDisposed(DisposeEvent e) {
        // TODO Auto-generated method stub
        
      }
    });
    this.actualGrid.addControlListener(new ControlListener() {

      @Override
      public void controlMoved(ControlEvent e) {
        // intentionally empty
      }

      @Override
      public void controlResized(ControlEvent e) {
        GridWrapper.this.resizeColumns();
      }
    });
  }

  /**
   * Activates a cell based on the UUID of its contents
   * 
   * @author Benedikt Markt
   * 
   * @param uuid
   *          the UUID
   */
  public void activateCell(UUID uuid) {
    this.activateRecursive(this.rows, uuid);
  }

  /**
   * Traverses all rows recursively to find the cell containing the element with
   * the given uuid and the activates it
   * 
   * @author Benedikt Markt
   * 
   * @param cellRows
   *          the current grid row
   * @param uuid
   *          the UUID to search for
   */
  private void activateRecursive(List<GridRow> cellRows, UUID uuid) {
    for (GridRow r : cellRows) {
      for (IGridCell c : r.getCells()) {
        if (c.getUUID() != null) {
          if (c.getUUID().equals(uuid)) {
            GridWrapper.LOGGER.info("Activating: " + uuid); //$NON-NLS-1$
            c.activate();
            return;
          }
        }
      }
      this.activateRecursive(r.getChildren(), uuid);
    }
  }

  /**
   * Add a row to the grid.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param csRow
   *          the new row.
   */
  public void addRow(GridRow csRow) {
    this.rows.add(csRow);
  }

  /**
   * Removes all rows.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public void clearRows() {
    for (int i = 0; i < this.rows.size(); i++) {
      this.rows.get(i).cleanUp();
    }

    this.rows.clear();
  }

  /**
   * Clears the table but does not remove the rows.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public void clearTable() {
    this.actualGrid.disposeAllItems();
    this.actualGrid.clearItems();
  }

  /**
   * Fills the table using the current rows.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public void fillTable() {
    this.nebulaRows.clear();
    int maxCellCount = this.columnLabels.length;
    for (int i = 0; i < this.rows.size(); i++) {
      addChildRows(null, this.rows.get(i), i,0);
    }

    if (this.actualGrid.getColumnCount() > maxCellCount) {
      // too many columns -> remove
      for (int i = this.actualGrid.getColumnCount() - 1; i >= maxCellCount; i--) {
        this.actualGrid.getColumn(i).dispose();
      }
    } else if (this.actualGrid.getColumnCount() < maxCellCount) {
      // not enough colums -> add the missing ones
      int currentColumns = this.actualGrid.getColumnCount();
      for (int i = 0; i < (maxCellCount - currentColumns); i++) {
        GridColumn childColumn = new GridColumn(this.actualGrid, SWT.NONE);
        childColumn.setText(Messages.GridWrapper_Column);
        childColumn.setWordWrap(true);
        childColumn.setHeaderWordWrap(true);
        childColumn.setWidth(GridWrapper.DEFAULT_COLUMN_WIDTH);
//        childColumn.setResizeable(true);
        childColumn.setCellRenderer(cellRenderer);
        childColumn.setMinimumWidth(50);
        childColumn.addControlListener(new ColumnResizeAdapter(i));
      }
    }

    // update the labels
    if (GridWrapper.this.columnLabels != null) {
      for (int i = 0; i < Math.min(GridWrapper.this.columnLabels.length,
          GridWrapper.this.actualGrid.getColumnCount()); i++) {
        GridWrapper.this.actualGrid.getColumn(i).setText(GridWrapper.this.columnLabels[i]);

      }
    }

    this.resizeColumns();
    if ( actualGrid.getItemCount() > 0 ) {
//      this.actualGrid.select(29);
      this.actualGrid.showSelection();
    }
    
  }

  private int addChildRows(NebulaGridRowWrapper parent,
                                GridRow row,int parentIndex,int firstCellIndex){
    row.setRowIndex(parentIndex++);
    GridRow parentrow = null;
    if(parent != null){
      parentrow = parent.getGridRow();
    }
    NebulaGridRowWrapper item = new NebulaGridRowWrapper(this.getGrid(), SWT.NONE, row, parentrow);

    if ( row.getColumnSpan() != null ) {
      item.setColumnSpan(row.getColumnSpan().x,row.getColumnSpan().y);
    }
    // parentItem.pack();
    if ( parent != null ) {
      item.setParentWrapper(parent);
    }
    int childRowCount = row.getChildren().size();

      item.setHeight(row.getPreferredHeight());
    
    this.nebulaRows.add(item);
    // add cells for children cells
    for (int childI = 0; childI < row.getChildren().size(); childI++) {
      GridRow childRow = row.getChildren().get(childI);
      childRowCount += addChildRows(item,childRow, childI,firstCellIndex + 1);
    }
    for (int cellIndices : row.getRowSpanningCells()){
      item.setRowSpan(cellIndices, childRowCount);
    }
    return childRowCount;
  }
    
  public IGridCell getEditClient() {
    return this.editClient;
  }

  /**
   * Get the currently hovered cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the focused cell.
   */
  public IGridCell getFocusedCell() {
    return this.focusedCell;
  }

  /**
   * Get the wrapped nebula grid.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the actual grid.
   */
  public Grid getGrid() {
    return this.actualGrid;
  }

  /**
   * Get a cell from its coordinate in the grid.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param point
   *          the cell coordinate.
   * @return the cell.
   */
  public IGridCell getGridCellFromCellCoordinate(Point point) {
    if (this.getGrid() == null) {
      GridWrapper.LOGGER.error("Grid not initialized!"); //$NON-NLS-1$

      return null;
    }

    if ((point.y >= 0) && (point.y < this.getGrid().getItemCount())) {
      GridItem item = this.getGrid().getItem(point.y);
      if (item != null) {
        NebulaGridRowWrapper gridItem = (NebulaGridRowWrapper) item;
        return gridItem.getCell(point.x);
      }
    }

    return null;
  }

  /**
   * Get a cell from the mouse coordinate.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param mousePoint
   *          the mouse coordinate.
   * @return the cell.
   */
  public IGridCell getGridCellFromMouseCoordinate(Point mousePoint) {
    Point cellCoord = this.actualGrid.getCell(mousePoint);
    if (cellCoord == null) {
      return null;
    }

    return this.getGridCellFromCellCoordinate(cellCoord);
  }

  /**
   * Get the currently hovered cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the cell.
   */
  public IGridCell getHoveredCell() {
    return this.hoveredCell;
  }

  /**
   * 
   * Get a list of the rows.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the rows.
   */
  public List<GridRow> getRows() {
    return this.rows;
  }

  /**
   * Get a list of the selected cells.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the selected cells.
   */
  public final List<IGridCell> getSelectedCellList() {
    ArrayList<IGridCell> selectedCells = new ArrayList<IGridCell>();

    for (int i = 0; i < this.rows.size(); i++) {
      GridRow row = this.rows.get(i);
      for (int cellI = 0; cellI < row.getCells().size(); cellI++) {
        if (this.isCellSelected(row.getCells().get(cellI))) {
          selectedCells.add(row.getCells().get(cellI));
        }
      }

      int childCount = row.getChildren().size();

      // add cells for children cells
      for (int childI = 0; childI < childCount; childI++) {
        GridRow childRow = row.getChildren().get(childI);

        for (int cellI = 0; cellI < childRow.getCells().size(); cellI++) {
          if (this.isCellSelected(childRow.getCells().get(cellI))) {
            selectedCells.add(childRow.getCells().get(cellI));
          }
        }
      }
    }

    return selectedCells;
  }

  /**
   * Checks whether a given cell is hovered.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param cell
   *          the cell to be checked.
   * @return whether the cell is hovered.
   */
  public boolean isCellHovered(IGridCell cell) {
    if ((this.getGrid() != null) && (this.getHoveredCell() != null) && this.getHoveredCell().equals(cell)) {
      return true;
    }

    return false;
  }

  /**
   * Checks whether a given cell is selected.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param cell
   *          the cell to be checked.
   * @return whether the cell is selected.
   */
  public boolean isCellSelected(IGridCell cell) {
    if (this.getGrid() != null) {
      Point[] selections = this.getGrid().getCellSelection();

      for (Point selection : selections) {
        IGridCell selectedCell = this.getGridCellFromCellCoordinate(selection);

        if ((selectedCell != null) && selectedCell.equals(cell)) {
          return true;
        }
        if((selectedCell != null) && this.selectRow && selectedCell.getGridRow().equals(cell.getGridRow())){
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Clears and fills the whole table.
   * 
   * @author Benedikt Markt, Patrick Wickenhaeuser
   * 
   */
  public void reloadTable() {
    this.clearTable();
    this.fillTable();
  }

  public void setColumnratios(float[] columnratios) {
    this.columnratios = columnratios;
  }
  private void resizeColumns() {
    int colNr = Math.max(0, GridWrapper.this.actualGrid.getColumnCount());
    
    ScrollBar verticalBar = this.actualGrid.getVerticalBar();
    int firstColumnWidth = this.actualGrid.getSize().x;

    for (int i = 0; i < colNr; i++) {
      GridColumn column = GridWrapper.this.actualGrid.getColumn(i);
      if (i == 1&&verticalBar != null && verticalBar.isVisible()) {
        column.setWidth((int) (firstColumnWidth * columnratios[i] - verticalBar.getSize().x));
      } else {
        column.setWidth((int) (firstColumnWidth * columnratios[i]));
      }
    }
    this.resizeRows();
    this.actualGrid.recalculateHeader();
  }

  public void resizeRow(int rowIndex) {
    if (!this.nebulaRows.get(rowIndex).isDisposed()) {

      this.nebulaRows.get(rowIndex).setHeight(this.nebulaRows.get(rowIndex).getGridRow().getPreferredHeight());
    }
  }

  /**
   * Resize the rows accodring to the their preferred heights.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public void resizeRows() {
    for (int i = 0; i < this.nebulaRows.size(); i++) {
      if (this.nebulaRows.get(i).getGridRow().needsRefresh()) {
        resizeRow(i);

      }
    }
  }

  /**
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param columnLabels
   *          the new labels.
   */
  public final void setColumnLabels(String[] columnLabels) {
    this.columnLabels = columnLabels.clone();
  }

  public void setEditClient(IGridCell editClient) {
    this.editClient = editClient;
  }

  /**
   * Set the focused cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param cell
   *          the hovered cell.
   */
  public void setFocusedCell(IGridCell cell) {
    this.focusedCell = cell;
  }

  /**
   * Set the hovered cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param cell
   *          the hovered cell.
   */
  public void setHoveredCell(IGridCell cell) {
    this.hoveredCell = cell;
  }
  
  public void setSelectRow(boolean selectRow) {
    this.selectRow = selectRow;
  }
}