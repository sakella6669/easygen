package org.easygen.core.generators.common;

import org.apache.log4j.Logger;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;

public class CommonGenerator extends AbstractGenerator {
	
	private static final Logger logger = Logger.getLogger(CommonGenerator.class);

	private static final String MODULE_NAME = "common";

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
		logger.info("Generating common files");
        createPath(projectConfig.getCfgPath());
		generateFile("log4j.xml.vm", projectConfig.getCfgPath() + "log4j.xml");
	}
	/**
     * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
     */
    @Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
        generateFile("pom.xml.vm", projectConfig.getPath() + "pom.xml");
    }
}
