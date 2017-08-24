package ddd.simple.dao.model;

import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.Model;
import ddd.simple.dao.base.BaseDaoInterface;

public interface ModelDataDao extends BaseDaoInterface
{
	public int saveModelData(Model model,String tableName,Map<String,Object> map) throws Exception;
	
	public int updateModelData(Model model,Map<String,Object> map) throws Exception;
	
	public int deleteModelData(String tableName,Long modelDataId) throws Exception;
	
	public Set<Map<String, Object>> findModelDataByTableName(String tableName) throws Exception;

	public Map<String, Object> findModelDataByContentId(String tableName,Long contentId,String[] properties) throws Exception;
	
	/**
	 * 加载   外键数据
	 * @param model
	 * @param modelDataMap
	 * @param modelItemEnglishName 需要加载的模型项英文名
	 * @throws Exception
	 */
	public void loadForeignKey(Model model,Map<String,Object> modelDataMap,String modelItemEnglishName) throws Exception;
	/**
	 * 加载所有   外键数据
	 * @param model
	 * @param modelDataMap
	 * @throws Exception
	 */
	public void loadForeignKey(Model model,Map<String,Object> modelDataMap) throws Exception;
	
	/**
	 * 加载   子表数据
	 * @param model
	 * @param modelDataMap
	 * @param modelItemEnglishName 需要加载的模型项英文名
	 * @throws Exception 
	 */
	public void loadSubTable(Model model,Map<String,Object> modelDataMap,String modelItemEnglishName) throws Exception;
	/**
	 * 加载所有的   子表数据
	 * @param model
	 * @param modelDataMap
	 * @throws Exception
	 */
	public void loadSubTable(Model model,Map<String,Object> modelDataMap) throws Exception;
	/**
	* 加载所有复选框数据
	* @param model
	* @param modelDataMap
	* @param modelItemEnglishName
	* @throws Exception 
	 */
	public void loadCheckBox(Model model,Map<String,Object> modelDataMap) throws Exception;
	
	
}
