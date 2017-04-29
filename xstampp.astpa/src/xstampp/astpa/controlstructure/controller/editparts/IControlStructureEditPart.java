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

package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * @author Lukas
 * 
 */
public interface IControlStructureEditPart extends GraphicalEditPart,MouseMotionListener {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the id of this editParts model
	 */
	UUID getId();

	/**
	 * provides the right transformation to realize the a child-parent
	 * releationship in gef
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param t
	 *            the Translatable which is given to it's parents figure
	 */
	void translateToRoot(Translatable t);
	
	/**
	 * @param store the store to set
	 */
	public void setPreferenceStore(IPreferenceStore store);

	void refreshModel();
}
