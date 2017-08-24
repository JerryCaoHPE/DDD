package ddd.simple.service.model;

import ddd.base.persistence.EntitySet;
 
import ddd.simple.entity.model.ModelType;

public interface ModelTypeService  
{
	public ModelType saveModelType(ModelType modelType) ;
	
	public int deleteModelType(Long modelTypeId) ;
	
	public ModelType updateModelType(ModelType modelType) ;
	
	public ModelType findModelTypeById(Long modelTypeId) ;
	
	public EntitySet<ModelType> findAllModelType() ;
 
}