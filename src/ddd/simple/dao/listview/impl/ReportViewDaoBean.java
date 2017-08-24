package ddd.simple.dao.listview.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.listview.ReportViewDao;
import ddd.simple.entity.listview.ColumnMetaData;
import ddd.simple.entity.listview.DisplayAttribute;
import ddd.simple.entity.listview.OrderCondition;
import ddd.simple.entity.listview.ReportCategory;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.entity.listview.SearchConditions;
import ddd.simple.entity.listview.ViewTreeCondition;

@Service
public class ReportViewDaoBean extends BaseDao implements ReportViewDao {

	public Set<ColumnMetaData> findColumns(Long dataSourceId) throws Exception{
		String whereSql="datasourceId="+dataSourceId;
		return this.query(whereSql,ColumnMetaData.class);
	}

	@Override
	public Set<ReportCategory> findAllReportCategory() throws Exception {
		return this.query("",ReportCategory.class);
	}

	public ReportView addReportView(ReportView reportView) throws Exception {
		return this.save(reportView);
	}

	@Override
	public ReportView findReportViewById(Long reportViewId) throws Exception {
		return this.query(reportViewId,ReportView.class);
	}

	@Override
	public ReportView updateReportView(ReportView reportView) throws Exception {
		return this.update(reportView);
	}
	
	@Override
	public int deleteReportView(Long reportViewId) throws Exception {
		this.deleteByWhere(" reportViewId = "+reportViewId, DisplayAttribute.class);
		this.deleteByWhere(" reportViewId = "+reportViewId, SearchConditions.class);
		this.deleteByWhere(" reportViewId = "+reportViewId, OrderCondition.class);
		this.deleteByWhere(" reportViewId = "+reportViewId, ViewTreeCondition.class);
		return this.deleteById(reportViewId,ReportView.class);
	}

	
	@Override
	public ReportView findReportViewByKey(String reportViewKey)
			throws Exception {
		
		String sql = "reportViewKey = '"+reportViewKey+"'";
		
		ReportView reportView = null;
		Set<ReportView> reportViewSet = this.query(sql, ReportView.class);
		if(reportViewSet != null && reportViewSet.size()>0)
		{
			reportView = (ReportView)reportViewSet.toArray()[0];
		}
		
		return reportView;
	}
	@Override
	public Set<ReportView> findAllReportViews()
			throws Exception {
		
		Set<ReportView> reportViews= this.query("", ReportView.class);
		 
		return reportViews;
	}	
}
