package ddd.simple.service.model;

import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelItem;

public interface ModelItemService  
{
	public ModelItem saveModelItem(ModelItem modelItem) ;
	
	public int deleteModelItem(Long modelItemId) ;
	
	public ModelItem updateModelItem(ModelItem modelItem) ;
	
	public ModelItem findModelItemById(Long modelItemId) ;
	
	public EntitySet<ModelItem> findAllModelItem() ;
	
	public EntitySet<ModelItem> findModelItemsByModelId(Long modelId);
 
}