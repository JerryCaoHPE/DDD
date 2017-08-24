package ddd.simple.dao.listview.impl;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.listview.DataSourceDao;
import ddd.simple.entity.listview.ColumnMetaData;
import ddd.simple.entity.listview.DataSource;

@Service
public class DataSourceDaoBean extends BaseDao implements DataSourceDao {

	@Override
	public DataSource saveDataSource(DataSource dataSource) throws Exception{
		return this.save(dataSource);
	}

	@Override
	public int deleteDataSource(Long dataSourceId) throws Exception{
		this.deleteByWhere(" dataSourceId = "+dataSourceId, ColumnMetaData.class);
		return this.deleteById(dataSourceId,DataSource.class);
	}

	@Override
	public DataSource findDataSourceById(Long dataSourceId) throws Exception{
		return this.query(dataSourceId, DataSource.class);
	}

	@Override
	public String checkReportSql(String sql){
		try {
			this.query(sql);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return  new DDDException("checkReportSql", e.getMessage(), e).toString();
		}
	}

	public ResultSet getResultSetBySql(String sql)  throws Exception
	{
		return this.getResultSet(sql);
	}

	@Override
	public DataSource updateDataSource(DataSource dataSource) throws Exception{
		return this.update(dataSource);
	}
	
	public  Set<Map<String, Object>> querySql(String sql) throws Exception
	{
		return this.query(sql);
	}

	@Override
	public DataSource findDataSourceByCode(String dataSourceCode)
			throws Exception {
		DataSource dataSource = null;
		String whereSql =" dataSourceCode = '"+dataSourceCode+"'";
		
		Set<DataSource> dataSourceSet = this.query(whereSql, DataSource.class);
		if(dataSourceSet != null && dataSourceSet.size()>0)
		{
			dataSource = (DataSource)dataSourceSet.toArray()[0];
		}
		return dataSource;
	}

	@Override
	public EntitySet<DataSource> findDataSources() throws Exception {
		EntitySet<DataSource> dataSourceSet = this.query("", DataSource.class);
		return dataSourceSet;
	}
	@Override
	public Map<String,String> genSQLs(String tableName)
	{
		tableName = tableName.trim();
		EntityClass<?>  entityClass = SessionFactory.getEntityClass(tableName);
		if(entityClass == null)
		{
			throw new DDDException("没有的到表名为 "+ tableName+" 的实体定义，请注意大小写");
		}
		
		Map<String,String> sqls = new HashMap<String, String>();
		
		sqls.put("新增语句", entityClass.getSelectSql());
		sqls.put("新增语句", entityClass.getInsertSql());
		sqls.put("新增语句", entityClass.getDeleteSql());
		sqls.put("新增语句", entityClass.getDeleteSql());
		
		return sqls;
	}
}
