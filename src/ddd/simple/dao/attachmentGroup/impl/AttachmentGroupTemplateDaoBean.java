package ddd.simple.dao.attachmentGroup.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.attachmentGroup.AttachmentGroupTemplate;
import ddd.simple.dao.attachmentGroup.AttachmentGroupTemplateDao;

@Service
public class AttachmentGroupTemplateDaoBean extends BaseDao implements AttachmentGroupTemplateDao
{
	@Override
	public AttachmentGroupTemplate saveAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate)  throws Exception{
		return this.save(attachmentGroupTemplate);
	}

	@Override
	public int deleteAttachmentGroupTemplate(Long attachmentGroupTemplateId)  throws Exception{
		return this.deleteById(attachmentGroupTemplateId,AttachmentGroupTemplate.class);
	}

	@Override
	public AttachmentGroupTemplate updateAttachmentGroupTemplate(AttachmentGroupTemplate attachmentGroupTemplate)  throws Exception{
		return this.update(attachmentGroupTemplate);
	}

	@Override
	public AttachmentGroupTemplate findAttachmentGroupTemplateById(Long attachmentGroupTemplateId)  throws Exception{
		return this.query(attachmentGroupTemplateId, AttachmentGroupTemplate.class);
	}
	
	@Override
	public EntitySet<AttachmentGroupTemplate> findAllAttachmentGroupTemplate() throws Exception {
		return this.query("1=1",AttachmentGroupTemplate.class);
	}
}
