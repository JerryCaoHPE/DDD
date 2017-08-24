package ddd.base.dbmanager.tableHandler.oracle;

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

import ddd.base.annotation.ColumnInfo;
import ddd.base.dbmanager.bean.TableColumnInfo;
import ddd.base.dbmanager.bean.TableInfo;
import ddd.base.dbmanager.bean.TypeKeeper;
import ddd.base.dbmanager.tableHandler.TableHandler;
import ddd.base.dbmanager.util.TypeGetter;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class OracleTableHandler implements TableHandler {
	private int varcharMaxLength=255;
	@Override
	public Set<String> getDbTables() {
		Set<String> dbTables = new HashSet<String>();
		String sql = "SELECT t.TABLE_NAME tableName  FROM user_tables t";
		Connection connection = SessionFactory.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				dbTables.add(rs.getString("tableName"));
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
		String sql = "select au.constraint_name from user_constraints au where au.constraint_type ='R'";
		
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
	public Map<String, TableInfo> getDBTableInfo(List<EntityClass<? extends Entity>> entityClassesToModify){
		Map<String, TableInfo> result = new HashMap<String, TableInfo>();
		Connection connection = SessionFactory.getConnection();
		try {
			Statement statement = connection.createStatement();
			for (EntityClass<? extends Entity> entityClass : entityClassesToModify) {
				TableInfo tableInfo = new TableInfo();
				tableInfo.setEntityClass(entityClass);
				tableInfo.setTableName(entityClass.getEntityInfo().getName());
				String sql = "select c.TABLE_NAME,c.COLUMN_NAME,c.DATA_TYPE,c.DATA_LENGTH,c.DATA_PRECISION,c.DATA_SCALE,c.NULLABLE ,l.constraint_name,l.constraint_type "
						+ "from USER_TAB_COLS c LEFT JOIN "
						+ "(select cu.TABLE_NAME,cu.COLUMN_NAME,au.constraint_name,au.constraint_type "
						+ "from user_cons_columns cu, user_constraints au "
						+ "where cu.constraint_name = au.constraint_name and au.constraint_type ='U') l "
						+ "ON  c.TABLE_NAME=l.TABLE_NAME and c.COLUMN_NAME= l.COLUMN_NAME "
						+ "WHERE c.TABLE_NAME='"+entityClass.getEntityInfo().getName().toUpperCase()+"'";
			
				
				ResultSet rs = statement.executeQuery(sql);
				while(rs.next()){
					TableColumnInfo tableColumnInfo = new TableColumnInfo();
					tableColumnInfo.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
					tableColumnInfo.setType(rs.getString("DATA_TYPE"));
					tableColumnInfo.setLength(rs.getLong("DATA_LENGTH"));
					tableColumnInfo.setPrecision(rs.getInt("DATA_PRECISION"));
					tableColumnInfo.setScale(rs.getInt("DATA_SCALE"));
					if("N".equals(rs.getString("NULLABLE"))){
						tableColumnInfo.setNullable(false);
					}
					tableColumnInfo.setConstraintName(rs.getString("constraint_name"));
					if("U".equals(rs.getString("constraint_type"))){
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
		buffer.append("CREATE TABLE " + entityClass.getEntityInfo().getName()+ " (\n");
		
		Map<String, ColumnInfo> columnInfos =  entityClass.getColumnInfos();
		for (ColumnInfo columnInfo : columnInfos.values()) {
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
				continue;
			}
			TypeKeeper typeKeeper = TypeGetter.getByJavaType(columnInfo.getClazz().getName());
			
			if(columnInfo.isId()){
				buffer.append(columnInfo.getName() + " "+typeKeeper.getOracleType()+" NOT NULL,\n");
			}else{
				if (columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					typeKeeper  = TypeGetter.getByJavaType("java.lang.Long");
				}
				buffer.append(columnInfo.getName());
				if (!columnInfo.getColumnDefinition().equals(""))
				{
					buffer.append(columnInfo.getColumnDefinition()+",\n");
					continue;
				}
				if(columnInfo.getClazz().equals(String.class)&&columnInfo.getLength()>varcharMaxLength){
					buffer.append(" CLOB");
				}else{
					buffer.append(" "+typeKeeper.getOracleType());
				}
				if(columnInfo.getClazz().equals(String.class)&&columnInfo.getLength()<=varcharMaxLength){
					buffer.append("("+columnInfo.getLength()+")");
				}else if(columnInfo.getClazz().equals(BigDecimal.class)){
					buffer.append("("+columnInfo.getPrecision()+","+columnInfo.getScale()+")");
				}
				if(!columnInfo.isNullable()){
					buffer.append(" NOT NULL");
				}
				buffer.append(" ,\n");
			}
			
		}
		buffer.append("PRIMARY KEY (EId)\n");
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
		String type=typeKeeper.getOracleType();
		
		sb.append("CREATE TABLE " +joinTableName+ " (\n");
		sb.append(joinTableOneSide +" "+type+",\n");
		sb.append(joinTableManySide +" "+type+",\n");
		sb.append("displayIndex "+type+",\n");
		sb.append("primary key("+joinTableOneSide+","+joinTableManySide+")\n");
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
		String type = typeKeeper.getOracleType();
		if(columnInfo.getClazz().equals(String.class)){
			type = type + "("+columnInfo.getLength()+")";
		}
		sql="alter table "+tableName+" add "+columnName+" "+type;
		if(!columnInfo.isNullable()){
			sql +=" not null";
		}
		return sql;
	}

	@Override
	public String createFKConstraintSql(String manySideColumnName,String manySideTableName, String oneSideTableName,String contraintName) {
		String fkSql="alter table "+manySideTableName+" add constraint "+contraintName+" foreign key("+manySideColumnName+")"+" references "+oneSideTableName+"(EId)";
		return fkSql;
	}

	@Override
	public String createUniqueConstraintSql(String tableName, String columnName,String contraintName) {
		String sql ="alter table "+tableName+" add constraint "+contraintName+" unique ("+columnName+")";
		return sql;
	}

	@Override
	public void modify(String tableName,ColumnInfo columnInfo, TableColumnInfo tableColumnInfo,List<String> modifySqls) {
		TypeKeeper typeKeeper = TypeGetter.getByJavaType(columnInfo.getClazz().getName());
		if (columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
		{
			typeKeeper  = TypeGetter.getByJavaType("java.lang.Long");
		}
		String type = typeKeeper.getOracleType();
		if(columnInfo.getClazz().equals(String.class)){
			if(columnInfo.getLength()>varcharMaxLength){
				type = "CLOB";
			}else{
				type = type + "("+columnInfo.getLength()+")";
			}
		}else if(columnInfo.getClazz().equals(BigDecimal.class)){
			type = type + "("+columnInfo.getPrecision()+","+columnInfo.getScale()+")";
		}
		
		String dbType = tableColumnInfo.getType();
		String dbType2 = dbType;
		if(dbType.equals("VARCHAR2")){
			dbType2 = dbType+="("+tableColumnInfo.getLength()+")";
		}else if(dbType.equals("FLOAT")){
			dbType2 = dbType+="("+tableColumnInfo.getPrecision()+")";
		}else if(dbType.equals("NUMBER")){
			if(columnInfo.getClazz().equals(BigDecimal.class)){
				dbType2 = dbType;
				dbType+="("+tableColumnInfo.getPrecision()+","+tableColumnInfo.getScale()+")";
			}else if(tableColumnInfo.getPrecision()!=0){
				dbType2 = dbType+="("+tableColumnInfo.getPrecision()+","+tableColumnInfo.getScale()+")";
			}
		}
		
//		if(!type.equalsIgnoreCase(dbType)&&!TypeGetter.getByOracleType(dbType2).getJavaType().equalsIgnoreCase(columnInfo.getClazz().getName())){
		if(!type.equalsIgnoreCase(dbType2)){	
			String modifyType = "";
			if(type.equals("CLOB"))
			{
				this.modifyCloumnType(tableName, columnInfo.getName(), "CLOB",modifySqls);
			}
			else
			{
				modifyType = "alter table "+tableName+" modify "+columnInfo.getName()+" "+type;
				if(!columnInfo.isNullable()){
					modifyType +=" not null";
				}
				modifySqls.add(modifyType);
			}
		}else if(columnInfo.isNullable() != tableColumnInfo.isNullable()){
			String modifyNullable = "alter table "+tableName+" modify "+columnInfo.getName();
			if(!columnInfo.isNullable()){
				modifyNullable +=" not null";
			}else{
				modifyNullable +=" null";
			}
			modifySqls.add(modifyNullable);
		}
		if(columnInfo.isUnique()!=tableColumnInfo.isUnique() && !columnInfo.isId()){
			String modifyUnique = null;
			if(tableColumnInfo.isUnique()){
				modifyUnique="alter table "+tableName+" drop constraint "+tableColumnInfo.getConstraintName()+" cascade";
			}else{
				String uniqueName = columnInfo.getUniqueName();
				if("".equals(uniqueName)){
					uniqueName = tableName+"_u_"+columnInfo.getName();
				}
				modifyUnique="alter table "+tableName+" add constraint "+uniqueName+" unique ("+columnInfo.getName()+")";
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
		return "alter table "+tableName+" rename column "+oldName+" to "+newName;
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
				addSql.append("VARCHAR2");
				addSql.append("("+size+")");
			}else{
				addSql.append("CLOB");
			}
		}else{
			addSql.append(typeKeeper.getOracleType());
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
				addSql.append("VARCHAR2");
				addSql.append("("+size+")");
			}else{
				addSql.append("CLOB");
			}
		}else{
			addSql.append(typeKeeper.getMysqlType());
		}
		return addSql.toString();
	}
	
	
	private void modifyCloumnType(String tableName,String cloumnName,String columnType,List<String> modifySqls)
	{
////		修改原字段名name为temp
//		String sql = "alter table "+tableName+" rename column "+cloumnName+" to temp;";
//		modifySqls.add(sql);
//		
////		增加一个和原字段名同名的字段
//		sql = "alter table "+tableName+" add   "+cloumnName+" "+columnType+";";
//		modifySqls.add(sql);
//		
////		将原字段temp数据更新到增加的字段name
//		sql = "update "+tableName+" set "+cloumnName+"=temp;";
//		modifySqls.add(sql);
//		
////		更新完，删除原字段temp
//		 sql = "alter table "+tableName+" drop column temp;";
//		 modifySqls.add(sql);
	}

	
}
