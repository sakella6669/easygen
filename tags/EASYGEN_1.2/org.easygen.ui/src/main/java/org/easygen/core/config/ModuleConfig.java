package org.easygen.core.config;

import org.easygen.core.generators.Generator;

/**
 * @author eveno
 * Created on 16 nov. 06
 *
 */
public abstract class ModuleConfig extends Config
{
//	private ProjectConfig parent;
//
//	/**
//     * @return the projectConfig
//     */
//    public ProjectConfig getParent()
//    {
//    	return parent;
//    }
//	/**
//     * @param pProjectConfig the projectConfig to set
//     */
//    public void setParent(ProjectConfig pProjectConfig)
//    {
//    	parent = pProjectConfig;
//    }
    /**
	 * @return the generatorClass
	 */
	public abstract Generator getGenerator();

	@Override
	public String toString()
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("** "+getClass().getName()+" **\n");
    	buffer.append("\tPackageName: ").append(getPackageName()).append("\n");
    	buffer.append("\tNature: ").append(getNature()).append("\n");
    	return buffer.toString();
    }
}
