/**
* @Title: ExternalInterface.java
* @Package ddd.simple.service.externalInterface
* @Description: TODO(用一句话描述该文件做什么)
* @author 胡兴
* @date 2015年12月6日 下午4:23:53
* @version V1.0
*/
package ddd.simple.service.externalInterface;

import java.util.Map;

/**
* 项目名称：DDD3
* 类名称：ExternalInterface
* 类描述：   
* 创建人：胡兴
* 创建时间：2015年12月6日 下午4:23:53
* 修改人：胡兴
* 修改时间：2015年12月6日 下午4:23:53
* 修改备注：   
* @version 1.0
* Copyright (c) 2015  DDD
*/
public interface EIInterface
{
	public void execute(Map<String,Object> config);
}
