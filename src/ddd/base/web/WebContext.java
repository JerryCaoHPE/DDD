/**
* @Title: WebContext.java
* @Package ddd.base.web
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月8日 上午11:07:48
* @version V1.0
*/
package ddd.base.web;

import ddd.simple.entity.permission.LoginUser;

/**
 * 项目名称：DDD3
 * 类名称：WebContext
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月8日 上午11:07:48
 * 修改人：DDD
 * 修改时间：2015年11月8日 上午11:07:48
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class WebContext
{
	private static ThreadLocal<WebContext> webContextLocal = new ThreadLocal<WebContext>();

	private LoginUser loginUser;
	
	public static WebContext getWebContext()
	{
		WebContext context = webContextLocal.get();
		if(context == null)
		{
			context = new WebContext();
			webContextLocal.set(context);
		}
		return context;
	}

	public static void release()
	{
		webContextLocal.set(null);
	}

	/** 
	* @return loginUser 
	*/ 
	
	public  LoginUser getLoginUser()
	{
		return this.loginUser ;
	}

	/** 
	* @param loginUser 要设置的 loginUser 
	*/
	
	public  void setLoginUser(LoginUser loginUser)
	{
		this.loginUser = loginUser;
	}
	
 
	

}
