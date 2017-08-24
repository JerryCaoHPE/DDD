package ddd.simple.dao.organization;

import java.util.List;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.EntityProperty;
import ddd.simple.entity.organization.EntityPropertyDefine;

public interface EntityPropertyDao extends BaseDaoInterface
{
	public EntityProperty saveEntityProperty(EntityProperty entityProperty) throws Exception;
	
	public int deleteEntityProperty(Long entityPropertyId) throws Exception;
	
	public EntityProperty updateEntityProperty(EntityProperty entityProperty) throws Exception;
	
	public EntityProperty findEntityPropertyById(Long entityPropertyId) throws Exception;
	
	public EntitySet<EntityProperty> findAllEntityProperty() throws Exception;
	public EntitySet<EntityProperty> findEntityPropertyByIdandType(Long id,String type) throws Exception;
	
	
	public EntitySet<EntityProperty> saveEntityProperties(EntitySet<EntityPropertyDefine> entityProperties,String entity,Long entityId)throws Exception;
	public EntityProperty updataEntityProperty(String where,Object newValue)throws Exception;
}
