/**
 * @Title: TemplateGenerator.java
 * @Package ddd.base.jxls
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年9月29日 下午1:13:13
 * @version V1.0
 */
package ddd.base.jxls;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 项目名称：DDD3
 * 类名称：TemplateGenerator
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年9月29日 下午1:13:13
 * 修改人：DDD
 * 修改时间：2015年9月29日 下午1:13:13
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public interface TemplateGenerator
{
	public void generateHtml(String templateFileName,Map<String,Object> params,HttpServletResponse response) throws FileNotFoundException;
	public void generatePdf(String templateFileName,Map<String,Object> params,HttpServletResponse response) throws FileNotFoundException;
	public void generate(String templateFileName,Map<String,Object> params,HttpServletResponse response) throws FileNotFoundException;
	public Map<String, Object> getParams(String templateFileName,Map<String,Object> params) throws FileNotFoundException;
}