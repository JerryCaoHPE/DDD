package ddd.base.dbmanager.bean;

import ddd.base.annotation.ColumnInfo;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class MiddleTable {
	
	ColumnInfo columnInfo;
	EntityClass<? extends Entity> entityClass;
	EntityClass<?> manySideEntityClass;

}
