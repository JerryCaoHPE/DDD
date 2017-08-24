package ddd.simple.entity.attachmentGroup;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="文档组(模板)",name="attachmentGroupTemplate")
public class AttachmentGroupTemplate extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="模板名",comment="")
	private String name;
	
	@Column(label="类型",comment="码表(业务)")
	private String type;

	@Column(label="分类",FKName="attachementCategory",comment="级次")
	private AttachmentGroupCategory category;

	@Column(label="文档项",joinColumn="attachementTemplateId",composition=true,comment="")
	private EntitySet<AttachmentGroupItem> attachmentGroupItems;

	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		lazyLoad();
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AttachmentGroupCategory getCategory() {
		lazyLoad();
		return category;
	}

	public void setCategory(AttachmentGroupCategory category) {
		this.category = category;
	}

	public EntitySet<AttachmentGroupItem> getAttachmentGroupItems() {
		lazyLoad("attachmentGroupItems");
		return attachmentGroupItems;
	}

	public void setAttachmentGroupItems(
			EntitySet<AttachmentGroupItem> attachmentGroupItems) {
		super.LazyFieidsLoaded.put("attachmentGroupItems", true);
		this.attachmentGroupItems = attachmentGroupItems;
	}
}