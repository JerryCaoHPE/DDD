package ddd.simple.entity.organization;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="实体属性定义",name="entityPropertyDefine")
public class EntityPropertyDefine extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="实体")
	private String entity;

	@Column(label="字段名")
	private String name;

	@Column(label="字段类型")
	private String type;

	@Column(label="默认值")
	private String defaultValue;
	
	@Column(label="唯一码")
	private String uniqueCode;

	@Column(label="显示顺序")
	private Long sort;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	
}