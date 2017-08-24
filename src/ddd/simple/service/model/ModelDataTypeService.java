package ddd.simple.service.model;

import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelDataType;

public interface ModelDataTypeService  
{
	public ModelDataType saveModelDataType(ModelDataType modelDataType) ;
	
	public int deleteModelDataType(Long modelDataTypeId) ;
	
	public ModelDataType updateModelDataType(ModelDataType modelDataType) ;
	
	public ModelDataType findModelDataTypeById(Long modelDataTypeId) ;
	
	public EntitySet<ModelDataType> findAllModelDataType() ;
 
}