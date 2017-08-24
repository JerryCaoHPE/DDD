package ddd.simple.dao.entityTips.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.entityTips.EntityTips;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.dao.entityTips.EntityTipsDao;

@Service
public class EntityTipsDaoBean extends BaseDao implements EntityTipsDao
{
	@Override
	public EntityTips saveEntityTips(EntityTips entityTips)  throws Exception{
		return this.save(entityTips);
	}

	@Override
	public int deleteEntityTips(Long entityTipsId)  throws Exception{
		return this.deleteById(entityTipsId,EntityTips.class);
	}

	@Override
	public EntityTips updateEntityTips(EntityTips entityTips)  throws Exception{
		return this.update(entityTips);
	}

	@Override
	public EntityTips findEntityTipsById(Long entityTipsId)  throws Exception{
		return this.query(entityTipsId, EntityTips.class);
	}
	
	@Override
	public EntitySet<EntityTips> findAllEntityTips() throws Exception {
		return this.query("1=1",EntityTips.class);
	}

	@Override
	public EntitySet<EntityTips> findEntityTipsByName(String name) throws Exception {
		String where = " EntityName = '"+name+"' ";
		
		return this.query(where,EntityTips.class);
	}
}
