package org.easygen.core.config;

import org.easygen.core.db.DatabaseManager;

public class DatabaseConfig
{
	private String databaseType;
	private String databaseName;
	private String dataBaseDriver;
	private String jarPath;
	private String host;
	private int port;
	private String username;
	private String password;
	
	/**
     * @return the databaseType
     */
    public String getDatabaseType()
    {
    	return databaseType;
    }
	/**
     * @param pDatabaseType the databaseType to set
     */
    public void setDatabaseType(String pDatabaseType)
    {
    	databaseType = pDatabaseType.trim();
    }
    
	public String getJarPath() {
		return jarPath;
	}

	public void setJarPath(String path) {
		this.jarPath = path.trim();
	}
	/**
	 * @return Returns the dataBaseType.
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	/**
	 * @param dataBaseType The dataBaseType to set.
	 */
	public void setDatabaseName(String n) {
		this.databaseName = n.trim();
	}
	/**
     * @return the host
     */
    public String getHost()
    {
    	return host;
    }
	/**
     * @param pHost the host to set
     */
    public void setHost(String pHost)
    {
    	host = pHost.trim();
    }
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(String p) {
		setPort(Integer.parseInt(p.trim()));
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int p) {
		this.port = p;
	}
	/**
     * @return the url
     */
    public String getUrl()
    {
		return DatabaseManager.computeURL(databaseType, host, port, databaseName);
    }
	/**
	 * @return Returns the userLogin.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The userLogin to set.
	 */
	public void setUsername(String u) {
		this.username = u.trim();
	}
	/**
	 * @return Returns the userPassword.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The userPassword to set.
	 */
	public void setPassword(String p) {
		this.password = p.trim();
	}
	/**
	 * @return Returns the dataBaseDriver.
	 */
	public String getDataBaseDriver() {
		return dataBaseDriver;
	}
	/**
	 * @param dataBaseDriver The dataBaseDriver to set.
	 */
	public void setDataBaseDriver(String d) {
		this.dataBaseDriver = d.trim();
	}
    
    @Override
	public String toString()
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("** DatabaseConfig **\n");
    	buffer.append("\tName:").append(databaseName).append("\n");
    	buffer.append("\tType:").append(databaseType).append("\n");
    	buffer.append("\tDriver:").append(dataBaseDriver).append("\n");
    	buffer.append("\tJar:").append(jarPath).append("\n");
    	buffer.append("\tHost:").append(host).append("\n");
    	buffer.append("\tPort:").append(port).append("\n");
    	buffer.append("\tUsername:").append(username).append("\n");
    	buffer.append("\tPassword:").append(password).append("\n");
    	return buffer.toString();
    }
}
