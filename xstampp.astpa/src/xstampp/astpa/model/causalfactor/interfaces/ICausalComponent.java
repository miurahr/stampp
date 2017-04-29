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

package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlstructure.components.ComponentType;

/**
 * A Component for the causal factors table
 * 
 * @author Fabian
 * 
 */
public interface ICausalComponent {

	/**
	 * Getter for the text
	 * 
	 * @return the text
	 * 
	 * @author Fabian Toth
	 */
	String getText();

	/**
	 * Getter for the id
	 * 
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	UUID getId();

	/**
	 * Getter for the causal factors
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the list of the causal factors
	 */
	List<ICausalFactor> getCausalFactors();

	/**
	 * 
	 * @return The ComponentType which is stored in the model
	 * 
	 * @author Lukas Balzer
	 */
	ComponentType getComponentType();
}
