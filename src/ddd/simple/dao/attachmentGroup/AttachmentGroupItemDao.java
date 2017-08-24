package ddd.simple.dao.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;

public interface AttachmentGroupItemDao extends BaseDaoInterface
{
	public AttachmentGroupItem saveAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) throws Exception;
	
	public int deleteAttachmentGroupItem(Long attachmentGroupItemId) throws Exception;
	
	public AttachmentGroupItem updateAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) throws Exception;
	
	public AttachmentGroupItem findAttachmentGroupItemById(Long attachmentGroupItemId) throws Exception;
	
	public EntitySet<AttachmentGroupItem> findAllAttachmentGroupItem() throws Exception;
	
	public EntitySet<AttachmentGroupItem> findAttachmentGroupItemByTemplate(Long templateId) throws Exception;
}
