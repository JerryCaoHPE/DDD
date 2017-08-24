package ddd.simple.dao.listview;

import java.util.Set;

import ddd.simple.entity.listview.ColumnMetaData;
import ddd.simple.entity.listview.ReportCategory;
import ddd.simple.entity.listview.ReportView;

public interface ReportViewDao {
	public Set<ColumnMetaData> findColumns(Long dataSourceId) throws Exception;

	public Set<ReportCategory> findAllReportCategory() throws Exception;

	public ReportView addReportView(ReportView reportView) throws Exception;

	public ReportView findReportViewById(Long reportViewId) throws Exception;

	public ReportView updateReportView(ReportView reportView) throws Exception;

	public int deleteReportView(Long reportViewId) throws Exception;

	public ReportView findReportViewByKey(String reportViewKey)  throws Exception;
	
	public Set<ReportView> findAllReportViews() throws Exception;
}
