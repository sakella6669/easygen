package org.easygen.ui.modules;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public class ModuleException extends Exception
{
    private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ModuleException()
	{
	}
	/**
	 * @param pMessage
	 */
	public ModuleException(String pMsg)
	{
		this(pMsg, null);
	}
	/**
	 * @param pCause
	 */
	public ModuleException(Throwable pCause)
	{
		super(pCause);
	}
	/**
	 * @param pMessage
	 * @param pCause
	 */
	public ModuleException(String pMsg, Throwable pCause)
	{
		super(pMsg, pCause);
	}
}
