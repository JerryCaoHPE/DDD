package ddd.simple.action.attachmentGroup;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;
import ddd.simple.service.attachmentGroup.AttachmentGroupItemService;

@Action
@RequestMapping("/AttachmentGroupItem")
@Controller
public class AttachmentGroupItemAction
{
	@Resource(name="attachmentGroupItemServiceBean")
	private AttachmentGroupItemService attachmentGroupItemService;
	
	public AttachmentGroupItem saveAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem)
	{
		try {
			AttachmentGroupItem saveAttachmentGroupItem = this.attachmentGroupItemService.saveAttachmentGroupItem(attachmentGroupItem);
			return saveAttachmentGroupItem;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteAttachmentGroupItem(Long attachmentGroupItemId){
		
		try {
			return this.attachmentGroupItemService.deleteAttachmentGroupItem(attachmentGroupItemId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public AttachmentGroupItem updateAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) {
		try {
			AttachmentGroupItem updateAttachmentGroupItem = this.attachmentGroupItemService.updateAttachmentGroupItem(attachmentGroupItem);
			return updateAttachmentGroupItem;
		} catch (DDDException e) {
			throw e;
		}
	}

	public AttachmentGroupItem findAttachmentGroupItemById(Long attachmentGroupItemId){
		try {
			AttachmentGroupItem findAttachmentGroupItem = this.attachmentGroupItemService.findAttachmentGroupItemById(attachmentGroupItemId);
			return  findAttachmentGroupItem;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<AttachmentGroupItem> findAllAttachmentGroupItem(){
		try{
			EntitySet<AttachmentGroupItem> allAttachmentGroupItem = this.attachmentGroupItemService.findAllAttachmentGroupItem();
			return allAttachmentGroupItem;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<AttachmentGroupItem> findAttachmentGroupItemByTemplate(Long templateId){
		try{
			EntitySet<AttachmentGroupItem> attachmentGroupItems = this.attachmentGroupItemService.findAttachmentGroupItemByTemplate(templateId);
			return attachmentGroupItems;
		} catch (DDDException e) {
			throw e;
		}
	}
}