package ddd.simple.dao.attachmentGroup.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;
import ddd.simple.dao.attachmentGroup.AttachmentGroupItemDao;

@Service
public class AttachmentGroupItemDaoBean extends BaseDao implements AttachmentGroupItemDao
{
	@Override
	public AttachmentGroupItem saveAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem)  throws Exception{
		return this.save(attachmentGroupItem);
	}

	@Override
	public int deleteAttachmentGroupItem(Long attachmentGroupItemId)  throws Exception{
		return this.deleteById(attachmentGroupItemId,AttachmentGroupItem.class);
	}

	@Override
	public AttachmentGroupItem updateAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem)  throws Exception{
		return this.update(attachmentGroupItem);
	}

	@Override
	public AttachmentGroupItem findAttachmentGroupItemById(Long attachmentGroupItemId)  throws Exception{
		return this.query(attachmentGroupItemId, AttachmentGroupItem.class);
	}
	
	@Override
	public EntitySet<AttachmentGroupItem> findAllAttachmentGroupItem() throws Exception {
		return this.query("1=1",AttachmentGroupItem.class);
	}

	@Override
	public EntitySet<AttachmentGroupItem> findAttachmentGroupItemByTemplate(
			Long templateId) throws Exception {
		String sql="attachementTemplateId = "+templateId+"";
		return this.query(sql, AttachmentGroupItem.class);
	}
}
