package ddd.base.persistence;

import java.util.HashMap;
import java.util.Map;

import sun.misc.SoftCache;

import ddd.base.persistence.baseEntity.Entity;

public class EntityCache {

	Map<String,SoftCache> entityCaches = new HashMap<String, SoftCache>();
	public void put(String entityName,Long id,Entity entity)
	{
		SoftCache entityCache = this.getEntityCache(entityName);
		if(entityCache == null)
		{
			entityCache = new SoftCache();
			this.entityCaches.put(entityName, entityCache);
		}
		entityCache.put(id, entity);
		entityCaches.put(entityName, entityCache);
		
	}
	public Entity get(String entityName,Long id)
	{
		SoftCache entityCache = this.getEntityCache(entityName);
		if(entityCache == null)
		{
			return null;
		}
		return (Entity)this.getEntityCache(entityName).get(id);
	}
	private SoftCache getEntityCache(String entityName)
	{
		SoftCache entityCache = this.entityCaches.get(entityName);
		return entityCache;
	}
	/**
	 * 	清除所有缓存
	 */
	public void clear(){
		entityCaches.clear();
	}
	/**
	 * 清除某个类型的实体缓存
	 * @param entityName
	 * @return
	 */
	public void clearByEntityName(String entityName){
		if(this.entityCaches.get(entityName)!=null){
			this.entityCaches.get(entityName).clear();
		}
	}
	/**
	 * 清除某个类型的实体缓存
	 * @param entityName
	 * @return 返回清除的实体
	 */
	public <T extends Entity> void clearByEntityName(Class<T> clazz){
		String entityName = SessionFactory.getEntityClass(clazz).getEntityInfo().getName();
		if(this.entityCaches.get(entityName)!=null){
			this.entityCaches.get(entityName).clear();
		}
	}
	/**
	 * 清除某个实体的缓存
	 * @param entityName
	 * @param id
	 * @return 返回清除的实体
	 */
	public Entity clearByEntityId(String entityName,Long id){
		Entity result = this.get(entityName, id);
		if(result != null){
			this.getEntityCache(entityName).remove(this.getEntityCache(entityName).get(id));
		}
		
		return result;
	}
	/**
	 * 清除某个实体的缓存
	 * @param clazz
	 * @param id
	 * @return 返回清除的实体
	 */
	public <T extends Entity> Entity clearByEntityId(Class<T> clazz,Long id){
		String entityName = SessionFactory.getEntityClass(clazz).getEntityInfo().getName();
		return clearByEntityId(entityName, id);
	}
	
	
	/**
	 * 清除某个实体的缓存
	 * 
	 * @return 返回清除的实体
	 */
	public <T extends Entity> Entity clearByEntity(T entity){
		String entityName = SessionFactory.getEntityClass(entity.getClass()).getEntityInfo().getName();
		return clearByEntityId(entityName, entity.getEId());
	}
}
