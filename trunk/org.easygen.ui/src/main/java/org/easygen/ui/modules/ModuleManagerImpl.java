package org.easygen.ui.modules;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.easygen.ui.modules.hibernate.HibernateDataModule;
import org.easygen.ui.modules.servicejava.ServiceJavaModule;
import org.easygen.ui.modules.springservice.SpringServiceModule;
import org.easygen.ui.modules.struts2.Struts2ViewModule;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public class ModuleManagerImpl implements ModuleManager
{
	protected Map<String, Map<String, Module>> moduleKinds = new Hashtable<String, Map<String, Module>>();

	/**
	 *
	 */
	public ModuleManagerImpl() {
		initModules();
	}
	/**
	 * Add the various modules from any kind 
	 */
	protected void initModules() {
		addModule(DATA_MODULE_KIND, new HibernateDataModule());
		addModule(SERVICE_MODULE_KIND, new ServiceJavaModule());
		addModule(SERVICE_MODULE_KIND, new SpringServiceModule());
		addModule(VIEW_MODULE_KIND, new Struts2ViewModule());
	}
	/**
	 * @throws ModuleException 
     *
     */
    protected void addModule(String kind, Module module) {
    	if (module == null)
    		return ;
    	if (moduleKinds.containsKey(kind) == false) {
    		moduleKinds.put(kind, new Hashtable<String, Module>());
    	}
    	Map<String, Module> modules = moduleKinds.get(kind);
    	if (modules.containsKey(module.getNature())) {
    		LogFactory.getLog(getClass()).warn("A module for this nature has already been registered: "
    			+module.getNature()+" ("+modules.get(module.getNature()).getClass().getName()+")");
    	}
		modules.put(module.getNature(), module);
    }
	/**
	 * @throws ModuleNotFoundException
	 * @see org.easygen.ui.modules.ModuleManager#getModule(java.lang.String)
	 */
	public Module getModule(String kind, String pName) throws ModuleNotFoundException {
    	if (kind == null || pName == null || moduleKinds.containsKey(kind) == false)
    		throw new ModuleNotFoundException(pName+" for kind "+kind);
    	Map<String, Module> modules = moduleKinds.get(kind);
		if (modules.containsKey(pName)) {
			return modules.get(pName);
		}
		throw new ModuleNotFoundException(pName);
	}
	/**
	 * @see org.easygen.ui.modules.ModuleManager#getModuleNatures()
	 */
	public String[] getModuleNatures(String kind) {
		Map<String, Module> modules = internalGetModules(kind);
		return (String[]) modules.keySet().toArray(new String[modules.size()]);
	}
	/**
	 * @see org.easygen.ui.modules.ModuleManager#getModules()
	 */
	public Module[] getModules(String kind) {
		Map<String, Module> modules = internalGetModules(kind);
		return (Module[]) modules.values().toArray(new Module[modules.size()]);
	}
	/**
	 * @see org.easygen.ui.modules.ModuleManager#getModules()
	 */
	protected Map<String, Module> internalGetModules(String kind) {
    	if (kind == null || moduleKinds.containsKey(kind) == false) {
    		return Collections.emptyMap();
    	}
    	Map<String, Module> modules = moduleKinds.get(kind);
		return modules;
	}
}
