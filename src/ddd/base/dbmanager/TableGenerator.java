package ddd.base.dbmanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.base.annotation.ColumnInfo;
import ddd.base.dbmanager.bean.TableColumnInfo;
import ddd.base.dbmanager.bean.TableInfo;
import ddd.base.dbmanager.tableHandler.TableHandler;
import ddd.base.dbmanager.tableHandler.mysql.MysqlTableHandler;
import ddd.base.dbmanager.tableHandler.oracle.OracleTableHandler;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class TableGenerator {
	
	public static void generator(TableHandler tableHander,Collection<EntityClass<? extends Entity>> entityClasses) throws Exception{
		//TableHander tableHander  = new OracleTableHander();
		
		//TableHander tableHander  = new MysqlTableHander();
		
		List<String> errors = new ArrayList<String>();
		
		List<EntityClass<? extends Entity>> entityClassesToCreate = new ArrayList<EntityClass<? extends Entity>>(); 
		
		List<EntityClass<? extends Entity>> entityClassesToModify = new ArrayList<EntityClass<? extends Entity>>(); 
		
		Map<String, String> createTableSqls = new HashMap<String, String>();
		
		Map<String, String> middleTableSqls = new HashMap<String, String>();
		
		Map<String, String> newColumnSqls = new HashMap<String, String>();
		
		List<String> modifySqls = new ArrayList<String>();
		
		Map<String, String> fKSqls = new HashMap<String, String>();
		
		Map<String, String> uniqueSqls = new HashMap<String, String>();
		
		Set<String> dbTables = tableHander.getDbTables();
		
		for (EntityClass<? extends Entity> entityClass : entityClasses) {
			if(entityClass.getEntityInfo().getName().length()>27){
				errors.add("实体："+entityClass.getClassName()+"对应的表名"+entityClass.getEntityInfo().getName()+" 大于27个字符，请控制在27个字符以内");
			}
			
			Map<String, ColumnInfo> one2ManyColumnInfos = entityClass.getOne2ManyFieldColumnInfos();
			
			for (ColumnInfo one2ManyColumnInfo : one2ManyColumnInfos.values()) {
				String joinColumn = one2ManyColumnInfo.getJoinColumn();
				Class<?> clazz=one2ManyColumnInfo.getClazz();
				EntityClass<?> manySideEntityClass = SessionFactory.getEntityClass(clazz);
				
				//如果是组合关系，则需要在多的一边，加上一的一端的引用
				if(one2ManyColumnInfo.isComposition())
				{
					ColumnInfo oneSideColumnInfo = manySideEntityClass.getColumnInfo(joinColumn);
					//如果多的一端不存在一的一端的引用，增加一个
					if(oneSideColumnInfo==null){
						errors.add("组合关系中多端实体:"+manySideEntityClass.getClassName()+" 必须有一端实体:"+entityClass.getClassName()+" 类型的属性");
						/*EntityInfo entityInfo=manySideEntityClass.getEntityInfo();
						String newColumnSql = tableHander.addNewColumnSql(entityInfo.getName(), joinColumn, manySideEntityClass.getIdField().getType().getName(), one2ManyColumnInfo);
						newColumnSqls.put((entityInfo.getName()+"_"+joinColumn).toLowerCase(), newColumnSql);
						
						String manySideTableName = manySideEntityClass.getEntityInfo().getName();
						String oneSideTableName = entityClass.getEntityInfo().getName();
						
						String fkSql = tableHander.createFKConstraintSql(joinColumn, manySideTableName, oneSideTableName,manySideTableName+"_fk_"+oneSideTableName);
						fKSqls.put((manySideTableName+"_fk_"+oneSideTableName).toLowerCase(), fkSql);*/
					}
				}
				else
				{
					//如果是一对多的关系，并且是聚合关系，则需要建立中间表
					if(ignoreCaseContains(dbTables, one2ManyColumnInfo.getJoinTableName())){
						continue;
					}
					if(one2ManyColumnInfo.getJoinTableName().length()>25){
						errors.add("中间表名："+one2ManyColumnInfo.getJoinTableName()+" 大于25个字符，请控制在25个字符以内");
					}
					String middleTableSql = tableHander.createMiddleTable(one2ManyColumnInfo);
					middleTableSqls.put(one2ManyColumnInfo.getJoinTableName(), middleTableSql);
					
					String manySideTableName = manySideEntityClass.getEntityInfo().getName();
					String oneSideTableName = entityClass.getEntityInfo().getName();
					
					String oneSidefkSql = tableHander.createFKConstraintSql(one2ManyColumnInfo.getJoinTableOneSide(),one2ManyColumnInfo.getJoinTableName(), oneSideTableName,one2ManyColumnInfo.getJoinTableName()+"_fk_1");
					String manySidefkSql = tableHander.createFKConstraintSql(one2ManyColumnInfo.getJoinTableManySide(),one2ManyColumnInfo.getJoinTableName(), manySideTableName,one2ManyColumnInfo.getJoinTableName()+"_fk_n");
					fKSqls.put((one2ManyColumnInfo.getJoinTableName()+"_fk_1").toLowerCase(), oneSidefkSql);
					fKSqls.put((one2ManyColumnInfo.getJoinTableName()+"_fk_n").toLowerCase(), manySidefkSql);
				}
			}
			
			Map<String, ColumnInfo> one2OneColumnInfos = entityClass.getOne2OneFieldColumnInfos();
			for (ColumnInfo one2OneColumnInfo : one2OneColumnInfos.values()) {
				Class<?> clazz=one2OneColumnInfo.getClazz();
				EntityClass<?> oneSideEntityClass = SessionFactory.getEntityClass(clazz);
				String manySideTableName = entityClass.getEntityInfo().getName();
				String oneSideTableName = oneSideEntityClass.getEntityInfo().getName();
				if("".endsWith(one2OneColumnInfo.getFKName())){
					one2OneColumnInfo.setFKName(manySideTableName+"_fk_"+oneSideTableName);
				}
				if(one2OneColumnInfo.getFKName().length()>=30){
					errors.add("实体："+entityClass.getClassName()+" 的："+one2OneColumnInfo.getField().getName()+" 属性的外键:"+one2OneColumnInfo.getFKName()+" 长度大于30个字符，请手动配置注解中的FKName");
				}
				String fkSql = tableHander.createFKConstraintSql(one2OneColumnInfo.getName(), manySideTableName, oneSideTableName,one2OneColumnInfo.getFKName());
				fKSqls.put((one2OneColumnInfo.getFKName()).toLowerCase(), fkSql);
			}
			
			if(ignoreCaseContains(dbTables, entityClass.getEntityInfo().getName())){
				entityClassesToModify.add(entityClass);
			}else{
				entityClassesToCreate.add(entityClass);
				String sql=tableHander.createTable(entityClass);
				createTableSqls.put(entityClass.getEntityInfo().getName(), sql);
				createUniqueKey(tableHander, uniqueSqls,entityClass);
			}
		}
		
		Map<String, TableInfo> dbTableInfos = tableHander.getDBTableInfo(entityClassesToModify);
		for (TableInfo tableInfo : dbTableInfos.values()) {
			EntityClass<? extends Entity> entityClass = tableInfo.getEntityClass();
			Map<String, TableColumnInfo> dbColumns = tableInfo.getColumns();
			String tableName = entityClass.getEntityInfo().getName();
			
			Map<String, ColumnInfo> columnInfos =  entityClass.getColumnInfos();
			//删除表中已经有的字段
			for (TableColumnInfo tableColumnInfo : dbColumns.values()) {
				String newColumn = (tableName+"_"+tableColumnInfo.getColumnName()).toLowerCase();
				newColumnSqls.remove(newColumn);
			}
			
			for (ColumnInfo columnInfo : columnInfos.values()) {
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
					continue;
				}
				String columnName = columnInfo.getName();
				TableColumnInfo tableColumnInfo = dbColumns.get(columnName.toLowerCase());
				
				if(tableColumnInfo==null){
					
					String newColumnSql = tableHander.addNewColumnSql(tableName, columnName,columnInfo.getClazz().getName() , columnInfo);
					newColumnSqls.put(tableName+"_"+columnName, newColumnSql);
					
					if(columnInfo.isUnique()){
						if("".equals(columnInfo.getUniqueName())){
							columnInfo.setUniqueName( tableName+"_u_"+columnName);
						}
						String uniqueName = columnInfo.getUniqueName();
						if(uniqueName.length()>=30){
							errors.add("实体："+entityClass.getClassName()+" 的："+columnInfo.getField().getName()+" 属性的唯一键名:"+uniqueName+" 长度大于30个字符，请手动配置注解中的uniqueName");
						}
						String uniqueSql = tableHander.createUniqueConstraintSql(tableName, columnName, uniqueName);
						uniqueSqls.put(uniqueName, uniqueSql);
					}
				}else{
					try {
						tableHander.modify(tableName,columnInfo,tableColumnInfo,modifySqls);
					} catch (Exception e) {
						System.err.println("tableName:"+tableName
								+" column:"+columnInfo.getName()
								+" clazz:"+columnInfo.getClazz()
								+" dbType:"+tableColumnInfo.getType());
					}
				}
			}
		}
		
		//删除数据库中已经有的外键
		Set<String> dbFKNames = tableHander.getDBFKNames();
		for (String fkName : dbFKNames) {
			if(fKSqls.get(fkName.toLowerCase())!=null){
				fKSqls.remove(fkName.toLowerCase());
			}
		}
		if(errors.size()>0){
			for (String error : errors) {
				System.err.println(error);
			}
			throw new Exception("请处理以上错误！");
		}
		
		executeSqls(createTableSqls.values());
		
		executeSqls(middleTableSqls.values());
		
		executeSqls(newColumnSqls.values());
		
		executeSqls(modifySqls);
		
		executeSqls(fKSqls.values());
		
		executeSqls(uniqueSqls.values());
	}
	
	public static void executeSqls(Collection<String> sqls){
		if(sqls==null || sqls.size()==0){
			return;
		}
		
		Connection connection = SessionFactory.getConnection();
		String sql = null;
		try {
			connection.setAutoCommit(false);
			
			Statement statement = connection.createStatement();
			
			for (String s : sqls) {
				sql = s;
				statement.executeUpdate(sql);
			}
			connection.commit();
		} catch (SQLException e) {
			System.out.println("执行出错:"+sql);
			e.printStackTrace();
		} finally{
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SessionFactory.closeConn(connection);
		}
		
	}
	
	private static void createUniqueKey(TableHandler tableHander,Map<String, String> uniqueSqls, EntityClass<? extends Entity> entityClass){
		Map<String, ColumnInfo> columnInfos =  entityClass.getColumnInfos();
		for (ColumnInfo columnInfo : columnInfos.values()) {
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY||columnInfo.isId()){
				continue;
			}
			if(columnInfo.isUnique()){
				String tableName = entityClass.getEntityInfo().getName();
				String columnName = columnInfo.getName();
				String uniqueSql = tableHander.createUniqueConstraintSql(tableName, columnName, tableName+"_u_"+columnName);
				uniqueSqls.put(tableName+"_u_"+columnName, uniqueSql);
			}
			
		}
	}
	
	
	private static boolean ignoreCaseContains(Collection<String> strs,String str){
		for (String string : strs) {
			if(string.equalsIgnoreCase(str)){
				return true;
			}
		}
		return false;
	}
}
