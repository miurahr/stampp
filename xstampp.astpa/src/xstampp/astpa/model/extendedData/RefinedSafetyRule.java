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
package xstampp.astpa.model.extendedData;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.haz.ITableModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;

@XmlRootElement(name = "rule")
public class RefinedSafetyRule extends AbstractLTLProvider{

	

	
	/**
	 * 
   * @param ucaLinks
   *          {@link AbstractLTLProvider#getUCALinks()}
   * @param combies
   *          {@link RefinedSafetyRule#getCriticalCombies()}
   * @param ltlExp
   *          {@link AbstractLTLProvider#getLtlProperty()}   
   * @param rule
   *          {@link AbstractLTLProvider#getSafetyRule()}
   * @param ruca
   *          {@link AbstractLTLProvider#getRefinedUCA()}
   * @param constraint
   *          {@link AbstractLTLProvider#getRefinedSafetyConstraint()}
   * @param nr
   *          {@link AbstractLTLProvider#getNumber()}
   * @param caID
   *          {@link AbstractLTLProvider#getRelatedControlActionID()}
   * @param type 
   *          {@link AbstractLTLProvider#getType()}
   * 
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * 
	 * @see IValueCombie
	 */
	public RefinedSafetyRule(AbstractLtlProviderData data, UUID caId,String type, int nr) {
		id = UUID.randomUUID();
		this.number = nr;
		this.combies = data.getCombies();
		this.rule = data.getSafetyRule();
		this.rSCt = data.getRefinedSafetyConstraint();
		this.rUCA = data.getRefinedUca();
		this.ltl = data.getLtlProperty();
		this.relatedUCAs = data.getUcaLinks();
		this.caID = caId;
		this.type = type;
		
	}

	public RefinedSafetyRule() {
		// TODO Auto-generated constructor stub
	}
	

  public boolean setUCALinks(List<UUID> ids){
		if(ids == null 
        || (this.relatedUCAs != null && this.relatedUCAs.equals(ids))){
			return false;
		}
		this.relatedUCAs = ids;
		return true;
	}
	
	public void removeUCALink(UUID id) {
		this.relatedUCAs.remove(id);
		
	}
	

	/**
	 * @param rule the rule to set
	 * @return 
	 */
	public boolean setSafetyRule(String rule) {
		if(rule == null 
        || (this.rule != null && this.rule.equals(rule))){
			return false;
		}
		this.rule = rule;
		return true;
	}


	

	/**
	 * @param refinedUCA the refinedUCA to set
	 * @return 
	 */
	public boolean setRefinedUCA(String refinedUCA) {
		if(refinedUCA == null 
        || (this.rUCA != null && this.rUCA.equals(refinedUCA))){
			return false;
		}
		this.rUCA = refinedUCA;
		return true;
	}


	/**
	 * @param refinedSafetyConstraint the refinedSafetyConstraint to set
	 * @return 
	 */
	public boolean setRefinedSafetyConstraint(String refinedSafetyConstraint) {
		if(refinedSafetyConstraint == null 
		    ||(this.rSCt != null && this.rSCt.equals(refinedSafetyConstraint))){
			return false;
		}
		this.rSCt = refinedSafetyConstraint;
		return true;
	}

	

	/**
	 * @param ltlProperty the ltlProperty to set
	 */
	public boolean setLtlProperty(String ltlProperty) {
		if(ltlProperty == null 
		    || (this.ltl != null && this.ltl.equals(ltlProperty))){
			return false;
		}
		this.ltl = ltlProperty;
		return true;
	}

	
	
	@Override
	public int compareTo(ITableModel sibling) {
		int sign =(int) Math.signum(getNumber() - sibling.getNumber());
		return sign;
	}

	/**
	 * @return A string that describes the critical combinations of process model values
	 */
	public String getCriticalCombies() {
		return this.combies;
	}
	
	/**
	 * @param controlActionID the controlActionID to set
	 * @return 
	 */
	public boolean setRelatedControlActionID(UUID controlActionID) {
		if(controlActionID == null 
        || (this.caID != null && this.caID.equals(controlActionID))){
			return false;
		}
		this.caID = controlActionID;
		return true;
	}

	/**
	 * @param number the number to set
	 * @return 
	 */
	public boolean setNumber(int number) {
		if(number == -1 ||this.number == number){
			return false;
		}
		this.number = number;
		return true;
	}

	/**
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * @return 
	 * @see IValueCombie
	 */
	public boolean setType(String type) {
		if(type == null 
        || (this.type != null && this.type.equals(type))){
			return false;
		}
		this.type = type;
		return true;
	}

	/**
	 * @param caID the caID to set
	 */
	public boolean setCaID(UUID caID) {
		if(caID == null 
        || (this.caID != null && this.caID.equals(caID))){
			return false;
		}
		this.caID = caID;
		return true;
	}


	/**
	 * @param criticalCombies the criticalCombies to set
	 * @return 
	 */
	public boolean setCriticalCombies(String criticalCombies) {
		if(criticalCombies == null 
        || (this.combies != null && this.combies.equals(criticalCombies))){
			return false;
		}
		this.combies = criticalCombies;
		return true;
	}
	/**
	 * @param controlAction the controlAction to set
	 * @return 
	 */
	public boolean setControlAction(String controlAction) {
		if(controlAction == null 
        || (this.controlAction != null && this.controlAction.equals(controlAction))){
			return false;
		}
		this.controlAction = controlAction;
		return true;
	}

	/**
	 * Set the Hazard Links as String in the form <i>H-1,H-2,...</i>
	 * @param links the links to set
	 * @return whether the links string could be changed,
	 *         if false than the given string is <code>null</code> or the equal to the
	 *         current links
	 */
	public boolean setLinks(String links) {
		if(links == null 
        || (this.links != null && this.links.equals(links))){
			return false;
		}
		this.links = links;
		return true;
	}

  @Override
  public String getDescription() {
    return rule;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return null;
  }
}
