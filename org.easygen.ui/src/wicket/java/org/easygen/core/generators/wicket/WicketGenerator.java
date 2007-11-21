package org.easygen.core.generators.wicket;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;

public class WicketGenerator extends AbstractGenerator {

	private static final Logger logger = Logger.getLogger(WicketGenerator.class);
	
	protected static final String MODULE_NAME = "wicket";

	private static final String PAGE_SUFFIX = "Page";
	private static final String FORM_SUFFIX = "Form";

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#getModuleName()
	 */
	@Override
	protected String getModuleName() {
		return MODULE_NAME;
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#generate(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void generate(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Generating view layer files");
		WicketModuleConfig viewModuleConfig = (WicketModuleConfig) projectConfig.getViewModuleConfig();
//		NavigationConfig navigationConfig = viewModuleConfig.getNavigationConfig();
		String srcPath = projectConfig.getSrcPath();
		String cfgPath = projectConfig.getCfgPath();
		createPackagePath(srcPath, viewModuleConfig.getPackageName());

		copyStaticFiles(projectConfig);

		generateFile("src/applicationContext-view.xml.vm", cfgPath + "applicationContext-view" + XML_FILE_EXTENSION);
		generateFile("src/common/BaseForm.java.vm", srcPath + createJavaFilename(viewModuleConfig, "BaseForm"));
		generateFile("src/common/IndexPage.java.vm", srcPath + createJavaFilename(viewModuleConfig, "IndexPage"));
		generateFile("src/common/WebSession.java.vm", srcPath + createJavaFilename(viewModuleConfig, projectConfig.getName()+"WebSession"));
		
		createPackagePath(srcPath, viewModuleConfig.getPackageName()+".util");
		generateFile("src/util/DirectPageLink.java.vm", srcPath + createJavaFilename(viewModuleConfig, "util/DirectPageLink"));
		generateFile("src/util/LocalizerUtils.java.vm", srcPath + createJavaFilename(viewModuleConfig, "util/LocalizerUtils"));

		List<String> classList = new LinkedList<String>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false) {
				continue;
			}
			context.put(OBJECT, object);
			
			WordUtils.swapCase(object.getClassName());
			String objectPackage = Character.toLowerCase(object.getClassName().charAt(0))+object.getClassName().substring(1);

			createPackagePath(srcPath, viewModuleConfig.getPackageName()+'.'+objectPackage);

			String filename = createJavaFilename(viewModuleConfig, objectPackage+"/Add" + object.getClassName() + PAGE_SUFFIX);
			generateFile("src/AddPage.java.vm", srcPath + filename);
			filename = createJavaFilename(viewModuleConfig, objectPackage+"/Edit" + object.getClassName() + PAGE_SUFFIX);
			generateFile("src/EditPage.java.vm", srcPath + filename);
			filename = createJavaFilename(viewModuleConfig, objectPackage+'/'+object.getClassName() + "List" + PAGE_SUFFIX);
			generateFile("src/ListPage.java.vm", srcPath + filename);
			filename = createJavaFilename(viewModuleConfig, objectPackage+"/View" + object.getClassName() + PAGE_SUFFIX);
			generateFile("src/ViewPage.java.vm", srcPath + filename);
			filename = createJavaFilename(viewModuleConfig, objectPackage+'/'+object.getClassName() + FORM_SUFFIX);
			generateFile("src/Form.java.vm", srcPath + filename);

			createPackagePath(cfgPath, viewModuleConfig.getPackageName()+'.'+objectPackage);

			filename = createFilename(viewModuleConfig, objectPackage+"/Add" + object.getClassName() + PAGE_SUFFIX, HTML_FILE_EXTENSION);
			generateFile("www/AddPage.html.vm", cfgPath + filename);
			filename = createFilename(viewModuleConfig, objectPackage+"/Edit" + object.getClassName() + PAGE_SUFFIX, HTML_FILE_EXTENSION);
			generateFile("www/EditPage.html.vm", cfgPath + filename);
			filename = createFilename(viewModuleConfig, objectPackage+"/View" + object.getClassName() + PAGE_SUFFIX, HTML_FILE_EXTENSION);
			generateFile("www/ViewPage.html.vm", cfgPath + filename);
			filename = createFilename(viewModuleConfig, objectPackage+'/'+object.getClassName() + "List" + PAGE_SUFFIX, HTML_FILE_EXTENSION);
			generateFile("www/ListPage.html.vm", cfgPath + filename);

			// TODO Add french translation
			filename = createFilename(viewModuleConfig, objectPackage+'/'+object.getClassName() + "List" + PAGE_SUFFIX, PROPERTY_FILE_EXTENSION);
			generateFile("www/ListPage.properties.vm", cfgPath + filename);

			context.remove(OBJECT);
			classList.add(object.getClassName());
		}

		context.put(CLASS_LIST, classList);
		// TODO Add french translation
		generateFile("www/common/BasePage.properties.vm", cfgPath + createFilename(viewModuleConfig, "BasePage", PROPERTY_FILE_EXTENSION));
		generateFile("src/common/Application.java.vm", srcPath + createJavaFilename(viewModuleConfig, projectConfig.getName()+"Application"));
		generateFile("src/common/BasePage.java.vm", srcPath + createJavaFilename(viewModuleConfig, "BasePage"));
		context.remove(CLASS_LIST);
	}
	
	protected void copyStaticFiles(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Génération des pages statiques de la couche view");
		WicketModuleConfig viewModuleConfig = (WicketModuleConfig) projectConfig.getViewModuleConfig();
        String webPath = projectConfig.getWebContentPath();
        String cfgPath = projectConfig.getCfgPath();
        createPath(webPath, "WEB-INF");
        createPath(webPath, "common");
		createPackagePath(cfgPath, viewModuleConfig.getPackageName());

        copyFile("www/web.xml", webPath + "WEB-INF/web.xml");
        copyFile("www/common/styles.css", webPath + "common/styles.css");
        copyFile("www/common/BasePage.html", cfgPath + createFilename(viewModuleConfig, "BasePage", HTML_FILE_EXTENSION));
        copyFile("www/common/IndexPage.html", cfgPath + createFilename(viewModuleConfig, "IndexPage", HTML_FILE_EXTENSION));
	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
	}
}
