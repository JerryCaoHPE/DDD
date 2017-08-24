package ddd.simple.dao.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;

public interface AttachmentGroupTemplateDao extends BaseDaoInterface
{
	public AttachmentGroupTemplate saveAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) throws Exception;
	
	public int deleteAttachmentGroupTemplate(Long attachmentGroupTemplateId) throws Exception;
	
	public AttachmentGroupTemplate updateAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) throws Exception;
	
	public AttachmentGroupTemplate findAttachmentGroupTemplateById(Long attachmentGroupTemplateId) throws Exception;
	
	public EntitySet<AttachmentGroupTemplate> findAllAttachmentGroupTemplate() throws Exception;
}
