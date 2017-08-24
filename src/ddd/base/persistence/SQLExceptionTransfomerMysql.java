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

import org.apache.commons.lang3.StringUtils;

import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;


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
public class SQLExceptionTransfomerMysql  extends SQLExceptionTransfomer
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
		if(sqlException.getErrorCode() == 1451)
		{
			return tranformForeignKeyConstraintMessage(sqlException);
		}
		else if(sqlException.getErrorCode() == 1062)
		{
			return tranformDuplicateEntryConstraintMessage(sqlException);
		}
		return null;
	}
	
	public String tranformForeignKeyConstraintMessage(SQLException sqlException)
	{
/*		 		com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: 
		  a foreign key constraint fails (`recruit`.`employee`, CONSTRAINT `employee_fk_organization` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`EId`))
				1451*/
		String message = sqlException.getMessage();
		String referenceTable = StringUtils.substringBetween(message, "REFERENCES `", "`");
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(referenceTable);
		if(entityClass == null)
		{
			System.err.println("SQLExceptionTransfomerMysql.tranformForeignKeyConstraintMessage:根据表名没有实体的定义，表名："+referenceTable);
			return message;
		}
		
		message = "由于数据已经被  "+entityClass.getEntityInfo().getLabel() +"  使用（被引用），不能删除或修改";
		
		return message;
/*		以下代码由徐传运注释，本打算错误能显示 引用的的表的主键的值，但由于找不到 引用的值，没法写下去了，如果有必要，以后再处理.
 * 		String uniqueNames = ""; 
		List<ColumnInfo> uniqueColumnInfos = new ArrayList<ColumnInfo>();
		Iterator<ColumnInfo> iterator =  entityClass.getColumnInfos().values().iterator();
		while (iterator.hasNext())
		{
			ColumnInfo columnInfo = (ColumnInfo) iterator.next();
			if(columnInfo.isUnique())
			{
				uniqueColumnInfos.add(columnInfo);
				uniqueNames+=","+ columnInfo.getName();
			}
			
		}
		if(uniqueNames.equals(""))
		{
			uniqueNames = uniqueNames.substring(1);
		}
		else
		{
			String referenceColumnName =  StringUtils.substringBetween(message, "`.`", "`, CONSTRAINT");
			String sql = "select " +uniqueNames+" from "+ referenceTable+" where " 
		}*/
						
	}

	public String tranformDuplicateEntryConstraintMessage(SQLException sqlException)
	{
		/*		java.sql.BatchUpdateException: Duplicate entry '000100002' for key 'codeIndex'

		1062	*/	
		String message = sqlException.getMessage();
		String entityValue = StringUtils.substringBetween(message, "entry '", "' for");
		
		message = "值  "+entityValue+"  已经存在，不允许重复";
		
		return message;
	}
	
}
