package ddd.simple.dao.model.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.model.ModelItem;
import ddd.simple.dao.model.ModelItemDao;

@Service
public class ModelItemDaoBean extends BaseDao implements ModelItemDao
{
	@Override
	public ModelItem saveModelItem(ModelItem modelItem)  throws Exception{
		return this.save(modelItem);
	}

	@Override
	public int deleteModelItem(Long modelItemId)  throws Exception{
		return this.deleteById(modelItemId,ModelItem.class);
	}

	@Override
	public ModelItem updateModelItem(ModelItem modelItem)  throws Exception{
		return this.update(modelItem);
	}

	@Override
	public ModelItem findModelItemById(Long modelItemId)  throws Exception{
		return this.query(modelItemId, ModelItem.class);
	}
	
	@Override
	public EntitySet<ModelItem> findAllModelItem() throws Exception {
		return this.query("",ModelItem.class);
	}

	@Override
	public EntitySet<ModelItem> findModelItemsByModelId(Long modelId)
			throws Exception {
		
		return this.query(" modelId='"+modelId+"'", ModelItem.class);
	}

}
