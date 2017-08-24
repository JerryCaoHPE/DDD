package ddd.simple.service.entityTips;

import java.util.List;
import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.entityTips.EntityTips;
import ddd.simple.entity.organization.EntityPropertyDefine;

public interface EntityTipsService extends BaseServiceInterface
{
	public EntityTips saveEntityTips(EntityTips entityTips) ;
	
	public int deleteEntityTips(Long entityTipsId) ;
	
	public EntityTips updateEntityTips(EntityTips entityTips) ;
	
	public EntityTips findEntityTipsById(Long entityTipsId) ;
	
	public EntitySet<EntityTips> findAllEntityTips() ;
	
	public EntitySet<EntityTips> findEntityTipsByName(String name);
 
	public List<Map<String,String>> getAllField(String entityClassStr);
}