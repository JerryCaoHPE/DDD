package ddd.simple.service.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;

public interface AttachmentGroupTemplateService extends BaseServiceInterface
{
	public AttachmentGroupTemplate saveAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) ;
	
	public int deleteAttachmentGroupTemplate(Long attachmentGroupTemplateId) ;
	
	public AttachmentGroupTemplate updateAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) ;
	
	public AttachmentGroupTemplate findAttachmentGroupTemplateById(Long attachmentGroupTemplateId) ;
	
	public EntitySet<AttachmentGroupTemplate> findAllAttachmentGroupTemplate() ;
 
}