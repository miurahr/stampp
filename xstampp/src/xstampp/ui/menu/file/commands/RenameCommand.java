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
package xstampp.ui.menu.file.commands;

import java.util.UUID;

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

/**
 * opens a rename dialoge and calls the function
 * {@link ProjectManager#renameProject(UUID, String)}
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class RenameCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IProjectSelection) {
      UUID projectId = ((IProjectSelection) selection).getProjectId();
      String currentString = ProjectManager.getContainerInstance().getTitle(projectId);

      InputDialog renameDiag = new InputDialog(Display.getCurrent().getActiveShell(), Messages.RenameCommand_RenameProject,
          Messages.RenameCommand_NewProjectName, currentString, new ProjectNameValidator(projectId));
      if (renameDiag.open() == Window.OK
          && ProjectManager.getContainerInstance().renameProject(projectId, renameDiag.getValue())) {
        MessageDialog.openError(null, Messages.Error, Messages.RenameCommand_CannotRenameProject);
      }
    }
    return null;
  }

  private class ProjectNameValidator implements IInputValidator {

    private UUID projectId;

    public ProjectNameValidator(UUID projectId) {
      this.projectId = projectId;
    }

    @Override
    public String isValid(String newName) {
      for (UUID id : ProjectManager.getContainerInstance().getProjects().keySet()) {

        if (!this.projectId.equals(id) && newName.equals(ProjectManager.getContainerInstance().getTitle(id))) {
          return Messages.RenameCommand_TheProject + newName + Messages.RenameCommand_alreadyExists;
        }
      }
      return null;
    }

  }

}
