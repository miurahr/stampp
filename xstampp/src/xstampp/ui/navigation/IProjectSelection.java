/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import xstampp.model.IDataModel;

/**
 * Describes a project item selection in a tree and provides an interface for
 * the interaction with it
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public interface IProjectSelection extends ISelection {
  /**
   * @param id
   *          the id of the Contribution Group the IContributionItems-/IActions
   *          are appended to
   * @param manager
   *          the MenuManager of the contextMenu
   */
  void addOpenEntry(String id, IMenuManager manager);

  /**
   * @return the projectId
   */
  UUID getProjectId();

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param expand
   *          expand or not
   * @param first
   *          TODO
   * @return TODO
   */
  boolean expandTree(boolean expand, boolean first);

  /**
   * changes the related tree item to the given
   * 
   * @author Lukas Balzer
   *
   * @param item
   *          {@link #getItem()}
   */
  void changeItem(TreeItem item);

  /**
   *
   * @author Lukas Balzer
   *
   * @return the item representing the project in the tree
   */
  TreeItem getItem();

  /**
   * tells the selector that he is active so that he can update the header
   * 
   * @author Lukas Balzer
   *
   */
  void activate();

  /**
   * Setter for the path history which is shown in the shell title
   * 
   * @author Lukas Balzer
   * @param pathHistory
   *          the relative path of the step which means: <br>
   *          <code>project&#8594;category&#8594;step</code>
   *
   */
  void setPathHistory(String pathHistory);

  /**
   * is called when the selection(e.g. the project data) is removed
   * 
   * @author Lukas Balzer
   *
   */
  void cleanUp();

  /**
   * @param children
   *          the children to set
   */
  public void addChild(IProjectSelection child);

  public void setSelectionListener(Listener selectionListener);

  IDataModel getProjectData();
}
