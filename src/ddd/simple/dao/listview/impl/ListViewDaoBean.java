package ddd.simple.dao.listview.impl;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.listview.ListViewDao;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.listview.DisplayAttribute;
import ddd.simple.entity.listview.OrderCondition;
import ddd.simple.entity.listview.Page;
import ddd.simple.entity.listview.ReportView;

@Service
public class ListViewDaoBean extends BaseDao implements ListViewDao {

	public void initReportView(ReportView reportView) throws Exception {

		reportView.getOrderConditions();
		reportView.getDisplayAttributes();
		
		EntitySet<DisplayAttribute> displays = reportView.getDisplayAttributes();
		
		Map<Integer,DisplayAttribute> displayMap = new HashMap<Integer,DisplayAttribute>();
		
		for(DisplayAttribute display:displays)
		{
			displayMap.put(display.getColumnIndex(), display);
		}
		
		EntitySet<DisplayAttribute> newDisplays = new EntitySet<DisplayAttribute>();
		
		for(int i=0,count = displays.size();i<count;i++ )
		{
			newDisplays.add(displayMap.get(i));
		}
		
		reportView.setDisplayAttributes(newDisplays);
		
		reportView.getSearchConditions();
		reportView.getViewTreeConditions();
	}

	// 判断是否需要输入框
	public boolean isExistInput(ReportView reportView) {
		return false;
	}

	public Map<String, Object> getSqlResultByDao(String reportSql,
			int beginIndex, int EveryPage, List<String> inputArray)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
//		Page page = new Page();
//		page.setBeginIndex(beginIndex);
//		page.setEveryPage(EveryPage);
		map.put("totalCount", this.getTotalCount( reportSql));
		return map;

	}

	public Long getTotalCount(String reportSql) throws Exception {
		int firstFromIndex = reportSql.indexOf("from");
		String sql = "select count(*) as totalCount from " +reportSql.substring(firstFromIndex+4);
		
		Set<Map<String, Object>> totalCounts = this.query(sql,new String[]{"totalCount"});
		for (Map<String, Object> map : totalCounts) {
			return Long.parseLong(map.get("totalCount").toString());
		}
		return 0l;
	}

	public Set<Map<String, Object>> getReportViewData(String getResultSql)
			throws Exception {
		return this.query(getResultSql);
	}

	@Override
	public Set<Map<String, Object>> querryEntity(String getResultSql)throws Exception {
		return this.query(getResultSql);
	}
	
	@Override
	public ReportView findListViewByKey(String listViewkey)
			throws Exception {
		String sql = "reportViewKey = '"+listViewkey+"'";
		
		ReportView reportView = null;
		Set<ReportView> reportViewSet = this.query(sql, ReportView.class);
		if(reportViewSet != null && reportViewSet.size()>0)
		{
			reportView = (ReportView)reportViewSet.toArray()[0];
			this.initReportView(reportView);
		}
		
		return reportView;

	}
}
