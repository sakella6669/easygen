package org.easygen.ui.modules;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public class ModuleNotFoundException extends ModuleException
{
    private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ModuleNotFoundException()
	{
	}
	/**
	 * @param pMessage
	 */
	public ModuleNotFoundException(String pModuleName)
	{
		this(pModuleName, null);
	}
	/**
	 * @param pCause
	 */
	public ModuleNotFoundException(Throwable pCause)
	{
		super(pCause);
	}
	/**
	 * @param pMessage
	 * @param pCause
	 */
	public ModuleNotFoundException(String pModuleName, Throwable pCause)
	{
		super("Module introuvable : "+pModuleName, pCause);
	}
}
