package ddd.simple.action.reportForm;

import java.util.HashMap;
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
import ddd.simple.entity.reportForm.ReportTest;
import ddd.simple.service.reportForm.ReportTestService;

@Action
@RequestMapping("/ReportTest")
@Controller
public class ReportTestAction
{
	@Resource(name="reportTestServiceBean")
	private ReportTestService reportTestService;
	
	public ReportTest saveReportTest(ReportTest reportTest)
	{
		try {
			ReportTest saveReportTest = this.reportTestService.saveReportTest(reportTest);
			return saveReportTest;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteReportTest(Long reportTestId){
		
		try {
			return this.reportTestService.deleteReportTest(reportTestId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ReportTest updateReportTest(ReportTest reportTest) {
		try {
			ReportTest updateReportTest = this.reportTestService.updateReportTest(reportTest);
			return updateReportTest;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ReportTest findReportTestById(Long reportTestId){
		try {
			ReportTest findReportTest = this.reportTestService.findReportTestById(reportTestId);
			return  findReportTest;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ReportTest> findAllReportTest(){
		try{
			EntitySet<ReportTest> allReportTest = this.reportTestService.findAllReportTest();
			return allReportTest;
		} catch (DDDException e) {
			throw e;
		}
	}

	public String preview(Long id){
		try{
			return reportTestService.preview(id);
		}catch(DDDException e){
			throw e;
		}
	}
	
	public void search(Long reportTestId,Map<String,Object> params,HttpServletResponse response) throws Exception{
		
		String templateFileName = "D:\\angular\\workspace\\DDD3\\src\\test\\report\\reportDemo1.xls";
		String fileType = FilenameUtils.getExtension(templateFileName).toLowerCase();
		if(fileType.equals("docx"))
		{
			TemplateGenerator generator = new Docx4jGenerator();
			generator.generate(templateFileName, params, response);
			//generator.generateDocx(templateFileName, params, "D://angular//ddd.docx");
		}
		else if(fileType.equals("xlsx") || fileType.equals("xls"))
		{
			TemplateGenerator generator = new JxlsGenerator();
			generator.generateHtml(templateFileName, params, response);
		}
		
	}
	public void search1(Long reportTestId,Map<String,Object> params,HttpServletResponse response) throws Exception{
		
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		String templateFileName = "D:\\angular\\workspace\\DDD3\\src\\test\\report\\reportDemo1.xls";
		String fileType = FilenameUtils.getExtension(templateFileName).toLowerCase();
		if(fileType.equals("docx"))
		{
			TemplateGenerator generator = new Docx4jGenerator();
			generator.generate(templateFileName, params, response);
		}
		else if(fileType.equals("xlsx") || fileType.equals("xls"))
		{
			TemplateGenerator generator = new JxlsGenerator();
			generator.generate(templateFileName, params, response);
		}
		
	}	
	public void search2(Long reportTestId,Map<String,Object> params,HttpServletResponse response) throws Exception{
		
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		String templateFileName = "D:\\angular\\workspace\\DDD3\\src\\test\\report\\reportDemo1.xls";
		String fileType = FilenameUtils.getExtension(templateFileName).toLowerCase();
		if(fileType.equals("docx"))
		{
			TemplateGenerator generator = new Docx4jGenerator();
			generator.generatePdf(templateFileName, params, response);
		}
		else if(fileType.equals("xlsx") || fileType.equals("xls"))
		{
			TemplateGenerator generator = new JxlsGenerator();
			generator.generatePdf(templateFileName, params, response);
		}
		
	}		
	
}