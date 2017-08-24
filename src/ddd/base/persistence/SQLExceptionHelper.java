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

import ddd.base.exception.DDDException;

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
public class SQLExceptionHelper
{
	public static Exception transformException(SQLException sqlException)
	{
		SQLExceptionTransfomer sqlExceptionTransfomer = null;
		if(SessionFactory.isOracle())
		{
			sqlExceptionTransfomer = new SQLExceptionTransfomerOracle();
		} 
		else if(SessionFactory.isMysql())
		{
			sqlExceptionTransfomer = new SQLExceptionTransfomerMysql();
		}
		String message =sqlExceptionTransfomer.tranformMessage(sqlException);
		
		if(message == null) return sqlException;
		
		DDDException dddException = new DDDException(message,sqlException);
		
		return dddException;
			
	}
}
