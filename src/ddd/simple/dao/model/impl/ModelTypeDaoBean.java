package ddd.simple.dao.model.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.model.ModelType;
import ddd.simple.dao.model.ModelTypeDao;

@Service
public class ModelTypeDaoBean extends BaseDao implements ModelTypeDao
{
	@Override
	public ModelType saveModelType(ModelType modelType)  throws Exception{
		return this.save(modelType);
	}

	@Override
	public int deleteModelType(Long modelTypeId)  throws Exception{
		return this.deleteById(modelTypeId,ModelType.class);
	}

	@Override
	public ModelType updateModelType(ModelType modelType)  throws Exception{
		return this.update(modelType);
	}

	@Override
	public ModelType findModelTypeById(Long modelTypeId)  throws Exception{
		return this.query(modelTypeId, ModelType.class);
	}
	
	@Override
	public EntitySet<ModelType> findAllModelType() throws Exception {
		return this.query("",ModelType.class);
	}
}
