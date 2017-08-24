package ddd.simple.dao.reportForm.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.reportForm.ReportForm;
import ddd.simple.dao.reportForm.ReportFormDao;

@Service
public class ReportFormDaoBean extends BaseDao implements ReportFormDao
{
	@Override
	public ReportForm saveReportForm(ReportForm reportForm)  throws Exception{
		return this.save(reportForm);
	}

	@Override
	public int deleteReportForm(Long reportFormId)  throws Exception{
		return this.deleteById(reportFormId,ReportForm.class);
	}

	@Override
	public ReportForm updateReportForm(ReportForm reportForm)  throws Exception{
		return this.update(reportForm);
	}

	@Override
	public ReportForm findReportFormById(Long reportFormId)  throws Exception{
		return this.query(reportFormId, ReportForm.class);
	}
	
	@Override
	public EntitySet<ReportForm> findAllReportForm() throws Exception {
		return this.query("",ReportForm.class);
	}
}
