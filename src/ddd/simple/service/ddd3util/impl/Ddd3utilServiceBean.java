package ddd.simple.service.ddd3util.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.annotation.EntityInfo;
import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.action.ddd3util.TabInfo;
import ddd.simple.dao.ddd3util.Ddd3utilDao;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.ddd3util.Ddd3utilService;

@Service
public class Ddd3utilServiceBean extends BaseService implements Ddd3utilService
{
	
	@Resource(name = "ddd3utilDaoBean")
	private Ddd3utilDao	ddd3utilDao;
	
	public List<String> queryTableFileds(String tableName) throws Exception
	{
		List<String> allFiledsOfTab = new ArrayList<String>();
		try
		{
			String sql = "select * from " + tableName + " where 1!=1";
			ResultSet rs = this.ddd3utilDao.querryRSbySql(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count=rsmd.getColumnCount();
			for(int i=1;i<=count;i++){
				allFiledsOfTab.add(rsmd.getColumnName(i));
			}
		} catch (Exception e)
		{
			throw e;
		}
		return allFiledsOfTab;
		
	}
	public String decorateFiledsWithTabName(List<String> allFiledsOfTab,String tabName){
		String result = "";
		for(int i =0;i<allFiledsOfTab.size();i++){
			result += ", "+tabName+"."+allFiledsOfTab.get(i) +" as "+tabName+"."+allFiledsOfTab.get(i)+" ";
		}
		return result;
	}
	
	@Override
	public String generatorFileds(List<TabInfo> tabs)
	{
		String result = "";
		try
		{
			for(int i =0;i<tabs.size();i++)
			{
				TabInfo tabInfo = tabs.get(i);
				String className = tabInfo.getClassName();
				String alias = tabInfo.getAlias();
				
				EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
				if(entityClass == null){
					throw new DDDException("实体类"+className+"不存在");
				}
				EntityInfo ei = entityClass.getEntityInfo();
				String tableName = ei.getName();
				List<String> allFiledsOfTab = this.queryTableFileds(tableName);
				
				String tabFileds = this.decorateFiledsWithTabName(allFiledsOfTab,alias == null?tableName:alias);
				result += tabFileds+" ";
			}
		} catch (Exception e)
		{
			throw new DDDException("generatorFileds",e);
		}
		result = result.substring(1);
		return result;
	}
	
}
