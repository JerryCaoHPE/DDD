package ddd.simple.dao.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroup;

public interface AttachmentGroupDao extends BaseDaoInterface
{
	public AttachmentGroup saveAttachmentGroup(AttachmentGroup attachmentGroup) throws Exception;
	
	public int deleteAttachmentGroup(Long attachmentGroupId) throws Exception;
	
	public AttachmentGroup updateAttachmentGroup(AttachmentGroup attachmentGroup) throws Exception;
	
	public AttachmentGroup findAttachmentGroupById(Long attachmentGroupId) throws Exception;
	
	public EntitySet<AttachmentGroup> findAllAttachmentGroup() throws Exception;
	
	public AttachmentGroup findAttachmentGroupByItem(Long attachmentGroupItemId) throws Exception;
}
