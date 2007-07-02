package org.easygen.ui.modules;

/**
 * @author eveno
 * Created on 9 mars 07
 */
public interface ModuleManager
{
	public static final String DATA_MODULE_KIND = "data";
	public static final String SERVICE_MODULE_KIND = "service";
	public static final String VIEW_MODULE_KIND = "view";
	
	public String[] getModuleNatures(String kind);

	public Module[] getModules(String kind);

	public Module getModule(String kind, String pName) throws ModuleNotFoundException;
}
