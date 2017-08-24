package ddd.simple.action.attachmentGroup;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;
import ddd.simple.service.attachmentGroup.AttachmentGroupTemplateService;

@Action
@RequestMapping("/AttachmentGroupTemplate")
@Controller
public class AttachmentGroupTemplateAction
{
	@Resource(name="attachmentGroupTemplateServiceBean")
	private AttachmentGroupTemplateService attachmentGroupTemplateService;
	
	public AttachmentGroupTemplate saveAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate)
	{
		try {
			AttachmentGroupTemplate saveAttachmentGroupTemplate = this.attachmentGroupTemplateService.saveAttachmentGroupTemplate(attachmentGroupTemplate);
			return saveAttachmentGroupTemplate;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteAttachmentGroupTemplate(Long attachmentGroupTemplateId){
		
		try {
			return this.attachmentGroupTemplateService.deleteAttachmentGroupTemplate(attachmentGroupTemplateId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public AttachmentGroupTemplate updateAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) {
		try {
			AttachmentGroupTemplate updateAttachmentGroupTemplate = this.attachmentGroupTemplateService.updateAttachmentGroupTemplate(attachmentGroupTemplate);
			return updateAttachmentGroupTemplate;
		} catch (DDDException e) {
			throw e;
		}
	}

	public AttachmentGroupTemplate findAttachmentGroupTemplateById(Long attachmentGroupTemplateId){
		try {
			AttachmentGroupTemplate findAttachmentGroupTemplate = this.attachmentGroupTemplateService.findAttachmentGroupTemplateById(attachmentGroupTemplateId);
			return  findAttachmentGroupTemplate;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<AttachmentGroupTemplate> findAllAttachmentGroupTemplate(){
		try{
			EntitySet<AttachmentGroupTemplate> allAttachmentGroupTemplate = this.attachmentGroupTemplateService.findAllAttachmentGroupTemplate();
			return allAttachmentGroupTemplate;
		} catch (DDDException e) {
			throw e;
		}
	}

}