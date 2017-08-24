package ddd.simple.service.attachmentGroup.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.dao.attachmentGroup.AttachmentGroupDao;
import ddd.simple.entity.attachmentGroup.AttachmentGroup;
import ddd.simple.entity.attachmentGroup.AttachmentGroupItem;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;
import ddd.simple.service.attachmentGroup.AttachmentGroupItemService;
import ddd.simple.service.attachmentGroup.AttachmentGroupService;
import ddd.simple.service.base.BaseService;

@Service
public class AttachmentGroupServiceBean extends BaseService implements AttachmentGroupService
{

	@Resource(name="attachmentGroupDaoBean")
	private AttachmentGroupDao attachmentGroupDao;
	
	@Resource(name="attachmentGroupItemServiceBean")
	private AttachmentGroupItemService attachmentGroupItemService;
	
	@Override
	public AttachmentGroup saveAttachmentGroup(AttachmentGroup attachmentGroup) 
	{
		try {
			return this.attachmentGroupDao.saveAttachmentGroup(attachmentGroup);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveAttachmentGroup", e.getMessage(), e);
		}
	}

	@Override
	public int deleteAttachmentGroup(Long attachmentGroupId) {
		try {
			return this.attachmentGroupDao.deleteAttachmentGroup(attachmentGroupId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteAttachmentGroup", e.getMessage(), e);
		}
		
	}

	@Override
	public AttachmentGroup updateAttachmentGroup(AttachmentGroup attachmentGroup) {
		try {
			return this.attachmentGroupDao.updateAttachmentGroup(attachmentGroup);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateAttachmentGroup", e.getMessage(), e);
		}
	}

	@Override
	public AttachmentGroup findAttachmentGroupById(Long attachmentGroupId) {
		try {
			return this.attachmentGroupDao.findAttachmentGroupById(attachmentGroupId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAttachmentGroupById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<AttachmentGroup> findAllAttachmentGroup() {
		try{
			return this.attachmentGroupDao.findAllAttachmentGroup();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllAttachmentGroup", e.getMessage(), e);
		}
	}

	@Override
	public void createAttachmentGroup(AttachmentGroupTemplate attachmentGroupTemplate) {
		try {
			EntitySet<AttachmentGroupItem> attachmentGroupItems=this.attachmentGroupItemService.findAttachmentGroupItemByTemplate(attachmentGroupTemplate.getEId());
			
			for(AttachmentGroupItem attachmentGroupItem:attachmentGroupItems){
				AttachmentGroup attachmentGroup=new AttachmentGroup();
				
				attachmentGroup.setName(attachmentGroupItem.getName());
				attachmentGroup.setAttachmentGroupItemId(attachmentGroupItem.getEId());
				
				EntityClass entityClass=SessionFactory.getEntityClass(attachmentGroupItem.getClass());
				
				attachmentGroup.setAssociateFormName(entityClass.getEntityInfo().getName());
				
				if(attachmentGroupItem.getMinFiles() != null){
					attachmentGroup.setMinFiles(attachmentGroupItem.getMinFiles());
				}
				
				if(attachmentGroupItem.getMaxFiles() != null){
					attachmentGroup.setMaxFiles(attachmentGroupItem.getMaxFiles());
				}
				if(attachmentGroupItem.getGroupItemCode()!=null){
					attachmentGroup.setGroupCode(attachmentGroupItem.getGroupItemCode());
				}
				
				this.attachmentGroupDao.saveAttachmentGroup(attachmentGroup);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("createAttachmentGroup", e.getMessage(), e);
		}
	}

	@Override
	public EntitySet<AttachmentGroup> findAttachmentGroupByTemplateId(Long templateId) {
		try {
			EntitySet<AttachmentGroup> attachmentGroups=new EntitySet<AttachmentGroup>();
			EntitySet<AttachmentGroupItem> attachmentGroupItems=this.attachmentGroupItemService.findAttachmentGroupItemByTemplate(templateId);
			
			for(AttachmentGroupItem attachmentGroupItem:attachmentGroupItems){
				AttachmentGroup attachmentGroup=this.attachmentGroupDao.findAttachmentGroupByItem(attachmentGroupItem.getEId());
				
				if(attachmentGroup != null){
					attachmentGroups.add(attachmentGroup);
				}
			}
			return attachmentGroups;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAttachmentGroupByTemplateId", e.getMessage(), e);
		}
	}

}
