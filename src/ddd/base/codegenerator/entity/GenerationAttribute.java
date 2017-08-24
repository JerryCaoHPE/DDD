package ddd.base.codegenerator.entity;

import java.util.List;

import ddd.base.annotation.EntityInfo;

public class GenerationAttribute {
	
	private String classNameUp;
	
	private String fullPackageName;
	
	private List<FieldAttribute> fieldAttributeList;
	
	private EntityInfo entityInfo = new EntityInfo();

	public String getClassNameUp() {
		return classNameUp;
	}

	public void setClassNameUp(String classNameUp) {
		this.classNameUp = classNameUp;
	}

	public String getFullPackageName() {
		return fullPackageName;
	}

	public void setFullPackageName(String fullPackageName) {
		this.fullPackageName = fullPackageName;
	}

	public List<FieldAttribute> getFieldAttributeList() {
		return fieldAttributeList;
	}

	public void setFieldAttributeList(List<FieldAttribute> fieldAttributeList) {
		this.fieldAttributeList = fieldAttributeList;
	}

	public EntityInfo getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(EntityInfo entityInfo) {
		this.entityInfo = entityInfo;
	}

}
