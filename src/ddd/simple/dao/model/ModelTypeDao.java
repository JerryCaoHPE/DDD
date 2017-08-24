package ddd.simple.dao.model;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.model.ModelType;

public interface ModelTypeDao extends BaseDaoInterface
{
	public ModelType saveModelType(ModelType modelType) throws Exception;
	
	public int deleteModelType(Long modelTypeId) throws Exception;
	
	public ModelType updateModelType(ModelType modelType) throws Exception;
	
	public ModelType findModelTypeById(Long modelTypeId) throws Exception;
	
	public EntitySet<ModelType> findAllModelType() throws Exception;
}
