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
/**
 * 
 * @author Lukas Balzer
 */
package xstampp.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class MultiDataPage extends CSVExportPage {

  private Composite control;
  private List<Button> steps;
  private List<String> stepViews;

  /**
   * @author Lukas Balzer
   * @param filters
   *          the file extensions, which shall be excepted by in the dialog
   * @param pageName
   *          the Name of this page, that is displayed in the header of the
   *          wizard
   * @param projectName
   *          The Name of the project
   * @param pluginID
   *          TODO
   */
  public MultiDataPage(ArrayList<String> expOptions, String[] filters, String pageName, String projectName,
      String pluginID) {
    super(filters, pageName, pluginID);
    this.setTitle(pageName);
    this.stepViews = expOptions;

    this.setDescription(Messages.SetValuesForTheExportFile);
  }

  @Override
  public void createControl(Composite parent) {
    this.control = new Composite(parent, SWT.NONE);
    this.control.setBounds(parent.getBounds());
    this.control.setLayout(new FormLayout());

    Group group = new Group(this.control, SWT.SHADOW_IN);
    group.setText(Messages.ChangeExportValues);
    group.setLayout(new RowLayout(SWT.VERTICAL));
    FormData data = new FormData();
    data.top = new FormAttachment(AbstractWizardPage.COMPONENT_OFFSET);
    data.width = parent.getBounds().width;
    group.setLayoutData(data);
    this.steps = new ArrayList<>();
    for (String ref : this.stepViews) {
      Button stepButton = new Button(group, SWT.CHECK);
      stepButton.setText(ref);
      stepButton.setSelection(true);
      stepButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          MultiDataPage.this.setPageComplete(checkFinish());
        }
      });
      // each button is mapped to a view id, so it can later be tracked
      this.steps.add(stepButton);
    }
    Composite selectionComp = new Composite(group, SWT.NONE);
    selectionComp.setLayout(new RowLayout(SWT.HORIZONTAL));
    Button chooseAll = new Button(selectionComp, SWT.PUSH);
    chooseAll.setText(Messages.AddAll);
    chooseAll.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        for (Button selector : MultiDataPage.this.steps) {
          selector.setSelection(true);
        }
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        // do nothing by default

      }
    });

    Button removeAll = new Button(selectionComp, SWT.PUSH);
    removeAll.setText(Messages.RemoveAll);
    removeAll.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        for (Button selector : MultiDataPage.this.steps) {
          selector.setSelection(false);
        }
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        // do nothing by default

      }
    });

    super.createControl(this.control);
    Composite inheritedComp = (Composite) this.getControl();
    data = new FormData();
    data.top = new FormAttachment(group, AbstractWizardPage.COMPONENT_OFFSET);
    inheritedComp.setLayoutData(data);
    this.setControl(this.control);

  }

  @Override
  public boolean checkFinish() {
    if (this.getSteps() == 0) {
      this.setErrorMessage(Messages.NoDataSelected);
      return false;
    }
    return super.checkFinish();
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return return an steps.size() long integer where a the selection are coded
   *         as bit states
   */
  public int getSteps() {
    int stepCode = 0;
    for (int i = 0; i < this.steps.size(); i++) {
      if (this.steps.get(i).getSelection()) {
        stepCode = stepCode | (1 << i);
      }
    }

    return stepCode;

  }

  @Override
  public boolean asOne() {
    return true;
  }

}