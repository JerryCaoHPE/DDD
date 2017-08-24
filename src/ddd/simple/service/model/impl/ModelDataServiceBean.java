package ddd.simple.service.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.MD5Util;
import ddd.simple.dao.model.ModelDataDao;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.service.model.ModelDataService;
import ddd.simple.service.model.ModelItemService;
import ddd.simple.service.model.ModelService;
import ddd.simple.service.model.extend.ModelDataExtInterface;

@Service
public class ModelDataServiceBean implements ModelDataService
{
	
	@Resource(name = "modelDataDaoBean")
	private ModelDataDao modelDataDao;
	
	@Resource(name = "modelServiceBean")
	private ModelService modelService;
	
	@Resource(name = "modelItemServiceBean")
	private ModelItemService modelItemService;
	
	private ModelDataExtInterface modelDataExtend;
	
	public ModelDataExtInterface getModelDataExtend()
	{
		return modelDataExtend;
	}
	
	public void setModelDataExtend(ModelDataExtInterface modelDataExtend)
	{
		this.modelDataExtend = modelDataExtend;
	}
	
	@Override
	public Map<String,Object> saveModelData(String modelName, Map<String, Object> map)
	{
		try
		{
			Model model = modelService.findModelByEnglishName(modelName);
			if (model == null)
			{
				throw new DDDException("不存在模型:" + modelName);
			}
			
//			map.put("EId", SessionFactory.getNewPrimarykey(model.getModelEnglishName()));
			this.modelDataDao.saveModelData(model, modelName, map);
			return this.findModelDataByContentId(modelName, Long.parseLong(map.get("EId").toString()));
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveModelData", e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String,Object> updateModelData(String modelName, Map<String, Object> map)
	{
		try
		{
			Model model = modelService.findModelByEnglishName(modelName);
			if (model == null)
			{
				throw new DDDException("不存在模型:" + modelName);
			}
			this.modelDataDao.updateModelData(model, map);
			return this.findModelDataByContentId(modelName, Long.parseLong(map.get("EId").toString()));
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateModelData", e.getMessage(), e);
		}
	}
	
	@Override
	public int deleteModelData(String modelName,Long modelDataId) {
		try {
			return this.modelDataDao.deleteModelData(modelName,modelDataId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModelData", e.getMessage(), e);
		}
		
	}
	
	
	@Override
	public Set<Map<String, Object>> findModelDataByTableName(String tableName)
	{
		try
		{
			return this.modelDataDao.findModelDataByTableName(tableName);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findModelDataByTableName", e.getMessage(), e);
		}
	}
	
	public String submitModelData(String modelName, Map<String, Object> modelData)
	{
		try
		{
			// 通用操作
			Model model = this.modelService.findModelByEnglishName(modelName);
			if (model == null)
			{
				throw new DDDException("不存在模型:" + modelName);
			}
			EntitySet<ModelItem> modelItems = this.modelItemService.findModelItemsByModelId(model.getEId());
			
			modelData.put("status", "已提交");
			if (modelData.containsKey("EId"))
			{
				this.modelDataDao.updateModelData(model, modelData);
			} else
			{
				modelData.put("EId", SessionFactory.getNewPrimarykey(model.getModelEnglishName()));
				this.modelDataDao.saveModelData(model, modelName, modelData);
			}
			Map<String, Object> processVar = this.getProcessVariable(modelItems, modelData);
			// 扩展
			if (modelDataExtend != null)
			{
				return modelDataExtend.submitModelData(processVar, model, modelData);
			}
			return "";
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("submitModelData", e.getMessage(), e);
		}
	}
	
	public Map<String, Object> findModelDataByContentId(String modelName, Long contentId)
	{
		try
		{
			String[] properties = null;
			EntityClass clazz = SessionFactory.getEntityClass(modelName);
			if (clazz != null)
			{
				properties = clazz.getProperties();
			}
			Map<String, Object> result = this.modelDataDao.findModelDataByContentId(modelName, contentId, properties);
			Model model = this.modelService.findModelByEnglishName(modelName);
			if (model == null)
			{
				throw new DDDException("不存在模型:" + modelName);
			}
			this.modelDataDao.loadForeignKey(model, result);
			this.modelDataDao.loadSubTable(model, result);
			this.modelDataDao.loadCheckBox(model, result);
			
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findModelDataByContentId", e.getMessage(), e);
		}
	}
	
	public Map<String, Object> findSubtableInfo(String tableName)
	{
		try
		{
			Model model = this.modelService.findModelByEnglishName(tableName);
			if (model == null)
			{
				throw new DDDException("不存在模型:" + tableName);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			this.modelDataDao.loadSubTable(model, result);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findSubtableInfo", e.getMessage(), e);
		}
	}
	
	public boolean checkIsChanged(String tableName, Map<String, Object> modelData)
	{
		
		MD5Util md5Util = new MD5Util();
		Map<String, Object> content = this.findModelDataByContentId(tableName, Long.valueOf(modelData.get("EId").toString()));
		
		Set<String> keys = modelData.keySet();
		for (String key : keys)
		{
			String newValue = modelData.get(key).toString();
			String oldValue = content.get(key).toString();
			if (!md5Util.getMD5(newValue).equals(md5Util.getMD5(oldValue)))
			{
				return false;
			}
		}
		return true;
		
	}
	
	public Map<String, Object> getProcessVariable(EntitySet<ModelItem> modelItems, Map<String, Object> modelData)
	{
		try
		{
			Map<String, Object> processVar = new HashMap<String, Object>();
			
			for (ModelItem modelItem : modelItems)
			{
				if (modelItem.getIsProcess() != null && modelItem.getIsProcess().equals("是"))
				{
					String key = modelItem.getModelItemEnglishName();
					processVar.put(key, modelData.get(key));
				}
			}
			return processVar;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getProcessVariable", e.getMessage(), e);
		}
	}
	
	
}
