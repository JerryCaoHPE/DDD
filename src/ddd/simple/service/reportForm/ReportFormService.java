package ddd.simple.service.reportForm;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.reportForm.ReportForm;

public interface ReportFormService extends BaseServiceInterface
{
	public ReportForm saveReportForm(ReportForm reportForm) ;
	
	public int deleteReportForm(Long reportFormId) ;
	
	public ReportForm updateReportForm(ReportForm reportForm) ;
	
	public ReportForm findReportFormById(Long reportFormId) ;
	
	public EntitySet<ReportForm> findAllReportForm() ;
 
}