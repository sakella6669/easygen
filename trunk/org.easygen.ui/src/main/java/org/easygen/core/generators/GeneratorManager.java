package org.easygen.core.generators;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.easygen.core.config.ProjectConfig;

/**
 * 
 */
public class GeneratorManager {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Chain the module generation
	 */
	public void generate(ProjectConfig projectConfig) throws GenerationException {
		Validate.notNull(projectConfig);
		Validate.notNull(projectConfig.getObjects());
		Validate.notNull(projectConfig.getDataModuleConfig());

		Generator[] generators = determineGenerators(projectConfig);
		for (Generator generator : generators) {
			generator.init(projectConfig);
		}
		for (Generator generator : generators) {
			generator.generate(projectConfig);
			generator.addMavenDependencies(projectConfig);
		}
		for (Generator generator : generators) {
			generator.postProcess(projectConfig);
		}
	}

	/**
	 * @param projectConfig
	 * @throws GenerationException
	 */
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {

	}

	/**
	 * 
	 * @param projectConfig
	 * @return
	 */
	protected Generator[] determineGenerators(ProjectConfig projectConfig) {
		List<Generator> generatorList = new LinkedList<Generator>();
		if (projectConfig.getProjectModuleConfig() != null) {
			generatorList.add(projectConfig.getProjectModuleConfig().getGenerator());
		}
		if (projectConfig.getCommonConfig() != null) {
			generatorList.add(projectConfig.getCommonConfig().getGenerator());
		}
		generatorList.add(projectConfig.getDataModuleConfig().getGenerator());
		if (projectConfig.getServiceModuleNature() != null) {
			generatorList.add(projectConfig.getServiceModuleConfig().getGenerator());
			if (projectConfig.getViewModuleNature() != null) {
				generatorList.add(projectConfig.getViewModuleConfig().getGenerator());
			}
		}
		return (Generator[]) generatorList.toArray(new Generator[generatorList.size()]);
	}

	/**
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	protected String[] merge(String[] array1, String[] array2) {
		if (array2.length == 0) {
			return array1;
		}
		logger.info("Adding " + array2.length + " libraries");
		String[] dest = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, dest, 0, array1.length);
		System.arraycopy(array2, 0, dest, array1.length, array2.length);
		return dest;
	}
}
