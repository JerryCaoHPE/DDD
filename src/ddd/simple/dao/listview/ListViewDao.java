package ddd.simple.dao.listview;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.Page;
import ddd.simple.entity.listview.ReportView;

public interface ListViewDao
{
	
	public Set<Map<String, Object>> getReportViewData(String sql) throws Exception;

	public ReportView findListViewByKey(String listViewkey) throws Exception;
	
	public Long getTotalCount(String reportSql) throws Exception;
	
	public Map<String, Object> getSqlResultByDao(String reportSql,
			int beginIndex, int EveryPage, List<String> inputArray)  throws Exception;

	public Set<Map<String, Object>> querryEntity(String finalSql)throws Exception;


}
