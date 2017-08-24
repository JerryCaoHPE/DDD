package ddd.simple.dao.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupCategory;

public interface AttachmentGroupCategoryDao extends BaseDaoInterface
{
	public AttachmentGroupCategory saveAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) throws Exception;
	
	public int deleteAttachmentGroupCategory(Long attachmentGroupCategoryId) throws Exception;
	
	public AttachmentGroupCategory updateAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) throws Exception;
	
	public AttachmentGroupCategory findAttachmentGroupCategoryById(Long attachmentGroupCategoryId) throws Exception;
	
	public EntitySet<AttachmentGroupCategory> findAllAttachmentGroupCategory() throws Exception;
}
