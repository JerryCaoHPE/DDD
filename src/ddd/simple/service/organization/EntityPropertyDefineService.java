package ddd.simple.service.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.organization.EntityPropertyDefine;

public interface EntityPropertyDefineService extends BaseServiceInterface
{
	public EntitySet<EntityPropertyDefine> saveEntityPropertyDefine(EntitySet<EntityPropertyDefine> propertiesDefine) ;
	
	public int deleteEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) ;
	
	public EntityPropertyDefine updateEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) ;
	
	public EntityPropertyDefine findEntityPropertyDefineById(Long entityPropertyDefineId) ;
	
	public EntitySet<EntityPropertyDefine> findAllEntityPropertyDefine() ;
	
	
	public EntitySet<EntityPropertyDefine> findEntityPropertyDefineByType(String type);
 
}