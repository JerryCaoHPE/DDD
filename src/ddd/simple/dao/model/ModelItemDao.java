package ddd.simple.dao.model;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.model.ModelItem;

public interface ModelItemDao extends BaseDaoInterface
{
	public ModelItem saveModelItem(ModelItem modelItem) throws Exception;
	
	public int deleteModelItem(Long modelItemId) throws Exception;
	
	public ModelItem updateModelItem(ModelItem modelItem) throws Exception;
	
	public ModelItem findModelItemById(Long modelItemId) throws Exception;
	
	public EntitySet<ModelItem> findAllModelItem() throws Exception;
	
	public EntitySet<ModelItem> findModelItemsByModelId(Long modelId) throws Exception;
}
