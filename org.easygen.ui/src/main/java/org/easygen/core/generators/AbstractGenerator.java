package org.easygen.core.generators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.util.StringUtils;
import org.easygen.core.InitException;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.velocity.VelocityStackUtils;

/**
 * @author eveno
 * Created on 2 nov. 06
 */
public abstract class AbstractGenerator implements Generator, GeneratorConstants {

	protected static final String LIBRARY_EXTENSION = ".jar";
	protected static final String LIBRARY_INDEX_FILE = "dir.list";
	
	protected Logger logger = Logger.getLogger(getClass());
	protected VelocityContext context = null;
	protected Map<String,Template> templates;
	
	private CompositeConfiguration configuration = new CompositeConfiguration();

	/**
	 * @throws InitException
     *
     */
    public AbstractGenerator()
    {
	    super();
	    loadConfiguration();
	    templates = new Hashtable<String,Template>();
    }
	/**
	 * 
	 */
	protected void loadConfiguration() {
		addConfiguration("cfg/generators.properties");
		String filename = getTemplateDir()+getModuleName()+PROPERTY_FILE_EXTENSION;
		URL configFile = Thread.currentThread().getContextClassLoader().getResource(filename);
		if (configFile != null) {
			logger.info("Loading module specific configuration from: "+filename);
			addConfiguration(configFile);
		}
	}
	/**
	 * @param filename
	 * @throws InitException
	 */
	protected void addConfiguration(String filename) throws InitException {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);
		addConfiguration(resource);
	}
	/**
	 * @param resource
	 * @throws ConfigurationException
	 */
	protected void addConfiguration(URL resource) throws InitException {
		try {
			configuration.addConfiguration(new PropertiesConfiguration(
					resource
			));
		} catch (ConfigurationException e) {
			throw new InitException("Can't load configuration file: "+resource.toString(), e);
		}
	}
	/**
	 * @return the configuration
	 */
	protected CompositeConfiguration getConfiguration() {
		return configuration;
	}
	/**
	 * @see org.easygen.core.generators.Generator#init()
	 */
	public void init(ProjectConfig projectConfig)
	{
		try
        {
			logger.info("Initialisation de Velocity");
			initVelocity();
			context.put("stringutils", new StringUtils());
			if (projectConfig != null) {
			   	context.put(PROJECT_INFO, projectConfig);
			}
        }
        catch (Exception e)
        {
        	throw new InitException("Velocity initialization failed", e);
        }
	}
	/**
	 * @throws Exception
	 */
	protected void initVelocity() throws Exception {
		Properties properties = new Properties();
		InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cfg/velocity.properties");
		if (inStream == null)
			throw new GenerationException("Velocity initialization failed");
		properties.load(inStream);
		Velocity.init(properties);
        context = new VelocityContext();
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws Exception
	 */
	protected Template loadTemplate(String filename) throws InitException {
	    try {
			String templateFilename = getTemplateDir() + filename;
			logger.debug("Loading Template file: "+templateFilename);
			Template template = Velocity.getTemplate(templateFilename);
			return template;
		} catch (ResourceNotFoundException e) {
        	throw new InitException("Velocity file template not found: "+filename, e);
		} catch (ParseErrorException e) {
        	throw new InitException("Velocity template parse error: "+filename, e);
		} catch (Exception e) {
        	throw new InitException("Velocity template initialization failed: "+filename, e);
		}
	}
	/**
	 * @param filename
	 * @return
	 * @throws GenerationException
	 */
	protected Template getTemplate(String filename) throws GenerationException {
		if (templates.containsKey(filename)) {
			return templates.get(filename);
		}
		Template template = loadTemplate(filename);
		templates.put(filename, template);
		return template;
	}
	
	/**
     * @see org.easygen.core.generators.Generator#generate()
     */
	public abstract void generate(ProjectConfig projectConfig) throws GenerationException;

	protected void copyFile(String inputFilePath, String outputFilePath) throws GenerationException
	{
		String from = getTemplateDir()+inputFilePath;
		try {
			logger.debug("Copie du fichier "+from+" vers "+outputFilePath);
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(from);
			if (inputStream == null) {
				throw new GenerationException("Le fichier source est introuvable: "+from);
			}
			FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath));
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
			//FileUtils.copyFile(new File(from), new File(outputFilePath));
        } catch (Exception e) {
        	throw new GenerationException("Erreur lors de la copie du fichier: "+from, e);
        }
	}

	protected void generateFile(Template template, String outputFilePath) throws GenerationException
	{
		Validate.notNull(template, "Can't generate file : No given template");
		try {
			logger.debug("Generation du fichier "+outputFilePath);
			Writer writer = null;
	        writer = new FileWriter(outputFilePath);
	        template.merge(context, writer);
	        writer.close();
        } catch (Exception e) {
        	throw new GenerationException("Erreur lors de la génération du template: "+template.getName(), e);
        }
	}

	/**
     * @see org.easygen.core.generators.Generator#postProcess(ProjectConfig)
     */
	public abstract void postProcess(ProjectConfig projectConfig) throws GenerationException;

	/**
	 * Convert package to path with trailing path separator
	 * Ex: java.lang -> java/lang/
	 */
	protected String convertPackageNameToPath(String packageName)
	{
		return packageName.replace('.', File.separatorChar) + File.separatorChar;
	}
	/**
     * @param pSrcPath
     * @param pPackagePath
	 * @throws GenerationException
     */
    protected void createPath(String pPath) throws GenerationException
    {
    	createPath(new File(pPath));
    }
	/**
     * @param pSrcPath
     * @param pPackagePath
	 * @throws GenerationException
     */
    protected void createPackagePath(String pPath, String packageName) throws GenerationException
    {
    	Validate.notNull("packageName in createPackagePath", packageName);

    	String packagePath = convertPackageNameToPath(packageName);
		createPath(pPath, packagePath);
    }
	/**
	 * @param pPath
	 * @param subPath
	 * @throws GenerationException
	 */
	protected void createPath(String pPath, String subPath) throws GenerationException {
		createPath(new File(pPath, subPath));
	}
	/**
     * @param pFile
	 * @throws IOException
     */
    protected void createPath(File pFile) throws GenerationException
    {
    	if ( !pFile.exists() )
    	{
	    	boolean result = pFile.mkdirs();
	    	if (!result)
	    		throw new GenerationException("Génération impossible", new IOException("Impossible de créer le répertoire : "+pFile.toString()));
    	}
    }
    
    protected String createJavaFilename(ModuleConfig moduleConfig, DataObject object) {
    	return createFilename(moduleConfig, object, JAVA_FILE_EXTENSION);
    }
    
    protected String createJavaFilename(ModuleConfig moduleConfig, String baseName) {
    	return createFilename(moduleConfig, baseName, JAVA_FILE_EXTENSION);
    }

    protected String createFilename(ModuleConfig moduleConfig, DataObject object, String extension) {
        return createFilename(moduleConfig, object.getClassName(), extension);
    }

    protected String createFilename(ModuleConfig moduleConfig, String baseName, String extension) {
    	String packageName = moduleConfig.getPackageName();
    	String packagePath = convertPackageNameToPath(packageName);
    	String baseFilename = packagePath + baseName;
        String filename = baseFilename + extension;
        return filename;
    }
	/**
	 * @param filePath
	 */
	protected void delete(String filePath) {
		new File(filePath).delete();
	}
	/**
	 * Adds {@link VelocityStackUtils} the the Velocity context under "stack" variable name.
	 */
	protected void addStackToVelocityContext() {
		context.put(STACK, new VelocityStackUtils());
	}
	/**
	 * Removes {@link VelocityStackUtils} the the Velocity context
	 */
	protected void removeStackFromVelocityContext() {
		context.remove(STACK);
	}
	/**
	 * @return
	 */
	protected String getConfigurationDir() {
		String configDir = configuration.getString(CONFIG_DIR_KEY);
		return configDir + SEPARATOR_CHAR;
	}
	/**
     * @return
     */
    protected String getModuleDir() {
    	return getConfigurationDir() + getModuleName() + SEPARATOR_CHAR;
    }
	/**
	 * @return
	 */
	protected String getTemplateDir() {
		return getModuleDir() + TEMPLATE_DIR + SEPARATOR_CHAR;
	}
	/**
	 * @return
	 */
	protected String getLibraryDir() {
		return getModuleDir() + LIBRARY_DIR + SEPARATOR_CHAR;
	}
	/**
	 * @see org.easygen.core.generators.Generator#copyLibraries(org.easygen.core.config.ProjectConfig)
	 */
	public String[] copyLibraries(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Copying libraries");
		createPath(projectConfig.getLibPath());
		String libraryPath = getLibraryDir();
		logger.debug("Looking for library in "+libraryPath);
		List<String> libraryList = readLibraryList(libraryPath);
		if (libraryList.size() == 0)
			return new String[0];
		for (int i = 0; i < libraryList.size(); i++) {
			copyLibrary(libraryPath, projectConfig.getLibPath(), libraryList.get(i));
		}
		return (String[]) libraryList.toArray(new String[libraryList.size()]);
	}
	/**
	 * @param from
	 * @param to
	 * @param libraryName
	 */
	protected void copyLibrary(String from, String to, String libraryName) {
		URL libraryUrl = Thread.currentThread().getContextClassLoader().getResource(from+libraryName);
		copyLibrary(from, to, libraryName, libraryUrl);
	}
	/**
	 * @param libraryLocation
	 * @param destinationLibPath
	 * @param libraryName
	 * @param libraryUrl
	 */
	protected void copyLibrary(String libraryLocation, String destinationLibPath, String libraryName, URL libraryUrl) {
		logger.debug("Copying library: "+libraryName);
		if (libraryUrl == null) {
			logger.warn("Can't find library "+libraryName);
			return ;
		}
		File destFile = new File(destinationLibPath, libraryName);
		try {
			IOUtils.copy(libraryUrl.openStream(), new FileOutputStream(destFile));
		} catch (IOException e) {
			logger.warn("Can't copy library "+libraryName, e);
		}
	}
	/**
	 * @param libraryPath
	 * @return
	 */
	protected List<String> readLibraryList(String libraryPath) {
		List<String> libraryList = new LinkedList<String>();
		BufferedReader reader = null;
		try {
			logger.debug("Library list file: "+libraryPath+LIBRARY_INDEX_FILE);
			URL libraryListUrl = Thread.currentThread().getContextClassLoader().getResource(libraryPath+LIBRARY_INDEX_FILE);
			if (libraryListUrl == null) {
				logger.info("No library file "+LIBRARY_INDEX_FILE);
				return libraryList;
			}
			reader = new BufferedReader(new InputStreamReader(libraryListUrl.openStream()));
			String line = null;
			while ( (line = reader.readLine()) != null) {
				libraryList.add(line);
			}
		} catch (IOException e) {
			logger.warn("Can't read file "+LIBRARY_INDEX_FILE, e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return libraryList;
	}
	/**
     * @return
     */
    protected abstract String getModuleName();
}
