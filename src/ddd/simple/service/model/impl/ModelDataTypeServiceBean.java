package ddd.simple.service.model.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelDataType;
import ddd.simple.dao.model.ModelDataTypeDao;
import ddd.simple.service.model.ModelDataTypeService;

@Service
public class ModelDataTypeServiceBean   implements ModelDataTypeService
{

	@Resource(name="modelDataTypeDaoBean")
	private ModelDataTypeDao modelDataTypeDao;
	
	@Override
	public ModelDataType saveModelDataType(ModelDataType modelDataType) 
	{
		try {
			return this.modelDataTypeDao.saveModelDataType(modelDataType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveModelDataType", e.getMessage(), e);
		}
	}

	@Override
	public int deleteModelDataType(Long modelDataTypeId) {
		try {
			return this.modelDataTypeDao.deleteModelDataType(modelDataTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModelDataType", e.getMessage(), e);
		}
		
	}

	@Override
	public ModelDataType updateModelDataType(ModelDataType modelDataType) {
		try {
			return this.modelDataTypeDao.updateModelDataType(modelDataType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateModelDataType", e.getMessage(), e);
		}
	}

	@Override
	public ModelDataType findModelDataTypeById(Long modelDataTypeId) {
		try {
			return this.modelDataTypeDao.findModelDataTypeById(modelDataTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelDataTypeById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ModelDataType> findAllModelDataType() {
		try{
			return this.modelDataTypeDao.findAllModelDataType();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllModelDataType", e.getMessage(), e);
		}
	}

}
