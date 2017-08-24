package ddd.simple.dao.reportForm;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.reportForm.ReportForm;

public interface ReportFormDao extends BaseDaoInterface
{
	public ReportForm saveReportForm(ReportForm reportForm) throws Exception;
	
	public int deleteReportForm(Long reportFormId) throws Exception;
	
	public ReportForm updateReportForm(ReportForm reportForm) throws Exception;
	
	public ReportForm findReportFormById(Long reportFormId) throws Exception;
	
	public EntitySet<ReportForm> findAllReportForm() throws Exception;
}
