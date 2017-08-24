package ddd.simple.dao.reportForm.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.reportForm.ReportTest;
import ddd.simple.dao.reportForm.ReportTestDao;

@Service
public class ReportTestDaoBean extends BaseDao implements ReportTestDao
{
	@Override
	public ReportTest saveReportTest(ReportTest reportTest)  throws Exception{
		return this.save(reportTest);
	}

	@Override
	public int deleteReportTest(Long reportTestId)  throws Exception{
		return this.deleteById(reportTestId,ReportTest.class);
	}

	@Override
	public ReportTest updateReportTest(ReportTest reportTest)  throws Exception{
		return this.update(reportTest);
	}

	@Override
	public ReportTest findReportTestById(Long reportTestId)  throws Exception{
		return this.query(reportTestId, ReportTest.class);
	}
	
	@Override
	public EntitySet<ReportTest> findAllReportTest() throws Exception {
		return this.query("",ReportTest.class);
	}
}
