package ddd.base.persistence;

import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public interface ExtInterface {
	
	public EntityClass<? extends Entity> getEntityClass(String tableName);

}
