package org.easygen.core.generators.hibernate;

/**
 * @author eveno
 * Created on 2 nov. 06
 *
 */
public class JDBCConfig
{
    private String url = null;
    private String username = null;
    private String password = null;
    private String driver = null;
    private String dialect = null;
	/**
     * @return the dialect
     */
    public String getDialect()
    {
    	return this.dialect;
    }
	/**
     * @param pDialect the dialect to set
     */
    public void setDialect(String pDialect)
    {
    	this.dialect = pDialect;
    }
	/**
     * @return the driver
     */
    public String getDriver()
    {
    	return this.driver;
    }
	/**
     * @param pDriver the driver to set
     */
    public void setDriver(String pDriver)
    {
    	this.driver = pDriver;
    }
	/**
     * @return the password
     */
    public String getPassword()
    {
    	return this.password;
    }
	/**
     * @param pPassword the password to set
     */
    public void setPassword(String pPassword)
    {
    	this.password = pPassword;
    }
	/**
     * @return the url
     */
    public String getUrl()
    {
    	return this.url;
    }
	/**
     * @param pUrl the url to set
     */
    public void setUrl(String pUrl)
    {
    	this.url = pUrl;
    }
	/**
     * @return the username
     */
    public String getUsername()
    {
    	return this.username;
    }
	/**
     * @param pUsername the username to set
     */
    public void setUsername(String pUsername)
    {
    	this.username = pUsername;
    }
}
