package ddd.simple.service.listview.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import ddd.base.annotation.ColumnInfo;
import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.SQLUtil;
import ddd.simple.dao.listview.DataSourceDao;
import ddd.simple.entity.listview.ColumnMetaData;
import ddd.simple.entity.listview.DataSource;
//import ddd.simple.entity.listview.DisplayAttribute;
//import ddd.simple.entity.permission.LoginUser;
import ddd.simple.service.listview.DataSourceService;
//import ddd.simple.service.listview.impl.sql.*;

@Service
public class DataSourceServiceBean implements DataSourceService {
	@Resource(name = "dataSourceDaoBean")
	private DataSourceDao dataSourceDao;

	//DynamicSQLService dynamicSQLService = DynamicSQLService.getInsance();
	
	/**
	 * @Title: saveDataSource @Description:新增数据源
	 *
	 * @param dataSource 数据源
	 * @return DataSource 数据源
	 *
	 * @throws
	 */
	public DataSource saveDataSource(DataSource dataSource) {
		try {
			dataSource = this.dealDataSource(dataSource);
			return this.dataSourceDao.saveDataSource(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveDataSource", e.getMessage(), e);
		}
	}
	
	/**
	 * @Title: updateDataSource @Description:更新数据源
	 *
	 * @param dataSource 数据源
	 * @return DataSource  数据源
	 *
	 * @throws
	 */
	public DataSource updateDataSource(DataSource dataSource) {
		try {
			dataSource = this.dealDataSource(dataSource);
			return this.dataSourceDao.updateDataSource(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateDataSource", e.getMessage(), e);
		}
	}

	/**
	 * @Title: deleteDataSource @Description:删除数据源
	 *
	 * @param dataSourceId 数据源ID
	 * @return int 删除结果
	 *
	 * @throws
	 */
	public int deleteDataSource(Long dataSourceId) {
		try {
			int deleteCount = this.dataSourceDao.deleteDataSource(dataSourceId);

			return deleteCount;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteDataSource", e.getMessage(), e);
		}
	}

	/**
	 * @Title: findDataSourceById @Description:查询数据源
	 *
	 * @param dataSourceId 数据源ID
	 * @return DataSource 数据源
	 *
	 * @throws
	 */
	public DataSource findDataSourceById(Long dataSourceId) {
		try {
			return this.dataSourceDao.findDataSourceById(dataSourceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findDataSourceById", e.getMessage(), e);
		}
	}
	
	/**
	 * @Title: findDataSources @Description: 获取所有的数据源
	 *
	 * @return 
	 *
	 * @throws
	 */
	public EntitySet<DataSource> findDataSources()
	{
		try {
			return this.dataSourceDao.findDataSources();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findDataSources", e.getMessage(), e);
		}
	}

	/**
	 * @Title: getDynamicSQL @Description:通过数据源获取SQL语句
	 *
	 * @param dataSource 数据源
	 * @return String  动态SQL语句
	 *
	 * @throws
	 */
	/*public String getDynamicSQL(DataSource dataSource) {
		String sql = dataSource.getReportSql();
		this.dynamicSQLService.addDynamicSQL(dataSource.getDataSourceCode(), sql);
		return this.dynamicSQLService.genSQL(dataSource.getDataSourceCode(), new HashMap<String, Object>());
	}*/
	
	/**
	 * @Title: getDynamicSQL @Description:通过数据源及参数列表获取动态sql
	 *
	 * @param dataSource 数据源
	 * @param params  参数列表
	 * @return String  动态SQL语句
	 *
	 * @throws
	 */
	/*public String getDynamicSQL(DataSource dataSource, Map<String, Object> params) {
		return this.dynamicSQLService.genSQL(dataSource.getDataSourceCode().toString(), params);
	}*/

	
	/**
	 * @Title: getDynamicSQL @Description:获取动态sql
	 *
	 * @param key 动态文件KEY
	 * @param sql sql语句
	 * @param params 参数列表
	 * @return String 动态SQL语句
	 *
	 * @throws
	 */
	/*public String getDynamicSQL(String key, String sql, Map<String, Object> params) {
		return this.dynamicSQLService.genDynamicSQL(key,sql, params);
	}
	*/
	
	/**
	 * @Title: getDataSourceResult @Description:获取数据源结果
	 *
	 * @param dataSource
	 * @return Set<Map<String,Object>>
	 *
	 * @throws
	 */
	public Set<Map<String, Object>> getDataSourceResult(DataSource dataSource)
	{
		String sql = DynamicService.getDynamicSql(dataSource.getReportSql(), null);
		//this.getDynamicSQL(dataSource);
		
		try {
			return this.dataSourceDao.querySql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getDataSourceResult(DataSource dataSource)", "获取数据源结果失败", e);
		}
	}
	
	/**
	 * @Title: getDataSourceResult @Description:获取数据源结果
	 *
	 * @param dataSource
	 * @param params
	 * @return Set<Map<String,Object>>
	 *
	 * @throws
	 */
	public Set<Map<String, Object>> getDataSourceResult(DataSource dataSource,Map<String, Object> params)
	{
		String sql = DynamicService.getDynamicSql(dataSource.getReportSql(), params);
				//this.getDynamicSQL(dataSource,params);
		
		try {
			return this.dataSourceDao.querySql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getDataSourceResult(DataSource dataSource,Map<String, Object> params)", "获取数据源结果失败", e);
		}
	}

	/**
	 * @Title: checkReportSql @Description:验证sql
	 *
	 * @param sql sql
	 * @param params 参数列表
	 * @param request HttpServletRequest
	 * @return String 验证结果
	 *
	 * @throws
	 */
	public String checkReportSql(String sql, Map<String, Object> params, HttpServletRequest request) {
		try {
			try {
				//LoginUser user = (LoginUser) request.getSession().getAttribute( "loginUser");
				sql = DynamicService.getDynamicSql( sql, params);
				return this.dataSourceDao.checkReportSql(sql);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("checkReportSql", "获取当前登录用户ID失败", e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("checkReportSql", e.getMessage(), e);
		}
	}

	/**
	 * @Title: dealDataSource @Description:处理数据源
	 *
	 * @param dataSource 数据源
	 * @return DataSource 数据源
	 *
	 * @throws
	 */
	private DataSource dealDataSource(DataSource dataSource) {
		// 得到动态SQL
		String newReportSql  = DynamicService.getDynamicSql(dataSource.getReportSql(), null);
		//= this.getDynamicSQL(dataSource);

		// 填充 ColumnMetaDatas
		EntitySet<ColumnMetaData> columns = this.getColumnMetaData(newReportSql);
		dataSource.setHtmlColumns(columns);

		return dataSource;
	}
	
	/**
	 * @Title: getColumnMetaData @Description:获取SQL语句执行完成后的列信息
	 *
	 * @param sql sql
	 * @return EntitySet<ColumnMetaData> 
	 *
	 * @throws
	 */
	private EntitySet<ColumnMetaData> getColumnMetaData(String sql) {
		try{
			if(sql.split("from")[0].contains(",") && sql.split("from")[0].contains("*")){
				EntitySet<ColumnMetaData> columnMetaDatas=new EntitySet<ColumnMetaData>();
				
				columnMetaDatas=getColumnMetaDtatFromResultSet(sql);
				if(SessionFactory.isOracle()){
					columnMetaDatas=translate(columnMetaDatas, sql);
				}
				return columnMetaDatas;
			}
			else if(sql.split("from")[0].contains("*"))
			{
				return getColumnMetaDataFromEntityClass(sql);
			}
			else
			{
				return getColumnMetaDtatFromResultSet(sql);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("fillingColumnMetaData", e.getMessage(), e);
		}
	}
	
	private ColumnMetaData getColumnMetaData(EntitySet<ColumnMetaData> columnMetaDatas,String key){
		Iterator<ColumnMetaData> iterator=columnMetaDatas.iterator();
		
		while(iterator.hasNext()){
			ColumnMetaData columnMetaData=iterator.next();
			if(key.equals(columnMetaData.getColumnKey())){
				return columnMetaData;
			}
		}
		return null;
	}
	
	
	private EntitySet<ColumnMetaData> translate(EntitySet<ColumnMetaData> columnMetaDatas,String sql){
		try {
			String tableName = sql.split("from")[1].split(" ")[1].trim();
			EntityClass<?> entityClass = SessionFactory.getEntityClass(tableName);
			
			EntitySet<ColumnMetaData> result=new EntitySet<ColumnMetaData>();
			
			LinkedHashMap<String, ColumnInfo> columnInfoMap = entityClass.getColumnInfos();

			for (Entry<String, ColumnInfo> entry : columnInfoMap.entrySet()) {
				String key=entry.getKey().toUpperCase();
				
				ColumnMetaData columnMetaData=getColumnMetaData(columnMetaDatas, key);
				
				if(columnMetaData != null){
					ColumnMetaData temp=(ColumnMetaData) columnMetaData.clone();
					columnMetaDatas.remove(columnMetaData);
					
					temp.setColumnKey(entry.getKey());
					temp.setColumnValue(entry.getValue().getLabel());
					result.add(temp);
				}
			}
			result.addAll(columnMetaDatas);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("translate", e.getMessage(), e);
		}
		
	}
	
	private EntitySet<ColumnMetaData> getColumnMetaDataFromEntityClass(String sql)
	{
		try {
			String tableName = sql.split("from")[1].split(" ")[1].trim();
			EntityClass<?> entityClass = SessionFactory.getEntityClass(tableName);
			Set<Entry<String, ColumnInfo>> columnSet = entityClass.getClassColumnInfos().entrySet();
			EntitySet<ColumnMetaData> columns = new EntitySet<ColumnMetaData>();
			int i=0;
			for(Map.Entry<String, ColumnInfo> entry:columnSet){
				ColumnInfo columnInfo = entry.getValue();
				if(columnInfo.getType()==1)
				{
					ColumnMetaData data = new ColumnMetaData();
					data.setColumnIndex(i);
					data.setColumnKey(entry.getKey());
					data.setColumnValue(columnInfo.getLabel());
					if(columnInfo.getClazz().equals(Date.class)||columnInfo.getClazz().equals(java.sql.Date.class)||columnInfo.getClazz().equals(Timestamp.class))
						data.setType("date");
					else if(columnInfo.getClazz().equals(Long.class))
						data.setType("bigit");
					else
						data.setType("text");
					columns.add(data);
					i++;
				}
			} 
			return columns;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getColumnMetaDataFromEntityClass", e.getMessage(), e);
		}
		
	}


	private EntitySet<ColumnMetaData> getColumnMetaDtatFromResultSet(String sql) {
		
		sql = SQLUtil.addWhereExpression(sql," 1!=1");
		
		String[] properties = SQLUtil.getProperties(sql);
		EntitySet<ColumnMetaData> columns = new EntitySet<ColumnMetaData>();
		try {
			ResultSet rs = this.dataSourceDao.getResultSetBySql(sql);
			ResultSetMetaData data = rs.getMetaData();

			for (int i = 1; i <= data.getColumnCount(); i++) {

				// 获得指定列的列名
				String columnName = data.getColumnLabel(i);
				for (int j = 0; j < properties.length; j++) {
					if(columnName.equalsIgnoreCase(properties[j])){
						columnName = properties[j];
						break;
					}
				}
				// 获得指定列的数据类型名
				String columnTypeName = data.getColumnTypeName(i);

				ColumnMetaData column = new ColumnMetaData(columnName,
						columnName, i, columnTypeName);
				columns.add(column);
			}
			rs.close();

			return columns;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getColumnMetaDtatFromResultSet", e.getMessage(), e);
		}
	}

	/**
	 * @Title: getReportSqlByDataSourceId @Description:通过数据源ID获取数据源查询语句
	 *
	 * @param datasourceId 数据源ID
	 * @return String 数据源查询语句
	 *
	 * @throws
	 */
	public String getReportSqlByDataSourceId(Long datasourceId) {
		DataSource dataSource = this.findDataSourceById(datasourceId);
		String reportSql = dataSource.getReportSql();
		return reportSql;
	}
	
	/**
	 * @Title: getParams @Description:通过数据源获取数据源查询语句里面的所有参数
	 *
	 * @param datasourceId 数据源ID
	 * @return String[] 参数列表
	 *
	 * @throws
	 */
	public String[] getParams(String dataSourceCode)
	{
		
		/*String variableDefinitions = this.dynamicSQLService.genVariableDefinitions(dataSourceCode);
		
		if(variableDefinitions!="")
		{
			variableDefinitions = variableDefinitions.substring(0,variableDefinitions.length()-1);
		}
		
		String[] params = variableDefinitions.split(",");
		return params;*/
		return null;
	}
	
	/**
	 * @Title: getParams @Description:获取sql语句里面的所有参数
	 *
	 * @param key 动态文件KEY
	 * @param sql sql
	 * @return String[] 参数列表
	 *
	 * @throws
	 */
	public String[] getParams(String key,String sql)
	{
		/*this.dynamicSQLService.addDynamicSQL(key, sql);
		
		String variableDefinitions = this.dynamicSQLService.genVariableDefinitions(key);
		
		if(variableDefinitions!="")
		{
			variableDefinitions = variableDefinitions.substring(0,variableDefinitions.length()-1);
		}
		
		String[] params = variableDefinitions.split(",");
		return params;*/
		return null;
	}

	/**
	 * @Title: findDataSourceByKey @Description:通过数据源唯一编码查询数据源
	 *
	 * @param key 数据源唯一编码
	 * @return 
	 *
	 * @throws
	 */
	public DataSource findDataSourceByCode(String dataSourceCode) {
		
		try {
			return this.dataSourceDao.findDataSourceByCode(dataSourceCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findDataSourceByCode", "获取数据源失败", e);
		}
	}

	@Override
	public DataSource copyDataSource(Long dataSourceId, String newDataSourceCode) {
		try {
			DataSource orDataSource = this.findDataSourceById(dataSourceId);
			if(orDataSource!=null){
				DataSource newDataSource = new DataSource();
				newDataSource.setDataSourceName(orDataSource.getDataSourceName());
				newDataSource.setDataSourceCode(newDataSourceCode);
				newDataSource.setReportSql(orDataSource.getReportSql());
				newDataSource.setDataSourceDesc(orDataSource.getDataSourceDesc());
				return this.saveDataSource(newDataSource);
			}
			else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("copyDataSource", "复制数据源失败", e);
		}
	}
	
	public Map<String,String> genSQLs(String tableName)
	{
		return this.dataSourceDao.genSQLs(tableName);
	}	
}
