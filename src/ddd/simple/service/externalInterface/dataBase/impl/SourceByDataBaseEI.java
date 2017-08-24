package ddd.simple.service.externalInterface.dataBase.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.simple.service.externalInterface.dataBase.AbstractTargetCompleteEI;

/**
 * 
 * @项目名称：DDD3
 * @类名称：SourceByDataBaseExternalInterface @类描述：
 * @创建人：胡兴
 * @创建时间：2015年12月6日 下午5:33:03
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class SourceByDataBaseEI extends AbstractTargetCompleteEI
{
	protected Map<String, Object>	sourceConfig;
	protected Connection			sourceConnection;
	protected Map<String, Object>	sourceSqlParamsMap;
	protected String				sourceQuerySql;
	protected Map<String, List>		sourceUpdateTagSql;
	
	@Override
	public void analysisSourceConfig(Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		try
		{
			sourceConfig = (Map<String, Object>) config.get("source");
			if (sourceConfig == null)
				throw new DDDException("配置错误，为配置source!");
			Map<String, Object> sourceExecuteSqlConfig = (Map<String, Object>) sourceConfig.get("executeSql");
			if (sourceExecuteSqlConfig == null)
				throw new DDDException("配置错误，为配置executeSql!");
			sourceSqlParamsMap = (Map<String, Object>) sourceExecuteSqlConfig.get("params");
			sourceQuerySql = (String) sourceExecuteSqlConfig.get("querySql");
			if (sourceQuerySql == null)
				throw new DDDException("配置错误，为配置querySql!");
			sourceUpdateTagSql = (Map<String, List>) sourceExecuteSqlConfig.get("updateTagSql");
		} catch (DDDException e)
		{
			throw e;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("配置错误:\n" + config.toString());
		}
	}
	
	@Override
	public void buildSourceConnection(Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		Map<String, Object> sourceJdbcconfig = (Map<String, Object>) this.sourceConfig.get("jdbc");
		this.sourceConnection = this.buildConnection(sourceJdbcconfig);
	}
	
	@Override
	public void openTransaction()
	{
		// TODO Auto-generated method stub
		try
		{
			if (this.sourceConnection != null)
				this.sourceConnection.setAutoCommit(false);
			if (this.targetConnection != null)
				this.targetConnection.setAutoCommit(false);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DDDException("开启事务失败");
		}
		
	}
	
	@Override
	public void beforeDatasCollect(Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Map<String, String>> datasCollect(Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		Statement statement;
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		
		if (this.sourceQuerySql == null || "".equals(this.sourceQuerySql))
		{
			return null;
		}
		
		try
		{
			statement = this.sourceConnection.createStatement();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DDDException("获取源statement失败");
		}
		
		try
		{
			String querysql = DynamicService.getDynamicSql(this.sourceQuerySql, this.sourceSqlParamsMap);
			
			ResultSet resultSet = statement.executeQuery(querysql);
			ResultSetMetaData md = resultSet.getMetaData();
			while (resultSet.next())
			{
				Map<String, String> resultMap = new HashMap<String, String>();
				for (int i = 1; i <= md.getColumnCount(); i++)
				{
					resultMap.put(md.getColumnName(i), resultSet.getString(i));
				}
				datas.add(resultMap);
			}
			return datas;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("数据收集失败");
		} finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw new DDDException(e.getMessage());
			}
		}
		
	}
	
	@Override
	public void afterDatasCollect(List<Map<String, String>> datas, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateSourceBySingle(Map<String, String> data, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		PreparedStatement preparedSt = null;
		List<String> updateSqls = this.sourceUpdateTagSql.get(this.tagResult);
		if (updateSqls.isEmpty())
			return;
		try
		{
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.putAll(this.sourceSqlParamsMap);
			paramsMap.putAll(data);
			if (this.tagResult != null && !"".equals(this.tagResult))
			{
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.putAll(data);
				params.putAll(sourceSqlParamsMap);
				for (String sql : updateSqls)
				{
					sql = DynamicService.getDynamicSql(sql, params);
					preparedSt = this.targetConnection.prepareStatement(sql);
					preparedSt.execute();
					preparedSt.close();
				}
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		} finally
		{
			try
			{
				if (preparedSt != null)
				{
					preparedSt.close();
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
				throw new DDDException(e.getMessage());
			}
		}
	}
	
	@Override
	public void commitTransaction()
	{
		// TODO Auto-generated method stub
		try
		{
			this.sourceConnection.commit();
			this.targetConnection.commit();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("提交事务失败");
		}
	}
	
	@Override
	public void afterUpdate(List<Map<String, String>> datas, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void beforeUpdateSourceSingle(Map<String, String> data, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void afterUpdateSourceSingle(Map<String, String> data, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		
	}
	
}
