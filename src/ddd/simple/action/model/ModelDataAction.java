package ddd.simple.action.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.Model;
import ddd.simple.service.model.ModelDataService;
import ddd.simple.service.model.ModelService;
@Action
@RequestMapping("/ModelData")
@Controller
public class ModelDataAction
{
	@Resource(name="modelDataServiceBean")
	private ModelDataService modelDataService;
	
	@Resource(name="modelServiceBean")
	private ModelService modelService;
	
	public Map<String,Object> saveModelData(String tableName,HashMap modelData)
	{
		try {
			return this.modelDataService.saveModelData(tableName,modelData);
		} catch (DDDException e) {
			
			throw e;
		}
	}
	
	public Map<String,Object> updateModelData(String tableName, Map<String, Object> modelData)
	{
		try {
			return this.modelDataService.updateModelData(tableName,modelData);
		} catch (DDDException e) {
			
			throw e;
		}
	}
	
	public int deleteModelData(String tableName,Long modelDataId){
		
		try {
			return this.modelDataService.deleteModelData(tableName,modelDataId);
		} catch (DDDException e) {
			throw e;
		}
		
	}
	
	public Map<String,Object> findModelDataByContentId(String tableName,Long contentId)
	{
		try 
		{
			 return this.modelDataService.findModelDataByContentId(tableName,contentId);
		} 
		catch (DDDException e) {
			throw e;
		}
	}
	
	public Map<String,Object>findSubtableInfo(String tableName)
	{
		try 
		{
			 return this.modelDataService.findSubtableInfo(tableName);
		} 
		catch (DDDException e) {
			throw e;
		}
	}

	public Set<Map<String, Object>> findModelDataByTableName(String tableName){
		try{
			Set<Map<String, Object>> allModelData = this.modelDataService.findModelDataByTableName(tableName);
			return allModelData;
		} catch (DDDException e) {
			throw e;
		}
	}
	

	public String submitModelData(String tableName,HashMap modelData,String operate)
	{
		try
		{
			return this.modelDataService.submitModelData(tableName, modelData);
		} 
		catch (DDDException e) 
		{
			throw e;
		}
	}
	
}