package ddd.simple.entity.importConfigs;

import ddd.base.annotation.ColumnInfo;

public class ConfigItem {
	private String fieldName;
	private String columnTitle;
	private boolean isUnique;//是否唯一
	private boolean isNotNull;//是否为空
	private String validate;//验证规则
	private boolean isEntity;//是否是外键
	private String relationEntityName;//外键实体名 
	private String relationEntityKey;//外键属性
	private ColumnInfo columnInfo; 
	
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
  
	public void setEntity(boolean isEntity) {
		this.setIsEntity(isEntity);
	}
	public String getRelationEntityName() {
		return relationEntityName;
	}
	public void setRelationEntityName(String relationEntityName) {
		this.relationEntityName = relationEntityName;
	}
	public String getRelationEntityKey() {

		return relationEntityKey;
	}
	public void setRelationEntityKey(String relationEntityKey) {
		{
		if(relationEntityKey != null)
			relationEntityKey = relationEntityKey.replaceAll(" ", "");
		}
		this.relationEntityKey = relationEntityKey;
	}
	public ColumnInfo getColumnInfo() {
		return columnInfo;
	}
	public void setColumnInfo(ColumnInfo columnInfo) {
		this.columnInfo = columnInfo;
	}
	public String getColumnTitle() {
		return columnTitle;
	}
	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}
	public boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	public boolean getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public boolean getIsEntity() {
		return isEntity;
	}
	public void setIsEntity(boolean isEntity) {
		this.isEntity = isEntity;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
}
