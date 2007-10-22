package org.easygen.core.config;

import java.util.Comparator;

public class DataObjectComparator implements Comparator<DataObject> {

	/**
	 * Order objects by tableName
	 * @param dataField1
	 * @param dataField2
	 * @return
	 */
	public int compare(DataObject o1, DataObject o2) {
		if (o1 == o2) {
			return 0;
		}
		return o1.getTableName().compareTo(o2.getTableName());
	}

}
