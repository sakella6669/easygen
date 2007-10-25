package org.easygen.ui.modules.springservice.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.BasicModulePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 */
public class SpringServiceConfigPage extends BasicModulePage {

	private Button useSimpleServices;

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public SpringServiceConfigPage(String nature, Class<? extends ModuleConfig> configClass) {
		super("springServiceConfigPage", Localization.get("spring.title.springConfigPage"), nature, configClass);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite mainComposite) {
		super.initControl(mainComposite);

		createGeneralPropertiesGroup(mainComposite);
	}

	/**
	 * @param mainComposite
	 */
	protected void createGeneralPropertiesGroup(Composite mainComposite) {
		Group group = WidgetUtils.createGroup(mainComposite, Localization.get("spring.title.springConfigPage.group"), 2);
		useSimpleServices = WidgetUtils.createCheckBox(group, Localization.get("spring.label.springConfigPage.useSimpleServices"), false);
	}
	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#updateConfig(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void updateConfig(ProjectConfig pProjectConfig) throws Exception {
		super.updateConfig(pProjectConfig);
		
		SpringServiceModuleConfig serviceConfig = (SpringServiceModuleConfig) pProjectConfig.getServiceModuleConfig();
		serviceConfig.setUseSimpleServices(useSimpleServices.getSelection());
	}
}
