/**
* @Title: JSONResult.java
* @Package ddd.base.util.json
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月26日 下午2:12:44
* @version V1.0
*/
package ddd.base.util.json;

/**
 * 项目名称：DDD3
 * 类名称：JSONResult
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月26日 下午2:12:44
 * 修改人：DDD
 * 修改时间：2015年11月26日 下午2:12:44
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class JSONResult
{
	/** 
	* @Title: JSONResult 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param hasReference
	* @param json 
	*/ 
	public JSONResult(boolean hasReference, String json)
	{
		super();
		this.hasReference = hasReference;
		this.json = json;
	}

	private String json ;
	//是否存在引用
	private boolean hasReference = false;
	/** 
	* @return hasReference 
	*/ 
	
	public boolean isHasReference()
	{
		return hasReference;
	}
 
	/** 
	* @return json 
	*/ 
	
	public String getJson()
	{
		return json;
	}
 
}
