/**
* @Title: AbstractDataBaseExternalInterface.java
* @Package ddd.simple.service.externalInterface
* @Description: TODO(用一句话描述该文件做什么)
* @author 胡兴
* @date 2015年12月6日 下午4:25:12
* @version V1.0
*/
package ddd.simple.service.externalInterface.dataBase;


import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import ddd.base.exception.DDDException;
import ddd.simple.service.externalInterface.EIInterface;

/**
 * 项目名称：DDD3 类名称：AbstractDataBaseExternalInterface 类描述： 创建人：胡兴 创建时间：2015年12月6日
 * 下午4:25:12 修改人：胡兴 修改时间：2015年12月6日 下午4:25:12 修改备注：
 * 
 * @Description 提供从建立连接->获取源数据->插入目标数据库的逻辑方法。
 * @version 1.0 Copyright (c) 2015 DDD
 */
public abstract class AbstractBaseEI implements EIInterface
{
	protected Map<String, Object> config;
	
	/**
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:40:00
	 * @Description: 入口，控制执行顺序
	 * @Title: execute
	 * @param configJson
	 * @see ddd.simple.service.externalInterface.EIInterface#execute(java.lang.String)
	 */
	public void execute(Map<String,Object> config)
	{
		this.config = config;
		
		analysisSourceConfig(config);
		analysisTargetConfig(config);
		buildSourceConnection(config);
		buildTargetConnection(config);
		openTransaction();
		beforeDatasCollect(config);
		List<Map<String, String>> datas = datasCollect(config);
		commitTransaction();
		afterDatasCollect(datas, config);
		beforeUpdate(datas, config);
		updateTargetByAll(datas, config);
		commitTransaction();
		updateSourceByAll(datas, config);
		commitTransaction();
		afterUpdate(datas, config);
	}
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午7:52:42
	 * @Title: analysisConfig
	 * @Description: 根据参数初始化变量等
	 * @return void
	 */
	
	public abstract void analysisSourceConfig(Map<String,Object> config);
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午7:52:42
	 * @Title: analysisConfig
	 * @Description: 根据参数初始化变量等
	 * @return void
	 */
	public abstract void analysisTargetConfig(Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:40:00
	 * @Title: buildConnection
	 * @Description: 建立源库连接
	 * @param configJson
	 * @return void
	 */
	public abstract void buildSourceConnection(Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午6:25:56
	 * @Title: buildSourceConnection
	 * @Description: 建立目标库连接
	 * @param configJson
	 * @return void
	 */
	public abstract void buildTargetConnection(Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午6:37:28
	 * @Title: buildConnection
	 * @Description: 建立connection
	 * @param configJson
	 *            { driverClassName : ‘com.mysql.jdbc.Driver’,
	 *            url:’jdbc:mysql:localhost:3306/recruit?Unicode=true&
	 *            characterEncoding=UTF-8’ username:’root’, password:’root’ }
	 * 			
	 * @return
	 * @return Connection
	 */
	protected abstract Connection buildConnection(Map<String,Object> jdbcconfig);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:39:06
	 * @Title: openTransaction
	 * @Description: 开启事务
	 * @return void
	 */
	public abstract void openTransaction();
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:40:19
	 * @Title: beforeDatasCollect
	 * @Description: 数据收集前操作（例如准备填充参数，动态sql等）
	 * @param configJson
	 * @return void
	 */
	public abstract void beforeDatasCollect(Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:40:30
	 * @Title: datasCollect
	 * @Description: 进行数据收集
	 * @param configJson
	 * @return
	 * @return List<Map<String,String>>
	 */
	public abstract List<Map<String, String>> datasCollect(Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:40:40
	 * @Title: afterDatasCollect
	 * @Description: 数据收集后操作
	 * @param datas
	 * @param configJson
	 * @return void
	 */
	public abstract void afterDatasCollect(List<Map<String, String>> datas, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午8:56:00
	 * @Title: beforeUpdate
	 * @Description: 更新前操作
	 * @return void
	 */
	public abstract void beforeUpdate(List<Map<String, String>> datas, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午8:56:27
	 * @Title: afterUpdate
	 * @Description: 更新后操作
	 * @return void
	 */
	public abstract void afterUpdate(List<Map<String, String>> datas, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:45:11
	 * @Title: updateTargetByAll
	 * @Description: 更新目标库（所有数据）
	 * @param datas
	 * @param configJson
	 * @return void
	 */
	public void updateTargetByAll(List<Map<String, String>> datas, Map<String,Object> config)
	{
		for (Map<String, String> data : datas)
		{
			this.beforeUpdateTargetSingle(data, config);
			this.updateTargetBySingle(data, config);
			this.afterUpdateTargetSingle(data, config);
		}
	}
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月7日 下午7:31:52
	 * @Title: beforeUpdateTargetSingle
	 * @Description: 更新目标库单条数据前操作
	 * @param data
	 * @param config
	 * @return void
	 */
	public abstract void beforeUpdateTargetSingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:45:27
	 * @Title: updateTargetBySingle
	 * @Description: 更新目标库（单条数据）
	 * @param data
	 * @param configJson
	 * @return void
	 */
	public abstract void updateTargetBySingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月7日 下午7:32:14
	 * @Title: afterUpdateTargetSingle
	 * @Description: 更新目标库单条数据后操作
	 * @param data
	 * @param config
	 * @return void
	 */
	public abstract void afterUpdateTargetSingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:45:36
	 * @Title: updateSourceByAll
	 * @Description: 更新源库（所有数据）
	 * @param datas
	 * @param configJson
	 * @return void
	 */
	public void updateSourceByAll(List<Map<String, String>> datas, Map<String,Object> config)
	{
		for (Map<String, String> data : datas)
		{
			this.beforeUpdateSourceSingle(data, config);
			this.updateSourceBySingle(data, config);
			this.afterUpdateSourceSingle(data, config);
		}
	}
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月7日 下午7:34:00
	 * @Title: beforeUpdateSourceSingle
	 * @Description: 更新源库单条数据前操作
	 * @param data
	 * @param config
	 * @return void
	 */
	public abstract void beforeUpdateSourceSingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月7日 下午7:34:26
	 * @Title: afterUpdateSourceSingle
	 * @Description: 更新源库单条数据后操作
	 * @param data
	 * @param config
	 * @return void
	 */
	public abstract void afterUpdateSourceSingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:45:47
	 * @Title: updateSourceBySingle
	 * @Description: 更新源库（单条数据）
	 * @param data
	 * @param configJson
	 * @return void
	 */
	public abstract void updateSourceBySingle(Map<String, String> data, Map<String,Object> config);
	
	/**
	 * 
	 * @author 胡兴
	 * @since 2015年12月6日 下午4:45:57
	 * @Title: commitTransaction
	 * @Description: 提交事务
	 * @return void
	 */
	public abstract void commitTransaction();
	
}
