package ddd.simple.dao.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.model.ModelDao;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.dao.base.BaseDao;
@Service
public class ModelDaoBean extends BaseDao implements ModelDao
{
	@Override
	public Model saveModel(Model model)  throws Exception{
		return this.save(model);
	}

	@Override
	public int deleteModel(Long modelId)  throws Exception{
		return this.deleteById(modelId,Model.class);
	}

	@Override
	public Model updateModel(Model model)  throws Exception{
		return this.update(model);
	}

	@Override
	public Model findModelById(Long modelId)  throws Exception{
		return this.query(modelId, Model.class);
	}
	
	public Model findModelByModelForm(String dynamicFormKey) throws Exception{
		String where = "modelForm = '"+dynamicFormKey+"'";
		return this.queryOne(where, Model.class);
	}
	
	@Override
	public EntitySet<Model> findAllModel() throws Exception {
		return this.query("1=1",Model.class);
	}
	
	@Override
	public Model findModelByEnglishName(String englishName)  throws Exception
	{
		String where = "modelEnglishName = '"+englishName+"'";
		return this.queryOne(where, Model.class);
	}
	
	public Set<Map<String,Object>> getWorkflowProcess() throws Exception
	{
		String where = "select NAME_,ID_,CREATE_TIME_ from act_re_model";
		return this.query(where);
	}
	
	public void updateModelState(Long modelId,String state) throws Exception
	{
		Map map = new HashMap();
		map.put("state",state);
		this.update("model", map, "EId="+modelId);
	}
	
	@Override
	public EntitySet<Model> findModelByParentId(Long EId) throws Exception
	{
		
		return this.query("parentId = '" + EId + "'", Model.class);
	}
	
}
