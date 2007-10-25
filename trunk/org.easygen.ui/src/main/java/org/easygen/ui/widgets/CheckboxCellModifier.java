package org.easygen.ui.widgets;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Mektoub
 * Created on 12 mai 07
 *
 */
public abstract class CheckboxCellModifier extends LabelProvider implements ITableLabelProvider, ICellModifier
{
	protected static final String CHECKED_IMAGE = "checked";
	protected static final String UNCHECKED_IMAGE = "unchecked";
	protected static final String ICON_PATH = "icons/";
	protected static final ImageRegistry imageRegistry = new ImageRegistry();
	/**
	 * Note: An image registry owns all of the image objects registered with it, and automatically disposes of them
	 * the SWT Display is disposed.
	 */
	static
	{
		imageRegistry.put(
			CHECKED_IMAGE,
			ImageDescriptor.createFromFile(
				CheckboxCellModifier.class,
				ICON_PATH + CHECKED_IMAGE + ".gif"
			)
		);
		imageRegistry.put(
			UNCHECKED_IMAGE,
			ImageDescriptor.createFromFile(
				CheckboxCellModifier.class,
				ICON_PATH + UNCHECKED_IMAGE + ".gif"
			)
		);
	}

	protected List<String> columnNames;
	protected TableViewer tableViewer;

	/**
	 *
	 */
	public CheckboxCellModifier(TableViewer pTableViewer, String[] pColumnNames)
	{
		super();
		tableViewer = pTableViewer;
		columnNames = Arrays.asList(pColumnNames);
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		if (isBooleanColumn(columnIndex)) {
			return null;
		}
		return String.valueOf(getValue(element, columnIndex));
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{
		if (isBooleanColumn(columnIndex) == false) {
			return null;
		}
		Object value = getValue(element, columnIndex);
		Image image = getImage(value);
		return image;
	}

	/**
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
    public Image getImage(Object value) {
		if (value instanceof Boolean) {
			return getImage( ((Boolean)value).booleanValue() );
		}
        return null;
    }

	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	protected Image getImage(boolean isSelected)
	{
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return imageRegistry.get(key);
	}

	/**
	 * @param property
	 * @return
	 */
	protected int getColumnIndex(String property) {
		return columnNames.indexOf(property);
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property)
	{
		int columnIndex = columnNames.indexOf(property);
		Object value = getValue(element, columnIndex);
		return value;
	}

	protected abstract Object getValue(Object element, int columnIndex);

	protected abstract boolean isEditableColumn(int columnIndex);

	protected abstract boolean isBooleanColumn(int columnIndex);

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property)
	{
		int columnIndex = columnNames.indexOf(property);
		boolean isEditableColumn = isEditableColumn(columnIndex);
		return isEditableColumn;
	}

	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem) element;
		int columnIndex = getColumnIndex(property);
		setValue(item.getData(), columnIndex, value);
		tableViewer.update(item.getData(), new String[] { property });
		tableViewer.refresh(item.getData());
	}

	protected abstract void setValue(Object element, int columnIndex, Object value);
}
