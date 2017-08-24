/**
* @Title: modelAndDynamicService.java
* @Package ddd.simple.service.model
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年10月22日 下午5:05:01
* @version V1.0
*/
package ddd.simple.service.model;

import ddd.base.exception.DDDException;
import ddd.simple.entity.model.Model;
 

/**
* 项目名称：simple
* 类名称：modelAndDynamicService
* 类描述：   处理模型和动态表单交接
* 创建人：Administrator
* 创建时间：2015年10月22日 下午5:05:01
* 修改人：胡兴
* 修改时间：2015年10月22日 下午5:05:01
* 修改备注：   
* @version 1.0
* Copyright (c) 2015  DDD
*/
public interface ModelAndDyFormService   
{
	/**
	 * 
	* @Title: creatForm 
	* @Description: 根据模型创建表单 
	* @param model
	* @throws DDDException 
	* @return void
	 */
	
	public void creatForm(Model model) throws DDDException;
	/**
	 * 
	* @Title: dealModelForDyForm 
	* @Description: 处理模型项
	* @param model
	* @throws DDDException 
	* @return void
	 */
	public void dealModelItem(Model model) throws DDDException;
	
	/**
	 * 
	* @Title: previewForm 
	* @Description: 根据模型预览表单 
	* @param model
	* @return
	* @throws DDDException 
	* @return String
	 */
	public String previewForm(Model model) throws DDDException;
}
