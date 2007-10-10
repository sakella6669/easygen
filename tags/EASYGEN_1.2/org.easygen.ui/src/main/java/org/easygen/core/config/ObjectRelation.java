package org.easygen.core.config;

public class ObjectRelation {
	private String foreignTableName;
	private String foreignColumnName;

	public ObjectRelation() {
	}

	public String getForeignColumnName() {
		return foreignColumnName;
	}

	public void setForeignColumnName(String pForeignColumnName) {
		this.foreignColumnName = pForeignColumnName.trim();
	}

	public String getForeignTableName() {
		return foreignTableName;
	}

	public void setForeignTableName(String pForeignTableName) {
		this.foreignTableName = pForeignTableName.trim();
	}

	@Override
	public String toString() {
		return " FK(" + "->" + foreignTableName + "." + foreignColumnName + ")";
	}
}
