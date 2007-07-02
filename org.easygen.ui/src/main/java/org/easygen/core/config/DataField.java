package org.easygen.core.config;

public class DataField {
	
	private boolean selected = true;
	private String propertyName = null;
	private String propertyType = null;
	private String columnName;
	private String sqlType;
	private int precision;
	private int scale;
	private boolean nullable;
	private boolean primaryKey = false;
	
	private boolean foreignKey = false;
	private boolean foreignList = false;
	private String foreignTableName;
	private String foreignColumnName;
	private DataObject foreignObject;

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String n) {
		this.columnName = n.trim();
	}

	/**
	 * @return the name
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param pName the name to set
	 */
	public void setPropertyName(String pName) {
		propertyName = pName.trim();
	}

	/**
	 * @return the type
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * @param pType the type to set
	 */
	public void setPropertyType(String pType) {
		this.propertyType = pType.trim();
	}

	public String getPropertyShortType() {
		int pos = propertyType.lastIndexOf('.');
		if (pos < 0) {
			return propertyType;
		}
		return propertyType.substring(pos+1);
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int l) {
		this.precision = l;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param pScale the scale to set
	 */
	public void setScale(int pScale) {
		scale = pScale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean b) {
		this.nullable = b;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String t) {
		this.sqlType = t.trim();
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean is) {
		this.primaryKey = is;
	}

	public boolean isForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(boolean isFk) {
		this.foreignKey = isFk;
	}

	public boolean isForeignList() {
		return foreignList;
	}

	public void setForeignList(boolean foreignList) {
		this.foreignList = foreignList;
	}

	public String getForeignTableName() {
		return foreignTableName;
	}

	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}

	public String getForeignColumnName() {
		return foreignColumnName;
	}

	public void setForeignColumnName(String foreignColumnName) {
		this.foreignColumnName = foreignColumnName;
	}

	public DataObject getForeignObject() {
		return foreignObject;
	}

	public void setForeignObject(DataObject foreignObject) {
		this.foreignObject = foreignObject;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (isForeignKey() == false) {
			buffer.append(propertyName).append('[').append(columnName).append(']');
			buffer.append(' ').append(propertyType).append('[').append(sqlType);
			buffer.append("(").append(precision).append(")").append(']');
			if (!nullable) {
				buffer.append(" NOT");
			}
			buffer.append(" NULL");
			if (isPrimaryKey()) {
				buffer.append(" (PK)");
			}
		} else {
			buffer.append(propertyName);
			buffer.append(" (FK) -> ");
			if (foreignList) {
				buffer.append("List(").append(propertyType).append(')');
			} else {
				buffer.append(foreignTableName).append(".").append(foreignColumnName);
			}
		}
		return buffer.toString();
	}
}
