package org.easygen.ui.wizards.validator;

import org.eclipse.swt.widgets.Control;

/**
 * @author Mektoub
 * Created on 20 mars 07
 */
class ValidateField
{
	private String fieldName = null;
	private Control control = null;
	private short fieldType;

	/**
	 *
	 */
	public ValidateField(String name, Control pControl, short pFieldType)
	{
		fieldName = name;
		control = pControl;
		fieldType = pFieldType;
	}
	/**
     * @return the fieldName
     */
    public String getFieldName()
    {
    	return this.fieldName;
    }
	/**
     * @param pFieldName the fieldName to set
     */
    public void setFieldName(String pFieldName)
    {
    	this.fieldName = pFieldName;
    }
	/**
     * @return the control
     */
    public Control getControl()
    {
    	return this.control;
    }
	/**
     * @param pControl the control to set
     */
    public void setControl(Control pControl)
    {
    	this.control = pControl;
    }
	/**
     * @return the fieldType
     */
    public short getFieldType()
    {
    	return this.fieldType;
    }
	/**
     * @param pFieldType the fieldType to set
     */
    public void setFieldType(short pFieldType)
    {
    	this.fieldType = pFieldType;
    }
}
