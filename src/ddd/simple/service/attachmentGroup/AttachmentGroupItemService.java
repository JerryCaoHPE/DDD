package ddd.simple.service.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;

public interface AttachmentGroupItemService extends BaseServiceInterface
{
	public AttachmentGroupItem saveAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) ;
	
	public int deleteAttachmentGroupItem(Long attachmentGroupItemId) ;
	
	public AttachmentGroupItem updateAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) ;
	
	public AttachmentGroupItem findAttachmentGroupItemById(Long attachmentGroupItemId) ;
	
	public EntitySet<AttachmentGroupItem> findAllAttachmentGroupItem() ;
 
	public EntitySet<AttachmentGroupItem> findAttachmentGroupItemByTemplate(Long templateId);
}