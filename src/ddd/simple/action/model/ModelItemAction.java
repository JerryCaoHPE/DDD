package ddd.simple.action.model;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.service.model.ModelItemService;

@Action
@RequestMapping("/ModelItem")
@Controller
public class ModelItemAction
{
	@Resource(name="modelItemServiceBean")
	private ModelItemService modelItemService;
	
	public ModelItem saveModelItem(ModelItem modelItem)
	{
		try {
			ModelItem saveModelItem = this.modelItemService.saveModelItem(modelItem);
			return saveModelItem;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteModelItem(Long modelItemId){
		
		try {
			return this.modelItemService.deleteModelItem(modelItemId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ModelItem updateModelItem(ModelItem modelItem) {
		try {
			ModelItem updateModelItem = this.modelItemService.updateModelItem(modelItem);
			return updateModelItem;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ModelItem findModelItemById(Long modelItemId){
		try {
			ModelItem findModelItem = this.modelItemService.findModelItemById(modelItemId);
			return  findModelItem;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ModelItem> findAllModelItem(){
		try{
			EntitySet<ModelItem> allModelItem = this.modelItemService.findAllModelItem();
			return allModelItem;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ModelItem> findModelItemsByModelId(Long modelId){
		try{
			EntitySet<ModelItem> modelItems =  this.modelItemService.findModelItemsByModelId(modelId);
			for(ModelItem item : modelItems){
				item.setModel(null);
			}
			return modelItems;
		}catch(DDDException e){
			throw e;
		}
	}
	
}