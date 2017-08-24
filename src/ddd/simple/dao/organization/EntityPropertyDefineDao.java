package ddd.simple.dao.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.EntityPropertyDefine;

public interface EntityPropertyDefineDao extends BaseDaoInterface
{
	public EntityPropertyDefine saveEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) throws Exception;
	
	public int deleteEntityPropertyDefine(Long entityPropertyDefineId) throws Exception;
	
	public EntityPropertyDefine updateEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) throws Exception;
	
	public EntityPropertyDefine findEntityPropertyDefineById(Long entityPropertyDefineId) throws Exception;
	
	public EntitySet<EntityPropertyDefine> findAllEntityPropertyDefine() throws Exception;
	public EntitySet<EntityPropertyDefine>findByType(String type) throws Exception;
}
