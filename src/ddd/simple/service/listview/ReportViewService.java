package ddd.simple.service.listview;

import java.util.Set;

import ddd.simple.entity.listview.ReportView;

public interface ReportViewService {
	
	public ReportView addReportView(ReportView reportView);

	public ReportView findReportViewById(Long reportViewId);

	public ReportView updateReportView(ReportView reportView);

	public int deleteReportView(Long reportViewId);
	
	public ReportView findReportViewByKey(String reportViewKey);

	public ReportView copyReportView(Long reportViewId, String reportViewKey);

	public Set<ReportView> findAllReportViews() throws Exception;
}
