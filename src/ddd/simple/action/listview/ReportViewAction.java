package ddd.simple.action.listview;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.util.EntityUtil;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.service.listview.DataSourceService;
import ddd.simple.service.listview.ReportViewService;

@Action
@RequestMapping("/ReportView")
@Controller
public class ReportViewAction
{
	
	@Resource(name="reportViewServiceBean")
	private ReportViewService reportViewService;
	
	@Resource(name = "dataSourceServiceBean")
	private DataSourceService dataSourceService;
	
	public Map<String,Object> getReportViewInitData(Long dataSourceId)
	{
		try {
			DataSource dataSource =this.dataSourceService.findDataSourceById(dataSourceId);
			
			EntityUtil.clearProperty(dataSource, "htmlColumns.dataSource");
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("dataSource", dataSource);
			map.put("sqlParams", this.dataSourceService.getParams(dataSource.getDataSourceCode()));
			
			return map;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public ReportView addReportView(ReportView reportView)
	{
		try {
			ReportView reportViewSaved = this.reportViewService.addReportView(reportView);
			return reportViewSaved;
		} catch (DDDException e) {
			throw e;
		}
		
	}
	
	public Map<String,Object> findReportViewById(Long reportViewId)
	{
		try {
			ReportView reportView = this.reportViewService.findReportViewById(reportViewId);
			
			EntityUtil.loadLazyProperty(reportView, "displayAttributes");
			EntityUtil.clearProperty(reportView, "displayAttributes.reportView");
			EntityUtil.loadLazyProperty(reportView, "orderConditions");
			EntityUtil.clearProperty(reportView, "orderConditions.reportView");
			EntityUtil.loadLazyProperty(reportView, "searchConditions");
			EntityUtil.clearProperty(reportView, "searchConditions.reportView");
			EntityUtil.loadLazyProperty(reportView, "viewTreeConditions");
			EntityUtil.clearProperty(reportView, "viewTreeConditions.reportView");
			
			Map<String,Object> map = getReportViewInitData(reportView.getDataSource().getEId());
			map.put("reportView", reportView);
			
			
			
			return map;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public ReportView updateReportView(ReportView reportView)
	{
		try {
			ReportView reportViewSaved = this.reportViewService.updateReportView(reportView);
			return reportViewSaved;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public int deleteReportView(Long reportViewId)
	{
		try {
			return this.reportViewService.deleteReportView(reportViewId);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public ReportView copyReportView(Long reportViewId,String reportViewKey)
	{
		try {
			return this.reportViewService.copyReportView(reportViewId,reportViewKey);
		} catch (DDDException e) {
			throw e;
		}
	}
}
