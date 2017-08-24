package ddd.simple.service.organization;

import java.util.List;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.organization.EntityProperty;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.service.base.BaseServiceInterface;

public interface EntityPropertyService extends BaseServiceInterface
{
	public EntityProperty saveEntityProperty(EntityProperty entityProperty) ;
	
	public int deleteEntityProperty(Long entityPropertyId) ;
	
	public EntityProperty updateEntityProperty(EntityProperty entityProperty) ;
	
	public EntityProperty findEntityPropertyById(Long entityPropertyId) ;
	
	public EntitySet<EntityProperty> findAllEntityProperty() ;
	public EntitySet<EntityProperty> findEntityPropertyByIdandType(Long id,String type);
	
	
	public EntitySet<EntityProperty> saveEntityProperties(EntitySet<EntityPropertyDefine> EntityProperties,String entity,Long entityId);
	public EntityProperty updataEntityProperty(String where,Object newValue);
	public int deleteEntityProperties(String uniqueInfo);
 
}