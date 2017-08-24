package ddd.simple.service.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;

public interface ModelDataService  
{
	public Map<String,Object> saveModelData(String modelName,Map<String,Object> map);
	
	public Map<String,Object> updateModelData(String modelName,Map<String,Object> map) ;
	
	public int deleteModelData(String modelName,Long modelDataId);
	
	public Set<Map<String, Object>> findModelDataByTableName(String tableName) ;
	
	public String submitModelData(String modelName,Map<String,Object> modelData);

	public Map<String, Object> findModelDataByContentId(String modelName, Long contentId);
	
	public Map<String, Object> findSubtableInfo(String tableName);
	
	public Map<String, Object> getProcessVariable(EntitySet<ModelItem> modelItems, Map<String, Object> modelData);
}