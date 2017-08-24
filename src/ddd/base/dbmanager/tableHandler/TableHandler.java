package ddd.base.dbmanager.tableHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.base.annotation.ColumnInfo;
import ddd.base.dbmanager.bean.TableColumnInfo;
import ddd.base.dbmanager.bean.TableInfo;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public interface TableHandler {
	public abstract Set<String> getDbTables();
	
	public abstract Set<String> getDBFKNames();
	
	public abstract Map<String, TableInfo> getDBTableInfo(List<EntityClass<? extends Entity>> entityClassesToModify);
	
	public abstract String createTable(EntityClass<? extends Entity> entityClass);
	
	public abstract String createMiddleTable(ColumnInfo columnInfo);
	
	public abstract String addNewColumnSql(String tableName,String columnName,String javaType,ColumnInfo columnInfo);
	
	public abstract String createFKConstraintSql(String manySideColumnName,String manySideTableName,String oneSideTableName,String contraintName);

	public abstract String createUniqueConstraintSql(String tableName,String columnName,String contraintName);

	public abstract void modify(String tableName, ColumnInfo columnInfo, TableColumnInfo tableColumnInfo,List<String> modifySqls);

	public abstract String renameTableName(String oldName,String newName);

	public abstract String renameColumnName(String tableName,String oldName,String newName,String javaType,Integer size);
	//不添加约束
	public abstract String addColumn(String tableName,String columnName,String javaType,Integer size);
	//不添加约束
	public abstract String modifyColumn(String tableName,String columnName,String javaType,Integer size);


}
