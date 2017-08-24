/**
* @Title: InternalParamHandlerInterface.java
* @Package ddd.simple.service.base
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年9月21日 上午11:56:52
* @version V1.0
*/
package ddd.simple.service.base;

import java.util.Map;

/**
 * 项目名称：DDD3
 * 类名称：InternalParamHandlerInterface
 * 类描述：   
 * 创建人：AnotherTen
 * 创建时间：2015年9月21日 上午11:56:52
 * 修改人：胡均
 * 修改时间：2015年9月21日 上午11:56:52
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public interface InternalParamHandlerInterface
{
	public Map<String,Object> addInternalParam(Map<String,Object> map);
}
