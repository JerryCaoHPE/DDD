package ddd.simple.entity.organization;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;



@ddd.base.annotation.Entity(label="实体属性",name="entityProperty")
public class EntityProperty extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="实体")
	private String entity;

	@Column(label="实体ID")
	private Long entityId;

	@Column(label="字段名")
	private String name;

	@Column(label="数字值")
	private Long numberValue;

	@Column(label="字符值")
	private String StringValue;

	@Column(label="字段类型")
	private String type;
	
	@Column(label="唯一码")
	private String uniqueCode;

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getEntity() {
		lazyLoad();
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Long getEntityId() {
		lazyLoad();
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getName() {
		lazyLoad();
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumberValue() {
		lazyLoad();
		return this.numberValue;
	}

	public void setNumberValue(Long numberValue) {
		this.numberValue = numberValue;
	}

	public String getStringValue() {
		lazyLoad();
		return this.StringValue;
	}

	public void setStringValue(String StringValue) {
		this.StringValue = StringValue;
	}

	public String getType() {
		lazyLoad();
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
}