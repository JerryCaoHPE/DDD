package ddd.simple.service.outInterface.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.simple.entity.outInterface.OutInterfaceConfig;
import ddd.simple.service.outInterface.OutInterfaceBaseService;

@Service
public  class OutInterfaceBaseServiceSupport implements OutInterfaceBaseService
{
	public Map<String,String> globalParams;//全局参数
	
	protected Connection localConnection;//连接本地的数据库连
	
	protected Connection outConnection;//连接外部的数据库连
	
	private List<String> localSqlLists = null;  
	
	private List<String> outSqlLists = null;
	
	@Override
	public void excute(OutInterfaceConfig config)
	{
		try
		{
			localSqlLists = new ArrayList<String>();  
			outSqlLists = new ArrayList<String>();
			this.openConnection(config);// 打开链接
			this.prepareGlobalParams(config);
			config = this.beforeSelectHandle(config);
			List<Map<String, String>> queryResult = this.executeSelect(config);
			this.outConnection.commit();
			queryResult = this.afterSelectHandle(queryResult);  //查询结果
			this.update(queryResult,config);
			this.localConnection.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				this.localConnection.rollback();
				this.outConnection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DDDException("链接回滚异常", "链接回滚异常", e1);
			}
			throw new DDDException("excute", e.getMessage(), e);
		}
		finally
		{
			this.closeConnection();
		}
		
	}
	@Override
	public void excuteByParam(OutInterfaceConfig config,String param)
	{
		try
		{
			this.openConnection(config);//, globalParams
			Map<String,String> params = this.analysisToMap(param);
			this.prepareGlobalParams(config);
			this.globalParams.putAll(params);
			config = this.beforeSelectHandle(config);
			List<Map<String, String>> queryResult = this.executeSelect(config);
			queryResult = this.afterSelectHandle(queryResult);
			this.update(queryResult,config);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			this.closeConnection();
		}
		
	}
	@Override
	public void prepareGlobalParams(OutInterfaceConfig config)
	{
		this.globalParams = this.analysisToMap(config.getSpecialParam());
		if(!this.globalParams.containsKey("STARTDATE_VALUE"))
		{
			this.globalParams.put("STARTDATE_VALUE", "null");
		}
		if(!this.globalParams.containsKey("ENDDATE_VALUE"))
		{
			this.globalParams.put("ENDDATE_VALUE", "null");
		}
	}

	/**
	 * outDriverName==oracle.jdbc.driver.OracleDriver;;
	 * outDbURL==jdbc:oracle:thin:@119.84.123.172:1521:cqaz;;
	 * outUserName==cqaz57;;
	 * outPassword==cqaz57;;
	 * 
	 * localDriverName==oracle.jdbc.driver.OracleDriver;;
	 * localDbURL==jdbc:oracle:thin:@127.0.0.1:1521:CCM;;
	 * localUserName==ccm;
	 * ;localPassword==ccm;;
	 */
	@Override
	public void openConnection(OutInterfaceConfig config)//Map<String, String> params
	{
		try 
		{ 
			String connectionInfo = config.getConnectInfo();
			Map<String,String> connectionMap = this.analysisToMap(connectionInfo);
			String outUrl = connectionMap.get("outDbURL"); 
			String outUser = connectionMap.get("outUserName"); 
			String outPassword = connectionMap.get("outPassword"); 
			String outDriverName = connectionMap.get("outDriverName");
			
			String localUrl = connectionMap.get("localDbURL"); 
			String localUser = connectionMap.get("localUserName"); 
			String localPassword = connectionMap.get("localPassword"); 
			String localDriverName = connectionMap.get("localDriverName"); 
			Class.forName(outDriverName).newInstance(); 
			Class.forName(localDriverName).newInstance(); 
			this.outConnection = DriverManager.getConnection(outUrl, outUser, outPassword); 
			this.localConnection = DriverManager.getConnection(localUrl, localUser, localPassword);
			this.outConnection.setAutoCommit(false);
			this.localConnection.setAutoCommit(false);
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
			throw new DDDException("openConnection", e.getMessage(), e);
		} 
	}
	
	
	@Override
	public void closeConnection() {
		
		try {
			if(this.localConnection != null)
			{
				
				this.localConnection.close();
			}
			
			if(this.outConnection != null)
			{
				this.outConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DDDException("excute", e.getMessage(), e);
		}
		
	}
	
	
	@Override
	public List<Map<String, String>> executeSelect(OutInterfaceConfig config) 
	{
		Statement statement = null;
		try 
		{
			List<Map<String, String>> queryResult = new ArrayList<Map<String,String>>();

			String querySql;
			ResultSet resultSet;
			if("YES".equals(config.getIsImport()))  //根据config中的isImport的yes和no判断是导入和导出。
			{
				statement = this.outConnection.createStatement();
				querySql = config.getOutQuerySql();  //获取源库查询语句
			}
			else
			{
				statement = this.localConnection.createStatement();
//				querySql = config.getLocalUpdateSql();  //更新本地语句
				querySql = config.getOutQuerySql();  //更新本地语句
			}
			if(querySql == null || querySql.length() == 0)
			{
				return null;
			}
			Iterator<String> iterator = this.globalParams.keySet().iterator();
			while(iterator.hasNext())
			{
				String paramName = iterator.next();
				String paramValue = this.globalParams.get(paramName);
				querySql = querySql.replaceAll(paramName, paramValue);
			}
			String[] querySqls = querySql.split(";;");
			for (String singleQueryStrig : querySqls)
			{
				if(singleQueryStrig == null || singleQueryStrig.length() < 6)
				{
					continue;
				}
				resultSet = statement.executeQuery(singleQueryStrig);
				ResultSetMetaData md = resultSet.getMetaData();
				while(resultSet.next())
				{
					Map<String, String> resultMap = new HashMap<String, String>();
					for (int i = 1; i <= md.getColumnCount(); i++) 
					{  
						resultMap.put(md.getColumnName(i), resultSet.getString(i));
		            }  
					queryResult.add(resultMap);
				}
			}
			
			return queryResult;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
		finally
		{
			try {
				if(statement != null)
				{
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DDDException(e.getMessage());
			}
		}
		
	}

	
	@Override
	public void update(List<Map<String, String>> queryResults,OutInterfaceConfig config) 
	{
		Statement localStatement = null;
		Statement outStatement = null;
		try 
		{
			String isImport = config.getIsImport();
			
			for (Map<String, String> singleQueryResult : queryResults) 
			{
				if("YES".equals(isImport))
				{
					this.updateLocals(singleQueryResult, config);
					this.updateOuts(singleQueryResult, config);
				}
				else
				{
					this.updateOuts(singleQueryResult, config);
					this.updateLocals(singleQueryResult, config);
				}
			}
			localStatement = this.localConnection.createStatement();
			outStatement = this.outConnection.createStatement();
			if(localSqlLists!=null&&localSqlLists.size()>0){
				for(String localSql:localSqlLists){
					localStatement.addBatch(localSql);
				}
				localStatement.executeBatch();
			}
			
			if(outSqlLists!=null&&outSqlLists.size()>0){
				for(String outSql:outSqlLists){
					outStatement.addBatch(outSql);
				}
				outStatement.executeBatch();
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
		finally
		{
			try {
				if(localStatement != null)
				{
					localStatement.close();
				}
				if(outStatement != null)
				{
					outStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateLocals(Map<String, String> queryResult,OutInterfaceConfig config) 
	{
		try 
		{
			String localQuerySql = config.getLocalQuerySql();    //获取目标的查询语句
			String localUpdateSql = config.getLocalUpdateSql();		//获取目标的更新语句
			
			if(localQuerySql != null&&localQuerySql.length() > 0){  //如果有目标查询语句,则根据查询返回的值,执行相应的语句
				
				String localQueryMark = queryMark(localQuerySql, queryResult)+"";
				
				if(localUpdateSql != null && localUpdateSql.length() > 0){
					String[] localUpdateSqls = localUpdateSql.split(";;");
					Map<String, String> map = new HashMap<String, String>();
					
					for(String singleUpdateSql : localUpdateSqls){
						if(singleUpdateSql == null || singleUpdateSql.length() < 6)
						{
							continue;
						}
						String[] tempSqls = singleUpdateSql.split(":=");
						map.put(tempSqls[0], tempSqls[1]);
					}
					
					if(map.containsKey(localQueryMark)){
						queryResult = this.beforeUpdateLocal(queryResult, config);
						this.updateLocal(queryResult, map.get(localQueryMark));
						this.afterUpdateLocal();
						
					}else{
						throw new DDDException("key没有对应sql语句", "key没有对应sql语句");
					}
				}
				
			}else{     //如果没localQuerySql,就执行LocalUpdate中的语句
				if(localUpdateSql != null && localUpdateSql.length() > 0){
					String[] localUpdateSqls = localUpdateSql.split(";;");
					
					for (String singleUpdateSql : localUpdateSqls) 
					{
						if(singleUpdateSql == null || singleUpdateSql.length() < 6)
						{
							continue;
						}
						String tempSql = singleUpdateSql.split(":=")[1];
						queryResult = this.beforeUpdateLocal(queryResult, config);
						this.updateLocal(queryResult, tempSql);
						this.afterUpdateLocal();
					}
				}
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
	}
	
	/**
	 * 根据本地的查询语句返回的值，执行本地更新语句中相应标示的语句。
	 * 	  如：  1:select * from demo;;2:select * from demo1
	 * 		返回1，则执行1这条语句。返回2，则执行2这条语句
	 * @param localQuerySql：select * from managementbasedoc where managementcode = '项目编码_VALUE'
	 * @return
	 */
	private int queryMark(String localQuerySql,Map<String, String> queryResult){
		Iterator<String> iterator = queryResult.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = queryResult.get(key);
			localQuerySql = localQuerySql.replaceAll(key, value);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = this.localConnection.createStatement();
			resultSet = statement.executeQuery(localQuerySql);
			int mark = 0;
			if(resultSet.next()){
				mark = resultSet.getInt("case");
			}
			return mark;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DDDException("SQL出错", "SQL出错"+e.getMessage()+"请与管理员联系", e);
		}finally{
			try {
				if(resultSet!=null){
					resultSet.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DDDException("关闭结果集异常");
			}finally{
				try{
					if(statement!=null){
						statement.close();
					}
				}catch (Exception e) {
					throw new DDDException("关闭statement异常");
				}
			}
		}
	}
	

	@Override
	public void updateLocal(Map<String, String> queryResult,String singleUptateLocalSql) 
	{
//		Statement statement = null;
//		try 
//		{
			queryResult.putAll(globalParams);
			Iterator<String> iterator = queryResult.keySet().iterator();
			while(iterator.hasNext())
			{
				String paramName = iterator.next();
				String paramValue;
				if(queryResult.get(paramName) == null)
				{
					paramValue = "";
				}
				else
				{
					paramValue = queryResult.get(paramName);
				}
				singleUptateLocalSql = singleUptateLocalSql.replaceAll(paramName, paramValue);
			}
//			statement = this.localConnection.createStatement();
//			statement.executeUpdate(singleUptateLocalSql);
			localSqlLists.add(singleUptateLocalSql);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			throw new DDDException(e.getMessage());
//		}
//		finally
//		{
//			try {
//				if(statement != null)
//				{
//					statement.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
	}

	@Override
	public void updateOuts(Map<String, String> queryResult,OutInterfaceConfig config) 
	{
		try 
		{
			String updateSql = config.getOutUpdateSql();
			
			
			if(updateSql != null && updateSql.length() > 0)
			{
				String[] updateSqls = updateSql.split(";;");
				for (String singleUpdateSql : updateSqls) 
				{
					if(singleUpdateSql == null || singleUpdateSql.length() < 6)
					{
						continue;
					}
					queryResult = this.beforeUpdateOut(queryResult, config);
					this.updateOut(queryResult, singleUpdateSql);
					this.afterUpdateOut();
				}
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
	}

	@Override
	public void updateOut(Map<String, String> queryResult,String singleUpdateSql) 
	{
//		Statement statement = null;
//		try 
//		{
			queryResult.putAll(globalParams);
			Iterator<String> iterator = queryResult.keySet().iterator();
			while(iterator.hasNext())
			{
				String paramName = iterator.next();
				String paramValue = queryResult.get(paramName);
				singleUpdateSql = singleUpdateSql.replaceAll(paramName, paramValue);
			}
//			statement = this.outConnection.createStatement();
//			statement.executeUpdate(singleUpdateSql);
			outSqlLists.add(singleUpdateSql);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			throw new DDDException(e.getMessage());
//		}
//		finally
//		{
//			try {
//				if(statement != null)
//				{
//					statement.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
	}
	@Override
	public Connection getOutConnection() 
	{
		return this.outConnection;
	}
	@Override
	public Connection getLocalConnection() 
	{
		return this.localConnection;
	}
	@Override
	public OutInterfaceConfig beforeSelectHandle(OutInterfaceConfig config) 
	{
		return config;
	}
	@Override
	public List<Map<String, String>> afterSelectHandle(
			List<Map<String, String>> queryResult)
	{
		return queryResult;
	}
	@Override
	public Map<String, String> beforeUpdateLocal(
			Map<String, String> queryResults,OutInterfaceConfig config) 
	{
		return queryResults;
	}
	@Override
	public void afterUpdateLocal() 
	{
		// TODO Auto-generated method stub
	}
	@Override
	public Map<String, String> beforeUpdateOut(Map<String, String> updateOutSql,OutInterfaceConfig config) 
	{
		return updateOutSql;
	}
	@Override
	public void afterUpdateOut() 
	{
		// TODO Auto-generated method stub
	}
	//把格式为   xxx==dd;;sss==eee 的字符串解析为Map
	public Map<String,String> analysisToMap(String param)
	{
		Map<String, String> map = new HashMap<String, String>();
		if(param == null || param.length() == 0)
		{
		}
		else
		{
			String[] params = param.split(";;");
			for (String singleParam : params) 
			{
				if(singleParam == null) continue;
				singleParam = singleParam.trim();
				if("".equals(singleParam)) continue;
				
				String[] signParams = singleParam.split("==");
				if(signParams != null && signParams.length >= 2)
				{
					signParams[0] = signParams[0].trim();
					signParams[0] = signParams[1].trim();
					if("selectSql".equals(signParams[0]))
					{
						Map<String,String> queryParamMap = this.prepareSelectParams(signParams[1]);
						map.putAll(queryParamMap);
					}
					else
					{
						map.put(signParams[0], signParams[1]);
					}
				}
			}
		}
		return map;
	}
	private Map<String,String> prepareSelectParams(String paramSql)
	{
		Statement statement = null;
		try {
				Map<String,String> resultParamsMap = new HashMap<String, String>();
				Map<String,String> dateSourceMap = new HashMap<String, String>();
				String[] params = paramSql.split("||");
				for (String singleParam : params) 
				{
					String[] signParams = singleParam.split(":");
					if(signParams != null && signParams.length >= 2)
					{
						dateSourceMap.put(signParams[0].trim(), signParams[1].trim());
					}
				}
				String dateSource = dateSourceMap.get("dateSourceMap");
				String paramName = dateSourceMap.get("paramName");
				String sql = dateSourceMap.get("sql");
				
				ResultSet resultSet;
				if(sql == null || sql.length() < 6)
				{
					return null;
				}
				if("local".equals(dateSource))
				{
					statement = this.localConnection.createStatement();
				}
				else
				{
					statement = this.outConnection.createStatement();
				}
				resultSet = statement.executeQuery(sql);
				String queryResult = "";
				while(resultSet.next())
				{
					queryResult = queryResult+resultSet.getString(1)+",";
				}
				queryResult = queryResult.substring(0, queryResult.length()-1);
				resultParamsMap.put(paramName, queryResult);
				statement.close();
				return resultParamsMap;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
		finally
		{
			try {
				if(statement != null)
				{
					statement.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
}
