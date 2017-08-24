package ddd.simple.service.outInterface;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import ddd.simple.entity.outInterface.OutInterfaceConfig;

public interface OutInterfaceBaseService 
{
	public void excute(OutInterfaceConfig config);
	
	public void excuteByParam(OutInterfaceConfig config,String param);
	
	public void prepareGlobalParams(OutInterfaceConfig config);

	public void openConnection(OutInterfaceConfig config);//,Map<String,String> params
	
	public void closeConnection();
	
	public Connection getOutConnection();
	
	public Connection getLocalConnection();
	
	public OutInterfaceConfig beforeSelectHandle(OutInterfaceConfig config);
	
	public List<Map<String,String>> executeSelect(OutInterfaceConfig config);
	
	public List<Map<String,String>> afterSelectHandle(List<Map<String,String>> queryResult);
	
	public void update(List<Map<String,String>> queryResults,OutInterfaceConfig config);//执行更新语句
	
	public void updateLocals(Map<String,String> queryResult,OutInterfaceConfig config);//批量执行更新本地的sql语句
	
	public Map<String,String> beforeUpdateLocal(Map<String,String> queryResult,OutInterfaceConfig config);
	
	public void updateLocal(Map<String,String> queryResult,String singleUptateLocalSql);//执行更新本地sql语句  一条一条执行
	
	public void afterUpdateLocal();
	
	public void updateOuts(Map<String,String> queryResult,OutInterfaceConfig config);//批量执行更新外部接口sql语句

	public Map<String,String> beforeUpdateOut(Map<String,String> queryResult,OutInterfaceConfig config);
	
	public void updateOut(Map<String,String> queryResult,String singleUpdateSql);//单独执行更新外部接口sql语句
	
	public void afterUpdateOut();

}
