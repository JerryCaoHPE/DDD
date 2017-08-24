package ddd.simple.dao.attachment.impl;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.attachment.AttachmentDao;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.entity.attachment.Attachment;

@Service
public class AttachmentDaoBean extends BaseDao implements AttachmentDao{
	
	public Attachment saveAttachment(Attachment attachment) throws Exception {
		return this.save(attachment);
	}

	public void deleteAttachment(Long attachmentId) throws Exception {
		this.deleteById(attachmentId,Attachment.class);
	}

	public Attachment findAttachmentById(Long attachmentId) throws Exception
	{
		return this.query(attachmentId, Attachment.class);
	}
	
	public EntitySet<Attachment> findAttachmentByForm(Long associateFormId,String associateFormName) throws Exception {
		String sql = "associateFormId="+associateFormId+" and associateFormName='"+associateFormName+"' order by uploadTime DESC";
		return this.query(sql, Attachment.class);
	}
	public void deleteAttachmentByForm(Long associateFormId,
			String associateFormName) throws Exception
	{
		String where = "associateFormId="+associateFormId+" and associateFormName='"+associateFormName+"'";
		this.deleteByWhere(where, Attachment.class);
	}	
}
