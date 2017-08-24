package ddd.simple.service.attachment.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.util.SpringContextUtil;
import ddd.simple.dao.attachment.AttachmentDao;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.organization.Employee;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.organization.EmployeeService;
import ddd.simple.service.systemConfig.SystemConfigService;

@Service
public class AttachmentServiceBean extends BaseService implements AttachmentService
{
	
	@Resource(name = "attachmentDaoBean")
	private AttachmentDao attachmentDao;
	
	@Resource(name = "employeeServiceBean")
	private EmployeeService	employeeService;
	
	@Resource(name = "systemConfigServiceBean")
	private SystemConfigService	systemConfigService;
	
	private String	filePath = "";
	
	public Attachment saveAttachment(Attachment attachment)
	{
		try
		{
			Attachment newAttachment = this.attachmentDao.saveAttachment(attachment);
			return newAttachment;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveAttachment", e.getMessage(), e);
		}
	}
	
	public void deleteAttachment(Long attachmentId)
	{
		try
		{
			this.attachmentDao.deleteAttachment(attachmentId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("deleteAttachment", e.getMessage(), e);
		}
	}
	
	public Attachment findAttachmentById(Long attachmentId)
	{
		try
		{
			Attachment attachment = this.attachmentDao.findAttachmentById(attachmentId);
			return attachment;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAttachmentById", e.getMessage(), e);
		}
	}
	public EntitySet<Attachment> findAttachmentByForm(Long associateFormId, String associateFormName)
	{
		try
		{
			EntitySet<Attachment> attachments = this.attachmentDao.findAttachmentByForm(associateFormId, associateFormName);
			return attachments;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAttachmentByForm", e.getMessage(), e);
		}
	}
	public void deleteAttachmentByForm(Long associateFormId,
			String associateFormName)   {
		try {
			this.attachmentDao.deleteAttachmentByForm(associateFormId, associateFormName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteAttachmentByForm", e.getMessage(), e);
		}
	}	
	/**
	 * entityId 和 entityName分别是表单中上传附件的id和formname
	 * fileName是文件的真实名称
	 * file是一个已经创建在entityName下entityId文件夹下且被重命名的文件
	 */
	@Override
	public void createAttachmentFromFile(String entityId,String entityName,String fileName,Long fileSize)
	{
		BaseService bs = (BaseService) SpringContextUtil.getBean("baseService");
		Long loginEmpId = bs.getLoginUser().getLoginOperator().getEmployee().getEId();
		Long uploadPeopleId = loginEmpId;
		
		DiskFileItemFactory diskFactory = new DiskFileItemFactory();
		diskFactory.setSizeThreshold(4096);
		Object employeeServiceBean = ContextLoaderListener.getCurrentWebApplicationContext().getBean("employeeServiceBean");
		
		EmployeeService employeeService = (EmployeeService) employeeServiceBean;
		
		Employee uploadPeople = employeeService.findEmployeeById(uploadPeopleId);
		
		Attachment attachment = new Attachment();
		
		attachment.setAssociateFormId(Long.parseLong(entityId));
		attachment.setAssociateFormName(entityName);
		attachment.setAttachmentAddr(entityName + "\\" + entityId + "\\" + entityName + "_" + entityId + "_" + fileName);
		attachment.setAttachmentRealName(fileName);
		attachment.setAttachmentLogicalName(fileName);
		attachment.setUploadPeople(uploadPeople);
		attachment.setUploadTime(new Date());
		attachment.setAssociateSize(fileSize);
		
		this.saveAttachment(attachment);
	}
	
	@Override
	public String getFilePathFormSystemConfig()
	{
		try
		{
			String attachmentPath = this.systemConfigService.findSystemConfigValueBykey("attachmentPath");
			if (attachmentPath == null || attachmentPath.equals(""))
			{
				attachmentPath = "D:\\java\\upload\\";
			}
			return attachmentPath;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getFilePathFormSystemConfig", e.getMessage(), e);
		}
		
	}
}
