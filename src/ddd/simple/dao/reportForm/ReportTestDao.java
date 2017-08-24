package ddd.simple.dao.reportForm;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.reportForm.ReportTest;

public interface ReportTestDao extends BaseDaoInterface
{
	public ReportTest saveReportTest(ReportTest reportTest) throws Exception;
	
	public int deleteReportTest(Long reportTestId) throws Exception;
	
	public ReportTest updateReportTest(ReportTest reportTest) throws Exception;
	
	public ReportTest findReportTestById(Long reportTestId) throws Exception;
	
	public EntitySet<ReportTest> findAllReportTest() throws Exception;
}
