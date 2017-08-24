package ddd.simple.service.reportForm.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.reportForm.ReportForm;
import ddd.simple.dao.reportForm.ReportFormDao;
import ddd.simple.service.reportForm.ReportFormService;

@Service
public class ReportFormServiceBean extends BaseService implements ReportFormService
{

	@Resource(name="reportFormDaoBean")
	private ReportFormDao reportFormDao;
	
	@Override
	public ReportForm saveReportForm(ReportForm reportForm) 
	{
		try {
			return this.reportFormDao.saveReportForm(reportForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveReportForm", e.getMessage(), e);
		}
	}

	@Override
	public int deleteReportForm(Long reportFormId) {
		try {
			return this.reportFormDao.deleteReportForm(reportFormId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteReportForm", e.getMessage(), e);
		}
		
	}

	@Override
	public ReportForm updateReportForm(ReportForm reportForm) {
		try {
			return this.reportFormDao.updateReportForm(reportForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateReportForm", e.getMessage(), e);
		}
	}

	@Override
	public ReportForm findReportFormById(Long reportFormId) {
		try {
			return this.reportFormDao.findReportFormById(reportFormId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findReportFormById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ReportForm> findAllReportForm() {
		try{
			return this.reportFormDao.findAllReportForm();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllReportForm", e.getMessage(), e);
		}
	}

}
