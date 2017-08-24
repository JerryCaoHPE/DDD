package ddd.simple.entity.attachmentGroup;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(label="文档组类型",name="attachmentGroupCategory")
public class AttachmentGroupCategory  extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(label="名称",comment="")
	private String name;
	
	@Column(label="编码",comment="")
	private String categoryCode;

	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryCode() {
		lazyLoad();
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
}
