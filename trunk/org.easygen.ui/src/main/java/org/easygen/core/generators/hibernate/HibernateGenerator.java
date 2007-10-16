package org.easygen.core.generators.hibernate;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.easygen.core.InitException;
import org.easygen.core.config.DataField;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.db.DatabaseManager;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;

/**
 * @author eveno
 * Created on 16 nov. 06
 */
public class HibernateGenerator extends AbstractGenerator {

	private static final String META_INF_DIR = "META-INF/";

	private static final String MODULE_NAME = "hibernate";

//	private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
	private static final String JPA_CFG_FILE = "persistence.xml";

	private static final String EHCACHE_CFG_FILE = "ehcache.xml";

	private static final String MAPPING_FILE_EXTENSION = ".hbm.xml";

	private static final String USE_ANNOTATIONS = "useAnnotations";
	private static final String HIBERNATE_DIALECT = "hibernateSqlDialect";

	/**
	 * @throws InitException
	 * @throws InitException
	 */
	public HibernateGenerator() {
		super();
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#getModuleDir()
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
		logger.info("Génération de la couche data");
		HibernateModuleConfig dataModuleConfig = (HibernateModuleConfig) projectConfig.getDataModuleConfig();
		String packageName = dataModuleConfig.getPackageName();
		createPackagePath(projectConfig.getSrcPath(), packageName);

		if (dataModuleConfig.isUseAnnotations()) {
			context.put(USE_ANNOTATIONS, Boolean.TRUE);
		}
		String hbmDir = projectConfig.getPath();
		if (dataModuleConfig.getHbmDir() == HibernateModuleConfig.SOURCE_DIR) {
			hbmDir += projectConfig.getSrcDirname();
		} else {
			hbmDir += dataModuleConfig.getHbmCustomDir();
		}
		hbmDir += File.separatorChar;

		Collection<DataObject> mappings = convertToHibernateMappings(dataModuleConfig, projectConfig.getObjects(),
				packageName);

		List<String> hbmList = new LinkedList<String>();
		List<String> cachedClassList = new LinkedList<String>();
		for (DataObject mapping : mappings) {
			context.put(OBJECT, mapping);
			String javaFilename = createJavaFilename(dataModuleConfig, mapping);
			generateFile("class.java.vm", projectConfig.getSrcPath() + javaFilename);
			if (dataModuleConfig.isUseAnnotations() == true) {
				hbmList.add(packageName + '.' + mapping.getClassName());
			} else {
				String hbmFilename = createFilename(dataModuleConfig, mapping, MAPPING_FILE_EXTENSION);
				generateFile("mapping.hbm.vm", hbmDir + hbmFilename);
				hbmList.add(FilenameUtils.separatorsToUnix(hbmFilename));
			}
			context.remove(OBJECT);
			if (mapping.isUseCache())
				cachedClassList.add(packageName + '.' + mapping.getClassName());
		}

		// TODO Tester si le dialect est necessaire
		String databaseType = projectConfig.getDatabaseConfig().getDatabaseType().toLowerCase();
		String hibernateDialect = getConfiguration().getString(databaseType + ".dialect");
		context.put(HIBERNATE_DIALECT, hibernateDialect);
		context.put(CLASS_LIST, hbmList);
//		generateFile("hibernate.cfg.vm", projectConfig.getCfgPath() + HIBERNATE_CFG_FILE);
		String metaInfPath = projectConfig.getCfgPath() + META_INF_DIR;
		createPath(metaInfPath);
		generateFile("persistence.xml.vm", metaInfPath + JPA_CFG_FILE);
		context.put(CLASS_LIST, cachedClassList);
		generateFile("ehcache.vm", projectConfig.getCfgPath() + EHCACHE_CFG_FILE);
	}

	/**
	 * @param pTables
	 * @return
	 */
	protected List<DataObject> convertToHibernateMappings(HibernateModuleConfig dataModuleConfig,
			List<DataObject> objects, String packageName) {
		List<DataObject> mappings = new LinkedList<DataObject>();
		for (DataObject dataObject : objects) {
			if (dataObject.isSelected() == false)
				continue;
			HibernateMapping mapping = new HibernateMapping(dataModuleConfig, dataObject/*, packageName*/);
			List<DataField> selectedFields = new LinkedList<DataField>();
			for (DataField field : mapping.getFields()) {
				if (field.isSelected()) {
//					if (field.isFk()) {
//						DataObject refObject = findRefObject(mappings, field.getForeignTableName());
//						field.setForeignObject(refObject);
//					}
					selectedFields.add(field);
				}
			}
			mapping.setFields(selectedFields);
			mappings.add(mapping);
		}
		return mappings;
	}

	protected DataObject findRefObject(List<DataObject> objects, String tableName) {
		for (DataObject object : objects) {
			if (object.getTableName().equals(tableName))
				return object;
		}
		return null;
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#copyLibraries(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public String[] copyLibraries(ProjectConfig projectConfig) throws GenerationException {
		String[] libraries = super.copyLibraries(projectConfig);
		URL jarUrl = DatabaseManager.getDriverJarUrl(projectConfig.getDatabaseConfig().getDatabaseType());
		String jarFilename = FilenameUtils.getName(jarUrl.getPath());
		copyLibrary(getLibraryDir(), projectConfig.getLibPath(), jarFilename, jarUrl);
		libraries = (String[]) ArrayUtils.add(libraries, jarFilename);
		return libraries;
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
	}
}
