package org.easygen.ui.modules.hibernate.pages;

import java.util.List;

import org.easygen.core.config.DataField;
import org.easygen.core.config.DataObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author admin
 */
public class FieldsContentProvider implements IStructuredContentProvider {

	public FieldsContentProvider() {
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof DataObject))
			return new Object[0];

		List<DataField> fields = ((DataObject) inputElement).getFields();
		return fields.toArray();
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}
}
