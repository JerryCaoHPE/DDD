package ddd.base.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;

public class SQLUtil {
	/**
	 * 获取select sql语句中查询的字段名
	 * 例如:select id,name,date as birthday from student   返回[id,name,birthday]
	 */
	public static String[] getProperties(String sql){
		return getProperties2(sql);
	}
	private static String[] getProperties1(String sql){
		while(sql.charAt(0)==' '){
			sql = sql.substring(1, sql.length());
		}
		String sqlLowerCase = sql.toLowerCase();
		int start = 7;
		int end = sqlLowerCase.indexOf(" from", start);
		String params = sql.substring(start, end);
		
		String[] properties = params.split(",");
		for (int i = 0; i < properties.length; i++) {
			//properties[i] = properties[i].replaceAll(".*[aA][sS]", "").trim();
			String[] lable = properties[i].split("[ .]");
			properties[i] = lable[lable.length-1].trim();
		}
		return properties;
	}
	private static String[] getProperties2(String sql){
		while(sql.charAt(0)==' '){
			sql = sql.substring(1, sql.length());
		}
		String sqlLowerCase = sql.toLowerCase();
		int start = 7;
		int end = sqlLowerCase.indexOf(" from", start);
		String params = sql.substring(start, end);
		
		String[] properties = split(params, ",");
		for (int i = 0; i < properties.length; i++) {
			String[] lable = split(properties[i],"[ .]");
			properties[i] = lable[lable.length-1].trim();
		}
		return properties;
	}
	public static String[] split(String source,String delim){
		StringTokenizer stringTokenizer = new StringTokenizer(source, delim);
		String[] array = new String[stringTokenizer.countTokens()];
		int i = 0;
		while (stringTokenizer.hasMoreTokens()) {
			array[i++] = stringTokenizer.nextToken();
		}
		return array;
	}
	/**
	 * 
	* @Title: addWhereExpression 在sql语句的where 后面插入条件表达式
	* @Description: 在sql语句的where 后面插入条件表达式,如果没有where子句，则增加子句，如果有，则并上原有子句
	* 例如：select *　from codetable where name = 'type' , =》  select *　from codetable where name = 'type' and 1!=1
	* @param sql
	* @param expression
	* @return 
	* @return String
	 */
	public static String addWhereExpression(String sql ,String expression)
	{
		sql = StringUtils.replace(sql, "\r\n", " ");
		int whereStartIndex = StringUtils.lastIndexOfIgnoreCase(sql, " where ");
		StringBuilder sqlBuilder = new StringBuilder(sql) ;
		if( whereStartIndex == -1)
		{	
			int orderStartIndex = StringUtils.lastIndexOfIgnoreCase(sql, " order ");
			if(orderStartIndex == -1)
			{
				sqlBuilder.append(" where "+expression+" ");
			}
			else
			{
				sqlBuilder.insert(orderStartIndex, " where "+expression+" ");
			}
		}
		else
		{
			sqlBuilder.insert(whereStartIndex+7, "("+expression+") and ");
		}
		
		return sqlBuilder.toString();
	}
	public static <T extends Entity>  List<String> getProperties(Class<T> clazz){
		Field[] fields = ClassUtil.getClassNestedFields(clazz);
		List<String> result = new ArrayList<String>();
		result.add("ClassName:"+clazz.getSimpleName()+",Fileds:");
		for (Field field : fields) {
			if(field.isAnnotationPresent(Column.class)){
				Column column = field.getAnnotation(Column.class);
				result.add("".equals(column.name().trim()) ? field.getName() : column.name());
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		String sql ="  Select s.Id as    fffd    , s.name,  s.date  birthday from student s";
		
		String sqld = "select Id as id, name , date AS birthday from student";
		//sqld = sqld.replaceAll("[.]{0,}?[aA][sS]", "");
	
		long start = System.currentTimeMillis();
		String[] dd = getProperties2(sql);
		System.out.println(" time use:" + (System.currentTimeMillis()-start));   
		for (String string : dd) {
			System.out.println(string);
		}
		String temp = "   date as birthday";
	}

}
