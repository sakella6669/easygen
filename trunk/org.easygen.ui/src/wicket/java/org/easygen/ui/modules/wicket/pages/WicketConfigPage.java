package org.easygen.ui.modules.wicket.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.ui.wizards.pages.BasicModulePage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class WicketConfigPage extends BasicModulePage {

//	private Button showPage;
//	private Button addPage;
//	private Button viewPage;
//	private Button editPage;
//	private Button enableRemoveObject;

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public WicketConfigPage(String nature, Class<? extends ModuleConfig> configClass) {
		super("wicketConfigPage", nature, configClass);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite mainComposite) {
		super.initControl(mainComposite);
//		createNavigationConfigurationGroup(mainComposite);
	}
//
//	/**
//	 * This method initializes idConfigurationGroup
//	 */
//	protected void createNavigationConfigurationGroup(Composite pParent) {
//		Group navigationGroup = WidgetUtils.createGroup(pParent,
//				Localization.get("wicket.title.configPage.navigation"), 2);
//
//		showPage = WidgetUtils.createCheckBox(navigationGroup,
//				Localization.get("wicket.label.configPage.navigation.showPage"), true);
//		showPage.setEnabled(false);
//
//		addPage = WidgetUtils.createCheckBox(navigationGroup,
//				Localization.get("wicket.label.configPage.navigation.addPage"), true);
//
//		viewPage = WidgetUtils.createCheckBox(navigationGroup,
//				Localization.get("wicket.label.configPage.navigation.viewPage"), true);
//		
//		editPage = WidgetUtils.createCheckBox(navigationGroup,
//				Localization.get("wicket.label.configPage.navigation.editPage"), true);
//		
//		enableRemoveObject  = WidgetUtils.createCheckBox(navigationGroup,
//				Localization.get("wicket.label.configPage.navigation.enableRemoveObject"), true);
//	}
//	/**
//	 * @see org.easygen.ui.wizards.pages.BasicModulePage#updateConfig(org.easygen.core.config.ProjectConfig)
//	 */
//	@Override
//	public void updateConfig(ProjectConfig pProjectConfig) throws Exception {
//		super.updateConfig(pProjectConfig);
//
//		WicketModuleConfig viewConfig = (WicketModuleConfig) pProjectConfig.getViewModuleConfig();
//		
//		NavigationConfig navigationConfig = new NavigationConfig();
//		navigationConfig.setShowPage(showPage.getSelection());
//		navigationConfig.setAddPage(addPage.getSelection());
//		navigationConfig.setViewPage(viewPage.getSelection());
//		navigationConfig.setEditPage(editPage.getSelection());
//		navigationConfig.setEnableRemoveObject(enableRemoveObject.getSelection());
//		viewConfig.setNavigationConfig(navigationConfig);
//	}
}
