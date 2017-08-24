package ddd.simple.action.attachmentGroup;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachmentGroup.AttachmentGroupCategory;
import ddd.simple.service.attachmentGroup.AttachmentGroupCategoryService;

@Action
@RequestMapping("/AttachmentGroupCategory")
@Controller
public class AttachmentGroupCategoryAction
{
	@Resource(name="attachmentGroupCategoryServiceBean")
	private AttachmentGroupCategoryService attachmentGroupCategoryService;
	
	public AttachmentGroupCategory saveAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory)
	{
		try {
			AttachmentGroupCategory saveAttachmentGroupCategory = this.attachmentGroupCategoryService.saveAttachmentGroupCategory(attachmentGroupCategory);
			return saveAttachmentGroupCategory;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteAttachmentGroupCategory(Long attachmentGroupCategoryId){
		
		try {
			return this.attachmentGroupCategoryService.deleteAttachmentGroupCategory(attachmentGroupCategoryId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public AttachmentGroupCategory updateAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) {
		try {
			AttachmentGroupCategory updateAttachmentGroupCategory = this.attachmentGroupCategoryService.updateAttachmentGroupCategory(attachmentGroupCategory);
			return updateAttachmentGroupCategory;
		} catch (DDDException e) {
			throw e;
		}
	}

	public AttachmentGroupCategory findAttachmentGroupCategoryById(Long attachmentGroupCategoryId){
		try {
			AttachmentGroupCategory findAttachmentGroupCategory = this.attachmentGroupCategoryService.findAttachmentGroupCategoryById(attachmentGroupCategoryId);
			return  findAttachmentGroupCategory;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<AttachmentGroupCategory> findAllAttachmentGroupCategory(){
		try{
			EntitySet<AttachmentGroupCategory> allAttachmentGroupCategory = this.attachmentGroupCategoryService.findAllAttachmentGroupCategory();
			return allAttachmentGroupCategory;
		} catch (DDDException e) {
			throw e;
		}
	}

}