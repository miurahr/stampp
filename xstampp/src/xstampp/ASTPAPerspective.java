package xstampp;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ASTPAPerspective implements IPerspectiveFactory {

	private static final Logger LOGGER = Logger.getRootLogger();

	@Override
	public void createInitialLayout(IPageLayout layout) {
		ASTPAPerspective.LOGGER.debug("Setup perspective"); //$NON-NLS-1$
		layout.setFixed(false);
		
		layout.setEditorAreaVisible(true);
		layout.addView(
				"astpa.explorer", IPageLayout.LEFT, 0.2f, layout.getEditorArea()); //$NON-NLS-1$
	}

}