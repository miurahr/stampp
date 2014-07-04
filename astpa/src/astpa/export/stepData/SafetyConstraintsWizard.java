package astpa.export.stepData;

import messages.Messages;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.SimpleExportPage;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.sds.SafetyConstraintView;

/**
 *
 * @author Lukas Balzer
 *
 */
public class SafetyConstraintsWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public SafetyConstraintsWizard() {
		super(SafetyConstraintView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new SimpleExportPage(filters,Messages.ExportPreferences,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}