package ddd.simple.service.model.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelType;
import ddd.simple.dao.model.ModelTypeDao;
import ddd.simple.service.model.ModelTypeService;

@Service
public class ModelTypeServiceBean   implements ModelTypeService
{

	@Resource(name="modelTypeDaoBean")
	private ModelTypeDao modelTypeDao;
	
	@Override
	public ModelType saveModelType(ModelType modelType) 
	{
		try {
			return this.modelTypeDao.saveModelType(modelType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveModelType", e.getMessage(), e);
		}
	}

	@Override
	public int deleteModelType(Long modelTypeId) {
		try {
			return this.modelTypeDao.deleteModelType(modelTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModelType", e.getMessage(), e);
		}
		
	}

	@Override
	public ModelType updateModelType(ModelType modelType) {
		try {
			return this.modelTypeDao.updateModelType(modelType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateModelType", e.getMessage(), e);
		}
	}

	@Override
	public ModelType findModelTypeById(Long modelTypeId) {
		try {
			ModelType modelType =  this.modelTypeDao.findModelTypeById(modelTypeId);
			if(modelType.getParent()!=null){
				modelType.getParent().getTypeName();
			}
			if(modelType.getParent() != null){
				modelType.getParent().getTypeName();
			}			
			return modelType;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelTypeById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ModelType> findAllModelType() {
		try{
			return this.modelTypeDao.findAllModelType();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllModelType", e.getMessage(), e);
		}
	}

}
