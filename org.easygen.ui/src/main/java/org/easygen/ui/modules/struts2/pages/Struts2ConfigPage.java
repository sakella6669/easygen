package org.easygen.ui.modules.struts2.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.BasicModulePage;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 */
public class Struts2ConfigPage extends BasicModulePage {

	@SuppressWarnings("unused")
	private Combo templateEngineCombo;

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public Struts2ConfigPage(String nature, Class<? extends ModuleConfig> configClass) {
		super("struts2ConfigPage", nature, configClass);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite mainComposite) {
		super.initControl(mainComposite);

		createConfigurationGroup(mainComposite);
	}

	/**
	 * This method initializes idConfigurationGroup
	 */
	protected void createConfigurationGroup(Composite pParent) {
		// TODO Ajouter le support de SiteMesh
		Group configurationGroup = WidgetUtils.createGroup(pParent,
				Localization.get("struts2.title.configPage.templateEngine"), 2);

		templateEngineCombo = WidgetUtils.createLabelComboPair(configurationGroup,
				Localization.get("struts2.label.configPage.templateEngine"),
				new String[] { "Tiles2", "SiteMesh" } );
	}
}
