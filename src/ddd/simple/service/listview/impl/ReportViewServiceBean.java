package ddd.simple.service.listview.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.util.EntityUtil;
import ddd.simple.dao.listview.ReportViewDao;
import ddd.simple.entity.listview.DisplayAttribute;
import ddd.simple.entity.listview.OrderCondition;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.service.cache.JSONCacheEngine;
import ddd.simple.service.listview.ReportViewService;
import ddd.simple.util.Strings;

@Service
public class ReportViewServiceBean implements ReportViewService{
	
	@Resource(name="reportViewDaoBean")
	private ReportViewDao reportViewDao;
	
	public ReportView addReportView(ReportView reportView) {
		try {
			String filnalSql = generateSql(reportView);
			reportView.setFinalSql(filnalSql);
			reportView = this.reportViewDao.addReportView(reportView);
			//JSONCacheEngine.putCache("reportView", reportView.getReportViewKey(), reportView);
			return reportView;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("addReportView", e.getMessage(), e);
		}
	}

	@Override
	public ReportView updateReportView(ReportView reportView) {
		try {
			String finalSql  = generateSql(reportView);
			reportView.setFinalSql(finalSql); 
			reportView = this.reportViewDao.updateReportView(reportView);
			//JSONCacheEngine.putCache("reportView", reportView.getReportViewKey(), reportView);
			return reportView;  
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateReportView", e.getMessage(), e);
		}
	}

	@Override
	public ReportView findReportViewById(Long reportViewId) {
		try {
			return this.reportViewDao.findReportViewById(reportViewId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findReportViewById", e.getMessage(), e);
		}
	}
	
	public ReportView findReportViewByKey(String reportViewKey) {
		try {
			return this.reportViewDao.findReportViewByKey(reportViewKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findReportViewByKey", e.getMessage(), e);
		}
	}

	@Override
	public int deleteReportView(Long reportViewId) {
		try {
			return this.reportViewDao.deleteReportView(reportViewId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteReportView", e.getMessage(), e);
		}
	}

	/*public Set<ColumnMetaData> findColumns(Long dataSourceId) {
		try {
			return this.reportViewDao.findColumns(dataSourceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findColumns", e.getMessage(), e);
		}
	}*/

	private String generateSql(ReportView reportView)
	{
		String finalSql = Strings.BASESQL;
		
		finalSql = processSqlWithOrder(reportView,finalSql);
		finalSql = processSqlWithDislay(reportView,finalSql);
		
		return finalSql;
	}
	
	private String processSqlWithDislay(ReportView reportView,String finalSql){
		
		if(reportView.getDisplayAttributes().size()>0){
			String field = "" ;
			boolean noEId = true;
			for(DisplayAttribute display :reportView.getDisplayAttributes()){
				field = field + display.generateDisplaySql();
				if("EId".equalsIgnoreCase(display.getAttributeName()))
					noEId=false;
			}
			if(noEId)
				field = "EId EId , " +field;
			finalSql = "select "+field+" from (" +finalSql+ ")dt0";
		}
		return finalSql;
	}
	
	private String processSqlWithOrder(ReportView reportView,String finalSql){
		
		if(reportView.getOrderConditions().size() > 0){
			String sql = finalSql + " order by " ;
			
			for(OrderCondition order : reportView.getOrderConditions()){
				sql = sql+order.generateOrderSql(sql)+" , ";
			}
			
			sql = sql.substring(0, sql.length()-2);
		}
		
		return finalSql;
	}

	@Override
	public ReportView copyReportView(Long reportViewId, String reportViewKey) {
		try {
			ReportView orReportView = this.findReportViewById(reportViewId);
			if(orReportView!=null){
				orReportView.getDisplayAttributes();
				orReportView.getOrderConditions();
				orReportView.getDataSource();
				ReportView newPeportView = (ReportView)EntityUtil.clone(orReportView);
				newPeportView.setReportViewKey(reportViewKey);
				newPeportView.setEId(null);
				newPeportView = cleanAttrbuiltsEId(newPeportView);
				
				String filnalSql = generateSql(newPeportView);
				newPeportView.setFinalSql(filnalSql);
				
				return this.reportViewDao.addReportView(newPeportView);
			}
			else
			{
				return new ReportView();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("copyReportView", "复制视图失败", e);
		}
	}

	private ReportView cleanAttrbuiltsEId(ReportView reportView) {
		EntitySet<DisplayAttribute> displays = reportView.getDisplayAttributes();
		EntitySet<OrderCondition> orders = reportView.getOrderConditions();
		
		for(DisplayAttribute dispaly:displays)
		{
			dispaly.setEId(null);
		}
		for(OrderCondition order:orders)
		{
			order.setEId(null);
		}
		return reportView;
	}

	/* (非 Javadoc) 
	* <p>Title: findAllReportViews</p> 
	* <p>Description: </p> 
	* @return
	* @throws Exception 
	* @see ddd.simple.service.listview.ReportViewService#findAllReportViews() 
	*/
	@Override
	public Set<ReportView> findAllReportViews() throws Exception
	{
		return this.reportViewDao.findAllReportViews();
	}
}
