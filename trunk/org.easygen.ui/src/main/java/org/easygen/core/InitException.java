package org.easygen.core;

/**
 * @author eveno
 * Created on 2 nov. 06
 */
public class InitException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

	public InitException(String pMessage)
    {
	    super(pMessage);
    }

	public InitException(String pMessage, Throwable pCause)
    {
	    super(pMessage, pCause);
    }
}
