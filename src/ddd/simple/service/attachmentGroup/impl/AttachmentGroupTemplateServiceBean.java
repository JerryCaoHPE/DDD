package ddd.simple.service.attachmentGroup.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;
import ddd.simple.dao.attachmentGroup.AttachmentGroupTemplateDao;
import ddd.simple.service.attachmentGroup.AttachmentGroupTemplateService;

@Service
public class AttachmentGroupTemplateServiceBean extends BaseService implements AttachmentGroupTemplateService
{

	@Resource(name="attachmentGroupTemplateDaoBean")
	private AttachmentGroupTemplateDao attachmentGroupTemplateDao;
	
	@Override
	public AttachmentGroupTemplate saveAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) 
	{
		try {
			return this.attachmentGroupTemplateDao.saveAttachmentGroupTemplate(attachmentGroupTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveAttachmentGroupTemplate", e.getMessage(), e);
		}
	}

	@Override
	public int deleteAttachmentGroupTemplate(Long attachmentGroupTemplateId) {
		try {
			return this.attachmentGroupTemplateDao.deleteAttachmentGroupTemplate(attachmentGroupTemplateId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteAttachmentGroupTemplate", e.getMessage(), e);
		}
		
	}

	@Override
	public AttachmentGroupTemplate updateAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate) {
		try {
			return this.attachmentGroupTemplateDao.updateAttachmentGroupTemplate(attachmentGroupTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateAttachmentGroupTemplate", e.getMessage(), e);
		}
	}

	@Override
	public AttachmentGroupTemplate findAttachmentGroupTemplateById(Long attachmentGroupTemplateId) {
		try {
			return this.attachmentGroupTemplateDao.findAttachmentGroupTemplateById(attachmentGroupTemplateId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAttachmentGroupTemplateById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<AttachmentGroupTemplate> findAllAttachmentGroupTemplate() {
		try{
			return this.attachmentGroupTemplateDao.findAllAttachmentGroupTemplate();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllAttachmentGroupTemplate", e.getMessage(), e);
		}
	}

}
