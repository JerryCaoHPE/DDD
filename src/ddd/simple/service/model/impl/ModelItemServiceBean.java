package ddd.simple.service.model.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelItem;
import ddd.simple.dao.model.ModelItemDao;
import ddd.simple.service.model.ModelItemService;

@Service
public class ModelItemServiceBean   implements ModelItemService
{

	@Resource(name="modelItemDaoBean")
	private ModelItemDao modelItemDao;
	
	@Override
	public ModelItem saveModelItem(ModelItem modelItem) 
	{
		try {
			return this.modelItemDao.saveModelItem(modelItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveModelItem", e.getMessage(), e);
		}
	}

	@Override
	public int deleteModelItem(Long modelItemId) {
		try {
			return this.modelItemDao.deleteModelItem(modelItemId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModelItem", e.getMessage(), e);
		}
		
	}

	@Override
	public ModelItem updateModelItem(ModelItem modelItem) {
		try {
			return this.modelItemDao.updateModelItem(modelItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateModelItem", e.getMessage(), e);
		}
	}

	@Override
	public ModelItem findModelItemById(Long modelItemId) {
		try {
			return this.modelItemDao.findModelItemById(modelItemId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelItemById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ModelItem> findAllModelItem() {
		try{
			return this.modelItemDao.findAllModelItem();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllModelItem", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ModelItem> findModelItemsByModelId(Long modelId){
		try{
			return this.modelItemDao.findModelItemsByModelId(modelId);
		}catch (Exception e){
			e.printStackTrace();
			throw new DDDException("findModelItemByModelId", e.getMessage(), e);
		}
	}

}
