package ddd.simple.dao.attachmentGroup.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.attachmentGroup.AttachmentGroupCategory;
import ddd.simple.dao.attachmentGroup.AttachmentGroupCategoryDao;

@Service
public class AttachmentGroupCategoryDaoBean extends BaseDao implements AttachmentGroupCategoryDao
{
	@Override
	public AttachmentGroupCategory saveAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory)  throws Exception{
		return this.save(attachmentGroupCategory);
	}

	@Override
	public int deleteAttachmentGroupCategory(Long attachmentGroupCategoryId)  throws Exception{
		return this.deleteById(attachmentGroupCategoryId,AttachmentGroupCategory.class);
	}

	@Override
	public AttachmentGroupCategory updateAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory)  throws Exception{
		return this.update(attachmentGroupCategory);
	}

	@Override
	public AttachmentGroupCategory findAttachmentGroupCategoryById(Long attachmentGroupCategoryId)  throws Exception{
		return this.query(attachmentGroupCategoryId, AttachmentGroupCategory.class);
	}
	
	@Override
	public EntitySet<AttachmentGroupCategory> findAllAttachmentGroupCategory() throws Exception {
		return this.query("1=1",AttachmentGroupCategory.class);
	}
}
