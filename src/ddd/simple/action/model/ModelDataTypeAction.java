package ddd.simple.action.model;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.model.ModelDataType;
import ddd.simple.service.model.ModelDataTypeService;

@Action
@RequestMapping("/ModelDataType")
@Controller
public class ModelDataTypeAction
{
	@Resource(name="modelDataTypeServiceBean")
	private ModelDataTypeService modelDataTypeService;
	
	public ModelDataType saveModelDataType(ModelDataType modelDataType)
	{
		try {
			ModelDataType saveModelDataType = this.modelDataTypeService.saveModelDataType(modelDataType);
			return saveModelDataType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteModelDataType(Long modelDataTypeId){
		
		try {
			return this.modelDataTypeService.deleteModelDataType(modelDataTypeId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ModelDataType updateModelDataType(ModelDataType modelDataType) {
		try {
			ModelDataType updateModelDataType = this.modelDataTypeService.updateModelDataType(modelDataType);
			return updateModelDataType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ModelDataType findModelDataTypeById(Long modelDataTypeId){
		try {
			ModelDataType findModelDataType = this.modelDataTypeService.findModelDataTypeById(modelDataTypeId);
			return  findModelDataType;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ModelDataType> findAllModelDataType(){
		try{
			EntitySet<ModelDataType> allModelDataType = this.modelDataTypeService.findAllModelDataType();
			return allModelDataType;
		} catch (DDDException e) {
			throw e;
		}
	}

}