package ddd.base.dbmanager.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ddd.base.dbmanager.bean.TypeKeeper;

public class TypeGetter {
	
	private static List<TypeKeeper> typeKeepers = new ArrayList<TypeKeeper>();
	
	static{
		typeKeepers.add(new TypeKeeper(Types.BIGINT, "java.lang.Long", "NUMBER(19,0)", "BIGINT"));
		typeKeepers.add(new TypeKeeper(Types.INTEGER, "java.lang.Integer", "NUMBER(10,0)", "INT"));
		typeKeepers.add(new TypeKeeper(Types.DOUBLE, "java.lang.Double", "FLOAT(64)", "DOUBLE"));
		typeKeepers.add(new TypeKeeper(Types.FLOAT, "java.lang.Float", "FLOAT(32)", "FLOAT"));
		typeKeepers.add(new TypeKeeper(Types.BOOLEAN, "java.lang.Boolean", "NUMBER(3,0)", "TINYINT(1)"));
		typeKeepers.add(new TypeKeeper(Types.VARCHAR, "java.lang.String", "VARCHAR2", "VARCHAR"));
		typeKeepers.add(new TypeKeeper(Types.DATE, "java.util.Date", "DATE", "DATETIME"));
		typeKeepers.add(new TypeKeeper(Types.DATE, "java.sql.Date", "DATE", "DATETIME"));
		typeKeepers.add(new TypeKeeper(Types.TIMESTAMP, "java.sql.Timestamp", "DATE", "DATE"));
		typeKeepers.add(new TypeKeeper(Types.DECIMAL, "java.math.BigDecimal", "NUMBER", "DECIMAL"));
		typeKeepers.add(new TypeKeeper(Types.BLOB, "byte[]", "BLOB", "BLOB"));
		
		typeKeepers.add(new TypeKeeper(Types.BIT, "byte[]", "RAW", "TINYBLOB"));
		typeKeepers.add(new TypeKeeper(Types.BINARY, "byte[]", "LONG RAW", "BLOB"));
		typeKeepers.add(new TypeKeeper(Types.CHAR, "java.lang.String", "CHAR", "CHAR"));
		typeKeepers.add(new TypeKeeper(Types.CLOB, "java.lang.String", "CLOB", "TEXT"));
		typeKeepers.add(new TypeKeeper(Types.CLOB, "java.lang.String", "CLOB", "LONGTEXT"));
		
				
	}
	
	public static TypeKeeper getByJavaType(String javaType){
		
		for (TypeKeeper typeKeeper : typeKeepers) {
			if (typeKeeper.getJavaType().toLowerCase().equals(javaType.toLowerCase())) {
				return typeKeeper;
			}
		}
		return null;
	}
	
	public static TypeKeeper getByOracleType(String oracleType){
		
		for (TypeKeeper typeKeeper : typeKeepers) {
			if (typeKeeper.getOracleType().toLowerCase().equals(oracleType.toLowerCase())) {
				return typeKeeper;
			}
		}
		return null;
	}

	public static TypeKeeper getByMysqlType(String mysqlType){
	
	for (TypeKeeper typeKeeper : typeKeepers) {
		if (typeKeeper.getMysqlType().toLowerCase().equals(mysqlType.toLowerCase())) {
			return typeKeeper;
		}
	}
	return null;
}
	
}
