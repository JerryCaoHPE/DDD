package ddd.simple.action.attachmentGroup;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachmentGroup.AttachmentGroup;
import ddd.simple.service.attachmentGroup.AttachmentGroupService;

@Action
@RequestMapping("/AttachmentGroup")
@Controller
public class AttachmentGroupAction
{
	@Resource(name="attachmentGroupServiceBean")
	private AttachmentGroupService attachmentGroupService;
	
	public AttachmentGroup saveAttachmentGroup(AttachmentGroup attachmentGroup)
	{
		try {
			AttachmentGroup saveAttachmentGroup = this.attachmentGroupService.saveAttachmentGroup(attachmentGroup);
			return saveAttachmentGroup;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteAttachmentGroup(Long attachmentGroupId){
		
		try {
			return this.attachmentGroupService.deleteAttachmentGroup(attachmentGroupId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public AttachmentGroup updateAttachmentGroup(AttachmentGroup attachmentGroup) {
		try {
			AttachmentGroup updateAttachmentGroup = this.attachmentGroupService.updateAttachmentGroup(attachmentGroup);
			return updateAttachmentGroup;
		} catch (DDDException e) {
			throw e;
		}
	}

	public AttachmentGroup findAttachmentGroupById(Long attachmentGroupId){
		try {
			AttachmentGroup findAttachmentGroup = this.attachmentGroupService.findAttachmentGroupById(attachmentGroupId);
			return  findAttachmentGroup;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<AttachmentGroup> findAllAttachmentGroup(){
		try{
			EntitySet<AttachmentGroup> allAttachmentGroup = this.attachmentGroupService.findAllAttachmentGroup();
			return allAttachmentGroup;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<AttachmentGroup> findAttachmentGroupByTemplateId(Long templateId){
		try{
			EntitySet<AttachmentGroup> attachmentGroups = this.attachmentGroupService.findAttachmentGroupByTemplateId(templateId);
			return attachmentGroups;
		} catch (DDDException e) {
			throw e;
		}
	}
}