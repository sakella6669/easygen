package org.easygen.ui.modules.hibernate.pages;

import org.apache.commons.lang.IllegalClassException;
import org.easygen.core.config.DataObject;
import org.easygen.ui.widgets.CheckboxCellModifier;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author admin
 */
public class TableCellModifier extends CheckboxCellModifier
{
	/**
	 *
	 */
	public TableCellModifier(TableViewer pTableViewer, String[] columnNames)
	{
		super(pTableViewer, columnNames);
	}

	/**
     * @see org.easygen.ui.widgets.CheckboxCellModifier#isBooleanColumn(int)
     */
    @Override
    protected boolean isBooleanColumn(int columnIndex)
    {
	    return columnIndex == 0 || columnIndex == 3;
    }
	/**
     * @see org.easygen.ui.widgets.CheckboxCellModifier#isEditableColumn(int)
     */
    @Override
    protected boolean isEditableColumn(int columnIndex)
    {
	    return columnIndex != 1;
    }

	/**
	 * @param object
	 * @param columnIndex
	 * @return
	 */
    @Override
	protected Object getValue(Object element, int columnIndex)
	{
		if (!(element instanceof DataObject))
			return null;
		DataObject object = (DataObject) element;

		Object result = null;
		switch (columnIndex)
		{
			case 0:
				result = Boolean.valueOf(object.isSelected());
				break;
			case 1:
				result = object.getTableName();
				break;
			case 2:
				result = object.getClassName();
				break;
			case 3:
				result = Boolean.valueOf(object.isUseCache());
				break;
			default:
				break;
		}
		return result;
	}

	/**
	 * @see org.easygen.ui.widgets.CheckboxCellModifier#setValue(java.lang.Object, int, java.lang.Object)
	 */
	@Override
	public void setValue(Object element, int columnIndex, Object value)
	{
		if (element instanceof DataObject)
		{
			DataObject object = (DataObject) element;
			switch (columnIndex)
			{
				case 0:
					object.setSelected(((Boolean) value).booleanValue());
					break;
				case 1:
					break;
				case 2:
					object.setClassName((String) value);
					break;
				case 3:
					object.setUseCache(((Boolean) value).booleanValue());
					break;
				default:
					break;
			}
			return;
		}
		throw new IllegalClassException(DataObject.class, element.getClass());
	}
}
