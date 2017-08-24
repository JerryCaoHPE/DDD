package ddd.simple.service.reportForm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.entity.reportForm.ReportTest;
import ddd.simple.service.base.BaseServiceInterface;

public interface ReportTestService extends BaseServiceInterface
{
	public ReportTest saveReportTest(ReportTest reportTest) ;
	
	public int deleteReportTest(Long reportTestId) ;
	
	public ReportTest updateReportTest(ReportTest reportTest) ;
	
	public ReportTest findReportTestById(Long reportTestId) ;
	
	public EntitySet<ReportTest> findAllReportTest() ;
 
	public String preview(Long id);
	
	public void export2Excel(Set<Map<String, Object>>data ,ReportView reportView,OutputStream out ) throws IOException;
	
	public void genReport(Long reportTestId,Map<String,Object> pargenReportams,HttpServletResponse response) throws Exception;

}