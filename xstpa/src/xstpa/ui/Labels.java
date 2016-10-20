package xstpa.ui;

import org.eclipse.osgi.util.NLS;

public class Labels extends NLS {
	private static final String BUNDLE_NAME = "xstpa.ui.labels"; //$NON-NLS-1$
	// Table column names/properties
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
		NLS.initializeMessages(BUNDLE_NAME, Labels.class);
	}

	private Labels() {
	}
}
