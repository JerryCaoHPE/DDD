package ddd.simple.action.attachment;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.service.attachment.AttachmentService;

@Action
@RequestMapping("/Attachment")
@Controller
public class AttachmentAction {

	@Resource(name = "attachmentServiceBean")
	private AttachmentService attachmentService;

	public Attachment saveAttachment(Attachment attachment) throws Exception {
		try {
			Attachment newAttachment = this.attachmentService
					.saveAttachment(attachment);
			return newAttachment;
		} catch (DDDException e) {
			throw e;
		}
	}

	public void deleteAttachment(Long attachmentId) throws Exception {
		try {
			this.attachmentService.deleteAttachment(attachmentId);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Attachment findAttachmentById(Long attachmentId) throws Exception{
		try {
			Attachment attachment = this.attachmentService.findAttachmentById(attachmentId);
			return attachment;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<Attachment> findAttachmentByForm(Long associateFormId,
			String associateFormName) throws Exception {
		try {
			EntitySet<Attachment> attachments = this.attachmentService
					.findAttachmentByForm(associateFormId, associateFormName);
			return attachments;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public void deleteAttachmentByForm(Long associateFormId,String associateFormName) throws Exception {
		try {
			this.attachmentService.deleteAttachmentByForm(associateFormId, associateFormName);
		} catch (DDDException e) {
			throw e;
		}
	}

	/*public void uploadFile(Long entityId,
			String entityName, Long uploadPeopleId,HttpServletRequest request) throws Exception {
		try {
			
			this.attachmentService.uploadFile(entityId, entityName, uploadPeopleId,request);
			
		}catch (DDDException e) {
			throw e;
		}
	}*/
}
