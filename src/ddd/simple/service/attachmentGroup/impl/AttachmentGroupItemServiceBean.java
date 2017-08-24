package ddd.simple.service.attachmentGroup.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;
import ddd.simple.dao.attachmentGroup.AttachmentGroupItemDao;
import ddd.simple.service.attachmentGroup.AttachmentGroupItemService;

@Service
public class AttachmentGroupItemServiceBean extends BaseService implements AttachmentGroupItemService
{

	@Resource(name="attachmentGroupItemDaoBean")
	private AttachmentGroupItemDao attachmentGroupItemDao;
	
	@Override
	public AttachmentGroupItem saveAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) 
	{
		try {
			return this.attachmentGroupItemDao.saveAttachmentGroupItem(attachmentGroupItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveAttachmentGroupItem", e.getMessage(), e);
		}
	}

	@Override
	public int deleteAttachmentGroupItem(Long attachmentGroupItemId) {
		try {
			return this.attachmentGroupItemDao.deleteAttachmentGroupItem(attachmentGroupItemId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteAttachmentGroupItem", e.getMessage(), e);
		}
		
	}

	@Override
	public AttachmentGroupItem updateAttachmentGroupItem(AttachmentGroupItem attachmentGroupItem) {
		try {
			return this.attachmentGroupItemDao.updateAttachmentGroupItem(attachmentGroupItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateAttachmentGroupItem", e.getMessage(), e);
		}
	}

	@Override
	public AttachmentGroupItem findAttachmentGroupItemById(Long attachmentGroupItemId) {
		try {
			AttachmentGroupItem attachmentGroupItem=this.attachmentGroupItemDao.findAttachmentGroupItemById(attachmentGroupItemId);
			if(attachmentGroupItem.getAttachementGroupTemplate()!=null){
				attachmentGroupItem.getAttachementGroupTemplate().getName();
			}
			
			return attachmentGroupItem;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAttachmentGroupItemById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<AttachmentGroupItem> findAllAttachmentGroupItem() {
		try{
			return this.attachmentGroupItemDao.findAllAttachmentGroupItem();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllAttachmentGroupItem", e.getMessage(), e);
		}
	}

	@Override
	public EntitySet<AttachmentGroupItem> findAttachmentGroupItemByTemplate(
			Long templateId) {
		try{
			return this.attachmentGroupItemDao.findAttachmentGroupItemByTemplate(templateId);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllAttachmentGroupItem", e.getMessage(), e);
		}
	}

}
