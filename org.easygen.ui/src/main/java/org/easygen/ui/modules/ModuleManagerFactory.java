package org.easygen.ui.modules;

/**
 * @author Mektoub
 * Created on 12 mai 07
 *
 */
public class ModuleManagerFactory
{
	public static ModuleManager newInstance() {
		return new ModuleManagerImpl();
	}

	/**
	 *
	 */
	private ModuleManagerFactory() {
	}
}
