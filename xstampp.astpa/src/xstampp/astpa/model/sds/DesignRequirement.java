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

package xstampp.astpa.model.sds;

import xstampp.astpa.model.hazacc.ATableModel;

/**
 * Class representing the design requirement objects.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 */
public class DesignRequirement extends ATableModel {

	/**
	 * Constructor of a design requirement
	 * 
	 * @param title
	 *            the title of the new design requirement
	 * @param description
	 *            the description of the new design requirement
	 * @param number
	 *            the number of the new design requirement
	 * 
	 * @author Fabian Toth, Jaqueline Patzek
	 */
	public DesignRequirement(String title, String description, int number) {
		super(title, description, number);
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth, Jaqueline Patzek
	 */
	public DesignRequirement() {
		// empty constructor for JAXB
	}
}
