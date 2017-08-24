package ddd.base.codegenerator.entity;

import ddd.base.annotation.ColumnInfo;

public class FieldAttribute {
	
	private String visitModifier;
	
	private String attributeModifier;
	
	private String type;
	
	private String fieldName;
	
	private ColumnInfo columnInfo = new ColumnInfo();

	public String getVisitModifier() {
		return visitModifier;
	}

	public void setVisitModifier(String visitModifier) {
		this.visitModifier = visitModifier;
	}

	public String getAttributeModifier() {
		return attributeModifier;
	}

	public void setAttributeModifier(String attributeModifier) {
		this.attributeModifier = attributeModifier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ColumnInfo getColumnInfo() {
		return columnInfo;
	}

	public void setColumnInfo(ColumnInfo columnInfo) {
		this.columnInfo = columnInfo;
	}

}
