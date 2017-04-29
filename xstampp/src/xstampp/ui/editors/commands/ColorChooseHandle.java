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
package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

/**
 * this Handler changes either the {@link ITextEditor#FOREGROUND} or the
 * {@link ITextEditor#BACKGROUND} this is done by calling
 * {@link ITextEditor#setStyleColor(String, RGB)} with the value of the
 * parameters
 * <ul>
 * <li><code>xstampp.commandParameter.color.type</Code>
 * <li><code>xstampp.commandParameter.color.red</Code>
 * <li><code>xstampp.commandParameter.color.green</Code>
 * <li><code>xstampp.commandParameter.color.blue</Code>
 * </ul>
 * 
 * @author Lukas Balzer
 * 
 * @see ITextEditor
 * @since 1.0
 */
public class ColorChooseHandle extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    String colorType = event.getParameter("xstampp.commandParameter.color.type"); //$NON-NLS-1$
    String redValue = event.getParameter("xstampp.commandParameter.color.red"); //$NON-NLS-1$
    String greenValue = event.getParameter("xstampp.commandParameter.color.green"); //$NON-NLS-1$
    String blueValue = event.getParameter("xstampp.commandParameter.color.blue"); //$NON-NLS-1$
    if (activeEditor instanceof ITextEditor && colorType != null) {
      RGB newRgb;
      if (redValue != null && greenValue != null && blueValue != null) {
        newRgb = new RGB(Integer.parseInt(redValue), Integer.parseInt(greenValue), Integer.parseInt(blueValue));
      } else {
        ColorDialog dialog = new ColorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

        newRgb = dialog.open();

      }
      if (newRgb != null) {
        ((ITextEditor) activeEditor).setStyleColor(colorType, newRgb);
      }
      return newRgb;
    }
    return null;
  }

}
