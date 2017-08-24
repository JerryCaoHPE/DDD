package ddd.simple.service.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroup;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;

public interface AttachmentGroupService extends BaseServiceInterface
{
	public AttachmentGroup saveAttachmentGroup(AttachmentGroup attachmentGroup) ;
	
	public int deleteAttachmentGroup(Long attachmentGroupId) ;
	
	public AttachmentGroup updateAttachmentGroup(AttachmentGroup attachmentGroup) ;
	
	public AttachmentGroup findAttachmentGroupById(Long attachmentGroupId);
	
	public EntitySet<AttachmentGroup> findAllAttachmentGroup() ;
 
	public void createAttachmentGroup(AttachmentGroupTemplate attachmentGroupTemplate);
	
	public EntitySet<AttachmentGroup> findAttachmentGroupByTemplateId(Long templateId) ;
}