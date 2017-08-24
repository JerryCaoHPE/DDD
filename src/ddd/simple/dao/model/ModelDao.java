package ddd.simple.dao.model;

import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;

public interface ModelDao extends BaseDaoInterface
{
	public Model saveModel(Model model) throws Exception;
	
	public int deleteModel(Long modelId) throws Exception;
	
	public Model updateModel(Model model) throws Exception;
	
	public Model findModelById(Long modelId) throws Exception;
	
	public Model findModelByModelForm(String dynamicFormKey) throws Exception;
	
	public EntitySet<Model> findAllModel() throws Exception;

	public Model findModelByEnglishName(String englishName)  throws Exception;
	
	public Set<Map<String,Object>> getWorkflowProcess() throws Exception;
	
	public void updateModelState(Long modelId,String state) throws Exception;
	
	public EntitySet<Model> findModelByParentId(Long EId) throws Exception;
	
}
