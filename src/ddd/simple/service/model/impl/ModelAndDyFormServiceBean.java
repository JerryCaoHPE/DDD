/**
* @Title: ModelAndDyFormServiceBean.java
* @Package ddd.simple.service.model.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年10月22日 下午5:08:21
* @version V1.0
*/
package ddd.simple.service.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.model.ModelDao;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.service.model.ModelAndDyFormService;
import ddd.simple.service.model.ModelItemService;
import ddd.simple.util.model.ModelItemTypeTojavaType;

import ddd.simple.service.dynamicForm.DynamicFormService;

/**
 * 项目名称：simple 类名称：ModelAndDyFormServiceBean 类描述： 处理模型与动态表单的交接 创建人：Administrator
 * 创建时间：2015年10月22日 下午5:08:21 修改人：胡兴 修改时间：2015年10月22日 下午5:08:21 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
@Service
public class ModelAndDyFormServiceBean implements ModelAndDyFormService
{
	@Resource(name = "dynamicFormServiceBean")
	private DynamicFormService dynamicFormService;
	
	@Resource(name = "modelItemServiceBean")
	private ModelItemService modelItemService;
	
	@Resource(name = "modelDaoBean")
	private ModelDao modelDao;
	
	/**
	 * 
	 * @Title: creatForm
	 * @Description: 获取表单数据基本信息
	 * @param model
	 * @return
	 * @return String 表单key
	 */
	public void creatForm(Model model) throws DDDException
	{
		try
		{
			Map<String, Object> dynamicFormData = analysisDyFormData(model);
			Map<String, Object> formInfo = (Map<String, Object>) dynamicFormData.get("formInfo");
			this.dynamicFormService.creatForm(dynamicFormData);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getDynamicFormData", e.getMessage(), e);
		}
		
	}
	
	private Map<String, Object> analysisDyFormData(Model model)
	{
		Map<String, Object> dynamicFormData = new HashMap<String, Object>();
		Map<String, Object> formInfo = null, columnInfo;
		if (model != null)
		{
			formInfo = getFormInfo(model);
			columnInfo = getColumnInfo(model);
			dynamicFormData.put("formInfo", formInfo);
			dynamicFormData.put("columnInfo", columnInfo);
			model.getModelItems();
		}
		return dynamicFormData;
	}
	
	/**
	 * 
	 * @Title: getFormInfo
	 * @Description: 获取表单数据基本信息
	 * @param model
	 * @return
	 * @return Map<String,Object>
	 */
	private Map<String, Object> getFormInfo(Model model)
	{
		Map<String, Object> formInfo = new HashMap<String, Object>();
		String formKey = model.getModelForm();
		if (formKey == null || "".equals(formKey))
		{
			formKey = model.getModelEnglishName() + "Template";
			model.setModelForm(formKey);
		}
		formInfo.put("dynamicFormKey", formKey);
		formInfo.put("totalCount", model.getModelItems().size());
		formInfo.put("dynamicFormName", model.getModelName() + "表单");
		return formInfo;
	}
	
	/**
	 * 
	 * @Title: getColumnInfo
	 * @Description: 获取表单config参数
	 * @param model
	 * @return
	 * @return Map<String,Object>
	 */
	private Map<String, Object> getColumnInfo(Model model)
	{
		List<Map<String, Object>> configs = new ArrayList<Map<String, Object>>();
		EntitySet<ModelItem> modelItems = model.getModelItems();
		Iterator iterator = modelItems.iterator();
		while (iterator.hasNext())
		{
			ModelItem modelItem = (ModelItem) iterator.next();
			if (modelItem.getDatatype() != null)
			{
				if (modelItem.getIsUse() == null)
				{
					System.out.println(model.getModelEnglishName() + "模型" + modelItem.getModelItemEnglishName() + "模型项是否启用字段值为空");
				} else if (modelItem.getIsUse().equals("是"))
				{
					Map<String, Object> config = new HashMap<String, Object>();
					JSONObject relatedParameters = JSONObject
							.parseObject(modelItem.getRelatedParameters() == null ? "" : modelItem.getRelatedParameters());
					if ("choose".equalsIgnoreCase(modelItem.getDatatype()))
					{
						String dataSource = relatedParameters.getString("dataSource").replaceAll("[\"()\\[\\]]", "");
						String[] dataSourceArry = dataSource.split("\n|,");
						relatedParameters.put("dataSource", dataSourceArry);
						relatedParameters.put("dataSourceString", dataSource.replace("\n", "\\n"));
					} else if ("subTable".equalsIgnoreCase(modelItem.getDatatype()))
					{
						String displayFields = relatedParameters.getString("displayFields");
						relatedParameters.put("displayFields", displayFields.replace("\n", "\\n"));
					}else if("operateTips".equalsIgnoreCase(modelItem.getDatatype())){
						String defaultValue = relatedParameters.getString("defaultValue");
						relatedParameters.put("defaultValue", defaultValue.replace("\n", "\\n"));
					}
					config.putAll(relatedParameters);
					config.put("dataType", modelItem.getDatatype());
					config.put("title", modelItem.getModelItemName());
					config.put("bundle", modelItem.getModelItemEnglishName());
					configs.add(config);
				}
			} else
			{
				System.out.println(model.getModelEnglishName() + "模型" + modelItem.getModelItemEnglishName() + "模型项数据类型字段值为空");
			}
		}
		Map<String, Object> columnInfo = new HashMap<String, Object>();
		columnInfo.put("configs", configs);
		return columnInfo;
	}
	
	/**
	 * 
	 * @Title: dealModelForDyForm
	 * @Description: 处理模型
	 * @param model
	 * @return
	 * @return void
	 */
	public void dealModelItem(Model model) throws DDDException
	{
		
		// 处理模型项
		Long index = 1L;
		
		for (ModelItem item : model.getModelItems())
		{
			// 首次保存或提交时 处理继承下来的模型项
			if (model.getEId() == null)
			{
				if (item.getEId() != null && !item.getEId().equals("0"))
				{
					item.setEId(null);
					item.setIsInherited("是");
				} else
				{
					item.setIsInherited("否");
				}
			}
			
			if (item.getIsProcess()==null)
			{
				item.setIsProcess("否");
			}
			
			item.setModel(model);
			
			// 处理java类型
			if (item.getJavatype() == null || "".equals(item.getJavatype()))
			{
				item.setJavatype(ModelItemTypeTojavaType.getJavaType(item.getDatatype()));
			}
			
			// 设置默认排序
			item.setDisplayIndex(index++);
			
			// 处理特殊数据类型
			if ("subTable".equals(item.getDatatype()))
			{
				try
				{
					this.dealSubItem(item);
				} catch (Exception e)
				{
					e.printStackTrace();
					throw new DDDException("创建子表失败!");
				}
			}
		}
	}
	
	// --------------------------------------------------------------------
	// --------------------------------------处理模型项(子表)----------------
	// --------------------------------------------------------------------
	/**
	 * 
	 * @Title: dealSubItem
	 * @Description: 处理子表模型项
	 * @param item
	 * @return void
	 */
	private void dealSubItem(ModelItem item) throws Exception
	{
		JSONObject relatedParameters = JSON.parseObject(item.getRelatedParameters());
		item.setJoinTableName(relatedParameters.getString("joinTableName"));
		JSONObject subModelInfo = (JSONObject) relatedParameters.get("subModel");
		item.setSubModelEnglishName(subModelInfo.getString("modelEnglishName"));
		
		Model subModel = modelDao.findModelByEnglishName(item.getSubModelEnglishName());
		if (subModel == null)
		{
			throw new DDDException("子表：" + item.getSubModelEnglishName() + "不存在");
		}
		
		String displayFields = relatedParameters.getString("displayFields");
		if (displayFields != null && !"".equals(displayFields))
		{
			
			String[] columnNames = displayFields.split("[ ,，\n]");
			List<String> columns = new ArrayList<String>();
			for (String columnName : columnNames)
			{
				if (!"".equals(columnName))
				{
					columns.add(columnName);
				}
			}
			
			JSONArray displayColumns = createSubTableDisplayColumns(subModel, columns);
			relatedParameters.put("displayColumns", displayColumns);
			
			JSONArray operationColumns = createSubTableOperationColumns(subModel);
			relatedParameters.put("operationColumns", operationColumns);
			
			JSONArray operationBarButtons = createSubTableOperationBarButtons(subModel);
			relatedParameters.put("operationBarButtons", operationBarButtons);
			
			item.setRelatedParameters(relatedParameters.toJSONString());
		}
	}
	
	private JSONArray createSubTableOperationColumns(Model subModel)
	{
		JSONArray displayColumns = new JSONArray();
		
		JSONObject displayColumn = new JSONObject();
		displayColumn.put("label", "删除");
		displayColumn.put("operate", "delete");
		displayColumns.add(displayColumn);
		
		return displayColumns;
	}
	
	private JSONArray createSubTableOperationBarButtons(Model subModel)
	{
		JSONArray displayColumns = new JSONArray();
		
		JSONObject displayColumn1 = new JSONObject();
		displayColumn1.put("label", "新增");
		displayColumn1.put("operate", "add");
		displayColumn1.put("template", "simple/view/model/html/modelData/SubTableTemplate.html");
		displayColumns.add(displayColumn1);
		
		return displayColumns;
	}
	
	private JSONArray createSubTableDisplayColumns(Model subModel, List<String> columns)
	{
		JSONArray displayColumns = new JSONArray();
		EntitySet<ModelItem> modelItems = subModel.getModelItems();
		
		if (columns.size() == 0)
		{
			for (ModelItem modelItem : modelItems)
			{
				JSONObject displayColumn = createSubTableDisplayColumn(modelItem);
				displayColumns.add(displayColumn);
			}
		} else
		{
			for (String name : columns)
			{
				ModelItem modelItem = haveModelItem(modelItems, name);
				if (modelItem != null)
				{
					JSONObject displayColumn = createSubTableDisplayColumn(modelItem);
					displayColumns.add(displayColumn);
				}
			}
		}
		return displayColumns;
	}
	
	private JSONObject createSubTableDisplayColumn(ModelItem modelItem)
	{
		JSONObject displayColumn = new JSONObject();
		displayColumn.put("filed", modelItem.getModelItemEnglishName());
		displayColumn.put("label", modelItem.getModelItemName());
		displayColumn.put("type", "label");
		return displayColumn;
	}
	
	private ModelItem haveModelItem(EntitySet<ModelItem> modelItems, String modelItemEnglishName)
	{
		for (ModelItem modelItem : modelItems)
		{
			if (modelItem.getModelItemEnglishName().equals(modelItemEnglishName))
			{
				return modelItem;
			}
		}
		return null;
	}
	// --------------------------------------------------------------------
	
	/**
	 * 
	 * @Title: previewForm
	 * @Description: 根据模型预览表单
	 * @param model
	 * @return
	 * @return String 表单的html代码
	 */
	public String previewForm(Model model) throws DDDException
	{
		this.dealModelItem(model);
		Map<String, Object> dynamicFormData = analysisDyFormData(model);
		Map<String, Object> columnInfo = (Map<String, Object>) dynamicFormData.get("columnInfo");
		List<Map<String, Object>> configs = (List<Map<String, Object>>) columnInfo.get("configs");
		return this.dynamicFormService.createNewHtml(configs);
	}
}
