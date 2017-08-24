package ddd.simple.entity.entityTips;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="说明",name="entityTips")
public class EntityTips extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="实体名",length=255,nullable=false,comment="")
	private String EntityName;

	@Column(label="实体属性",length=2000,comment="")
	private String EntityAttr;


	public String getEntityName() {
		lazyLoad();
		return this.EntityName;
	}

	public void setEntityName(String EntityName) {
		this.EntityName = EntityName;
	}

	public String getEntityAttr() {
		lazyLoad();
		return this.EntityAttr;
	}

	public void setEntityAttr(String EntityAttr) {
		this.EntityAttr = EntityAttr;
	}


	
}