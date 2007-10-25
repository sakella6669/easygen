package org.easygen.ui.modules.struts2.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.struts2.NavigationConfig;
import org.easygen.core.generators.struts2.Struts2ModuleConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.BasicModulePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 */
public class Struts2ConfigPage extends BasicModulePage {

	private Combo templateEngineCombo;
	private Button showPage;
	private Button addPage;
	private Button viewPage;
	private Button editPage;
	private Button enableRemoveObject;

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
		createNavigationConfigurationGroup(mainComposite);
	}

	/**
	 * This method initializes idConfigurationGroup
	 */
	protected void createConfigurationGroup(Composite pParent) {
		Group configurationGroup = WidgetUtils.createGroup(pParent,
				Localization.get("struts2.title.configPage.templateEngine"), 2);

		templateEngineCombo = WidgetUtils.createLabelComboPair(configurationGroup,
				Localization.get("struts2.label.configPage.templateEngine"),
				Struts2ModuleConfig.TEMPLATE_ENGINES );
	}

	/**
	 * This method initializes idConfigurationGroup
	 */
	protected void createNavigationConfigurationGroup(Composite pParent) {
		Group navigationGroup = WidgetUtils.createGroup(pParent,
				Localization.get("struts2.title.configPage.navigation"), 2);

		showPage = WidgetUtils.createCheckBox(navigationGroup,
				Localization.get("struts2.label.configPage.navigation.showPage"), true);
		showPage.setEnabled(false);

		addPage = WidgetUtils.createCheckBox(navigationGroup,
				Localization.get("struts2.label.configPage.navigation.addPage"), true);

		viewPage = WidgetUtils.createCheckBox(navigationGroup,
				Localization.get("struts2.label.configPage.navigation.viewPage"), true);
		
		editPage = WidgetUtils.createCheckBox(navigationGroup,
				Localization.get("struts2.label.configPage.navigation.editPage"), true);
		
		enableRemoveObject  = WidgetUtils.createCheckBox(navigationGroup,
				Localization.get("struts2.label.configPage.navigation.enableRemoveObject"), true);
	}
	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#updateConfig(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void updateConfig(ProjectConfig pProjectConfig) throws Exception {
		super.updateConfig(pProjectConfig);

		Struts2ModuleConfig viewConfig = (Struts2ModuleConfig) pProjectConfig.getViewModuleConfig();
		String templateEngine = templateEngineCombo.getItem(templateEngineCombo.getSelectionIndex());
		viewConfig.setTemplateEngine(templateEngine);
		
		NavigationConfig navigationConfig = new NavigationConfig();
		navigationConfig.setShowPage(showPage.getSelection());
		navigationConfig.setAddPage(addPage.getSelection());
		navigationConfig.setViewPage(viewPage.getSelection());
		navigationConfig.setEditPage(editPage.getSelection());
		navigationConfig.setEnableRemoveObject(enableRemoveObject.getSelection());
		viewConfig.setNavigationConfig(navigationConfig);
	}
}
