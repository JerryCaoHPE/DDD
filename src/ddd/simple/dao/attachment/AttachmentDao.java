package ddd.simple.dao.attachment;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;

public interface AttachmentDao {

	public Attachment saveAttachment(Attachment attachment) throws Exception;
	
	public void deleteAttachment(Long attachmentId) throws Exception;
	
	public Attachment findAttachmentById(Long attachmentId) throws Exception;
	
	public EntitySet<Attachment> findAttachmentByForm(Long associateFormId,String associateFormName) throws Exception;
	
	public void deleteAttachmentByForm(Long associateFormId,
			String associateFormName) throws Exception;	
}
