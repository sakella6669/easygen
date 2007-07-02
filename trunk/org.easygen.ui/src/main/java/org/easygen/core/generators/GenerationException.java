package org.easygen.core.generators;

/**
 * @author eveno
 * Created on 2 nov. 06
 *
 */
public class GenerationException extends Exception
{
    private static final long serialVersionUID = -8772448221385045468L;

	public GenerationException(String pMessage)
    {
	    super(pMessage);
    }

	public GenerationException(String pMessage, Throwable pCause)
    {
	    super(pMessage, pCause);
    }
}
