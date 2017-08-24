package ddd.simple.service.reportForm.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.jxls.JxlsGenerator;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.dynamicForm.DynamicFormDao;
import ddd.simple.dao.reportForm.ReportTestDao;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.dynamicForm.DynamicForm;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.entity.reportForm.ReportTest;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.listview.ListViewService;
import ddd.simple.service.reportForm.ReportTestService;

@Service
public class ReportTestServiceBean extends BaseService implements ReportTestService
{

	@Resource(name="reportTestDaoBean")
	private ReportTestDao reportTestDao;
	
	@Resource(name="dynamicFormDaoBean")
	private DynamicFormDao dynamicFormDao;
	
	@Resource(name="listViewServiceBean")
	private ListViewService listViewService;
	
	@Override
	public ReportTest saveReportTest(ReportTest reportTest) 
	{
		try {
			return this.reportTestDao.saveReportTest(reportTest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveReportTest", e.getMessage(), e);
		}
	}

	@Override
	public int deleteReportTest(Long reportTestId) {
		try {
			return this.reportTestDao.deleteReportTest(reportTestId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteReportTest", e.getMessage(), e);
		}
		
	}

	@Override
	public ReportTest updateReportTest(ReportTest reportTest) {
		try {
			return this.reportTestDao.updateReportTest(reportTest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateReportTest", e.getMessage(), e);
		}
	}

	@Override
	public ReportTest findReportTestById(Long reportTestId) {
		try {
			return this.reportTestDao.findReportTestById(reportTestId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findReportTestById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ReportTest> findAllReportTest() {
		try{
			return this.reportTestDao.findAllReportTest();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllReportTest", e.getMessage(), e);
		}
	}

	@Override
	public String preview(Long id) {
		try {
			ReportTest reportTest=reportTestDao.findReportTestById(id);
			DynamicForm dynamicForm=reportTest.getDynamicForm();
			String html=dynamicForm.getDynamicFormNewHtml();
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("preview", e.getMessage(), e);
		}
	}
	
	public void export2Excel(Set<Map<String, Object>>data ,ReportView reportView,OutputStream out ) throws IOException
	{
		JxlsGenerator.exportReportView2Excel(data, reportView, out); 
	}
	
	public void genReport(Long reportTestId,Map<String,Object> params,HttpServletResponse response) throws Exception
	{
		
		ReportTest reportTest = this.findReportTestById(reportTestId) ;
		
		EntitySet<Attachment> excelFiles = reportTest.getExcelFile();
		
 
		String templateFileName = "D:\\angular\\workspace\\DDD3\\jxls\\resources\\org\\jxls\\demo\\jdbc_template.xls";
		 
		 
	}

}
