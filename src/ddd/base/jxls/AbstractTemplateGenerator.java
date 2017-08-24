/**
* @Title: AbstractTemplateGenerator.java
* @Package ddd.base.jxls
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年9月29日 下午1:18:48
* @version V1.0
*/
package ddd.base.jxls;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import ddd.base.dynamicSql.HierachySqlProcessor;


/**
 * 项目名称：DDD3
 * 类名称：AbstractTemplateGenerator
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年9月29日 下午1:18:48
 * 修改人：DDD
 * 修改时间：2015年9月29日 下午1:18:48
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public abstract class AbstractTemplateGenerator implements TemplateGenerator
{
	public Map<String, Object> getParams(String templateFileName,Map<String,Object> params) throws FileNotFoundException
	{
		
		String hierachySqlFileName = FilenameUtils.removeExtension(templateFileName)+".sql";
		HierachySqlProcessor test1 = new HierachySqlProcessor();
		Map<String, Object> datas = test1.loadData(hierachySqlFileName,params);
		
		params.putAll(datas);
		
		return params;
	}
	
}
