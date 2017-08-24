package ddd.base.persistence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoaderListener;

import ddd.base.annotation.ColumnInfo;
import ddd.base.exception.ClassMatchException;
import ddd.base.exception.DBException;
import ddd.base.exception.DDDException;
import ddd.base.exception.WhereConditionException;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.EntityUtil;

public class Session {
	/**
	 * 随机获取的连接，用完就释放
	 */
	private Connection queryConnection;
	
	private EntityCache entityCache;
	/**
	 * 事务连接，有事务期间保留，事务提交后释放
	 */
	private Connection transactionConn ;
	
	private DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) ContextLoaderListener.getCurrentWebApplicationContext().getBean("transactionManager");
	/**
	 * 事务状态
	 */
	private TransactionStatus transactionStatus;
	
	private int batchSize=500;
	
	public Session() {
		super();
		this.entityCache = new EntityCache();
	}
	
 
	public <T extends Entity> int save(T entity) throws Exception{
		
		return saveEntity(entity,true);
	}	
	/**
	 * 自动判断是更新还是新增
	 * @param entity
	 * @param needCascade 是否需要级联保存 子对象，即自动维护一对多的关系 
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> int saveOrUpdate(T entity,boolean needCascade) throws Exception{
		if(entity.getEId()==null || this.isEntityExisting(entity.getClass(), entity.getEId()) == false )
		{
			return this.save(entity);
		}
		else
		{
			return this.update(entity,needCascade);
		}
	}	
	/**
	 * 自动判断是更新还是新增,并自动维护一对多的关系 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> int saveOrUpdate(T entity) throws Exception{
		return this.saveOrUpdate(entity, true);
	}	
	
	public <T extends Entity> int saveEntity(T entity,boolean needCascade) throws Exception{
		if(entity==null) return 0;
		
		beginTransaction();
		
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(entity.getClass());
		PreparedStatement preparedStatement;
			try {
				preparedStatement = this.transactionConn.prepareStatement(entityClass.getInsertSql());
				preparedStatement = entityClass.fillInsertStatement(preparedStatement, entity);
				
				int result = preparedStatement.executeBatch()[0];
				preparedStatement.close();
				entity.setNewEntity(false);
				if(needCascade == true)
				{
					cascadeUpdate(this.transactionConn,entity);
				}
				return result;
			} catch (ClassMatchException e) {
				e.printStackTrace();
				throw e;
			} catch (SQLException e) {
				e.printStackTrace();
				Exception exception = SQLExceptionHelper.transformException(e);
				throw exception;
			}
			
	}
	
	private <T extends Entity> void cascadeUpdate(Connection conn,T entity) throws Exception{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		Long entityEid = entityClass.getEid(entity);
		
		Map<String, ColumnInfo> one2Many = entityClass.getOne2ManyFieldColumnInfos();
		for (String key : one2Many.keySet()) {
			ColumnInfo columnInfo = one2Many.get(key);
			//如果没有加载，则不需要保存
			if(entity.isFieldLazyLoaded(key) == false)
			{
				continue;
			}
			
			Set<Entity> list = null;
			list = (Set<Entity>) columnInfo.getField().get(entity);
			if(list==null){
				list = new EntitySet<Entity>();
			}
			if(columnInfo.isComposition() ){
				EntityClass<? extends Entity> one2oneEntityClass = SessionFactory.getEntityClass(columnInfo.getClazz());
				
				for (Entity e : list) {
					ColumnInfo one2OneColumnInfo = one2oneEntityClass.getOne2OneFieldColumnInfos().get(columnInfo.getJoinColumn());
					one2OneColumnInfo.getFieldSetter().invoke(e, entity);
				}
				
				Long start = System.currentTimeMillis();
				Map<Long, Entity> entityOne2Many = EntityUtil.getEIds(list,conn);
				System.out.println("获取eid时间："+(System.currentTimeMillis()-start)+"ms");
				String where =" and "+ columnInfo.getJoinColumn()+"="+entityEid;
				
				start = System.currentTimeMillis();
				Set<Long> deleteEids = getEIdsByWhere(conn, (Class<Entity>)columnInfo.getClazz(), where);
				System.out.println("查询eid时间："+(System.currentTimeMillis()-start)+"ms");
				Set<Entity> updateEntities = new  EntitySet<Entity>();
				Set<Entity> insertEntities = new EntitySet<Entity>();
				
				start = System.currentTimeMillis();
				for (Long eid : entityOne2Many.keySet()) {
					if(deleteEids.contains(eid))
					{
						updateEntities.add(entityOne2Many.get(eid));
						deleteEids.remove(eid);					
					}
					else
					{
						insertEntities.add(entityOne2Many.get(eid));
					}
				}
				System.out.println("对比时间："+(System.currentTimeMillis()-start)+"ms");
				start = System.currentTimeMillis();
				save(insertEntities,false);
				System.out.println("save时间："+(System.currentTimeMillis()-start)+"ms");
				start = System.currentTimeMillis();
				update(updateEntities,false);
				System.out.println("update时间："+(System.currentTimeMillis()-start)+"ms");
				start = System.currentTimeMillis();
				delete(deleteEids,(Class<Entity>)columnInfo.getClazz());
				System.out.println("delete时间："+(System.currentTimeMillis()-start)+"ms");
			}
			else
			{
				//如果是聚合关系，则维护关联表
				//1.删除关联表中的原有数据 
				String sql= "delete from "+columnInfo.getJoinTableName()+" where "
						+ columnInfo.getJoinTableOneSide()+"=?";
				this.excuteSql(sql, new Long[]{entityEid});
				
				Long[][] params = new Long[list.size()][3];
				int i =0;
				for(Entity foreignEntity:list)
				{
					if(foreignEntity.getEId() == null)
					{
						throw new DDDException("聚合关系中多的一方的实体必须先保，不允许存在没有保存的对象，关联表名为"+columnInfo.getJoinTableName());
					}
					
					params[i][0] = entityEid;
					params[i][1] = foreignEntity.getEId();
					params[i][2] = (long) i;
					i++;
				}
				sql= "insert into "+columnInfo.getJoinTableName()+" ("
						+ columnInfo.getJoinTableOneSide()+","+columnInfo.getJoinTableManySide()
						+",displayIndex) values (?,?,?)"; 
				this.excuteSql(sql, params);
			}
		}
	}
	
	private <T extends Entity> Set<Long> getEIdsByWhere(Connection conn,Class<T> clazz,String where) throws Exception{
		Set<Long> result = new HashSet<Long>();
		EntityClass<?> entityClass = SessionFactory.getEntityClass(clazz);
		String selectSql = "select EId from "+entityClass.getEntityInfo().getName()+" where 1=1 "+where;
		
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectSql);
			while(rs.next()){
				result.add(rs.getLong("EId"));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
		return result;
	}
	/**
	 * 保存实体集合(类型必须一致)
	 * @param entities
	 * @return
	 */
	public <T extends Entity> int[] save(Collection<T> entities) throws Exception{
		return save(entities,true);
		
	}
	/**
	 * 保存实体集合(类型必须一致)
	 * @param entities
	 * @return
	 */
	public <T extends Entity> int[] save(Collection<T> entities,boolean needCascade) throws Exception{
		if(entities==null||entities.size()==0){
			return null;
		}
		PreparedStatement preparedStatement = null;
		EntityClass<?> entityClass = null ;
		int[] oneBatch = null;
		int[] result = new int[entities.size()];
		int index=0;
		int count=0;
		beginTransaction();
		
		try {
			for (T entity : entities) {
				if(entityClass ==null){
					entityClass = SessionFactory.getEntityClass(entity.getClass());
					preparedStatement = this.transactionConn.prepareStatement(entityClass.getInsertSql());
				}
				preparedStatement = entityClass.fillInsertStatement(preparedStatement, entity);
				entity.setNewEntity(false);
				if(++count % batchSize == 0) {
					oneBatch = preparedStatement.executeBatch();
					for (int one : oneBatch) {
						result[index++] = one;
					}
			    }
			}
			oneBatch = preparedStatement.executeBatch();
			preparedStatement.close();
			for (int one : oneBatch) {
				result[index++] = one;
			}
			if(needCascade){
				for (T entity : entities) {
					cascadeUpdate(transactionConn, entity);
				}
			}
		} catch (ClassMatchException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
		return result;
	}
	
	
	/**
	 * 删除实体
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> int delete(T entity) throws Exception{
		if(entity==null) return 0;
		
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		Long eid = entityClass.getEid(entity);
		int result = deleteById(eid, entity.getClass());
		return result;
	}
	/**
	 * 删除集合，集合里的类型必须相同,并且继承基础实体Entity
	 * @param entity
	 * @return
	 * @throws SQLException 
	 * @throws WhereConditionException 
	 */
	public <T extends Entity> int delete(Collection<T> entities) throws Exception{
		if(entities==null||entities.size()==0){
			return 0;
		}
		List<Long> Eids = new ArrayList<Long>();
		EntityClass<?> entityClass =null ;
		Long eid = null;
		for (T entity : entities) {
			if(entityClass==null){
				entityClass = SessionFactory.getEntityClass(entity.getClass());
			}
			if(!entityClass.getClazz().equals(entity.getClass())){
				throw new  ClassMatchException("集合中的类型不一致");
			}
			eid = entityClass.getEid(entity);
			Eids.add(eid);
		}
		
		return delete(Eids, entityClass.getClazz());
	}
	/**
	 * 根据主键集合删除
	 * @param eids
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> int delete(Collection<Long> eids,Class<T> clazz) throws Exception{
		if(eids==null||eids.size()==0){
			return 0;
		}
		beginTransaction();
		
		StringBuffer sbEids = new StringBuffer();
		this.clearCache();
		for (Long eid : eids) {
			//this.clearByEntityId(clazz, eid); 清除不掉？？？
			sbEids.append(eid+",");
		}
		if(sbEids.length()>0)
		{
			sbEids.delete(sbEids.length()-1, sbEids.length());
		}
		return deleteEntity(this.transactionConn,clazz, sbEids.toString());
	}
	
	
	/**
	 * 根据主键删除数据
	 * @param eid
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> int deleteById(Long eid,Class<T> clazz) throws Exception{
		List<Long> eids = new ArrayList<Long>();
		eids.add(eid);
		int result = delete(eids, clazz);
		return result;
	}
	/**
	 *  私有方法不对外使用
	 * @param conn 连接
	 * @param clazz 类型
	 * @return
	 * @throws Exception 
	 */
	
	private <T extends Entity> int deleteEntity(Connection conn,Class<T> clazz,String eids) throws Exception{
		if(eids == null || "".equals(eids)){
			return 0;
		}
		
		EntityClass<?> entityClass = SessionFactory.getEntityClass(clazz);
		Map<String, ColumnInfo> one2Many = entityClass.getOne2ManyFieldColumnInfos();
		for (ColumnInfo columnInfo  : one2Many.values()) 
		{
			
			if(columnInfo.isComposition())
			{
				Class<Entity> casecadeClazz=(Class<Entity>) columnInfo.getClazz();
				EntityClass<?> casecadeEntityClass = SessionFactory.getEntityClass(casecadeClazz);
				
				String casecadeDeleteSql="delete from "+casecadeEntityClass.getEntityInfo().getName()
						+ " where "+columnInfo.getJoinColumn()+" in ("+eids+")";
				this.excuteSql(casecadeDeleteSql, new Long[]{});

			}
			else
			{
				String sql= "delete from "+columnInfo.getJoinTableName()+" where "
						+ columnInfo.getJoinTableOneSide()+" in ("+eids+")";
				this.excuteSql(sql, new Long[]{});
			}
		}
		Map<String, JoinTableInfo> joinTableInfos = entityClass.getJoinTableInfos();
		for (JoinTableInfo joinTableInfo : joinTableInfos.values()) {
			String deleteJoinTableSql="delete from "+joinTableInfo.getJoinTableName()
					+" where "+joinTableInfo.getJoinTableManySide() +" in ("+eids+")";
			this.excuteSql(deleteJoinTableSql, new Long[]{});
		}
		
		
		String deleteSql ="delete from "+entityClass.getEntityInfo().getName()
				+" where " +entityClass.getIdColumnInfo().getName() 
				+ " in ("+eids+")";
		int result = this.excuteSql(deleteSql, new Long[]{});
		return result;
	}
	/**
	 * 根据where条件删除数据
	 * @param where
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	//如果表中有外键关联，使用此方法会有问题
	public <T extends Entity> int deleteByWhere(String where,Class<T> clazz) throws Exception{
		if(where==null||"".equals(where.trim())){
			throw new WhereConditionException("where条件为空") ;
		}
		if(!where.trim().toLowerCase().startsWith("and ")){
			where = "and "+where;
		}
		
		beginTransaction();
		
		EntityClass<?> entityClass = SessionFactory.getEntityClass(clazz);
		String sql = "select "+ entityClass.getIdColumnInfo().getName() +" as eidd "
				+ " from " +entityClass.getEntityInfo().getName() 
				+ " where 1=1 "+ where;
				
		List<Long> eids   = (List<Long>)this.excuteQuery(sql, new Object[]{},new ORListScalarMapper<Long>(Long.class));
		
		if(eids.size() == 0)
		{
			return 0;
		}
		int result = this.delete(eids, clazz);
		return result;
	}
	
	public <T extends Entity> int update(T entity) throws Exception{
		return updateEntity(entity,true);
	}
	
	public <T extends Entity> int update(T entity,String[] columnNames) throws Exception{
		if(columnNames==null||columnNames.length==0){
			return 0;
		}
		int result = 0;
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		beginTransaction();
		String sql = createUpdateSQL(entityClass, columnNames);
		try {
			PreparedStatement preparedStatement =  this.transactionConn.prepareStatement(sql);
			preparedStatement = entityClass.fillUpdateSql(preparedStatement, entity,columnNames);
			result = preparedStatement.executeBatch()[0];
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
		return result;
	}
	
	private String createUpdateSQL(EntityClass<?> entityClass ,String[] columnNames){
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(entityClass.getEntityInfo().getName());
		sb.append(" set ");
		
		for (String columnName : columnNames) {
			sb.append(columnName);
			sb.append("=?,");
		}
		sb = sb.delete(sb.length()-1, sb.length());
		sb.append(" where ");
		sb.append("Eid");
		sb.append("=?");
		return sb.toString();
	}
	
	public <T extends Entity> int update(T entity,boolean needCascade) throws Exception{
		return updateEntity(entity,needCascade);
	}
	
	private <T extends Entity> int updateEntity(T entity,boolean needCascade) throws Exception{
		if(entity==null) return 0;
		beginTransaction();
		
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		int result=0;
			try {
				if( !entity.isNewEntity() ){
					this.clearCache();
					PreparedStatement preparedStatement = this.transactionConn.prepareStatement(entityClass.getUpdateSql());
					preparedStatement = entityClass.fillUpdateSql(preparedStatement, entity);
					result = preparedStatement.executeBatch()[0];
					preparedStatement.close();
				}
				if(needCascade == true)
				{
					cascadeUpdate(this.transactionConn,entity);
				}
				return result;
			} catch (ClassMatchException e) {
				e.printStackTrace();
				throw e;
			} catch (SQLException e) {
				e.printStackTrace();
				Exception exception = SQLExceptionHelper.transformException(e);
				throw exception;
			}
	}
	
	public <T extends Entity> int[] update(Collection<T> entities) throws Exception{
		return update(entities, true);
	}
	public <T extends Entity> int[] update(Collection<T> entities,boolean needCascade) throws Exception{
		if(entities==null||entities.size()==0){
			return null;
		}
		PreparedStatement preparedStatement = null;
		EntityClass<?> entityClass = null ;
		int[] oneBatch = null;
		int[] result = new int[entities.size()];
		int index=0;
		int count=0;
		
		beginTransaction();
		
		try {
			for (T entity : entities) {
				if(entityClass ==null){
					entityClass = SessionFactory.getEntityClass(entity.getClass());
					preparedStatement = this.transactionConn.prepareStatement(entityClass.getUpdateSql());
				}
				if(entity.isInitialized()){
					preparedStatement = entityClass.fillUpdateSql(preparedStatement, entity);
					if(++count % batchSize == 0) {
						oneBatch = preparedStatement.executeBatch();
						for (int one : oneBatch) {
							result[index++] = one;
						}
				    }
				}
			}
			oneBatch = preparedStatement.executeBatch();
			preparedStatement.close();
			for (int one : oneBatch) {
				result[index++] = one;
			}
			if(needCascade){
				for (T entity : entities) {
					if(entity.isInitialized()){
						cascadeUpdate(transactionConn, entity);
					}
				}
			}
		} catch (ClassMatchException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
		return result;
	}
	/**
	 *根据Id查询某个实体的某个属性
	 * @param <T>
	 * @param eid
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> Object getScalar(Class<T> clazz,String fieldName,Long eid) throws Exception{
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(clazz);
		String sql = "select "+entityClass.mapFieldName2ColumnName(fieldName)+" as fieldValue from "+entityClass.getEntityInfo().getName()+" where EId=?";
 		
		List<Object> params = new ArrayList<Object>();
		params.add(0, eid);
		
		Object value = null; 
		
		ResultSet resultSet = null;
		try{
			resultSet = this.getResultSetBySql(sql, params.toArray());
			
			if(resultSet.next() == false)
			{
				throw new DBException("getScalar", 
						"取字段值出错，"+entityClass.getEntityInfo().getName()+"id为 "+eid+" 的记录不存在");
			}
			
			value = resultSet.getObject("fieldValue");
			
			return value;
		}finally{
			if(resultSet != null) resultSet.close();
			this.closeQueryConn();
		}
		
	}	
	/**
	 *根据sql语句最第一行的第一个字段的值，如果没有数据，则返回空
	 * @param <T>
	 * @param eid
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public Object getScalar(String sql) throws Exception{
		Object value = null; 
		
		ResultSet resultSet = null;
		try{
			resultSet = this.getResultSetBySql(sql);
			
			if(resultSet.next() == false)
			{
				return null; 
			}
			
			value = resultSet.getObject(1);
			
			return value;
		}finally{
			if(resultSet != null) resultSet.close();
			this.closeQueryConn();
		}
		
	}	
	/**
	 * 指定Id的实体在数据库中是否已经存在
	 * @param clazz
	 * @param eid
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> boolean isEntityExisting(Class<T> clazz,Long eid) throws Exception{
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(clazz);
		String sql = "select EId from "+entityClass.getEntityInfo().getName()+" where EId=?";
 		
		List<Object> params = new ArrayList<Object>();
		params.add(eid);
		ResultSet resultSet = null;
		try{
			resultSet = this.getResultSetBySql(sql, params.toArray());
			return resultSet.next();
		}
		finally
		{
			if(resultSet != null) resultSet.close();
			this.closeQueryConn();
		}
	}	
	/**
	 *根据Id查询某条数据，并封装成对应的实体
	 * @param eid
	 * @param class1
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> T get(Long eid,T entity,Class<? extends Entity> class1) throws Exception{
		 
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(class1);
		
		T resultCached = (T)this.getEntityCache().get(entityClass.getEntityInfo().getName(), eid);
		if(resultCached != null && resultCached.isInitialized())
		{
			return resultCached;
		}
		if(entity==null){
			entity = resultCached;
		}
		
		String sql = entityClass.getSelectSql();
		Collection<T> entities = this.excuteQuery(sql, new Object[]{eid}, new OREntityMapper<T>(entityClass,entity));
		
		if(entities.size() == 0)
		{
			return null;
		}
		Iterator<T> iterator = entities.iterator();
		T result = iterator.next();
		return result;
	}	
	/**
	 *根据Id查询某条数据，并封装成对应的实体
	 * @param eid
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Entity> T get(Long eid,Class<T> clazz) throws Exception{
		
		T result = this.get(eid, null,clazz);
	 
		return result;
	}
	/**
	 * 根据where条件查询
	 * @param where  例如 当clazz=Book.class时 where="and bookName='java'";
	 * @param clazz  
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> EntitySet<T> getByWhere(String where, Object[] params,Class<T> clazz) throws Exception{
		EntitySet<T> result;
		EntityClass<? extends Entity> entityClass = SessionFactory.getEntityClass(clazz);
		String sql = "select * from "+entityClass.getEntityInfo().getName();
		
		if(where != null && ! "".equals(where.trim()))
		{
			if(!where.trim().toLowerCase().startsWith("and ")){
				where = "and "+where;
			}
			sql+=" where 1=1 "+ where;			
		}
		result = (EntitySet<T>) excuteQuery(sql, params, new OREntityMapper<T>(clazz));
		
		return result;
	}
	/**
	 * 根据sql查询
	 * @param sql  完整的sql 查询的表名必须与clazz对应;
	 * @param clazz  实体对应的Class
	 * @return
	 * @throws Exception 
	 */
	public <T extends Entity> EntitySet<T> getBySql(String sql,Object[] params,Class<T> clazz) throws Exception{
		EntitySet<T> result;
		result = (EntitySet<T>) excuteQuery(sql,params , new OREntityMapper<T>(clazz));
		
		return result;
	}
	public <T extends Entity> Set<T> getByLeftJoin(String leftJoinSql,Class<T>[] classes){
		this.getQueryConnection();

		
		
		return null;
	}
	
	
	
	
	/**
	 * 根据sql语句和参数查询，将结果封装成set
	 * @param selectSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Set<Map<String, Object>> get(String selectSql,Object[] params,String[] properties) throws Exception{
		Set<Map<String, Object>> result = (Set<Map<String, Object>>) excuteQuery( selectSql, params, new ORMapMapper(selectSql,properties));
		return result;
	}
	/**
	 * 根据sql语句和参数查询，将结果封装成set
	 * @param selectSql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Set<Map<String, Object>> get(String selectSql) throws Exception{
		Set<Map<String, Object>> result = (Set<Map<String, Object>>) excuteQuery( selectSql, new Object[]{}, new ORMapMapper(selectSql,null));
		return result;
	}	
	/**
	 * 根据sql语句和参数查询,进行自定义处理
	 * @param selectSql
	 * @param rsh 结果集处理
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public <T> Collection<T> get(String selectSql,ORMapper<T> orMapper,Object[] params) throws Exception{
		Collection<T> result = excuteQuery( selectSql, params, orMapper);
		return result;
	}
	/**
	 * 执行update,delete,insert语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int excuteSql(String sql,Object[] params) throws Exception{
		beginTransaction();
		try {
			PreparedStatement stmt = this.transactionConn.prepareStatement(sql);
			fillStatement(stmt, params);
			int rowCount = stmt.executeUpdate();
			stmt.close();
			return rowCount;
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}		 
	}
	/**
	 * 执行update,delete,insert语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int[] excuteSql(String sql,Object[][] params) throws Exception{
		beginTransaction();
		int[] result;
		int[] oneBatch = null;
		int index=0;
		int count=0;
		try {
			PreparedStatement stmt = this.transactionConn.prepareStatement(sql);
			if(params!=null){
				 result = new int[params.length];
				for(int i=0;i<params.length;i++)
				{
					fillStatement(stmt, params[i]);
					stmt.addBatch();
					
					if(++count % batchSize == 0) {
						oneBatch = stmt.executeBatch();
						for (int one : oneBatch) {
							result[index++] = one;
						}
				    }
				}
				oneBatch = stmt.executeBatch();
				for (int one : oneBatch) {
					result[index++] = one;
				}
			}else{
				stmt.addBatch();
				result = stmt.executeBatch();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
		return result;
	}
	private <T> Collection<T>  excuteQuery(String sql, Object[] params,ORMapper<T> orMapper) throws Exception {
		this.getQueryConnection();

		Collection<T>  resultList = null;
		try {
			ResultSet rs = this.getResultSetBySql(sql, params);
			resultList = orMapper.maps(rs);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}finally
		{
			this.closeQueryConn();
		}
		return resultList;
	}

	public ResultSet getResultSetBySql(String sql, Object[] params) throws Exception {
		ResultSet rs = null;
		
		this.getQueryConnection();

		try {
			PreparedStatement stmt = this.queryConnection.prepareStatement(sql);
			stmt.setFetchSize(30);
			fillStatement(stmt, params);
			rs = stmt.executeQuery();
			rs.setFetchSize(30);
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}finally
		{
		}
		return rs;
	}	
	public ResultSet getResultSetBySql(String sql) throws Exception {
		return this.getResultSetBySql(sql,null);
	}		
	private void fillStatement(PreparedStatement stmt, Object[] params)throws SQLException {
		if(params == null) return;
		
		ParameterMetaData pmd = stmt.getParameterMetaData();
		int stmtCount = pmd.getParameterCount();
		int paramsCount = params.length;

		if (stmtCount != paramsCount) {
			System.err.println("stmtCount:"+stmtCount+",paramsCount:"+paramsCount);
			throw new SQLException("SQL语句参数个数不正确: 需要 ："
					+ stmtCount + "个, 提供： " + paramsCount);
		}
		
		for (int i = 0; i < params.length; i++) {
			EntityClass.setPreparedStatement(stmt, i + 1, params[i]);
		}
	}
	
	public void cacheEntity(String entityName,Long id,Entity entity)
	{
		this.getEntityCache().put(entityName, id,entity);			
	}
	public Entity getEntityCached(String entityName,Long id)
	{
		return this.getEntityCache().get(entityName, id);			
	}
	
	/**
	 * 清除所有缓存
	 */
	public void clearCache(){
		this.getEntityCache().clear();
	}
	
	/**
	 * 清除某个类型的实体缓存
	 * @param entityName
	 * @return
	 */
	public void clearByEntityName(String entityName){
		this.getEntityCache().clearByEntityName(entityName);
	}
	/**
	 * 清除某个类型的实体缓存
	 * @param entityName
	 * @return 返回清除的实体
	 */
	public <T extends Entity> void clearByEntityName(Class<T> clazz){
		this.getEntityCache().clearByEntityName(clazz);
	}
	/**
	 * 清除某个实体的缓存
	 * @param entityName
	 * @param id
	 * @return 返回清除的实体
	 */
	public Entity clearByEntityId(String entityName,Long id){
		return this.getEntityCache().clearByEntityId(entityName, id);
	}
	/**
	 * 清除某个实体的缓存
	 * @param clazz
	 * @param id
	 * @return 返回清除的实体
	 */
	public <T extends Entity> Entity clearByEntityId(Class<T> clazz,Long id){
		return this.getEntityCache().clearByEntityId(clazz, id);
	}
	
	
	/**
	 * 清除某个实体的缓存
	 * 
	 * @return 返回清除的实体
	 */
	public <T extends Entity> Entity clearByEntity(T entity){
		return this.getEntityCache().clearByEntity(entity);
	}
	
	public Connection getQueryConnection(){
		try {
			if(this.queryConnection != null)
			{
				if(! this.queryConnection.isClosed())
				{
					return this.queryConnection;
				}
			}
			if(this.transactionStatus != null && !this.transactionStatus.isCompleted())
			{
				this.queryConnection = this.transactionConn;
			}
			else
			{
				this.queryConnection = SessionFactory.getConnection();
				this.queryConnection.setReadOnly(true);
			}
			
			return this.queryConnection;
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw new DDDException(exception); //这里处理是为了兼容暂时时的处理方式，徐传运
		}
	}
	public void closeConn() {
		this.commitTransaction();
		this.closeQueryConn();
		
	}	
	public void closeQueryConn() {
		try {
			if (this.queryConnection!=null && !this.queryConnection.isClosed() && !this.queryConnection.equals(this.transactionConn)) 
			{
				this.queryConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw new DDDException(e);
		} finally {
			this.queryConnection = null;
		}
	}
 
	/**
	 * 释放当前线程的session
	 */
	public void releaseSession()
	{
		if(SessionFactory.getThreadSession() == this)
		{
			SessionFactory.releaseSession();
		}
		else
		{
			this.closeConn();
		}
	}	
	/**
	 * 开启事务
	 * @throws SQLException 
	 */
	public void beginTransaction() throws SQLException{
		if(this.transactionStatus == null|| this.transactionStatus.isCompleted()){
			//如果有事务连接就把普通查询连接关闭，并把事务连接作为查询连接
			this.closeQueryConn();
			
			DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
			//spring事务的传播行为  PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
			transactionDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
			//事务的隔离级别  ISOLATION_READ_COMMITTED：表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。
			transactionDefinition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_READ_COMMITTED);
			//开启事务
			this.transactionStatus = this.transactionManager.getTransaction(transactionDefinition);
			DataSource dataSourcel = this.transactionManager.getDataSource();
			
			this.transactionConn =  DataSourceUtils.getConnection(dataSourcel);
			this.queryConnection = this.transactionConn;
		}
	}
	/**
	 * 提交事务
	 * @throws SQLException 
	 */
	public void commitTransaction(){
		clearCache();
		closeQueryConn();
		try {
			if (this.transactionConn != null && !this.transactionConn.isClosed()) 
			{
				if (!this.transactionConn.getAutoCommit()) {
					try {
						//conn.commit();
						this.transactionManager.commit(transactionStatus);
					} catch (TransactionException e) {
						//conn.rollback();
						this.transactionManager.rollback(transactionStatus);
					} 
				}
				this.transactionConn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw new DDDException(exception);
		} finally{
			this.transactionConn=null;
		}
	}
	/**
	 * 回滚事务
	 */
	public void rollbackTransaction(){
		clearCache();
		closeQueryConn();
		try {
			if (this.transactionConn != null && !this.transactionConn.isClosed()) 
			{
				try {
					if (!this.transactionConn.getAutoCommit()) {
						this.transactionManager.rollback(transactionStatus);
					}
				} catch (TransactionException e) {
					e.printStackTrace();
				} finally{
					this.transactionConn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw new DDDException(exception);
		} finally{
			this.transactionConn=null;
		}
	}

	public void setEntityFieldValue(Entity entity,String fieldName,Object value)
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		entityClass.setFieldValue(fieldName, entity, value);
	}
	public Object getEntityFieldValue(Entity entity,String fieldName)
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		Object value =entityClass.getFieldValue(fieldName, entity);
		return value;
	}	
	
	public <T extends Entity> void lazyLoad(T entity){
		try {
			this.get(entity.getEId(), entity,entity.getClass());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T extends Entity> void lazyLoad(String listField,T entity){
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		Method method = entityClass.getColumnInfo(listField).getFieldSetter();
		ColumnInfo columnInfo = entityClass.getColumnInfo(listField);
		EntityClass<? extends Entity> manySideEntityClass = SessionFactory.getEntityClass(columnInfo.getClazz());
		
		String sql = entityClass.getClassOne2ManySelectSQLs().get(listField);
		Set<Entity> list = null;
		try {
			list = (Set<Entity>)this.excuteQuery( sql, new Object[]{entity.getEId()}, new OREntityMapper<Entity>(manySideEntityClass));
			
		} catch (Exception e) {
			throw new DDDException(e);
		}
		try {
			method.invoke(entity, list);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}


	public EntityCache getEntityCache() {
		return entityCache;
	}


	public void executeSqls(Collection<String> sqls) throws Exception {
		if(sqls==null || sqls.size()==0){
			return;
		}
		String sql = null;
		try {
			beginTransaction();
			Statement statement = transactionConn.createStatement();
			for (String s : sqls) {
				sql = s;
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Exception exception = SQLExceptionHelper.transformException(e);
			throw exception;
		}
	}
	public Connection getTransactionCon() throws SQLException{
		beginTransaction();
		return transactionConn;
	}
	
	public <T extends Entity> Object getFieldValue(String fieldName,T entity){
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entity.getClass());
		
		Object fieldValue = entityClass.getFieldValue(fieldName, entity);
		return fieldValue;
	}
	
}
