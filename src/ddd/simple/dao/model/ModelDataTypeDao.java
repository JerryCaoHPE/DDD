package ddd.simple.dao.model;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.model.ModelDataType;

public interface ModelDataTypeDao extends BaseDaoInterface
{
	public ModelDataType saveModelDataType(ModelDataType modelDataType) throws Exception;
	
	public int deleteModelDataType(Long modelDataTypeId) throws Exception;
	
	public ModelDataType updateModelDataType(ModelDataType modelDataType) throws Exception;
	
	public ModelDataType findModelDataTypeById(Long modelDataTypeId) throws Exception;
	
	public EntitySet<ModelDataType> findAllModelDataType() throws Exception;
}
