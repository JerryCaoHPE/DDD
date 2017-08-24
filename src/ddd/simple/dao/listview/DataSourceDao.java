package ddd.simple.dao.listview;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.DataSource;

public interface DataSourceDao {
	public DataSource saveDataSource(DataSource dataSource) throws Exception;
	
	public int deleteDataSource(Long dataSourceId) throws Exception;
	
	public DataSource findDataSourceById(Long dataSourceId) throws Exception;
	
	public String checkReportSql(String sql);
	
	public DataSource updateDataSource(DataSource dataSource) throws Exception;
	
	public ResultSet getResultSetBySql(String sql)  throws Exception;
	
	public Set<Map<String, Object>> querySql(String sql) throws Exception;

	public DataSource findDataSourceByCode(String dataSourceCode) throws Exception;

	public EntitySet<DataSource> findDataSources() throws Exception;
	
	public Map<String,String> genSQLs(String tableName);
}
