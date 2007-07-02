package org.easygen.ui.modules.hibernate.pages;

import org.apache.commons.lang.IllegalClassException;
import org.easygen.core.config.DataField;
import org.easygen.ui.widgets.CheckboxCellModifier;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author admin
 */
public class FieldsCellModifier extends CheckboxCellModifier implements ITableLabelProvider, ICellModifier {

	/**
	 *
	 */
	public FieldsCellModifier(TableViewer pTableViewer, String[] pColumnNames) {
		super(pTableViewer, pColumnNames);
	}

	@Override
	protected boolean isEditableColumn(int columnIndex) {
		if (columnIndex == 1 || columnIndex == 2)
			return false;
		return true;
	}

	@Override
	protected boolean isBooleanColumn(int columnIndex) {
		if (columnIndex == 0)
			return true;
		return false;
	}

	/**
	 * @param field
	 * @param columnIndex
	 * @return
	 */
	@Override
	protected Object getValue(Object element, int columnIndex) {
		if (!(element instanceof DataField))
			return null;
		DataField field = (DataField) element;
		Object value = null;
		switch (columnIndex) {
		case 0:
			value = field.isSelected();
			break;
		case 1:
			value = field.getColumnName();
			break;
		case 2:
			value = field.getSqlType();
			break;
		case 3:
			value = field.getPropertyName();
			break;
		case 4:
			value = field.getPropertyType();
			break;

		default:
			break;
		}
		return value;
	}
	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValue(Object element, int columnIndex, Object value) {
		if (!(element instanceof DataField))
			throw new IllegalClassException(DataField.class, element.getClass());

		DataField field = (DataField) element;
		switch (columnIndex) {
		case 0:
			field.setSelected((Boolean) value);
			break;
		case 1:
		case 2:
			break;
		case 3:
			field.setPropertyName((String) value);
			break;
		case 4:
			field.setPropertyType((String) value);
			break;

		default:
			break;
		}
	}
}
