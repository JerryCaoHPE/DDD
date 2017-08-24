package ddd.simple.service.listview;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.DataSource;

public interface DataSourceService {

	public DataSource saveDataSource(DataSource dataSource);

	public DataSource updateDataSource(DataSource dataSource);

	public int deleteDataSource(Long dataSourceId);
	
	public DataSource findDataSourceById(Long dataSourceId);
	
	public EntitySet<DataSource> findDataSources();
	
	/*public String getDynamicSQL(DataSource dataSource);
	
	public String getDynamicSQL(DataSource dataSource,Map<String,Object> params);*/

	/*public String getDynamicSQL(String key,String sql,Map<String,Object> params);*/

	public String checkReportSql(String sql, Map<String,Object> params,HttpServletRequest request);
	
	public String  getReportSqlByDataSourceId(Long datasourceId);
	
	public String[] getParams(String dataSourceCode);
	
	public String[] getParams(String key,String sql);
	
	public Set<Map<String, Object>> getDataSourceResult(DataSource dataSource);
	
	public Set<Map<String, Object>> getDataSourceResult(DataSource dataSource,Map<String, Object> params);

	public DataSource findDataSourceByCode(String dataSourceCode);

	public DataSource copyDataSource(Long dataSourceId, String newDataSourceCode);
	
	public Map<String,String> genSQLs(String tableName);
}
