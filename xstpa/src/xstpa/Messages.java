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
package xstpa;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "xstpa.messages"; //$NON-NLS-1$
	public static String AddsNewTableEntry;
	public static String ChecksTableForConflicts;
	public static String ContextTableContext_NotProvided;
	public static String ContextTableContext_Provided;
	public static String ContextTableFilter_All;
	public static String ContextTableFilter_Hazardous;
	public static String ContextTableFilter_NotHazardous;
	public static String DeletesRow;
	public static String GenerateNewTableMsg;
	public static String KeepRules;
	public static String LinkSomeVariables;
	public static String LinkVariablesMessage;
	public static String NoLinkedVariables;
	public static String NoLinkedVariablesMsg;
	public static String NOTestsetsAvailable;
	public static String OpensSettingsACTS;
	public static String Param_ConflictsMsg;
	public static String PreserveOldRules;
	public static String RefinedRulesTable_ConfirmDelete;
	public static String RefinedRulesTable_DeleteAll;
	public static String RefinedRulesTable_EditUCALinks;
	public static String RefinedRulesTable_Export;
	public static String RefinedRulesTable_NoHazardousCombies;
	public static String RefinedRulesTable_ReallyDeleteRefinedSafety;
	public static String RefinedRulesTable_Remove;
	public static String RefinedRulesTable_RemoveAll;
	public static String RefinedRulesTable_Type;
	public static String SelectAControlAction;
	public static String FOR_MODERATE_SIZE_SYSTEM;
	public static String FOR_LARGE_SIZE_SYSTEM;
	public static String SPECIAL_TESTING_ALGO;
	public static String IGNORE_CONSTRAINTS;
	public static String FORBIDDEN_TUPLES;
	public static String CSP_SOLVER;
	public static String CONTEXT_TABLE_SETTINGS;
	public static String GENERAL_OPTIONS;
	public static String RELATIONS;
	public static String CONSTRAINTS;
	public static String OK_BUTTON;
	public static String CANCEL_BUTTON;
	public static String ALGORITHM_LABEL;
	public static String STRENGTH_LABEL;
	public static String MODE_LABEL;
	public static String CONSTRAINT_HANDLING_LABEL;
	public static String PARAMETERS_LABEL;
	public static String STRENGTH_LABEL2;
	public static String ADD_BUTTON;
	public static String REMOVE_BUTTON;
	public static String SET_ACTS_PATH_BUTTON;
	public static String LOAD_DEFAULT_BUTTON;
	public static String PARAMETER_NAME;
	public static String PARAMETER_NAMES;
	public static String PARAMETER_VALUE;
	public static String CONSTRAINT_EDITOR;
	public static String ADD_CONSTRAINT;
	public static String ADDED_CONSTRAINTS;
	public static String SYMBOLS;
	public static String ENTER_A_EXPRESSION;
	public static String CONTROLLER;
	public static String PM;
	public static String PMV;
	public static String PMVV;
	public static String COMMENTS;
	public static String CONTROL_ACTIONS;
	public static String SAFETY_CRITICAL;
	public static String ENTRY_ID;
	public static String LIST_of_CA;
	public static String CONTEXT;
	public static String IS_HAZARDOUS;
	public static String HAZ_IF_ANYTIME;
	public static String HAZ_IF_EARLY;
	public static String HAZ_IF_LATE;
	public static String LTL_RULES;
	public static String UCA;
	public static String REL_HAZ;
	public static String REFINED_RULES;
	public static String CRITICAL_COMBI;
	public static String CONTEXT_TABLE;
	public static String CONTEXT_TYPE;
	public static String RULES_TABLE;
	public static String DEPENDENCIES_TABLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
