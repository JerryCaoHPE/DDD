package ddd.simple.action.model;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.ModelType;
import ddd.simple.service.model.ModelTypeService;

@Action
@RequestMapping("/ModelType")
@Controller
public class ModelTypeAction
{
	@Resource(name="modelTypeServiceBean")
	private ModelTypeService modelTypeService;
	
	public ModelType saveModelType(ModelType modelType)
	{
		try {
			ModelType saveModelType = this.modelTypeService.saveModelType(modelType);
			return saveModelType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteModelType(Long modelTypeId){
		
		try {
			return this.modelTypeService.deleteModelType(modelTypeId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ModelType updateModelType(ModelType modelType) {
		try {
			ModelType updateModelType = this.modelTypeService.updateModelType(modelType);
			return updateModelType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ModelType findModelTypeById(Long modelTypeId){
		try {
			ModelType findModelType = this.modelTypeService.findModelTypeById(modelTypeId);
			return  findModelType;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ModelType> findAllModelType(){
		try{
			EntitySet<ModelType> allModelType = this.modelTypeService.findAllModelType();
			return allModelType;
		} catch (DDDException e) {
			throw e;
		}
	}

}