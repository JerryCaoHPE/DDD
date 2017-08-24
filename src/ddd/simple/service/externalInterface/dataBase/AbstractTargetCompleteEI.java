package ddd.simple.service.externalInterface.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;

/**
 * 
 * @项目名称:DDD3
 * @类名称:AbstractTargetCompleteEI
 * @类描述:由于目标基本都为数据库，因此实现目标库插入的部分代码
 * @创建人:胡兴
 * @创建时间:2015年12月7日 下午7:03:49
 * @version 1.0 Copyright (c) 2015 DDD
 */
public abstract class AbstractTargetCompleteEI extends AbstractBaseEI
{
	protected Connection	targetConnection;
	protected Connection	sourceConnection;
	
	protected Map<String, Object>	targetConfig;
	protected Map<String, Object>	targetSqlParamsMap;
	protected Map<String, List>		targetUpdateTagSql;
	protected String				tagSql;
	protected String				tagResult;
	
	private List<String> updateTargetSqls = null;
	
	public final void execute(Map<String, Object> config)
	{
		try
		{
			super.execute(config);
		} catch (Exception e)
		{
			e.printStackTrace();
			this.rollBack();
			throw new DDDException("execute", e.getMessage(), e);
		} finally
		{
			this.closeConnection();
		}
	}
	
	public void analysisTargetConfig(Map<String, Object> config)
	{
		try
		{
			targetConfig = (Map<String, Object>) config.get("target");
			if (targetConfig == null)
				throw new DDDException("配置错误，为配置target!");
			Map<String, Object> targetExecuteSqlConfig = (Map<String, Object>) targetConfig.get("executeSql");
			if (targetExecuteSqlConfig == null)
				throw new DDDException("配置错误，为配置executeSql!");
			targetSqlParamsMap = (Map<String, Object>) targetExecuteSqlConfig.get("params");
			targetUpdateTagSql = (Map<String, List>) targetExecuteSqlConfig.get("updateTagSql");
			tagSql = (String) targetExecuteSqlConfig.get("tagSql");
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("配置错误");
		}
	}
	
	private void rollBack()
	{
		try
		{
			if (this.sourceConnection != null)
			{
				
				this.sourceConnection.rollback();
			}
			if (this.targetConnection != null)
			{
				
				this.targetConnection.rollback();
			}
		} catch (SQLException e1)
		{
			e1.printStackTrace();
			throw new DDDException("链接回滚异常", "链接回滚异常", e1);
		}
	}
	
	private void closeConnection()
	{
		try
		{
			if (this.sourceConnection != null)
			{
				
				this.sourceConnection.close();
			}
			
			if (this.targetConnection != null)
			{
				this.targetConnection.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new DDDException("excute", e.getMessage(), e);
		}
	}
	
	public void buildTargetConnection(Map<String, Object> config)
	{
		Map<String, Object> TargetJdbcconfig = (Map<String, Object>) this.targetConfig.get("jdbc");
		this.targetConnection = this.buildConnection(TargetJdbcconfig);
	}
	
	protected Connection buildConnection(Map<String, Object> jdbcconfig)
	{
		Connection connection;
		
		String driverClassName, url, username, password;
		
		driverClassName = (String) jdbcconfig.get("driverClassName");
		url = (String) jdbcconfig.get("url");
		username = (String) jdbcconfig.get("username");
		password = (String) jdbcconfig.get("password");
		
		try
		{
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new DDDException("未导入jdbc驱动jar包");
		}
		try
		{
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DDDException("建立连接失败");
		}
		
		return connection;
	}
	
	public void updateTargetByAll(List<Map<String, String>> datas, Map<String, Object> config)
	{
		for (Map<String, String> data : datas)
		{
			updateTargetBySingle(data, config);
		}
	}
	
	public void updateSourceByAll(List<Map<String, String>> datas, Map<String, Object> config)
	{
		for (Map<String, String> data : datas)
		{
			updateSourceBySingle(data, config);
		}
	}
	
	public void beforeUpdate(List<Map<String, String>> datas, Map<String, Object> config)
	{
		this.tagResult = this.queryTag();
	}
	
	public void beforeUpdateTargetSingle(Map<String, String> data, Map<String, Object> config)
	{
	}
	
	public void updateTargetBySingle(Map<String, String> data, Map<String, Object> config)
	{
		// TODO Auto-generated method stub
		PreparedStatement preparedSt = null;
		List<String> updateSqls = this.targetUpdateTagSql.get(this.tagResult);
		if (updateSqls.isEmpty())
			return;
		try
		{
			
			if (this.tagResult != null && !"".equals(this.tagResult))
			{
				Pattern pt = Pattern.compile("insert\\s+into\\s+(\\w+)\\s*[\\s\\S]+");
				for (String sql : updateSqls)
				{
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.putAll(targetSqlParamsMap);
					paramsMap.putAll(data);
					
					Matcher mt = pt.matcher(sql);
					if (sql.contains("${EId}") && mt.find())
					{
						String tableName = mt.group(1);
						Long eid = SessionFactory.getNewPrimarykey(tableName);
						paramsMap.put("EId", eid.toString());
					}
					sql = DynamicService.getDynamicSql(sql, paramsMap);
					
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
	
	public void afterUpdateTargetSingle(Map<String, String> data, Map<String, Object> config)
	{
	
	}
	
	private String queryTag()
	{
		Statement statement = null;
		try
		{
			statement = this.targetConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(this.tagSql);
			String tag = "";
			if (resultSet.next())
			{
				tag = resultSet.getString("tag");
			}
			return tag;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("在目标库查询tag失败");
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
	
}
