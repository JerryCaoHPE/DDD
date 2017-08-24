package ddd.simple.dao.base;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

public interface BaseDaoInterface {

	public abstract <T extends Entity> T save(T entity) throws Exception;
	/**
	 * 新增数据到指定的表中
	 * @param tableName
	 * @param map   键是字段名
	 * @return
	 * @throws Exception
	 */
	public abstract int save(String sql ,Object [] params) throws Exception;
	
	public int save(String tableName, Map<String, Object> map) throws Exception;
	
	public int[] save(String tableName,Map<String,Object>[] maps) throws Exception;
	
	public <T extends Entity> T saveOrUpdate(T entity) throws Exception;
	
	public abstract <T extends Entity> Collection<T> save(Collection<T> entities)throws Exception;

	public abstract <T extends Entity> int delete(T entity) throws Exception;

	public int delete(String tableName, Long Eid) throws Exception;

	public int delete(String tableName, String where) throws Exception;

	public abstract <T extends Entity> int delete(Collection<T> entities)throws Exception;

	public abstract <T extends Entity> int delete(Collection<Long> eids,Class<T> clazz) throws Exception;

	public abstract <T extends Entity> int deleteById(Long eid, Class<T> clazz)throws Exception;

	public abstract <T extends Entity> int deleteByWhere(String where,Class<T> clazz) throws Exception;

	public abstract <T extends Entity> T update(T entity) throws Exception;
	
	public abstract int update(String sql ,Object [] params) throws Exception;
	/**
	 * 更新指定字段的数据
	 * @param entity
	 * @param columnNames 数据库字段名称 不能保存集合类型
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> int update(T entity, String[] columnNames) throws Exception;
	
	/**
	 * 更新指定的键值对
	 * @param tableName 表名
	 * @param map   键是字段名 值是要更新的值
	 * @param where
	 * @return
	 * @throws Exception
	 */
	public int update(String tableName, Map<String, Object> map, String where) throws Exception;
	
	public int[] update(String tableName,Map<String, Object> maps[],String where) throws Exception;
	
	public abstract <T extends Entity> Collection<T> update(Collection<T> entities) throws Exception;

	public abstract <T extends Entity> T query(Long eid, Class<T> clazz)throws Exception;
	
	public abstract Map<String, Object> query(Long eid, String tableName) throws Exception;
	
	public abstract <T extends Entity> EntitySet<T> query(String where,Class<T> clazz) throws Exception;
	
	public Set<Map<String, Object>> query(String where,String tableName) throws Exception;
	/**
	 * 根据where条件查询
	 * @param where
	 * @param params where条件中的参数
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> EntitySet<T> query(String where,Object[] params,Class<T> clazz) throws Exception;
	/**
	 * 根据sql查询
	 * @param sql  完整的sql 查询的表名必须与clazz对应;
	 * @param clazz  实体对应的Class
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> EntitySet<T> queryBySql(String sql,Class<T> clazz) throws Exception;
	/**
	 * 根据动态sql的key查询指定的实体
	 * @param dynamicSql
	 * @param dynamicSqlparams
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> EntitySet<T> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams,Class<T> clazz) throws Exception;
	
	/**
	 * 根据动态sql的key查询指定的实体
	 * @param dynamicSql
	 * @param dynamicSqlparams
	 * @param sqlParams 生成的sql语句的参数
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> EntitySet<T> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams,Object[] sqlParams,Class<T> clazz) throws Exception;
	
	public abstract <T extends Entity> T queryOne(String where,Class<T> clazz) throws Exception;
	
	public <T extends Entity> T queryOne(String where,Object[] params,Class<T> clazz) throws Exception;
	
	/**
	 * 根据sql查询
	 * @param sql  完整的sql 查询的表名必须与clazz对应;
	 * @param clazz  实体对应的Class
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> T queryOneBySql(String sql,Class<T> clazz) throws Exception;
	/**
	 * 根据动态sql查询 返回结果
	 * @param dynamicSql
	 * @param dynamicSqlparams  动态sql的参数
	 * @return
	 * @throws Exception
	 */
	public Set<Map<String, Object>> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams) throws Exception;
	/**
	 * 
	 * @param dynamicSql
	 * @param dynamicSqlparams  动态sql的参数
	 * @param sqlParams  sql语句的参数
	 * @return
	 * @throws Exception
	 */
	public Set<Map<String, Object>> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams,Object[] sqlParams) throws Exception;
	/**
	 * 根据动态sql查询 返回结果
	 * @param dynamicSql
	 * @param dynamicSqlparams      动态sql的参数
	 * @param properties    要查询的字段名
	 * @return
	 * @throws Exception
	 */
	public Set<Map<String, Object>> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams,String[] properties) throws Exception;
	/**
	 * 根据动态sql查询 返回结果
	 * @param dynamicSql
	 * @param dynamicSqlparams   动态sql的参数
	 * @param sqlParams    sql语句的参数
	 * @param properties   要查询的字段名
	 * @return
	 * @throws Exception
	 */
	public Set<Map<String, Object>> queryByDynamicSql(String dynamicSql,Map<String, Object> dynamicSqlparams,Object[] sqlParams,String[] properties) throws Exception;
	/**
	 * 
	 * @param selectSql 查询sql语句，如果使用select * 则必须传properties属性
	 * @param params  sql语句中的参数
	 * @param properties  要查询的字段名
	 * @return
	 * @throws Exception
	 */
	public abstract Set<Map<String, Object>> query(String selectSql,Object[] params,String[] properties) throws Exception;
	/**
	 * 
	 * @param selectSql   查询sql语句，如果使用select * 则必须传properties属性
	 * @param params  sql语句中的参数
	 * @return
	 * @throws Exception
	 */
	public abstract Set<Map<String, Object>> query(String selectSql, Object[] params)throws Exception;
	/**
	 *
	 * @param selectSql   查询sql语句，如果使用select * 则必须传properties属性
	 * @param properties    要查询的字段名
	 * @return
	 * @throws Exception
	 */
	public abstract Set<Map<String, Object>> query(String selectSql,String[] properties)throws Exception;
	
	public abstract Set<Map<String, Object>> query(String selectSql) throws Exception;

	public abstract ResultSet getResultSet(String selectSql) throws Exception;

	/**
	 * 判断某张表的某个字段是否存在指定的值
	 * @param tableName 表名
	 * @param columnName 字段名
	 * @param value 值
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(String tableName,String columnName,Object value) throws Exception;
	
	public int saveMiddleTable(String tableName, Map<String, Object> map)
			throws Exception;
	
}