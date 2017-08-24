package ddd.simple.service.listview;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.listview.Page;
import ddd.simple.entity.listview.ReportView;

public interface ListViewService 
{
	public ReportView findListViewByKey(String listViewkey);

	public Map<String, Object> getResult(Page page, String listViewKey,Map<String, String> filterCondition, Map<String, Object> paramsMap,Map<String, String> initFilter,Map<String, String> sortingMap);
	
	public boolean createDataSourceWithState(String className) throws ClassNotFoundException;
	
	public DataSource createDataSourceWithEntity(String className) throws ClassNotFoundException;
	
	public boolean createReportViewWithState(String className) throws ClassNotFoundException;
	
	public List<ReportView> createReportViewWithEntity(String className) throws ClassNotFoundException;
	
	public boolean createDataSourceAndReportView(String className) throws ClassNotFoundException;
	
	public List<Object> createDataSourceAndReportViewWithEntity(String className) throws ClassNotFoundException;

	public Set<Map<String, Object>> findEntityByKey(String listViewKey,String eIds);
}
