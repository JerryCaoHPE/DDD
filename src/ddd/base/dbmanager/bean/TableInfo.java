package ddd.base.dbmanager.bean;

import java.util.HashMap;
import java.util.Map;

import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class TableInfo {
	
	private String tableName;
	
	private EntityClass<? extends Entity> entityClass;
	
	private Map<String, TableColumnInfo> columns = new HashMap<String, TableColumnInfo>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public EntityClass<? extends Entity> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(EntityClass<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	public Map<String, TableColumnInfo> getColumns() {
		return columns;
	}
	
	public TableColumnInfo getTableColumnInfo(String columnName) {
		return columns.get(columnName);
	}
	
	public void addTableColumnInfo(TableColumnInfo tableColumnInfo) {
		this.columns.put(tableColumnInfo.getColumnName(), tableColumnInfo);
	}
	
	
	
}
