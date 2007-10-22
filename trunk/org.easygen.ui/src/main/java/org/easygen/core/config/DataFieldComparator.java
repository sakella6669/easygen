package org.easygen.core.config;

import java.util.Comparator;

public class DataFieldComparator implements Comparator<DataField> {

	/**
	 * Order fields according to this order (primary keys, normal fields, foreign keys)
	 * @param dataField1
	 * @param dataField2
	 * @return
	 */
	public int compare(DataField dataField1, DataField dataField2) {
		if (dataField1 == dataField2) {
			return 0;
		}
		if (dataField1.isPrimaryKey()) {
			if (dataField2.isPrimaryKey()) {
				return compareNames(dataField1, dataField2);
			}
			return -1;
		}
		if (dataField1.isForeignKey()) {
			if (dataField2.isForeignKey()) {
				return compareNames(dataField1, dataField2);
			}
		}
		if (dataField2.isPrimaryKey()) {
			return 1;
		}
		if (dataField2.isForeignKey()) {
			return -1;
		}
		return compareNames(dataField1, dataField2);
	}

	/**
	 * @param dataField1
	 * @param dataField2
	 * @return
	 */
	protected int compareNames(DataField dataField1, DataField dataField2) {
		return dataField1.getColumnName().compareTo(dataField2.getColumnName());
	}
}
