package org.easygen.core.generators.struts2;

import java.util.LinkedList;
import java.util.List;

import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.config.ViewModuleConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;

/**
 * 
 */
public class Struts2Generator extends AbstractGenerator {

	protected static final String MODULE_NAME = "struts2";
	private static final String ACTION_SUFFIX = "Action";
	private static final String IS_SPRING_SERVICE_MODULE = "isSpringServiceModule";

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
		logger.info("Génération de la couche view");
		ViewModuleConfig viewModuleConfig = projectConfig.getViewModuleConfig();
		String srcPath = projectConfig.getSrcPath();
		createPackagePath(srcPath, viewModuleConfig.getPackageName());
		createPath(projectConfig.getWebContentPath(), "WEB-INF");
		createPath(projectConfig.getWebContentPath(), "common");

		// Variable qui précise si la couche service utilise Spring ou non
		boolean isSpringServiceModule = projectConfig.getServiceModuleNature().equals("spring");
		context.put(IS_SPRING_SERVICE_MODULE, isSpringServiceModule);
		
		copyStaticFiles(projectConfig);

        String filename = createJavaFilename(viewModuleConfig, "GenericAction");
        generateFile(getTemplate("src/GenericAction.java.vm"), srcPath + filename);

		List<String> classList = new LinkedList<String>();
		List<DataObject> objectList = new LinkedList<DataObject>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false)
				continue;
			context.put(OBJECT, object);

			// TODO Générer le mapping Service DTO <-> Web Form Bean avec un champ label
			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-add-validation", XML_FILE_EXTENSION);
			generateFile(getTemplate("src/Action-add-validation.xml.vm"), srcPath + filename);
			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-update-validation", XML_FILE_EXTENSION);
			generateFile(getTemplate("src/Action-update-validation.xml.vm"), srcPath + filename);

			filename = createJavaFilename(viewModuleConfig, object.getClassName() + ACTION_SUFFIX);
			generateFile(getTemplate("src/Action.java.vm"), srcPath + filename);
			
			String jspPath = projectConfig.getWebContentPath() + object.getClassName().toLowerCase();
			createPath(jspPath);
			// TODO Gérer l'option avec ou sans Struts Tiles
			generateFile(getTemplate("www/add.jsp.vm"), jspPath + "/add.jsp");
			generateFile(getTemplate("www/edit.jsp.vm"), jspPath + "/edit.jsp");
			generateFile(getTemplate("www/show.jsp.vm"), jspPath + "/show.jsp");

			context.remove(OBJECT);
			classList.add(object.getClassName());
			objectList.add(object);
		}
		
		context.put(CLASS_LIST, classList);
		generateFile(getTemplate("src/applicationContext-view.xml.vm"), srcPath + "applicationContext-view.xml");
		context.remove(CLASS_LIST);

		context.put(CLASS_LIST, classList);
		generateFile(getTemplate("src/struts.xml.vm"), srcPath + "struts.xml");
		generateFile(getTemplate("www/common/menu.jsp.vm"), projectConfig.getWebContentPath() + "common/menu.jsp");
		context.remove(CLASS_LIST);
	}
	/**
	 * @throws GenerationException 
	 * 
	 */
	protected void copyStaticFiles(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Génération des pages statiques de la couche view");
		ViewModuleConfig viewModuleConfig = projectConfig.getViewModuleConfig();
		String srcPath = projectConfig.getSrcPath();

        String filename = createFilename(viewModuleConfig, "GenericAction", PROPERTY_FILE_EXTENSION);
        copyFile("src/GenericAction.properties", srcPath + filename);

        String webPath = projectConfig.getWebContentPath();
        copyFile("WEB-INF/tiles.xml", webPath + "WEB-INF/tiles.xml");
        copyFile("WEB-INF/web.xml", webPath + "WEB-INF/web.xml");
        copyFile("www/common/layout.jsp", webPath + "common/layout.jsp");
        copyFile("www/common/styles.css", webPath + "common/styles.css");
        copyFile("www/index.jsp", webPath + "index.jsp");
	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {

	}
}
