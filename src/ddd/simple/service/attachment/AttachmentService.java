package ddd.simple.service.attachment;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.service.base.BaseServiceInterface;

public interface AttachmentService extends BaseServiceInterface{

	public Attachment saveAttachment(Attachment attachment) ;
	
	public void deleteAttachment(Long attachmentId) ;
	
	public Attachment findAttachmentById(Long attachmentId) ;
	
	public EntitySet<Attachment> findAttachmentByForm(Long associateFormId,String associateFormName) ;

	public String getFilePathFormSystemConfig() ;

	public void createAttachmentFromFile(String entityId, String entityName, String fileName,Long fileSize);
	
	public void deleteAttachmentByForm(Long associateFormId,
			String associateFormName);
}
