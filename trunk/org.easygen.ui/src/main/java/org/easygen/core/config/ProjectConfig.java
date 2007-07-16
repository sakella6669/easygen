package org.easygen.core.config;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * @author eveno
 * Created on 16 nov. 06
 */
public class ProjectConfig
{
	private String name;
	private String path;
	private String srcDirname;
	private String testDirname;
	private String cfgDirname;
	private String libDirname;
	private String buildDirname;
	private String webContentDirname;
	private String packageName;
	private transient String[] libraries;

	private DatabaseConfig databaseConfig = null;

	private String projectNature;
	private ProjectModuleConfig projectModuleConfig; 

	private String commonModuleNature;
	private CommonConfig commonConfig;
	
	private String dataModuleNature;
	private DataModuleConfig dataModuleConfig;

	private String serviceModuleNature;
	private ServiceModuleConfig serviceModuleConfig;

	private String viewModuleNature;
	private ViewModuleConfig viewModuleConfig;

	private transient List<DataObject> objects;

	/**
     *
     */
    public ProjectConfig()
    {
	    super();
	    commonConfig = new CommonConfig();
	    commonModuleNature = CommonConfig.NATURE;
    }
	/**
     * @return the name
     */
    public String getName()
    {
    	return name;
    }
	/**
     * @param pName the name to set
     */
    public void setName(String pName)
    {
    	name = pName.trim();
    }
	/**
     * @return the path
     */
    public String getPath()
    {
    	return path;
    }
	/**
     * @param pPath the path to set
     */
    public void setPath(String pPath)
    {
    	pPath = FilenameUtils.normalize(pPath);
    	if (pPath.endsWith("/") || pPath.endsWith("\\"))
    		path = pPath;
    	else
    		path = pPath + File.separatorChar;
    }
	/**
     * @param pSrcPath the srcPath to set
     */
    public void setSrcDirname(String pSrcPath)
    {
    	this.srcDirname = pSrcPath.trim();
    }
	/**
     * @return the srcPath
     */
    public String getSrcDirname()
    {
    	return srcDirname;
    }
	/**
     * @return the srcPath
     */
    public String getSrcPath()
    {
    	return getPath() + srcDirname + File.separatorChar;
    }
    /**
     * @return
     */
	public String getTestDirname() {
		return testDirname;
	}
	/**
	 * @param testDirname
	 */
	public void setTestDirname(String testDirname) {
		this.testDirname = testDirname;
	}
	/**
     * @return the srcPath
     */
    public String getTestPath()
    {
    	return getPath() + testDirname + File.separatorChar;
    }
	/**
     * @return the cfgPath
     */
    public String getCfgDirname()
    {
    	return cfgDirname;
    }
	/**
     * @return the srcPath
     */
    public String getCfgPath()
    {
    	return getPath() + cfgDirname + File.separatorChar;
    }
	/**
	 * @return the libDirname
	 */
	public String getLibDirname() {
		return libDirname;
	}
	/**
	 * @param libDirname the libDirname to set
	 */
	public void setLibDirname(String libDirname) {
		this.libDirname = libDirname;
	}
	/**
	 * @return the libDirname
	 */
	public String getLibPath() {
		return getPath() + libDirname + File.separatorChar;
	}
	
	public String[] getLibraries() {
		String[] clonedlibrairies = new String[libraries.length];
		System.arraycopy(libraries, 0, clonedlibrairies, 0, libraries.length);
		return clonedlibrairies;
	}
	
	public void setLibraries(String[] newLibraries) {
		String[] clonedlibrairies = new String[newLibraries.length];
		System.arraycopy(newLibraries, 0, clonedlibrairies, 0, newLibraries.length);
		this.libraries = clonedlibrairies;
	}
	/**
     * @return the classes path
     */
    public String getBuildDirname()
    {
    	return buildDirname;
    }
	/**
     * @param pBuildPath the buildPath to set
     */
    public void setBuildDirname(String pBuildPath)
    {
    	this.buildDirname = pBuildPath.trim();
    }
	/**
     * @return the srcPath
     */
    public String getBuildPath()
    {
    	return getPath()+buildDirname+File.separatorChar;
    }
	/**
     * @param pCfgPath the cfgPath to set
     */
    public void setCfgDirname(String pCfgPath)
    {
    	this.cfgDirname = pCfgPath.trim();
    }
	/**
     * @return the classes path
     */
    public String getWebContentDirname()
    {
    	return webContentDirname;
    }
	/**
     * @return the classes path
     */
    public void setWebContentDirname(String pPath)
    {
    	webContentDirname = pPath.trim();
    }
	/**
     * @return the srcPath
     */
    public String getWebContentPath()
    {
    	return getPath()+webContentDirname+File.separatorChar;
    }
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the tables
	 */
	public void setObjects(List<DataObject> objects) {
    	this.objects = objects;
    }

	/**
	 * @return the tables
	 */
	public List<DataObject> getObjects() {
    	return objects;
    }

	public ProjectModuleConfig getProjectModuleConfig() {
		return projectModuleConfig;
	}
	public void setProjectModuleConfig(ProjectModuleConfig projectModuleConfig) {
		this.projectModuleConfig = projectModuleConfig;
	}
	public String getProjectNature() {
		return projectNature;
	}
	public void setProjectNature(String projectNature) {
		this.projectNature = projectNature;
	}
	/**
     * @param pDbConfig the dbConfig to set
     */
    public void setDatabaseConfig(DatabaseConfig pDbConfig)
    {
    	databaseConfig = pDbConfig;
    }

	/**
     * @param pDbConfig the dbConfig to set
     */
    public DatabaseConfig getDatabaseConfig()
    {
    	return databaseConfig;
    }

    public ModuleConfig getModuleConfigForNature(String nature) {
    	if (nature == null) {
    		return null;
    	}
    	if (nature.equals(getProjectNature())) {
    		return getProjectModuleConfig();
    	}
    	if (nature.equals(getCommonModuleNature())) {
    		return getCommonConfig();
    	}
    	if (nature.equals(getDataModuleNature())) {
    		return getDataModuleConfig();
    	}
    	if (nature.equals(getServiceModuleNature())) {
    		return getServiceModuleConfig();
    	}
    	if (nature.equals(getViewModuleNature())) {
    		return getViewModuleConfig();
    	}
    	return null;
    }

    public String getCommonModuleNature() {
		return commonModuleNature;
	}
	public void setCommonModuleNature(String commonConfigNature) {
		this.commonModuleNature = commonConfigNature;
	}
	/**
     * @return the commonConfig
     */
    public CommonConfig getCommonConfig()
    {
    	return commonConfig;
    }
	/**
     * @param pCommonConfig the commonConfig to set
     */
    public void setCommonConfig(CommonConfig pCommonConfig)
    {
//    	pCommonConfig.setParent(this);
    	commonConfig = pCommonConfig;
    }
	/**
     * @return the dataModuleNature
     */
    public String getDataModuleNature()
    {
    	return this.dataModuleNature;
    }
	/**
     * @param pDataModuleNature the dataModuleNature to set
     */
    public void setDataModuleNature(String pDataModuleNature)
    {
    	this.dataModuleNature = pDataModuleNature.trim();
    }
	/**
     * @return the serviceModuleNature
     */
    public String getServiceModuleNature()
    {
    	return this.serviceModuleNature;
    }
	/**
     * @param pServiceModuleNature the serviceModuleNature to set
     */
    public void setServiceModuleNature(String pServiceModuleNature)
    {
    	this.serviceModuleNature = pServiceModuleNature.trim();
    }
	/**
     * @return the viewModuleNature
     */
    public String getViewModuleNature()
    {
    	return this.viewModuleNature;
    }
	/**
     * @param pViewModuleNature the viewModuleNature to set
     */
    public void setViewModuleNature(String pViewModuleNature)
    {
    	this.viewModuleNature = pViewModuleNature.trim();
    }
	/**
     * @return the dataConfig
     */
    public DataModuleConfig getDataModuleConfig()
    {
    	return dataModuleConfig;
    }
	/**
     * @param pDataConfig the dataConfig to set
     */
    public void setDataModuleConfig(DataModuleConfig pDataConfig)
    {
//    	pDataConfig.setParent(this);
    	dataModuleConfig = pDataConfig;
    }
	/**
     * @return the serviceConfig
     */
    public ServiceModuleConfig getServiceModuleConfig()
    {
    	return serviceModuleConfig;
    }
	/**
     * @param pServiceConfig the serviceConfig to set
     */
    public void setServiceModuleConfig(ServiceModuleConfig pServiceConfig)
    {
//    	pServiceConfig.setParent(this);
    	serviceModuleConfig = pServiceConfig;
    }
	/**
     * @return the viewConfig
     */
    public ViewModuleConfig getViewModuleConfig()
    {
    	return viewModuleConfig;
    }
	/**
     * @param pViewConfig the viewConfig to set
     */
    public void setViewModuleConfig(ViewModuleConfig pViewConfig)
    {
//    	pViewConfig.setParent(this);
    	viewModuleConfig = pViewConfig;
    }
    /**
     * @see org.easygen.core.config.ModuleConfig#toString()
     */
    @Override
	public String toString()
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("** ProjectConfig **\n");
    	buffer.append("\tName: ").append(name).append('\n');
    	buffer.append("\tPath: ").append(path).append('\n');
    	buffer.append("\tSrcPath: ").append(srcDirname).append('\n');
    	buffer.append("\tCfgPath: ").append(cfgDirname).append('\n');
    	buffer.append("\tBuildPath: ").append(buildDirname).append('\n');
    	buffer.append("\tWebPath: ").append(webContentDirname).append('\n');
    	buffer.append("\tPackageName: ").append(getPackageName()).append('\n');
    	buffer.append("\tProject Nature: ").append(getProjectNature()).append('\n');
    	buffer.append("\tCommon Module Nature: ").append(getCommonModuleNature()).append('\n');
    	buffer.append("\tData Module Nature: ").append(getDataModuleNature()).append('\n');
    	buffer.append("\tService Module Nature: ").append(getServiceModuleNature()).append('\n');
    	buffer.append("\tView Module Nature: ").append(getViewModuleNature()).append('\n');
    	if (databaseConfig != null)
    		buffer.append(databaseConfig).append('\n');
    	if (objects != null) {
    		for (DataObject dataObject : objects) {
    			buffer.append(dataObject).append('\n');
    		}
    	}
    	if (projectModuleConfig != null)
    		buffer.append(projectModuleConfig).append('\n');
    	if (commonConfig != null)
    		buffer.append(commonConfig).append('\n');
    	if (dataModuleConfig != null)
    		buffer.append(dataModuleConfig).append('\n');
    	if (serviceModuleConfig != null)
    		buffer.append(serviceModuleConfig).append('\n');
    	if (viewModuleConfig != null)
    		buffer.append(viewModuleConfig).append('\n');
    	return buffer.toString();
    }
}
