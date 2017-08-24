/**
 * @Title: ClientError.java
 * @Package ddd.base.exception
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年11月5日 下午9:56:53
 * @version V1.0
 */
package ddd.base.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddd.base.Config;
import ddd.base.persistence.SessionFactory;

/**
 * 项目名称：DDD3 类名称：ClientError 类描述： 传输到前台的异常信息 创建人：DDD 创建时间：2015年11月5日 下午9:56:53
 * 修改人：DDD 修改时间：2015年11月5日 下午9:56:53 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class ClientError
{
	private String id;
	private String				url;
	private String				message;
	private List<String>		rootMessages	= new ArrayList<String>();
	private Map<String, Object>	extendedData	= new HashMap<String, Object>();
	
	public static String createNewId()
	{
		Long id = SessionFactory.getNewPrimarykey("前台错误号");
		return Config.serverId+"-"+id;
	}
	public static ClientError create(Throwable cause, String url,String id)
	{
		ClientError clientError = new ClientError();
		
		clientError.id = id;
		clientError.url = url;
		clientError.message = cause.getMessage();
		
		while (cause != null)
		{
			clientError.rootMessages.add(cause.getClass().getSimpleName() + ":" + cause.getMessage());
			if (cause instanceof DDDException)
			{
				DDDException dddException = (DDDException) cause;
				clientError.extendedData.put("DDDException编码", dddException.getCode());
				if (dddException.extendedData != null)
				{
					clientError.extendedData.putAll(dddException.extendedData);
				}
			}
			cause = cause.getCause();
		}
		
		return clientError;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public List<String> getRootMessages()
	{
		return rootMessages;
	}
	
	public void setRootMessages(List<String> rootMessages)
	{
		this.rootMessages = rootMessages;
	}
	
	public Map<String, Object> getExtendedData()
	{
		return extendedData;
	}
	
	public void setExtendedData(Map<String, Object> extendedData)
	{
		this.extendedData = extendedData;
	}

	/** 
	* @return id 
	*/ 
	
	public String getId()
	{
		return id;
	}

	/** 
	* @param id 要设置的 id 
	*/
	
	public void setId(String id)
	{
		this.id = id;
	}
}
