package org.easygen.core.generators.project;

import java.io.File;

import org.apache.log4j.Logger;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;

/**
 * @author eveno
 * Created on 20 déc. 06
 *
 */
public class EclipseProjectGenerator extends AbstractGenerator {

	private static final Logger logger = Logger.getLogger(EclipseProjectGenerator.class);

	private static final String MODULE_NAME = "eclipseproject";
	private static final String SETTINGS_DIR = ".settings";
	private static final String BUILDER_DIR = ".externalToolBuilders";
	private static final String LIBRARY_LIST = "libraries";

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#getModuleName()
	 */
	@Override
	protected String getModuleName() {
		return MODULE_NAME;
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#generate()
	 */
	@Override
	public void generate(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Generating eclipse project files");
		createPath(projectConfig.getPath());
		createPath(projectConfig.getSrcPath());

		// Génération du fichier .project
		generateFile("project.vm", projectConfig.getPath() + ".project");

		if (projectConfig.getProjectNature().equals(EclipseProjectConfig.WTP_NATURE)) {
			createPath(projectConfig.getPath(), SETTINGS_DIR);
			String settingsDir = projectConfig.getPath() + SETTINGS_DIR + File.separatorChar;

			// Génération du fichier org.eclipse.wst.common.component
			String filename = "org.eclipse.wst.common.component";
			generateFile(filename + ".vm", settingsDir + filename);

			// Génération du fichier org.eclipse.wst.common.project.facet.core.xml
			filename = "org.eclipse.wst.common.project.facet.core.xml";
			generateFile(filename + ".vm", settingsDir + filename);

			// Génération du fichier org.eclipse.wst.validation.prefs
			filename = "org.eclipse.wst.validation.prefs";
			generateFile(filename + ".vm", settingsDir + filename);

			createPath(projectConfig.getPath(), BUILDER_DIR);
			String builderDir = projectConfig.getPath() + BUILDER_DIR + File.separatorChar;
			// Génération du fichier org.eclipse.wst.common.project.facet.core.builder.launch
			filename = "org.eclipse.wst.common.project.facet.core.builder.launch";
			generateFile(filename + ".vm", builderDir + filename);
		}
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
		// Génération du fichier .classpath
		// TODO Gérer les différences Eclipse 3.2 et 3.3 
		String filename = ".classpath";
		context.put(LIBRARY_LIST, projectConfig.getLibraries());
		generateFile("classpath.vm", projectConfig.getPath() + filename);
	}
}
