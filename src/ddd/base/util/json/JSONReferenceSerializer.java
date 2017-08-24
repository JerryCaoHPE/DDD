/**
* @Title: JSONReferenceSerializer.java
* @Package ddd.base.util.json
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月26日 下午1:59:57
* @version V1.0
*/
package ddd.base.util.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;

/**
 * 项目名称：DDD3
 * 类名称：JSONReferenceSerializer
 * 类描述：   本类在JSONSerializer的基基础上记录
 * 创建人：DDD
 * 创建时间：2015年11月26日 下午1:59:57
 * 修改人：DDD
 * 修改时间：2015年11月26日 下午1:59:57
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class JSONReferenceSerializer extends JSONSerializer
{
	//是否存在引用
	private boolean hasReference = false;
    /** 
	* @Title: JSONReferenceSerializer 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param out 
	*/ 
	public JSONReferenceSerializer(SerializeWriter out)
	{
		 super(out);
	}
	public JSONReferenceSerializer(SerializeWriter out,SerializeConfig config)
	{
		 super(out,config);
	}	
	public void writeReference(Object object) {
    	this.hasReference = true;
    	super.writeReference(object);
    }
	/** 
	* @return hasReference 
	*/ 
	
	public boolean isHasReference()
	{
		return hasReference;
	}
	
}
