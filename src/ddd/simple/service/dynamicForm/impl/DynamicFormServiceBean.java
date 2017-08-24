package ddd.simple.service.dynamicForm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ddd.base.Config;
import ddd.base.codegenerator.Generator;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.service.model.ModelService;
import ddd.simple.dao.dynamicForm.DynamicFormDao;
import ddd.simple.entity.dynamicForm.DynamicForm;
import ddd.simple.service.dynamicForm.DynamicFormService;

@Service
public class DynamicFormServiceBean implements DynamicFormService
{
	
	@Resource(name = "dynamicFormDaoBean")
	private DynamicFormDao	dynamicFormDao;
	
	@Resource(name = "modelServiceBean")
	private ModelService	modelService;
	
	// 替换模板路径
	private final String path = Config.dynamicFormVMPath == null || "".equals(Config.dynamicFormVMPath) ? (this.getClass().getClassLoader().getResource("/").getPath().replaceFirst("/", "") + "ddd/dynamicFormVM/") :Config.dynamicFormVMPath;
	//private String path = Config.applicationPath + "WEB-INF/classes/ddd/dynamicFormVM/";
	@Override
	public DynamicForm saveDynamicForm(DynamicForm dynamicForm)
	{
		try
		{
			DynamicForm oldDynamicForm = this.dynamicFormDao.findDynamicFormByKey(dynamicForm.getDynamicFormKey());
			if(oldDynamicForm != null)
			{
				throw new DDDException("唯一编码："+dynamicForm.getDynamicFormKey()+"已存在！");
			}
			return this.dynamicFormDao.saveDynamicForm(dynamicForm);
		} catch (Exception e)
		{
			throw new DDDException("saveDynamicForm", e.getMessage(), e);
		}
	}
	
	@Override
	public int deleteDynamicForm(Long dynamicFormId)
	{
		try
		{
			return this.dynamicFormDao.deleteDynamicForm(dynamicFormId);
		} catch (Exception e)
		{
			throw new DDDException("deleteDynamicForm", e.getMessage(), e);
		}
		
	}
	
	@Override
	public DynamicForm updateDynamicForm(DynamicForm dynamicForm)
	{
		try
		{
			DynamicForm oldDynamicForm = this.dynamicFormDao.findDynamicFormByKey(dynamicForm.getDynamicFormKey());
			if(oldDynamicForm != null && !oldDynamicForm.getEId().equals(dynamicForm.getEId()))
			{
				throw new DDDException("唯一编码："+dynamicForm.getDynamicFormKey()+"已存在！");
			}
			return this.dynamicFormDao.updateDynamicForm(dynamicForm);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateDynamicForm", e.getMessage(), e);
		}
	}
	
	public Map<String, Object> preViewForm(String dynamicFormNewHtml)
	{	
		try
		{
			Map dynamicFormNewHtmlMap = JSON.parseObject(dynamicFormNewHtml);
			dynamicFormNewHtml = dynamicFormNewHtmlMap.get("parse").toString();
			List<Map> configs = (List<Map>) dynamicFormNewHtmlMap.get("datas");
			String newHtml = createNewHtml("", configs);
			
			Map newHtmlMap = new HashMap();
			newHtmlMap.put("newHtml", newHtml);
			String preHtml = Generator.generate(path + "preForm.vm",newHtmlMap);
			
			Map<String, Object> preHtmlMap = new HashMap<String, Object>();
			preHtmlMap.put("preHtml", preHtml);
			
			return preHtmlMap;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("preViewForm", e.getMessage(), e);
		}
	}
	
	public DynamicForm updateDynamicFormById(Long dynamicFormId, String dynamicFormOldHtml, String dynamicFormNewHtml)
	{
		try
		{
			DynamicForm dynamicForm = this.findDynamicFormById(dynamicFormId);
			dynamicForm.setDynamicFormOldHtml(dynamicFormOldHtml);
			dynamicForm.setDynamicFormNewHtml(dynamicFormNewHtml);
			this.modelService.synchronousModelItem(dynamicForm);// 同步模型项数据
			this.setDynamicForm(dynamicForm);// 模板替换
			return this.dynamicFormDao.updateDynamicForm(dynamicForm);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateDynamicForm", e.getMessage(), e);
		}
	}
	
	public DynamicForm setDynamicForm(DynamicForm dynamicForm)
	{
		HashMap<String, String> map = this.replaceDynamicFormTemplate(dynamicForm);
		if (map == null)
		{
			return dynamicForm;
		}
		String dynamicFormNewHtml = map.get("dynamicFormNewHtml");
		String dynamicFormDisplayHtml = map.get("dynamicFormDisplayHtml");
		dynamicForm.setDynamicFormNewHtml(dynamicFormNewHtml);
		dynamicForm.setDynamicFormDisplayHtml(dynamicFormDisplayHtml);
		return dynamicForm;
	}
	
	@Override
	public DynamicForm findDynamicFormById(Long dynamicFormId)
	{
		try
		{
			return this.dynamicFormDao.findDynamicFormById(dynamicFormId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findDynamicFormById", e.getMessage(), e);
		}
	}
	
	/**
	 * 根据模型ID获取它的所有模型项
	 */
	public String findModelItems(Long modelId)
	{
		Model model = this.modelService.findModelById(modelId);
		List<Map<String, Object>> configs = new ArrayList<Map<String, Object>>();
		Map<String, Object> modelItemsMap = new HashMap<String, Object>();
		for (ModelItem modelItem : model.getModelItems())
		{
			Map<String, Object> config = new HashMap<String, Object>();
			Map relatedParameters = JSONObject.parseObject(getRelatedParameters(modelItem));
			if (relatedParameters == null)
				continue;
			config.put("bundle", modelItem.getModelItemEnglishName());
			config.put("title", modelItem.getModelItemName());
			config.put("dataType", modelItem.getDatatype());
			config.putAll(relatedParameters);
			configs.add(config);
		}
		modelItemsMap.put("configs", configs);
		return Generator.generate(path + "createModelHtml.vm", modelItemsMap);
	}
	
	public String getRelatedParameters(ModelItem modelItem)
	{
		if (modelItem.getRelatedParameters() == null)
		{
			System.out.println(modelItem.getModelItemName() + "模型项相关参数字段值为空");
			return "";
		} else
		{
			return modelItem.getRelatedParameters();
		}
	}
	
	@Override
	public EntitySet<DynamicForm> findAllDynamicForm()
	{
		try
		{
			return this.dynamicFormDao.findAllDynamicForm();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAllDynamicForm", e.getMessage(), e);
		}
	}
	
	public DynamicForm findDynamicFormByKey(String dynamicFormKey)
	{
		try
		{
			return this.dynamicFormDao.findDynamicFormByKey(dynamicFormKey);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveDynamicForm", e.getMessage(), e);
		}
	}
	
	/**
	 * 根据模板替换动态表单内容
	 * 
	 * @param dynamicForm
	 * @return
	 */
	private HashMap<String, String> replaceDynamicFormTemplate(DynamicForm dynamicForm)
	{
		String dynamicFormNewHtml = dynamicForm.getDynamicFormNewHtml();
		if (dynamicFormNewHtml == null || "".equals(dynamicFormNewHtml))
		{
			System.out.println(dynamicForm.getDynamicFormName()+"NewHtml值为空");
			return null;
		}
		
		Map dynamicFormNewHtmlMap = JSON.parseObject(dynamicFormNewHtml);
		dynamicFormNewHtml = dynamicFormNewHtmlMap.get("parse").toString();
		List<Map> configs = (List<Map>) dynamicFormNewHtmlMap.get("datas");
		dynamicForm.setTotalCount(configs.size());
		
		dynamicFormNewHtml = createNewHtml(dynamicForm.getDynamicFormKey(), configs);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("dynamicFormNewHtml", dynamicFormNewHtml);
		return map;
	}
	
	/**
	 * 创建用于添加或者编辑内容的表单
	 * 
	 * @param html
	 * @param configs
	 * @param layoutStyle
	 * @param path
	 * @return
	 */
	public String createNewHtml(String formKey, List<Map> configs)
	{
		for (Map config : configs)
		{
			if("否".equals(config.get("isEdit")))
			{
				config.put("isDisplay", "true");
			}
			Map dataResource = new HashMap();
			dataResource.put("dataResource", config);
			if("choose".equals(config.get("holderType"))){
				String[] dataSourceArry = config.get("dataSource").toString().replaceAll("[\"()\\[\\]]", "").split("\n|,|，");
				config.put("dataSource", dataSourceArry);
			}
			String itemHtml = Generator.generate(path + config.get("type") + ".vm", dataResource);
			config.put("itemHtml", itemHtml);
		}
		Map<String, Object> replaceMap = new HashMap<String, Object>();
		replaceMap.put("configs", configs);
		return Generator.generate(path + "createNewHtml.vm", replaceMap);
	}
	
	
	/**
	 * 创建动态表单
	 * 
	 * @param dynamicFormData
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void creatForm(Map<String, Object> dynamicFormData)
	{
		try
		{
			
			Map<String, Object> columnInfo = (Map<String, Object>) dynamicFormData.get("columnInfo");
			List<Map<String, Object>> configs = (List<Map<String, Object>>) columnInfo.get("configs");
			
			String oldHtml = this.createOldHtml(configs);// 创建用于设计表单的Html
			String newHtml = this.createNewHtml(configs);// 创建用于添加或编辑的表单
			Map<String, Object> formInfo = (Map<String, Object>) dynamicFormData.get("formInfo");
			
			DynamicForm findDynamicForm = dynamicFormDao.findDynamicFormByKey((String) formInfo.get("dynamicFormKey"));
			if (findDynamicForm == null)
			{
				DynamicForm dynamicForm = creatDynamicFormEntity(formInfo, oldHtml, newHtml);
				this.dynamicFormDao.saveDynamicForm(dynamicForm);
			} 
			else
			{
				findDynamicForm.setDynamicFormNewHtml(newHtml);
				findDynamicForm.setDynamicFormOldHtml(oldHtml);
				dynamicFormDao.updateDynamicForm(findDynamicForm);
			}
			
			//创建查看表单
			creatDisplayForm(formInfo, configs);
			
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("creatForm", e.getMessage(), e);
		}
	}
	
	public void creatDisplayForm(Map<String, Object> formInfo,List<Map<String, Object>> configs)
	{
		try
		{
			String oldHtml = this.createDisPlayFormOldHtml(configs);
			String displayHtml = this.createDisplayFormNewHtml(configs);// 创建查看表单数据的表单
			String displayFormKey = formInfo.get("dynamicFormKey")+"_display";
			formInfo.put("dynamicFormKey", displayFormKey);
			DynamicForm findDynamicForm = dynamicFormDao.findDynamicFormByKey(displayFormKey);
			if (findDynamicForm == null)
			{
				DynamicForm disDynamicForm = creatDynamicFormEntity(formInfo, oldHtml, displayHtml);
				this.dynamicFormDao.saveDynamicForm(disDynamicForm);
			}else
			{
				findDynamicForm.setDynamicFormNewHtml(displayHtml);
				findDynamicForm.setDynamicFormOldHtml(oldHtml);
				dynamicFormDao.updateDynamicForm(findDynamicForm);
			}
			
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("creatDisplayForm", e.getMessage(), e);
		}
	}
	
	/**
	 * 创建用于设计表单的Html
	 * 
	 * @param configs
	 * @param path
	 * @return
	 */
	private String createOldHtml(List<Map<String, Object>> configs)
	{
		for (Map<String, Object> config : configs)
		{
			config.put("isEdit", "是");
		}
		
		Map configMap = new HashMap();
		configMap.put("configs", configs);
		return Generator.generate(path + "creatOldHtml.vm", configMap);
	}
	
	private String createDisPlayFormOldHtml(List<Map<String, Object>> configs)
	{
		for (Map<String, Object> config : configs)
		{
			config.put("isEdit", "否");
		}
		
		Map configMap = new HashMap();
		configMap.put("configs", configs);
		return Generator.generate(path + "creatOldHtml.vm", configMap);
	}
	
	/**
	 * 创建用于添加或编辑的表单
	 * 
	 * @param configs
	 * @param path
	 * @return
	 */
	public String createNewHtml(List<Map<String, Object>> configs)
	{
		try
		{
			for (Map<String, Object> config : configs)
			{
				Map<String, Object> dataResource = new HashMap<String, Object>();
				dataResource.put("dataResource", config);
				String itemHtml = Generator.generate(path + config.get("type") + ".vm", dataResource);
				config.put("itemHtml", itemHtml);
			}
			Map<String, Object> replaceMap = new HashMap<String, Object>();
			replaceMap.put("configs", configs);
			return Generator.generate(path + "createNewHtml.vm", replaceMap);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("creatForm", e.getMessage(), e);
		}
	}
	
	/**
	 * 创建用于查看数据的表单
	 * 
	 * @param configs
	 * @param path
	 * @return
	 */
	public String createDisplayFormNewHtml(List<Map<String, Object>> configs)
	{
		try
		{
			for (Map<String, Object> config : configs)
			{
				if("否".equals(config.get("isEdit")))
				{
					config.put("isDisplay", "true");
				}
				Map<String, Object> dataResource = new HashMap<String, Object>();
				dataResource.put("dataResource", config);
				String itemHtml = Generator.generate(path + config.get("type") + ".vm", dataResource);
				config.put("itemHtml", itemHtml);
			}
			Map<String, Object> replaceMap = new HashMap<String, Object>();
			replaceMap.put("configs", configs);
			return Generator.generate(path + "createNewHtml.vm", replaceMap);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("creatForm", e.getMessage(), e);
		}
	}
	
	/**
	 * 创建动态表单实体数据
	 * 
	 * @param formInfo
	 * @param oldHtml
	 * @param newHtml
	 * @param displayHtml
	 * @return
	 */
	public DynamicForm creatDynamicFormEntity(Map<String, Object> formInfo, String oldHtml, String newHtml)
	{
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setDynamicFormKey((String) formInfo.get("dynamicFormKey"));
		dynamicForm.setDynamicFormName((String) formInfo.get("dynamicFormName"));
		dynamicForm.setTotalCount((Integer) formInfo.get("totalCount"));
		dynamicForm.setDynamicFormNewHtml(newHtml);
		dynamicForm.setDynamicFormOldHtml(oldHtml);
		return dynamicForm;
	}
}
