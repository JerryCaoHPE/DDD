package ddd.simple.dao.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.base.exception.WhereConditionException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.ORListScalarMapper;
import ddd.base.persistence.Session;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.service.base.BaseServiceInterface;
@Service
public class BaseDao implements BaseDaoInterface {
	
	@Resource(name="baseService")
	private BaseServiceInterface baseService;

	@Override
	public int save(String sql, Object[] params) throws Exception
	{
		sql = DynamicService.getDynamicSql(sql, null);
		Session session = SessionFactory.getThreadSession();
		return session.excuteSql(sql, params);
	}
	
	@Override
	public <T extends Entity> T save(T entity) throws Exception{
		Session session = SessionFactory.getThreadSession();
		baseService.initEntity(entity);
		session.save(entity);
		return entity;
	}
	
	@Override
	public int saveMiddleTable(String tableName,Map<String,Object> map) throws Exception{
		if(map==null||map.size()==0){
			return 0;
		}
		Session session = SessionFactory.getThreadSession();
		
		return this.saveBasic(tableName, map, session);
	}
	
	private int saveBasic(String tableName,Map<String,Object> map,Session session) throws SQLException{
		Object[] params = new Object[map.size()];
		int index=0;
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(tableName);
		sql.append(" (");
		for (String columnName : map.keySet()) {
			params[index++] = map.get(columnName);
			sql.append(columnName);
			sql.append(",");
		}
		sql = sql.delete(sql.length()-1, sql.length());
		sql.append(") values(");
		sql.append(EntityClass.createInsertValues(map.size()));
		sql.append(")");
		int result;
		try
		{
			result = session.excuteSql(sql.toString(),params);
			return result;
		} catch (Exception e)
		{
			if(e instanceof DDDException)
			{
				throw (DDDException)e;
			}
			else
			{
				throw  new DDDException(e);
			}
		}
	}
	
	@Override
	public int save(String tableName,Map<String,Object> map) throws Exception{
		if(map==null||map.size()==0){
			return 0;
		}
		Session session = SessionFactory.getThreadSession();
		if(!map.containsKey("EId")){
			Connection connection = session.getTransactionCon();
			map.put("EId", SessionFactory.getNewPrimarykey(tableName,connection , false));
		}
		return saveBasic(tableName, map,session);
	}
	
	@Override
	public int[] save(String tableName,Map<String,Object>[] maps) throws Exception{
		if(maps==null||maps.length==0||maps[0].size()==0){
			return new int[]{0};
		}
		boolean addEId=false;
		if(!maps[0].containsKey("EId")){
			maps[0].put("EId",null);
			addEId=true;
		}
		
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(tableName);
		sql.append(" (");
		for (String columnName : maps[0].keySet()) {
			sql.append(columnName);
			sql.append(",");
		}
		sql = sql.delete(sql.length()-1, sql.length());
		sql.append(") values(");
		sql.append(EntityClass.createInsertValues(maps[0].size()));
		sql.append(")");
		
		Session session = SessionFactory.getThreadSession();
		Connection connection = session.getTransactionCon();
		Object[][] params = new Object[maps.length][maps[0].size()];
		int row=0;
		for (Map<String, Object> map : maps) {
			if(addEId){
				map.put("EId", SessionFactory.getNewPrimarykey(tableName));
			}
			int index=0;
			for (String columnName : maps[0].keySet()) {
				params[row][index++] = map.get(columnName);
			}
			row++;
		}
		
		int[] result = session.excuteSql(sql.toString(),params);
		return result;
	}
	
	@Override
	public <T extends Entity> T saveOrUpdate(T entity) throws Exception{
		Session session = SessionFactory.getThreadSession();
		session.saveOrUpdate(entity);
		return entity;
	}

	@Override
	public <T extends Entity> Collection<T> save(Collection<T> entities) throws Exception{
		Session session = SessionFactory.getThreadSession();
		session.save(entities);
		return entities;
	}
	
	@Override
	public <T extends Entity> int delete(T entity) throws Exception{
		Session session = SessionFactory.getThreadSession();
		int result=session.delete(entity);
		return result;
	}

	@Override
	public int delete(String tableName,Long Eid) throws Exception{
		int result=delete(tableName, " and Eid="+Eid);
		return result;
	}
	@Override
	public int delete(String tableName,String where) throws Exception{
		where = DynamicService.getDynamicSql(where, null);
		Session session = SessionFactory.getThreadSession();
		if(where==null||"".equals(where.trim())){
			throw new WhereConditionException("where条件为空") ;
		}
		if(!where.trim().toLowerCase().startsWith("and ")){
			where = "and "+where;
		}
		
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(tableName);
		sql.append(" where 1=1 ");
		sql.append(where);
		int result=session.excuteSql(sql.toString(), new Object[]{});
		return result;
	}
	
	@Override
	public <T extends Entity> int delete(Collection<T> entities) throws Exception{
		Session session = SessionFactory.getThreadSession();
		int result=session.delete(entities);
		return result;
	}

	@Override
	public <T extends Entity> int delete(Collection<Long> eids,Class<T> clazz) throws Exception{
		Session session = SessionFactory.getThreadSession();
		int result=session.delete(eids, clazz);
		return result;
	}

	@Override
	public <T extends Entity> int deleteById(Long eid,Class<T> clazz) throws Exception{
		Session session = SessionFactory.getThreadSession();
		int result=session.deleteById(eid, clazz);
		return result;
	}

	@Override
	public <T extends Entity> int deleteByWhere(String where,Class<T> clazz) throws Exception{
		where = DynamicService.getDynamicSql(where, null);
		Session session = SessionFactory.getThreadSession();
		int result=session.deleteByWhere(where, clazz);
		return result;
	}
	
	@Override
	public int update(String sql ,Object [] params) throws Exception{
		sql = DynamicService.getDynamicSql(sql, null);
		Session session = SessionFactory.getThreadSession();
		return session.excuteSql(sql, params);
	}
	
	@Override
	public <T extends Entity> T update(T entity) throws Exception{
		Session session = SessionFactory.getThreadSession();
		session.update(entity);
		return entity;
	}
	
	@Override
	public <T extends Entity> int update(T entity,String[] columnNames) throws Exception{
		Session session = SessionFactory.getThreadSession();
		return session.update(entity, columnNames);
	}
	
	@Override
	public int update(String tableName,Map<String, Object> map,String where) throws Exception{
		
		where = DynamicService.getDynamicSql(where, null);
		if(map==null||map.size()==0){
			return 0;
		}
		if(where==null||"".equals(where.trim())){
			throw new WhereConditionException("where条件为空") ;
		}
		if(!where.trim().toLowerCase().startsWith("and ")){
			where = "and "+where;
		}
		Object[] params = new Object[map.size()];
		int index=0;
		Session session = SessionFactory.getThreadSession();
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tableName);
		sql.append(" set");
		for (String columnName : map.keySet()) {
			params[index++] = map.get(columnName);
			sql.append(" ");
			sql.append(columnName);
			sql.append("=?,");
		}
		sql = sql.delete(sql.length()-1, sql.length());
		sql.append(" where 1=1 ");
		sql.append(where);
		return session.excuteSql(sql.toString(),params);
	}
	
	@Override
	public int[] update(String tableName,Map<String, Object> maps[],String where) throws Exception{
		
		where = DynamicService.getDynamicSql(where, null);
		if(maps==null||maps.length==0||maps[0].size()==0){
			return new int[]{0};
		}
		if(where==null||"".equals(where.trim())){
			throw new WhereConditionException("where条件为空") ;
		}
		if(!where.trim().toLowerCase().startsWith("and ")){
			where = "and "+where;
		}
		
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tableName);
		sql.append(" set");
		for (String columnName : maps[0].keySet()) {
			sql.append(" ");
			sql.append(columnName);
			sql.append("=?,");
		}
		sql = sql.delete(sql.length()-1, sql.length());
		sql.append(" where 1=1 ");
		sql.append(where);
		
		Object[][] params = new Object[maps.length][maps[0].size()];
		int row=0;
		for (Map<String, Object> map : maps) {
			int index=0;
			for (String columnName : maps[0].keySet()) {
				params[row][index++] = map.get(columnName);
			}
			row++;
		}
		
		Session session = SessionFactory.getThreadSession();
		return session.excuteSql(sql.toString(),params);
	}
	@Override
	public <T extends Entity> Collection<T> update(Collection<T> entities) throws Exception{
		Session session = SessionFactory.getThreadSession();
		session.update(entities);
		return entities;
	}
	

	@Override
	public <T extends Entity> T query(Long eid,Class<T> clazz) throws Exception{
		Session session = SessionFactory.getThreadSession();
		return session.get(eid, clazz);
	}
	
	@Override
	public Map<String, Object> query(Long eid,String tableName) throws Exception{
		Session session = SessionFactory.getThreadSession();
		String selectSql="select * from "+tableName +" where eid=?";
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(tableName);
		Set<Map<String, Object>> results = session.get(selectSql, new Object[]{eid}, entityClass.getProperties());
		for (Map<String, Object> result : results) {
			return result;
		}
		return null;
	}
	
	
	@Override
	public <T extends Entity> EntitySet<T> query(String where,Class<T> clazz) throws Exception{
		where = DynamicService.getDynamicSql(where, null);
		Session session = SessionFactory.getThreadSession();
		return session.getByWhere(where,new Object[]{}, clazz);
	}
	
	@Override
	public Set<Map<String, Object>> query(String where,String tableName) throws Exception{
		where = DynamicService.getDynamicSql(where, null);
		if(where==null){
			where="";
		}else{
			if(!where.trim().toLowerCase().startsWith("and ")){
				where = "and "+where;
			}
		}
		Session session = SessionFactory.getThreadSession();
		String selectSql = "select * from "+tableName +" where 1=1 "+where;
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(tableName);
		Set<Map<String, Object>> results = session.get(selectSql, new Object[]{}, entityClass.getProperties());
		return results;
	}

	@Override
	public <T extends Entity> EntitySet<T> query(String where,Object[] params,Class<T> clazz) throws Exception{
		where = DynamicService.getDynamicSql(where, null);
		Session session = SessionFactory.getThreadSession();
		return session.getByWhere(where,params, clazz);
	}
	
	@Override
	public <T extends Entity> EntitySet<T> queryBySql(String sql,Class<T> clazz) throws Exception{
		sql = DynamicService.getDynamicSql(sql, null);
		Session session = SessionFactory.getThreadSession();
		return session.getBySql(sql, new Object[]{},clazz);
	}

	@Override
	public <T extends Entity> EntitySet<T> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams,Class<T> clazz) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.getBySql(sql,new Object[]{}, clazz);
	}
	
	@Override
	public <T extends Entity> EntitySet<T> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams,Object[] sqlParams,Class<T> clazz) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.getBySql(sql,sqlParams, clazz);
	}
	
	@Override
	public <T extends Entity> T queryOne(String where,Class<T> clazz) throws Exception{
		EntitySet<T> entitySet = query( where, clazz);
		if(entitySet == null || entitySet.size() ==0)
		{
			return null; 
		}
		if(entitySet.size() > 1)
		{
			throw new DDDException("使用方法  queryOne 查询返回的数据有多行，这是不允许的，请检查，条件是："+where);
		}
		for (T entity : entitySet) {
			return entity;
		}
		return null;
	}
	@Override
	public <T extends Entity> T queryOne(String where,Object[] params,Class<T> clazz) throws Exception{
		EntitySet<T> entitySet = query(where, params, clazz);
		if(entitySet == null || entitySet.size() ==0)
		{
			return null; 
		}
		if(entitySet.size() > 1)
		{
			throw new DDDException("使用方法  queryOne 查询返回的数据有多行，这是不允许的，请检查，条件是："+where);
		}
		for (T entity : entitySet) {
			return entity;
		}
		return null;
	}
	@Override
	public <T extends Entity> T queryOneBySql(String sql,Class<T> clazz) throws Exception{
		sql = DynamicService.getDynamicSql(sql, null);
		Session session = SessionFactory.getThreadSession();
		EntitySet<T> entitySet = session.getBySql(sql,new Object[]{}, clazz);
		if(entitySet == null || entitySet.size() ==0)
		{
			return null; 
		}
		if(entitySet.size() > 1)
		{
			throw new DDDException("使用方法  queryOne 查询返回的数据有多行，这是不允许的，请检查，条件是："+sql);
		}
		for (T entity : entitySet) {
			return entity;
		}
		return null;
	}
	
	@Override
	public Set<Map<String, Object>> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.get(sql, new Object[]{},new String[]{});  
	}
	
	@Override
	public Set<Map<String, Object>> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams,Object[] sqlParams) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.get(sql, sqlParams,new String[]{});  
	}
	
	@Override
	public Set<Map<String, Object>> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams,String[] properties) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.get(sql, new Object[]{},properties);  
	}
	
	@Override
	public Set<Map<String, Object>> queryByDynamicSql(String sql,Map<String, Object> dynamicSqlparams,Object[] sqlParams,String[] properties) throws Exception{
		Session session = SessionFactory.getThreadSession();
		sql = DynamicService.getDynamicSql(sql, dynamicSqlparams);
		return session.get(sql, sqlParams,properties);  
	}
	
	@Override
	public Set<Map<String, Object>> query(String selectSql,Object[] params,String[] properties) throws Exception{
		selectSql = DynamicService.getDynamicSql(selectSql, null);
		Session session = SessionFactory.getThreadSession();
		return session.get(selectSql, params,properties);  
	}
	
	@Override
	public Set<Map<String, Object>> query(String selectSql,Object[] params) throws Exception{
		selectSql = DynamicService.getDynamicSql(selectSql, null);
		Session session = SessionFactory.getThreadSession();
		return session.get(selectSql, params,null);  
	}
	
	@Override
	public Set<Map<String, Object>> query(String selectSql,String[] properties) throws Exception{
		selectSql = DynamicService.getDynamicSql(selectSql, null);
		Session session = SessionFactory.getThreadSession();
		return session.get(selectSql,new Object[]{},properties);
	}
	@Override
	public Set<Map<String, Object>> query(String selectSql) throws Exception{
		selectSql = DynamicService.getDynamicSql(selectSql, null);
		Session session = SessionFactory.getThreadSession();
		return session.get(selectSql,new Object[]{},null);
	}
	
	@Override
	public ResultSet getResultSet(String selectSql) throws Exception
	{
		selectSql = DynamicService.getDynamicSql(selectSql, null);
		Session session = SessionFactory.getThreadSession();
		return session.getResultSetBySql(selectSql, new Object[]{});
	}
	
	@Override
	public boolean isExist(String tableName,String columnName,Object value) throws Exception
	{
		Session session = SessionFactory.getThreadSession();
		String sql="select count(EId) num from "+tableName+" where "+columnName+" =?";
		Collection<Integer> num = session.get(sql, new ORListScalarMapper<Integer>(Integer.class), new Object[]{value});
		return num.iterator().next()>0;
	}

	/* (非 Javadoc) 
	* <p>Title: save</p> 
	* <p>Description: </p> 
	* @param sql
	* @param params
	* @return
	* @throws Exception 
	* @see ddd.simple.dao.base.BaseDaoInterface#save(java.lang.String, java.lang.Object[]) 
	*/
	
}
