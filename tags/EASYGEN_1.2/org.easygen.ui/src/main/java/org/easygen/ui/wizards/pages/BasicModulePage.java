package org.easygen.ui.wizards.pages;

import org.easygen.core.config.CommonConfig;
import org.easygen.core.config.DataModuleConfig;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.config.ProjectModuleConfig;
import org.easygen.core.config.ServiceModuleConfig;
import org.easygen.core.config.ViewModuleConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.validator.Validator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * @author eveno
 * Created on 2 mars 07
 */
public class BasicModulePage extends CommonPage
{
	private Text packageField;
	private String nature;
	private Class<? extends ModuleConfig> configClass;

	/**
	 * 
	 * @param pageName
	 * @param nature
	 * @param configClass
	 */
	public BasicModulePage(String pageName, String nature, Class<? extends ModuleConfig> configClass)
	{
		this(pageName, Localization.get("easygen.title.page.basicModulePage"), nature, configClass);
	}
	/**
	 * 
	 * @param pageName
	 * @param title
	 * @param nature
	 * @param configClass
	 */
	public BasicModulePage(String pageName, String title, String nature, Class<? extends ModuleConfig> configClass)
	{
		super(pageName, title);
		this.nature = nature;
		this.configClass = configClass;
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
	public void initControl(Composite mainComposite)
    {
		Group group = WidgetUtils.createGroup(mainComposite, Localization.get("easygen.title.page.basicModulePage.layer.group"), 2);

		WidgetUtils.createLabel(group, Localization.get("easygen.label.page.basicModulePage.layer.type"));
		WidgetUtils.createLabel(group, nature);
		packageField = WidgetUtils.createLabelTextPair(group, Localization.get("easygen.label.page.basicModulePage.layer.package"), "");
		addFieldToValidate(Localization.get("easygen.label.page.basicModulePage.layer.package"), packageField, Validator.PACKAGE);
    }
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#init(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void init(ProjectConfig pProjectConfig) {
		super.init(pProjectConfig);
		
		String packageName = pProjectConfig.getPackageName();
		try {
			ModuleConfig moduleconfig = configClass.newInstance();
			if (moduleconfig instanceof ProjectModuleConfig) {
				pProjectConfig.setProjectModuleConfig( (ProjectModuleConfig) moduleconfig);
				packageName += '.'+Localization.get("defaults.project.package");
			} else if (moduleconfig instanceof CommonConfig) {
				pProjectConfig.setCommonConfig( (CommonConfig) moduleconfig);
				packageName += '.'+Localization.get("defaults.package.common");
			} else if (moduleconfig instanceof DataModuleConfig) {
				pProjectConfig.setDataModuleConfig( (DataModuleConfig) moduleconfig);
				packageName += '.'+Localization.get("defaults.package.data");
			} else if (moduleconfig instanceof ServiceModuleConfig) {
				pProjectConfig.setServiceModuleConfig( (ServiceModuleConfig) moduleconfig);
				packageName += '.'+Localization.get("defaults.package.service");
			} else if (moduleconfig instanceof ViewModuleConfig) {
				pProjectConfig.setViewModuleConfig( (ViewModuleConfig) moduleconfig);
				packageName += '.'+Localization.get("defaults.package.view");
			}
		} catch (Exception e) {
			logger.error("Page initialization failed", e);
			MessageDialog.openError(getShell(), "Technical Error", "Page initialization failed: "+e.getLocalizedMessage());
		}
		packageField.setText(packageName);
		setPageComplete(validatePage());
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#updateConfig(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void updateConfig(ProjectConfig pProjectConfig) throws Exception {
		super.updateConfig(pProjectConfig);
		ModuleConfig moduleconfig = pProjectConfig.getModuleConfigForNature(nature);
		moduleconfig.setPackageName(packageField.getText());
	}
}
