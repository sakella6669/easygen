package org.easygen.ui.wizards.pages;

import org.easygen.core.config.ProjectConfig;
import org.easygen.ui.generators.project.EclipseProjectUIConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.EclipseUtils;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.validator.Validator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * @author eveno
 * Created on 2 févr. 07
 */
public class NewProjectPage extends CommonPage
{
	public static final String NAME = "newProjectPage";

	protected Text projectNameField;
	protected Text projectSrcDirField;
	protected Text projectClassesDirField;
	protected Text projectPackageField;
	protected Text projectCfgDirField;
	protected Text projectLibDirField;
	protected Text projectWebDirField;
	protected Text projectTestDirField;

	protected Button useDefaultLocation = null;
	protected Text projectPathField = null;

	/**
	 * @param pParent
	 * @param pStyle
	 */
    public NewProjectPage()
    {
    	super(NAME, Localization.get("easygen.title.page.newproject"));
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
   public void initControl(Composite mainComposite)
    {
		projectNameField = WidgetUtils.createLabelTextPair(
			mainComposite,
			Localization.get("easygen.label.page.newproject.name"),
			Localization.get("defaults.project.name")
		);
		addFieldToValidate(
			Localization.get("easygen.label.page.newproject.name"),
			projectNameField,
			Validator.PACKAGE
		);
		projectNameField.setFocus();

		createLocationGroup(mainComposite);

		createPropertiesGroup(mainComposite);
    }
	/**
     * @param pMainComposite
     */
    protected void createLocationGroup(Composite pMainComposite)
    {
		Group locationGroup = WidgetUtils.createGroup(pMainComposite, Localization.get("easygen.title.page.newproject.location"), 2);

		useDefaultLocation = WidgetUtils.createCheckBox(locationGroup, Localization.get("easygen.label.page.newproject.location.default"), true);
		WidgetUtils.createHorizontalSpan(useDefaultLocation, 2);
		projectPathField = WidgetUtils.createLabelTextPair(
			locationGroup,
			Localization.get("easygen.label.page.newproject.location"),
			EclipseUtils.getDefaultProjectLocationForName(projectNameField.getText())
		);
		projectPathField.setEnabled(false);
		addFieldToValidate(Localization.get("easygen.label.page.newproject.location"), projectPathField, Validator.PATH);
		useDefaultLocation.addSelectionListener(new CheckSelectedListener(projectPathField, false));
		projectNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent pEvent) {
				projectPathField.setText(
					EclipseUtils.getDefaultProjectLocationForName(projectNameField.getText())
				);
			}
		});
    }
	/**
     * @param pMainComposite
     */
    protected void createPropertiesGroup(Composite pMainComposite)
    {
		Group propertiesGroup = WidgetUtils.createGroup(pMainComposite, Localization.get("easygen.title.page.newproject.properties"), 2);

		projectSrcDirField = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.newproject.src.dir"), Localization.get("defaults.project.src.dir"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.src.dir"), projectSrcDirField, Validator.PATH);

		projectCfgDirField = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.newproject.config.dir"), Localization.get("defaults.project.config.dir"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.config.dir"), projectCfgDirField, Validator.PATH);

		projectClassesDirField = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.newproject.classes.dir"), Localization.get("defaults.project.classes.dir"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.classes.dir"), projectClassesDirField, Validator.PATH);
		
		projectWebDirField = WidgetUtils.createLabelTextPair(propertiesGroup,
				Localization.get("easygen.label.page.newproject.web.dir"),
				Localization.get("defaults.project.web.dir"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.web.dir"), projectWebDirField, Validator.PATH);
		
		projectTestDirField = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.newproject.test.dir"), Localization.get("defaults.project.test.dir"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.test.dir"), projectTestDirField, Validator.PATH);
		
		projectLibDirField = WidgetUtils.createLabelTextPair(propertiesGroup,
				Localization.get("easygen.label.page.newproject.lib.dir"),
				"");
		String defaultLibDir = Localization.get("defaults.project.lib.dir");
		if (defaultLibDir.startsWith("/") == false)
			defaultLibDir = '/'+defaultLibDir;
		updateLibDir(defaultLibDir);
		final String defaultProjectLibDir = defaultLibDir;
		projectLibDirField.setEditable(false);
		//addFieldToValidate(Localization.get("easygen.label.page.newproject.test.dir"), projectLibDirField, Validator.PATH);
		projectWebDirField.addModifyListener(new ModifyListener() {
			private final String DEFAULT_PROJECT_LIB_DIR = defaultProjectLibDir;
			public void modifyText(ModifyEvent pEvent) {
				updateLibDir(DEFAULT_PROJECT_LIB_DIR);
			}
		});
		
		projectPackageField = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.newproject.package"), Localization.get("defaults.project.package"));
		addFieldToValidate(Localization.get("easygen.label.page.newproject.package"), projectPackageField, Validator.PACKAGE);
    }
    
	private void updateLibDir(String defaultProjectLibDir) {
		projectLibDirField.setText(
			projectWebDirField.getText() + defaultProjectLibDir
		);
	}
	/**
     * @see org.easygen.ui.wizards.pages.ICommonPage#updateConfig(ProjectConfig)
     */
    @Override
	public void updateConfig(ProjectConfig pProjectConfig)
    {
		EclipseProjectUIConfig projectModuleConfig = new EclipseProjectUIConfig();
        try {
        	Class.forName("org.eclipse.wst.common.componentcore.ModuleCoreNature");
        	pProjectConfig.setProjectNature(EclipseProjectUIConfig.WTP_NATURE);
        } catch (Exception e) {
	        logger.info("WTP Web Project Not Found");
	    	pProjectConfig.setProjectNature(EclipseProjectUIConfig.JAVA_NATURE);
        }
		pProjectConfig.setProjectModuleConfig(projectModuleConfig);
    	pProjectConfig.setName(projectNameField.getText());
    	pProjectConfig.setPath(projectPathField.getText());
    	pProjectConfig.setPackageName(projectPackageField.getText());
    	pProjectConfig.setSrcDirname(projectSrcDirField.getText());
    	pProjectConfig.setTestDirname(projectTestDirField.getText());
    	pProjectConfig.setLibDirname(projectLibDirField.getText());
    	pProjectConfig.setBuildDirname(projectClassesDirField.getText());
    	pProjectConfig.setCfgDirname(projectCfgDirField.getText());
    	pProjectConfig.setWebContentDirname(projectWebDirField.getText());
    }
}
