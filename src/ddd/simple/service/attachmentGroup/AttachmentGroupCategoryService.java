package ddd.simple.service.attachmentGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.attachmentGroup.AttachmentGroupCategory;

public interface AttachmentGroupCategoryService extends BaseServiceInterface
{
	public AttachmentGroupCategory saveAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) ;
	
	public int deleteAttachmentGroupCategory(Long attachmentGroupCategoryId) ;
	
	public AttachmentGroupCategory updateAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) ;
	
	public AttachmentGroupCategory findAttachmentGroupCategoryById(Long attachmentGroupCategoryId) ;
	
	public EntitySet<AttachmentGroupCategory> findAllAttachmentGroupCategory() ;
 
}