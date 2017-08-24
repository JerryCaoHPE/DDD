package ddd.simple.entity.model;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="模型项数据类型",name="modelDataType")
public class ModelDataType extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="控件类型",nullable=false,comment="对应动态表单的表单设计中的类型")
	private String dataType;
	
	@Column(label="vm文件名",comment="对应config的type")
	private String type;

	@Column(label="Java类型",comment="")
	private String javaType;

	@Column(label="长度",comment="")
	private Integer dataSize;

	@Column(label="中文名称",comment="")
	private String descName;

	@Column(label="动态表单",comment="")
	private String dynamicFormKey;

	@Column(label="默认值",comment="",length=8000)
	private String defaultValue;
	

	public String getJavaType() {
		lazyLoad();
		return this.javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public Integer getDataSize() {
		lazyLoad();
		return this.dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}

	public String getDynamicFormKey() {
		lazyLoad();
		return this.dynamicFormKey;
	}

	public void setDynamicFormKey(String dynamicFormKey) {
		this.dynamicFormKey = dynamicFormKey;
	}

	public String getDefaultValue() {
		lazyLoad();
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDataType() {
		lazyLoad();
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getType() {
		lazyLoad();
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescName() {
		lazyLoad();
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}
}