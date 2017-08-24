package ddd.simple.action.reportForm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.docx4j.Docx4jGenerator;
import ddd.base.exception.DDDException;
import ddd.base.jxls.JxlsGenerator;
import ddd.base.jxls.TemplateGenerator;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.entity.modelFile.Report;
import ddd.simple.entity.reportForm.ReportForm;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.modelFile.ModelFileService;
import ddd.simple.service.reportForm.ReportFormService;

@Action
@RequestMapping("/ReportForm")
@Controller
public class ReportFormAction
{
	@Resource(name = "reportFormServiceBean")
	private ReportFormService	reportFormService;
	
	@Resource(name = "attachmentServiceBean")
	private AttachmentService	attachmentService;
	
	@Resource(name = "modelFileServiceBean")
	private ModelFileService	modelFileService;
	
	public ReportForm saveReportForm(ReportForm reportForm)
	{
		try
		{
			ReportForm saveReportForm = this.reportFormService.saveReportForm(reportForm);
			return saveReportForm;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public int deleteReportForm(Long reportFormId)
	{
		
		try
		{
			return this.reportFormService.deleteReportForm(reportFormId);
		} catch (DDDException e)
		{
			throw e;
		}
		
	}
	
	public ReportForm updateReportForm(ReportForm reportForm)
	{
		try
		{
			ReportForm updateReportForm = this.reportFormService.updateReportForm(reportForm);
			return updateReportForm;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public ReportForm findReportFormById(Long reportFormId)
	{
		try
		{
			ReportForm findReportForm = this.reportFormService.findReportFormById(reportFormId);
			return findReportForm;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public EntitySet<ReportForm> findAllReportForm()
	{
		try
		{
			EntitySet<ReportForm> allReportForm = this.reportFormService.findAllReportForm();
			return allReportForm;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public Report generateExcel(Map<String, Object> params, String modelFileKey, HttpServletResponse response) throws Exception
	{
		try{
			String templateFileName = this.findAttachmentFilePath(modelFileKey, Arrays.asList("xls","xlsx"));
			if(templateFileName != null){
				TemplateGenerator generator = new JxlsGenerator();
				generator.generate(templateFileName, params, response);
			}
		}catch(Exception e){
			throw e;
		}
		return new Report();
	}
	
	
	public Report reportFormPreview(Map<String, Object> params, String modelFileKey, HttpServletResponse response) throws Exception
	{
		try{
			String templateFileName = this.findAttachmentFilePath(modelFileKey, Arrays.asList("xls","xlsx"));
			if(templateFileName != null){
				TemplateGenerator generator = new JxlsGenerator();
				generator.generateHtml(templateFileName, params, response);
			}
		}catch(Exception e){
			throw e;
		}
		return new Report();
	}
	
	public void generateExcelReportForm(Map<String, Object> params, String modelFileKey, HttpServletResponse response) throws Exception
	{
		try
		{
			String templateFileName = this.findAttachmentFilePath(modelFileKey, Arrays.asList("docx"));
			if (templateFileName != null)
			{
				TemplateGenerator generator = new Docx4jGenerator(); 
				generator.generatePdf(templateFileName, params, response);
			}
		} catch (Exception e)
		{
			throw e;
		}
		
	}
	
	
	public Report generateDocxReportForm(Map<String, Object> params, String modelFileKey, HttpServletResponse response) throws Exception
	{
		try
		{
			String templateFileName = this.findAttachmentFilePath(modelFileKey, Arrays.asList("docx"));
			if (templateFileName != null)
			{
				TemplateGenerator generator = new Docx4jGenerator();
				generator.generate(templateFileName, params, response);
			}
		} catch (Exception e)
		{
			throw e;
		}
		return new Report();
	}
	
	public String findAttachmentFilePath(String modelFileKey, List<String> Types)
	{
		
		String templateFileName = "";
		
		ModelFile modelFile = this.modelFileService.findModelFileByKey(modelFileKey);
		EntitySet<Attachment> attachments = this.attachmentService.findAttachmentByForm(modelFile.getEId(), "modelFileKey(ddd3)");
		
		String serverAddr = this.attachmentService.getFilePathFormSystemConfig();
		
		templateFileName += serverAddr;
		
		Iterator<Attachment> ite = attachments.iterator();
		while (ite.hasNext())
		{
			Attachment attachment = ite.next();
			String fileType = FilenameUtils.getExtension(attachment.getAttachmentAddr()).toLowerCase();
			if (Types.contains(fileType))
			{
				templateFileName += attachment.getAttachmentAddr();
			}
		}
		if (!templateFileName.equals(serverAddr))
		{
			return templateFileName.replaceAll("/", "\\\\");
		}
		return null;
	}
	
}