/**
 * @Title: SQLExceptionTransfomer.java
 * @Package ddd.base.persistence
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年12月6日 下午7:11:25
 * @version V1.0
 */
package ddd.base.persistence;

import java.sql.SQLException;

import ddd.base.exception.DDDException;

/**
 * 项目名称：DDD3
 * 类名称：SQLExceptionTransfomer
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年12月6日 下午7:11:25
 * 修改人：DDD
 * 修改时间：2015年12月6日 下午7:11:25
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public abstract class SQLExceptionTransfomer
{
	
	/** 
	 * @Title: SQLExceptionTransfomer 
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 */
	public SQLExceptionTransfomer()
	{
		super();
	}
	public DDDException transformException(SQLException sqlException)
	{
		String message = "";
		DDDException dddException = new DDDException(message,sqlException);
		
		return dddException;
	}
	
	public abstract String tranformMessage(SQLException sqlException);
	
}