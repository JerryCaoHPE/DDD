package ddd.base.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import ddd.base.Config;
import ddd.base.dbmanager.TableGenerator;
import ddd.base.dbmanager.bean.TypeKeeper;
import ddd.base.dbmanager.tableHandler.TableHandler;
import ddd.base.dbmanager.tableHandler.mysql.MysqlTableHandler;
import ddd.base.dbmanager.tableHandler.oracle.OracleTableHandler;
import ddd.base.dbmanager.util.TypeGetter;
import ddd.base.dynamicSql.SqlHandler;
import ddd.base.dynamicSql.mysql.MysqlSqlHandler;
import ddd.base.dynamicSql.oracle.OracleSqlHandler;
import ddd.base.exception.DDDException;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.persistence.pkManager.PrimaryKeyEngine;
import ddd.base.util.ClassUtil;
import ddd.base.util.SpringContextUtil;
import ddd.simple.entity.billCode.BillCode;

public class SessionFactory {
	
	private static String dataBase="";
	
	private static DataSource dataSource;
	
	private static ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();

	private static Map<Class<? extends Entity>, EntityClass<? extends Entity>> allEntityClasses = new HashMap<Class<? extends Entity>, EntityClass<? extends Entity>>();
	
	private static Map<String,EntityClass<? extends Entity>> dynamicEntityClass = new HashMap<String, EntityClass<? extends Entity>>();
	
	private static Map<String, Class> dynamicClass = new HashMap<String, Class>();
	
	private static TableHandler tableHandler;
	
	private static SqlHandler sqlHandler;
	/**
	 * 扫描所有实体的注解，并生成实体信息对象
	 */
	public static void initContext(){
		//获取所有的带有Entity注解的类
		List<Class<? extends Entity>> classes = ClassUtil.getClassListByAnnotation(Config.packageName, ddd.base.annotation.Entity.class);
		classes.remove(Entity.class);//不包括Entity自己
		//将获取到的所有Class放入allEntityClasses中保存
		for (Class<? extends Entity> clazz : classes) {
			allEntityClasses.put(clazz, new EntityClass(clazz));
		}
		//调用所有的EntityClass的前置初始化方法
		for (EntityClass<? extends Entity> entityClass : allEntityClasses.values()) {
			//前置初始化
			entityClass.preInit();
		}
		//调用所有的EntityClass的后置初始化方法
		for (EntityClass<? extends Entity> entityClass : allEntityClasses.values()) {
			//后置初始化
			entityClass.postInit();
		}
		//显示扫描到的实体
		showEntity(true);
		//初始化连接池
		initDataSource();
		
		//初始化数据库 Database 和 主键
		initDbAndPK();
	}
	
	public static void initDbAndPK(){
		//建立实体表
		//TableHander.tableGenerator();
		try {
			TableGenerator.generator(getTableHandler(),allEntityClasses.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//初始化主键表
		PrimaryKeyEngine.init();
	}
	public static TableHandler getTableHandler(){
		if(tableHandler==null){
			if(SessionFactory.isMysql()){
				tableHandler= new MysqlTableHandler();
			}else if(SessionFactory.isOracle()){
				tableHandler= new OracleTableHandler();
			}
		}
		return tableHandler;
	}
	public static SqlHandler getSqlHandler(){
		if(sqlHandler==null){
			if(SessionFactory.isMysql()){
				sqlHandler= new MysqlSqlHandler();
			}else if(SessionFactory.isOracle()){
				sqlHandler= new OracleSqlHandler();
			}
		}
		return sqlHandler;
	}
	
	public static String getDBType(String javaType,Integer size){
		TypeKeeper keeper = TypeGetter.getByJavaType(javaType);
		if(SessionFactory.isMysql()){
			if(size!=null&&size>255&&javaType.equalsIgnoreCase("java.lang.String")){
				return "LONGTEXT";
			}
			return keeper.getMysqlType();
		}else if(SessionFactory.isOracle()){
			if(size!=null&&size>255&&javaType.equalsIgnoreCase("java.lang.String")){
				return "CLOB";
			}
			return keeper.getOracleType();
		}
		return null;
	}
	
	
	private static void showEntity(boolean showEntity){
		if(showEntity){
			System.out.println("扫描到的实体：");
			for (Class<? extends Entity> entity : allEntityClasses.keySet()) {
				System.out.println(entity.getName());
			}
		}
	}
	
	/**
	 * 初始化连接池
	 */
	private static void initDataSource(){
//		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//		try {
//			Map<String, String> mapPropterties = Loader.instance().load(Config.jdbcConfig);
//			String url = mapPropterties.get("url");
//			comboPooledDataSource.setUser(mapPropterties.get("username"));
//			comboPooledDataSource.setPassword(mapPropterties.get("password"));
//			comboPooledDataSource.setJdbcUrl(url);
//			comboPooledDataSource.setDriverClass(mapPropterties.get("driverClassName"));
//			database=url.split(":")[1];
//			if("".endsWith(database)){
//				System.err.println("数据库获取失败");
//			}
//			
//			SessionFactory.dataSource = comboPooledDataSource;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (PropertyVetoException e) {
//			e.printStackTrace();
//		}
		SessionFactory.dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
		
	}
	/**
	 * 获取新的session对象
	 * @return
	 */
	public static Session getNewSession( ){
		Session session = new Session();
		return session;
	}	
	
	/**
	 * 获取当前线程的session对象
	 * @return
	 */
	public static Session getThreadSession( ){
		if(sessionLocal.get() == null)
		{
			Session session = new Session();
			sessionLocal.set(session);
		}
		return sessionLocal.get();
	}
	
	/**
	 * 释放当前线程的session
	 */
	public static void releaseSession()
	{
		if(sessionLocal.get() != null)
		{
			sessionLocal.get().closeConn();
			sessionLocal.set(null);
		}		
	}
	
	
	public static <T extends Entity>  T createEntity(Class<T> clazz,Long id)
	{
		EntityClass<? extends Entity> entityClass = allEntityClasses.get(clazz);
		
		T entity = (T) entityClass.createEntity();
		entityClass.setEid(entity, id);
		return entity;
	}
	public static <T extends Entity> T createEntity(Class<T> clazz)
	{
		Connection connection = SessionFactory.getConnection();
		Long id = getNewPrimarykey(clazz,connection,true);
		return createEntity(clazz, id);
	}
	
	/**
	 * 申请一个新的T类型表的主键
	 * @param clazz
	 * @return
	 * 这个方法保留是为了兼容 
	 */
	@Deprecated
	public static <T> Long getNewPrimarykey(Class<T> clazz,Connection connection,boolean isCloseCon){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(clazz);
		 
		return eid;
	}
	/**
	 * 申请一个新的T类型表的主键
	 * @param clazz
	 * @return
	 */
	public static <T> Long getNewPrimarykey(Class<T> clazz){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(clazz);
		
		return eid;
	}	
	/**
	 * 申请一个tableName表的最新主键,
	 * @param clazz
	 * @return
	 * 此方法留着兼容
	 */
	@Deprecated
	public static Long getNewPrimarykey(String keyName,Connection connection,boolean isCloseCon){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(keyName);
		return eid;
	}
	
	/**
	 * 申请一个表的最新主键
	 * @param clazz
	 * @return
	 */
	public static Long getNewPrimarykey(String keyName){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(keyName);
		return eid;
	}
	/**
	 * 申请一个非表的序列 
	 * @param clazz
	 * @return
	 */
	public static Long getNewPrimarykey(String keyName,String tableName,String fieldName){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(keyName,tableName,fieldName);
		return eid;
	}		
	/*
	 * 申请一个编码序列
	 * 
	 */
	public static Long getNewPrimarykey(String baseCode,BillCode billCode){
		Long eid = PrimaryKeyEngine.getNewPrimaryKey(baseCode,billCode);
		return eid;
	}	
	
	/**获取扫描到的所有实体
	 * @return
	 */
	public static Map<Class<? extends Entity>, EntityClass<? extends Entity>> getEntityClasses(){
		return allEntityClasses;
	}
	
	/**得到指定class类型的EntityClass
	 * @param clazz
	 * @return
	 */
	public static  <T> EntityClass<? extends Entity> getEntityClass(Class<T> clazz){
		return allEntityClasses.get(clazz);
	}
	/**
	 * 
	* @Title: getEntityClass 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param className
	* @return EntityClass<? extends Entity> ,如果没有找到实体，则返回 null
	 */
	public static  EntityClass<? extends Entity> getEntityClassByName(String className){
		Class clazz = null;
		try
		{
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e)
		{
			return null;
			//throw new DDDException("getEntityClass", "取实体的信息时出错，原因是："+e.getMessage(), e);
		}
		return getEntityClass(clazz);
	}	
	public static EntityClass<? extends Entity> getEntityClass(String tableName){
		for (Class<? extends Entity> clazz : allEntityClasses.keySet()) {
			EntityClass<? extends Entity> entityClass = allEntityClasses.get(clazz);
			if(entityClass.getEntityInfo().getName().equalsIgnoreCase(tableName)){
				return entityClass;
			}
		}
		for (String dynamicTableName : dynamicEntityClass.keySet()) {
			if(dynamicTableName.equalsIgnoreCase(tableName)){
				return dynamicEntityClass.get(dynamicTableName);
			}
		}
		//bean可能不存在
		try {
			ExtInterface extInterface = (ExtInterface) SpringContextUtil.getBean("extInterfaceBean");
			if(extInterface!=null){
				return extInterface.getEntityClass(tableName);
			}
		} catch (NoSuchBeanDefinitionException e) {
			
		}
		return null;
	}
	
	
	public static DataSource getDataSource() {
		return dataSource;
	}

	/*public static void setDataSource(DataSource dataSource) {
		SessionFactory.dataSource = dataSource;
	}*/
	
	public static Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeConn(Connection conn){
		try {
			if (conn != null && !conn.isClosed()) 
			{
				if (!conn.getAutoCommit()) {
					try {
						conn.commit();
					} catch (SQLException e) {
						conn.rollback();
					} finally {
						conn.setAutoCommit(true);
					}
				}
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public static void commitTransaction(){
		if(sessionLocal.get()!=null){
			sessionLocal.get().commitTransaction();
		}
	}
	public static void rollbackTransaction(){
		if(sessionLocal.get()!=null){
			sessionLocal.get().rollbackTransaction();
		}
	}
	
	
	public static String getDatabase() {
		return dataBase;
	}
	public static void setDatabase(String database) {
		SessionFactory.dataBase = database;
	}
	public static boolean isOracle(){
		return dataBase.toLowerCase().equals("oracle");
	}
	
	public static boolean isMysql(){
		return dataBase.toLowerCase().equals("mysql");
	}
	public static Map<String, String> getConfig() {
		return Config.config;
	}

	public static Map<String, Class> getDynamicClass() {
		return dynamicClass;
	}

	public static void addDynamicClass(String key,Class dynamicClass) {
		allEntityClasses.remove(SessionFactory.dynamicClass.get(key));
		SessionFactory.dynamicClass.put(key, dynamicClass);
		EntityClass entityClass = new EntityClass<Entity>(dynamicClass);
		entityClass.preInit();
		entityClass.postInit();
		allEntityClasses.put(dynamicClass, entityClass);
	}
	
	public static Map<String, EntityClass<? extends Entity>> getDynamicEntityClass() {
		return dynamicEntityClass;
	}
	
	public synchronized static void addDynamicEntityClass(EntityClass<? extends Entity> entityClass) {
		dynamicEntityClass.put(entityClass.getEntityInfo().getName(), entityClass);
	}

	public static int executeSqlWithoutTransaction(String sql)
	{
		try
		{
			Connection connection = dataSource.getConnection();
			connection.setAutoCommit(true);
			try
			{
				Statement statement = connection.createStatement();
				return statement.executeUpdate(sql);
			}
			finally
			{
				connection.close();
			}
			
		} catch (SQLException e)
		{
			//e.printStackTrace();
			throw new DDDException("executeSqlWithoutTransaction", "执行无事务SQL出错，原因是："+e.getMessage()+",sql:"+sql, e);
		}
	}
 
	public  static String getTableName(Class clazz){
		EntityClass entityClass=SessionFactory.getEntityClass(clazz);
		
		return entityClass.getEntityInfo().getName();//获取表名
	}
	public static String getTableName(String clazzName){
		Class clazz;
		try {
			clazz = Class.forName(clazzName);
			return getTableName(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DDDException("没有找到实体类 "+clazzName, e);
		}
	}	
}	
