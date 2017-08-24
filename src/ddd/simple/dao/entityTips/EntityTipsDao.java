package ddd.simple.dao.entityTips;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.entityTips.EntityTips;

public interface EntityTipsDao extends BaseDaoInterface
{
	public EntityTips saveEntityTips(EntityTips entityTips) throws Exception;
	
	public int deleteEntityTips(Long entityTipsId) throws Exception;
	
	public EntityTips updateEntityTips(EntityTips entityTips) throws Exception;
	
	public EntityTips findEntityTipsById(Long entityTipsId) throws Exception;
	
	public EntitySet<EntityTips> findEntityTipsByName(String name) throws Exception;
	
	public EntitySet<EntityTips> findAllEntityTips() throws Exception;
}
