package ddd.simple.entity.model;

import java.io.Serializable;

import ddd.base.annotation.Column;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="modelType",label="模型类型")
public class ModelType extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column(label="类型编码")
	private String typeCode;	
	
	@Column(label="类型名称")
	private String typeName;

	@Column(name="parentId",label="父级模型类型" ,composition=true)
	private ModelType parent;
	
	@Column(joinColumn="parentId",composition=true)
	private EntitySet<ModelType> children;
	
	public ModelType getParent() {
		lazyLoad();
		return parent;
	}

	public void setParent(ModelType parent) {
		this.parent = parent;
	}

	public EntitySet<ModelType> getChildren() {
		lazyLoad("children");
		return children;
	}

	public void setChildren(EntitySet<ModelType> children) {
		this.children = children;
	}

	public String getTypeCode() {
		lazyLoad();
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


}