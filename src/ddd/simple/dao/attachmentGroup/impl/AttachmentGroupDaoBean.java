package ddd.simple.dao.attachmentGroup.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.attachmentGroup.AttachmentGroup;
import ddd.simple.dao.attachmentGroup.AttachmentGroupDao;

@Service
public class AttachmentGroupDaoBean extends BaseDao implements AttachmentGroupDao
{
	@Override
	public AttachmentGroup saveAttachmentGroup(AttachmentGroup attachmentGroup)  throws Exception{
		return this.save(attachmentGroup);
	}

	@Override
	public int deleteAttachmentGroup(Long attachmentGroupId)  throws Exception{
		return this.deleteById(attachmentGroupId,AttachmentGroup.class);
	}

	@Override
	public AttachmentGroup updateAttachmentGroup(AttachmentGroup attachmentGroup)  throws Exception{
		return this.update(attachmentGroup);
	}

	@Override
	public AttachmentGroup findAttachmentGroupById(Long attachmentGroupId)  throws Exception{
		return this.query(attachmentGroupId, AttachmentGroup.class);
	}
	
	@Override
	public EntitySet<AttachmentGroup> findAllAttachmentGroup() throws Exception {
		return this.query("1=1",AttachmentGroup.class);
	}

	@Override
	public AttachmentGroup findAttachmentGroupByItem(Long attachmentGroupItemId)
			throws Exception {
		String sql="attachmentGroupItemId = "+attachmentGroupItemId+"";
		return this.queryOne(sql, AttachmentGroup.class);
	}
}
