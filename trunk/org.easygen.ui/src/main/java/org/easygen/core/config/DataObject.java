package org.easygen.core.config;

import java.util.LinkedList;
import java.util.List;

/**
 * @author eveno
 * Created on 16 nov. 06
 *
 */
public class DataObject
{
	private boolean selected = true;
	private String className = null;
	private String tableName = null;
	private boolean useCache = false;
	private List<DataField> fields = null;

	public DataObject()
	{
		this.fields = new LinkedList<DataField>();
	}
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	/**
     * @return the name
     */
    public String getTableName()
    {
    	return tableName;
    }
	/**
     * @param pName the name to set
     */
    public void setTableName(String pName)
    {
    	tableName = pName;
    }
	/**
     * @return the className
     */
    public String getClassName()
    {
    	return className;
    }
	/**
     * @param pClassName the className to set
     */
    public void setClassName(String pClassName)
    {
    	className = pClassName;
    }
	
	/**
	 * @return the useCache
	 */
	public boolean isUseCache() {
		return useCache;
	}
	/**
	 * @param useCache the useCache to set
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	/**
     * @return the fields
     */
    public List<DataField> getFields()
    {
    	return fields;
    }
	/**
     * @param pFields the fields to set
     */
    public void setFields(List<DataField> pFields)
    {
    	fields = pFields;
    }
	/**
     * @param pFields the fields to set
     */
    public void addField(DataField pField)
    {
    	fields.add(pField);
    }
	
	@Override
	public String toString()
	{
        StringBuffer buffer = new StringBuffer();
        buffer.append("Object: ").append(className).append('[').append(tableName).append(']');
        if (!selected)
        	return buffer.toString();
        buffer.append('\n');
//        if (pk != null)
//        	buffer.append('\t').append(pk).append('\n');
        for (DataField field : fields) {
	        buffer.append('\t').append(field).append('\n');
        }
        buffer.append('\n');
        return buffer.toString();
	}
}
