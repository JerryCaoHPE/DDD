package ddd.simple.util.file_upload_download;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.ContextLoaderListener;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.FileUtil;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.organization.Employee;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.organization.EmployeeService;
import ddd.simple.service.systemConfig.SystemConfigService;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet
{
	private static final long serialVersionUID = 7868149110164667104L;
	
	private String filePath = "D:\\java\\upload";
	
	private String reportFormPath = "D:\\java\\reportForm\\upload";
	
	private SystemConfigService systemConfigService = (SystemConfigService) ContextLoaderListener.getCurrentWebApplicationContext()
			.getBean("systemConfigServiceBean");
			
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=utf-8");
		try
		{
			
			String associateType = request.getParameter("associateType");
			String uploadPeopleId = request.getParameter("uploadPeopleId");
			String isTemporary = request.getParameter("isTemporary");// 是否存为临时文件
			String isVip = request.getParameter("isVip");
			
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			diskFactory.setSizeThreshold(4096);
			
			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			upload.setSizeMax(80 * 1024 * 1024);
			
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			
			PrintWriter out = null;
			try
			{
				out = response.getWriter();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			while (iter.hasNext())
			{
				FileItem item = (FileItem) iter.next();
				String fileName = this.getUploadFileName(item);
				if (!item.isFormField() && fileName != null)
				{
					if (isTemporary != null && isTemporary.equals("true"))
					{
						
						String returnURL = this.uploadTemporaryFile(fileName, item);
						out.write(returnURL);
						
					} else if ("img".equals(associateType))
					{
						String returnURL = this.uploadImgFile(item, uploadPeopleId, fileName, isVip);
						out.write(returnURL);
					} else
					{
						String entityId = request.getParameter("entityId");
						String entityName = new String(request.getParameter("entityName").getBytes("iso-8859-1"), "utf-8");
						long fileSize = item.getSize();
						this.uploadCommonFile(fileName, fileSize, item, entityName, entityId, uploadPeopleId);
					}
				}
			}
			out.close();
			SessionFactory.commitTransaction();
		} catch (Exception e)
		{
			SessionFactory.rollbackTransaction();
			System.out.println("使用 fileupload 包时发生异常 ...");
			e.printStackTrace();
		}
		
	}
	
	public String uploadTemporaryFile(String fileName, FileItem item)
	{
		String path = FileUtil.randomTemporaryFilePath();
		this.uploadFile(item, path, fileName);
		return path + "/" + fileName;
	}
	
	public void uploadCommonFile(String fileName, long fileSize, FileItem item, String entityName, String entityId, String uploadPeopleId)
			throws Exception
	{
		Object attachmentServiceBean = ContextLoaderListener.getCurrentWebApplicationContext().getBean("attachmentServiceBean");
		Object employeeServiceBean = ContextLoaderListener.getCurrentWebApplicationContext().getBean("employeeServiceBean");
		
		AttachmentService attachmentService = (AttachmentService) attachmentServiceBean;
		EmployeeService employeeService = (EmployeeService) employeeServiceBean;
		
		Employee uploadPeople = employeeService.findEmployeeById(Long.parseLong(uploadPeopleId));
		
		Attachment attachment = new Attachment();
		attachment.setAssociateFormId(Long.parseLong(entityId));
		attachment.setAssociateFormName(entityName);
		attachment.setAttachmentAddr(entityName + "/" + entityId + "/" + entityName + "_" + entityId + "_" + fileName);
		attachment.setAttachmentRealName(fileName);
		attachment.setAttachmentLogicalName(fileName);
		attachment.setAssociateSize(fileSize);
		attachment.setUploadPeople(uploadPeople);
		attachment.setUploadTime(new Date());
		
		Attachment newAttachment = attachmentService.saveAttachment(attachment);
		
		String filePath = systemConfigService.findSystemConfigValueBykey("filePath");
		if(filePath.length() > 0){
			this.filePath = filePath;
		}
		
		String savePath = this.filePath + "/" + entityName + "/" + entityId;
		String finalFileName = entityName + "_" + entityId + "_" + fileName;
		this.uploadFile(item, savePath, finalFileName);
	}
	
	public String uploadImgFile(FileItem item, String uploadPeopleId, String fileName, String isVip) throws Exception
	{

		String imgFilePath = systemConfigService.findSystemConfigValueBykey("galleryPath");
		if(imgFilePath == null || "".equals(imgFilePath))
		{
			throw new DDDException("未配置系统参数galleryPath!");
		}
		String savePath = imgFilePath + "/";
		String relPath = "";
		
		if (isVip.equals("true"))
		{
			relPath += "vipUp/" + uploadPeopleId;
		} else
		{
			relPath += "employeeUp/" + uploadPeopleId;
		}
		
		Date nowTime = new Date();
		
		String suffix = item.getName().replaceAll("[\\s\\S]+(?=.\\b[\\w]+\\b)","");
		String finalFileName = String.valueOf(nowTime.getTime())+suffix;
		
		savePath += relPath;
		this.uploadFile(item, savePath, finalFileName);
		
		return relPath + "/" + finalFileName;
	}
	
	public void uploadFile(FileItem item, String savePath, String fileName)
	{
		File uploadFile = new File(savePath);
		
		if (!uploadFile.exists())
		{
			uploadFile.mkdirs();
		}
		
		try
		{
			item.write(new File(uploadFile, fileName));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getUploadFileName(FileItem item)
	{
		String fileName = item.getName();
		long fileSize = item.getSize();
		if ("".equals(fileName) && fileSize == 0)
		{
			System.out.println("文件名为空 ...");
			return null;
		}
		
		System.out.println("完整的文件名：" + fileName);
		int index = fileName.lastIndexOf("\\");
		
		fileName = fileName.substring(index + 1, fileName.length());
		
		return fileName;
	}
	
}
