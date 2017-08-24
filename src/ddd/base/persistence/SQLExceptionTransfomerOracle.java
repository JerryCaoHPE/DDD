/**
* @Title: SQLExceptionHelper.java
* @Package ddd.base.persistence
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年12月6日 下午6:59:06
* @version V1.0
*/
package ddd.base.persistence;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import ddd.base.annotation.ColumnInfo;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.SpringContextUtil;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.dao.organization.OrganizationDao;
import ddd.simple.service.codeTable.CodeTypeService;

/**
 * 项目名称：DDD3
 * 类名称：SQLExceptionHelper
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年12月6日 下午6:59:06
 * 修改人：DDD
 * 修改时间：2015年12月6日 下午6:59:06
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class SQLExceptionTransfomerOracle extends SQLExceptionTransfomer
{
	
	

	/* (非 Javadoc) 
	* <p>Title: tranformMessage</p> 
	* <p>Description: </p> 
	* @param sqlException
	* @return 
	* @see ddd.base.persistence.SQLExceptionTransfomer#tranformMessage(java.sql.SQLException) 
	*/
	@Override
	public String tranformMessage(SQLException sqlException) 
	{


/*		java.sql.BatchUpdateException: Duplicate entry '000100002' for key 'codeIndex'

		1062	*/	
		if(sqlException.getErrorCode() == 2292)
		{
			return tranformForeignKeyConstraintMessage(sqlException);
		}
		else if(sqlException.getErrorCode() == 1)
		{
			return tranformDuplicateEntryConstraintMessage(sqlException);
		}
		return null;
	}
	
	public String analyzeSqlMessage(String message,Boolean isGetTableName){
		BaseDaoInterface baseDao =  (BaseDao) SpringContextUtil.getBean("baseDao");
		String constraintName = StringUtils.substringBetween(message, "(", ")").split("\\.")[1];
		String queryTableNameSql = "select TABLE_NAME from all_constraints where CONSTRAINT_NAME='"+constraintName+"'";
		
		String queryColumnNameSql ="SELECT column_name FROM user_cons_columns cu, user_constraints au WHERE cu.constraint_name = au.constraint_name AND au.constraint_type = 'U' AND au.table_name = (select TABLE_NAME from all_constraints where CONSTRAINT_NAME='"+constraintName+"') AND INDEX_NAME = '"+constraintName+"'";
		
		
		String referenceTable = "";
		String repeatColumn = "";
		try
		{
			Set<Map<String,Object>> result = isGetTableName == true?baseDao.query(queryTableNameSql):baseDao.query(queryColumnNameSql);
			Iterator<Map<String,Object>> ite = result.iterator();
			while(ite.hasNext()){
				if(isGetTableName){
					referenceTable = (String)ite.next().get("TABLE_NAME");
				}else{
					repeatColumn = (String)ite.next().get("column_name");
				}
			}
		} catch (Exception e)
		{
			if(isGetTableName){
				System.err.println("通过约束名查询表名出错 ："+queryTableNameSql);
			}else{
				System.err.println("通过约束名查询违反唯一约束栏位出错 ："+repeatColumn);
			}
			
			return message;
		}
		if(isGetTableName){
			return referenceTable;
		}else{
			return repeatColumn;
		}
	}
	
	public String tranformForeignKeyConstraintMessage(SQLException sqlException)
	{
		String message = sqlException.getMessage();
		String referenceTable = this.analyzeSqlMessage(message,true);
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(referenceTable);
		if(entityClass == null)
		{
			System.err.println("SQLExceptionTransfomerMysql.tranformForeignKeyConstraintMessage:根据表名没有实体的定义，表名："+referenceTable);
			return message;
		}
		
		message = "由于数据已经被  "+entityClass.getEntityInfo().getLabel() +"  使用（被引用），不能删除或修改";
		
		return message;
	}

	public String tranformDuplicateEntryConstraintMessage(SQLException sqlException)
	{
		String message = sqlException.getMessage();
		String repeatColumn = this.analyzeSqlMessage(message,false);
		String referenceTable = this.analyzeSqlMessage(message,true);
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(referenceTable);
		if(entityClass == null)
		{
			System.err.println("SQLExceptionTransfomerMysql.tranformForeignKeyConstraintMessage:根据表名没有实体的定义，表名："+referenceTable);
			return message;
		}
		LinkedHashMap<String,ColumnInfo> columnInfos = entityClass.getClassColumnInfos();
		String repeatColumnLabel = "";
		Set<String> keySet = columnInfos.keySet();
		Iterator<String> ite = keySet.iterator();
		while(ite.hasNext()){
			String key = ite.next();
			if(key.toUpperCase().equals(repeatColumn)){
				repeatColumnLabel = columnInfos.get(key).getLabel();
			}
		}
		
		message = "栏位  "+repeatColumnLabel+"  的值违反了唯一约束，值重复，不能新增或修改";
		
		return message;
	}
	
}
