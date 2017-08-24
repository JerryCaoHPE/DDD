package ddd.base.dbmanager.tableHandler.mysql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import ddd.base.annotation.ColumnInfo;
import ddd.base.dbmanager.bean.TableColumnInfo;
import ddd.base.dbmanager.bean.TableInfo;
import ddd.base.dbmanager.bean.TypeKeeper;
import ddd.base.dbmanager.tableHandler.TableHandler;
import ddd.base.dbmanager.util.TypeGetter;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.SpringContextUtil;
import ddd.simple.service.codeTable.CodeTypeService;
import ddd.simple.service.systemConfig.SystemConfigService;

public class MysqlTableHandler implements TableHandler {
	private int varcharMaxLength=255;
	
	
	@Override
	public Set<String> getDbTables() {
		Set<String> dbTables = new HashSet<String>();
		String sql = "SHOW TABLES";
		Connection connection = SessionFactory.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				dbTables.add(rs.getString(1));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			SessionFactory.closeConn(connection);
		}
		return dbTables;
	}

	@Override
	public Set<String> getDBFKNames() {
		Set<String> dbFKNames = new HashSet<String>();
		String sql = "SELECT t.constraint_name FROM information_schema.TABLE_CONSTRAINTS t "
				+ "WHERE t.constraint_schema=(select database()) AND t.constraint_type='FOREIGN KEY'";
		Connection connection = SessionFactory.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				dbFKNames.add(rs.getString("constraint_name"));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			SessionFactory.closeConn(connection);
		}
		
		return dbFKNames;
	}

	@Override
	public Map<String, TableInfo> getDBTableInfo(List<EntityClass<? extends Entity>> entityClassesToModify) {
		Map<String, TableInfo> result = new HashMap<String, TableInfo>();
		Connection connection = SessionFactory.getConnection();
		try {
			Statement statement = connection.createStatement();
			for (EntityClass<? extends Entity> entityClass : entityClassesToModify) {
				TableInfo tableInfo = new TableInfo();
				tableInfo.setEntityClass(entityClass);
				tableInfo.setTableName(entityClass.getEntityInfo().getName());
				String sql = "SELECT c.TABLE_NAME,c.COLUMN_NAME,c.DATA_TYPE,c.CHARACTER_MAXIMUM_LENGTH,c.NUMERIC_PRECISION,c.NUMERIC_SCALE,c.IS_NULLABLE,l.constraint_name,l.constraint_type "
						+ "FROM information_schema.`COLUMNS` c LEFT JOIN "
						+ "(SELECT k.TABLE_NAME,k.COLUMN_NAME,k.constraint_name ,t.constraint_type "
						+ "FROM information_schema.KEY_COLUMN_USAGE k LEFT JOIN "
						+ "(SELECT * FROM information_schema.TABLE_CONSTRAINTS "
						+ "WHERE table_schema=(select database())) t "
						+ "ON t.table_schema=k.table_schema AND t.table_name=k.table_name AND k.constraint_name=t.constraint_name "
						+ "WHERE k.table_schema=(select database()) AND t.constraint_type ='UNIQUE') l "
						+ "ON c.TABLE_NAME=l.TABLE_NAME and c.COLUMN_NAME= l.COLUMN_NAME "
						+ "WHERE c.table_schema=(select database()) AND c.table_name='"+entityClass.getEntityInfo().getName()+"'";
			
				
				ResultSet rs = statement.executeQuery(sql);
				while(rs.next()){
					TableColumnInfo tableColumnInfo = new TableColumnInfo();
					tableColumnInfo.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
					tableColumnInfo.setType(rs.getString("DATA_TYPE"));
					tableColumnInfo.setLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
					tableColumnInfo.setPrecision(rs.getInt("NUMERIC_PRECISION"));
					tableColumnInfo.setScale(rs.getInt("NUMERIC_SCALE"));
					if("NO".equals(rs.getString("IS_NULLABLE"))){
						tableColumnInfo.setNullable(false);
					}
					tableColumnInfo.setConstraintName(rs.getString("constraint_name"));
					if("UNIQUE".equals(rs.getString("constraint_type"))){
						tableColumnInfo.setUnique(true);
					}
					tableInfo.addTableColumnInfo(tableColumnInfo);
				}
				rs.close();
				result.put(tableInfo.getTableName(), tableInfo);
			}
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			SessionFactory.closeConn(connection);
		}
		
		return result;
	}

	@Override
	public String createTable(EntityClass<? extends Entity> entityClass) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE `" + entityClass.getEntityInfo().getName()+ "` (\n");
		
		Map<String, ColumnInfo> columnInfos =  entityClass.getColumnInfos();
		for (ColumnInfo columnInfo : columnInfos.values()) {
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
				continue;
			}
			TypeKeeper typeKeeper = TypeGetter.getByJavaType(columnInfo.getClazz().getName());
			
			if(columnInfo.isId()){
				buffer.append("`"+columnInfo.getName() + "` "+typeKeeper.getMysqlType()+" NOT NULL,\n");
			}else{
				if (columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					typeKeeper  = TypeGetter.getByJavaType("java.lang.Long");
				}
				buffer.append("`"+columnInfo.getName()+"`");
				if (!columnInfo.getColumnDefinition().equals(""))
				{
					buffer.append(columnInfo.getColumnDefinition()+",\n");
					continue;
				}
				if(columnInfo.getClazz().equals(String.class)&&columnInfo.getLength()>varcharMaxLength){
					buffer.append(" LONGTEXT");
				}else{
					buffer.append(" "+typeKeeper.getMysqlType());
				}
				if(columnInfo.getClazz().equals(String.class)){
					if(columnInfo.getLength()<=varcharMaxLength){
						buffer.append("("+columnInfo.getLength()+")");
					}
				}else if(columnInfo.getClazz().equals(BigDecimal.class)){
					buffer.append("("+columnInfo.getPrecision()+","+columnInfo.getScale()+")");
				}else if(columnInfo.getClazz().equals(Double.class)||columnInfo.getClazz().equals(Float.class)){
					if(columnInfo.getPrecision()!=0){
						buffer.append("("+columnInfo.getPrecision()+","+columnInfo.getScale()+")");
					}
				}
				if(!columnInfo.isNullable()){
					buffer.append(" NOT NULL");
				}
				if(!columnInfo.getComment().equals("")){
					buffer.append(" COMMENT '"+columnInfo.getComment()+"'");
				}
				buffer.append(" ,\n");
			}
			
		}
		buffer.append("PRIMARY KEY (`EId`)\n");
		buffer.append(")");
		
		return buffer.toString();
	}

	@Override
	public String createMiddleTable(ColumnInfo columnInfo) {
		String joinTableName=columnInfo.getJoinTableName();
		String joinTableOneSide=columnInfo.getJoinTableOneSide();
		String joinTableManySide=columnInfo.getJoinTableManySide();
		StringBuffer sb = new StringBuffer();
		TypeKeeper typeKeeper = TypeGetter.getByJavaType("java.lang.Long");
		String type=typeKeeper.getMysqlType();
		
		sb.append("CREATE TABLE `" +joinTableName+ "` (\n");
		sb.append("`"+joinTableOneSide +"` "+type+",\n");
		sb.append("`"+joinTableManySide +"` "+type+",\n");
		sb.append("`displayIndex` "+type+",\n");
		sb.append("primary key(`"+joinTableOneSide+"`,`"+joinTableManySide+"`)\n");
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String addNewColumnSql(String tableName, String columnName,String javaType, ColumnInfo columnInfo) {
		String sql = null;
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(javaType);
		if (columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
		{
			typeKeeper  = TypeGetter.getByJavaType("java.lang.Long");
		}
		String type = typeKeeper.getMysqlType();
		if(columnInfo.getClazz().equals(String.class)){
			type = type + "("+columnInfo.getLength()+")";
		}
		sql="alter table `"+tableName+"` add `"+columnName+"` "+type;
		if(!columnInfo.isNullable()){
			sql +=" not null";
		}else{
			sql +=" null";
		}
		return sql;
	}

	@Override
	public String createFKConstraintSql(String manySideColumnName,String manySideTableName, String oneSideTableName,String contraintName) {
		String fkSql="alter table `"+manySideTableName+"` add constraint `"+contraintName+"` foreign key(`"+manySideColumnName+"`)"+" references `"+oneSideTableName+"`(`EId`)";
		return fkSql;
	}

	@Override
	public String createUniqueConstraintSql(String tableName,String columnName, String contraintName) {
		String sql ="alter table `"+tableName+"` add unique index `"+contraintName+"` (`"+columnName+"`)";
		return sql;
	}

	@Override
	public void modify(String tableName, ColumnInfo columnInfo,TableColumnInfo tableColumnInfo, List<String> modifySqls) {
		
		String dbType = tableColumnInfo.getType();
		
		if(dbType.equalsIgnoreCase("VARCHAR")){
			dbType+="("+tableColumnInfo.getLength()+")";
		}else if(dbType.equalsIgnoreCase("TINYINT")){
			dbType+="(1)";
			tableColumnInfo.setType(dbType);
		}else if(dbType.equalsIgnoreCase("DECIMAL")){
			dbType+="("+tableColumnInfo.getPrecision()+","+tableColumnInfo.getScale()+")";
		}else if(dbType.equalsIgnoreCase("DOUBLE")||dbType.equalsIgnoreCase("FLOAT")){
			if((tableColumnInfo.getPrecision()!=22&&dbType.equalsIgnoreCase("DOUBLE")) || 
					(tableColumnInfo.getPrecision()!=12&&dbType.equalsIgnoreCase("FLOAT"))){
				dbType+="("+tableColumnInfo.getPrecision()+","+tableColumnInfo.getScale()+")";
			}
		}
		
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(columnInfo.getClazz().getName());
		if (columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
		{
			typeKeeper  = TypeGetter.getByJavaType("java.lang.Long");
		}
		String type = typeKeeper.getMysqlType();
		/*if(TypeGetter.getByMysqlType(tableColumnInfo.getType()).getJavaType().equalsIgnoreCase(columnInfo.getClazz().getName())){
			type = tableColumnInfo.getType();
		}*/
		if(columnInfo.getClazz().equals(String.class)){
			if(columnInfo.getLength()>varcharMaxLength){
				type = "LONGTEXT";
			}else{
				type = type + "("+columnInfo.getLength()+")";
			}
		}else if(columnInfo.getClazz().equals(BigDecimal.class)){
			type = type + "("+columnInfo.getPrecision()+","+columnInfo.getScale()+")";
		}else if(columnInfo.getClazz().equals(Double.class)||columnInfo.getClazz().equals(Float.class)){
			if(columnInfo.getPrecision()!=0){
				if((columnInfo.getPrecision()!=22&&columnInfo.getClazz().equals(Double.class)) || 
						(columnInfo.getPrecision()!=12&&columnInfo.getClazz().equals(Float.class))){
					type = type + "("+columnInfo.getPrecision()+","+columnInfo.getScale()+")";
				}
			}
		}
		
		String modifyColumn=null;
		if(!type.equalsIgnoreCase(dbType)){
			modifyColumn = "alter table `"+tableName+"` modify column `"+columnInfo.getName()+"` "+type;
		}
		if(columnInfo.isNullable()!=tableColumnInfo.isNullable()){
			if(modifyColumn==null){
				modifyColumn = "alter table `"+tableName+"` modify column `"+columnInfo.getName()+"` "+dbType;
			}
		}
		if(modifyColumn!=null){
			if(!columnInfo.isNullable()){
				modifyColumn +=" not null";
			}else{
				modifyColumn +=" null";
			}
			modifySqls.add(modifyColumn);
		}
		if(columnInfo.isUnique()!=tableColumnInfo.isUnique() && !columnInfo.isId()){
			String modifyUnique = null;
			if(tableColumnInfo.isUnique()){
				modifyUnique="alter table `"+tableName+"` drop index `"+tableColumnInfo.getConstraintName()+"`";
			}else{
				String uniqueName = columnInfo.getUniqueName();
				if("".equals(uniqueName)){
					uniqueName = tableName+"_u_"+columnInfo.getName();
				}
				modifyUnique="alter table `"+tableName+"` add unique index `"+uniqueName+"` (`"+columnInfo.getName()+"`)";
			}
			modifySqls.add(modifyUnique);
		}
	}
	@Override
	public String renameTableName(String oldName, String newName) {
		return "ALTER  TABLE "+oldName+" RENAME TO "+newName;
	}

	@Override
	public String renameColumnName(String tableName,String oldName, String newName,String javaType,Integer size) {
		//alter table log  change remark2 remark VARCHAR(20);
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(javaType);
		StringBuffer addSql = new StringBuffer("alter table ");
		addSql.append(tableName);
		addSql.append(" change ");
		addSql.append(oldName);
		addSql.append(" ");
		addSql.append(newName);
		addSql.append(" ");
		if(javaType.equalsIgnoreCase("java.lang.String")){
			if(size<=varcharMaxLength){
				addSql.append("VARCHAR");
				addSql.append("("+size+")");
			}else{
				addSql.append("LONGTEXT");
			}
		}else{
			addSql.append(typeKeeper.getMysqlType());
		}
		return addSql.toString();
	}

	@Override
	public String addColumn(String tableName, String columnName, String javaType,Integer size) {
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(javaType);
		StringBuffer addSql = new StringBuffer("alter table ");
		addSql.append(tableName);
		addSql.append(" add ");
		addSql.append(columnName);
		addSql.append(" ");
		if(javaType.equalsIgnoreCase("java.lang.String")){
			if(size<=varcharMaxLength){
				addSql.append("VARCHAR");
				addSql.append("("+size+")");
			}else{
				addSql.append("LONGTEXT");
			}
		}else{
			addSql.append(typeKeeper.getMysqlType());
		}
		return addSql.toString();
	}

	@Override
	public String modifyColumn(String tableName, String columnName,String javaType, Integer size) {
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(javaType);
		StringBuffer addSql = new StringBuffer("alter table ");
		addSql.append(tableName);
		addSql.append(" modify column ");
		addSql.append(columnName);
		addSql.append(" ");
		if(javaType.equalsIgnoreCase("java.lang.String")){
			if(size<=varcharMaxLength){
				addSql.append("VARCHAR");
				addSql.append("("+size+")");
			}else{
				addSql.append("LONGTEXT");
			}
		}else{
			addSql.append(typeKeeper.getMysqlType());
		}
		return addSql.toString();
	}

}
