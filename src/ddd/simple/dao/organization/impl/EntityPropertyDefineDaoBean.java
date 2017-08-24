package ddd.simple.dao.organization.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.dao.organization.EntityPropertyDefineDao;

@Service
public class EntityPropertyDefineDaoBean extends BaseDao implements EntityPropertyDefineDao
{
	@Override
	public EntityPropertyDefine saveEntityPropertyDefine(EntityPropertyDefine propertiesDefine)  throws Exception{
		return this.save(propertiesDefine);
	}

	@Override
	public int deleteEntityPropertyDefine(Long entityPropertyDefineId)  throws Exception{
		return this.deleteById(entityPropertyDefineId,EntityPropertyDefine.class);
	}

	@Override
	public EntityPropertyDefine updateEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine)  throws Exception{
		return this.update(entityPropertyDefine);
	}

	@Override
	public EntityPropertyDefine findEntityPropertyDefineById(Long entityPropertyDefineId)  throws Exception{
		return this.query(entityPropertyDefineId, EntityPropertyDefine.class);
	}
	
	@Override
	public EntitySet<EntityPropertyDefine> findAllEntityPropertyDefine() throws Exception {
		return this.query("",EntityPropertyDefine.class);
	}
	@Override
	public EntitySet<EntityPropertyDefine> findByType(String type) throws Exception {
		String where = " entity = '"+type+"' ";
		return this.query(where,EntityPropertyDefine.class);
	}
}
