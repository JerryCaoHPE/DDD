package ddd.simple.service.attachmentGroup.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.attachmentGroup.AttachmentGroupCategory;
import ddd.simple.dao.attachmentGroup.AttachmentGroupCategoryDao;
import ddd.simple.service.attachmentGroup.AttachmentGroupCategoryService;

@Service
public class AttachmentGroupCategoryServiceBean extends BaseService implements AttachmentGroupCategoryService
{

	@Resource(name="attachmentGroupCategoryDaoBean")
	private AttachmentGroupCategoryDao attachmentGroupCategoryDao;
	
	@Override
	public AttachmentGroupCategory saveAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) 
	{
		try {
			return this.attachmentGroupCategoryDao.saveAttachmentGroupCategory(attachmentGroupCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveAttachmentGroupCategory", e.getMessage(), e);
		}
	}

	@Override
	public int deleteAttachmentGroupCategory(Long attachmentGroupCategoryId) {
		try {
			return this.attachmentGroupCategoryDao.deleteAttachmentGroupCategory(attachmentGroupCategoryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteAttachmentGroupCategory", e.getMessage(), e);
		}
		
	}

	@Override
	public AttachmentGroupCategory updateAttachmentGroupCategory(AttachmentGroupCategory attachmentGroupCategory) {
		try {
			return this.attachmentGroupCategoryDao.updateAttachmentGroupCategory(attachmentGroupCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateAttachmentGroupCategory", e.getMessage(), e);
		}
	}

	@Override
	public AttachmentGroupCategory findAttachmentGroupCategoryById(Long attachmentGroupCategoryId) {
		try {
			return this.attachmentGroupCategoryDao.findAttachmentGroupCategoryById(attachmentGroupCategoryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAttachmentGroupCategoryById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<AttachmentGroupCategory> findAllAttachmentGroupCategory() {
		try{
			return this.attachmentGroupCategoryDao.findAllAttachmentGroupCategory();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllAttachmentGroupCategory", e.getMessage(), e);
		}
	}

}
