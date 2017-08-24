package ddd.simple.dao.model.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.model.ModelDataType;
import ddd.simple.dao.model.ModelDataTypeDao;

@Service
public class ModelDataTypeDaoBean extends BaseDao implements ModelDataTypeDao
{
	@Override
	public ModelDataType saveModelDataType(ModelDataType modelDataType)  throws Exception{
		return this.save(modelDataType);
	}

	@Override
	public int deleteModelDataType(Long modelDataTypeId)  throws Exception{
		return this.deleteById(modelDataTypeId,ModelDataType.class);
	}

	@Override
	public ModelDataType updateModelDataType(ModelDataType modelDataType)  throws Exception{
		return this.update(modelDataType);
	}

	@Override
	public ModelDataType findModelDataTypeById(Long modelDataTypeId)  throws Exception{
		return this.query(modelDataTypeId, ModelDataType.class);
	}
	
	@Override
	public EntitySet<ModelDataType> findAllModelDataType() throws Exception {
		return this.query("1=1",ModelDataType.class);
	}
}
