/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.model;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.haz.ITableModel;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractLTLProvider implements ITableModel {

  @XmlElement(name = "ruleID")
  protected UUID id;

  @XmlElement(name = "ruleNR")
  protected int number;

  @XmlElement(name = "criticalCombies")
  protected String combies;

  @XmlElement(name = "RefinedSafetyRule")
  protected String rule;

  @XmlElement(name = "refinedUCA")
  protected String rUCA;

  @XmlElement(name = "refinedSC")
  protected String rSCt;

  @XmlElement(name = "ltlProp")
  protected String ltl;

  @XmlElement(name = "type")
  protected String type;

  @XmlElement(name = "controlAction")
  protected String controlAction;

  @XmlElement(name = "links")
  protected String links;

  @XmlElementWrapper(name = "relatedUCAIDs")
  @XmlElement(name = "ucaID")
  protected List<UUID> relatedUCAs;

  @XmlElement(name = "relatedCaID")
  protected UUID caID;

  /**
   * @return A formula which formulates the critical combination in Linear Temporal Logic 
   */
  public String getLtlProperty() {
    return this.ltl;
  }

  /**
   * @return A refined UCA entry which describes the interaction between control action and process values
   */
  public String getRefinedUCA() {
    return this.rUCA;
  }

  /**
   * @return A rule to prevent the critical state to happen
   */
  public String getSafetyRule() {
    return this.rule;
  }
  /**
   * 
   * @return The number of the referenced rule.
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * @return A UUID with which a control action is registered in the data model
   */
  public UUID getRelatedControlActionID() {
    return this.caID;
  }
  
  /**
   * 
   * @return A list with UUIDs which must belong to UCA's {@link #getAllUnsafeControlActions()}.
   */
  public List<UUID> getUCALinks() {
    return this.relatedUCAs;
  }

  /**
   * @return The constraint which should always hold in the system
   */
  public String getRefinedSafetyConstraint() {
    return this.rSCt;
  }

  /**
   * @return the id
   */
  public UUID getRuleId() {
    return this.id;
  }
  
  /**
   * 
   * @return the Type of the context the rule should be generated for one of the <code>TYPE</code> constants Defined in IValueCombie
   */
  public String getType() {
    return this.type;
  }

  public String getLinks() {
    return this.links;
  }
}
