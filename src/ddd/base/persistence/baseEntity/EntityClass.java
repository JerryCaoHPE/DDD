package ddd.base.persistence.baseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.javax.el.MethodNotFoundException;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.annotation.ColumnInfo;
import ddd.base.annotation.EntityInfo;
import ddd.base.exception.ClassMatchException;
import ddd.base.exception.DDDException;
import ddd.base.exception.PrimaryKeyException;
import ddd.base.persistence.JoinTableInfo;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.ClassAssist;
import ddd.base.util.ClassUtil;


/**
 * 实体信息类
 */
public class EntityClass<T extends Entity> {
	/**
	 * 类的包名
	 */
	private String packageName; 
	/**
	 * 当前EntityClass对应的Class
	 */
	private Class<? extends Entity> clazz;
	/**
	 * 类的名称
	 */
	private String className; 
	
	/**
	 * 当前实体的实体注解
	 */
	private EntityInfo entityInfo;

	/**
	 * String是类的属性名，即Field.name,Field属性上的Field
	 */
	private LinkedHashMap<String,Field> classFields = new LinkedHashMap<String, Field>();

	/**
	 * String是类的属性名，即Field.name,ColumnInfo是Field属性上的注解
	 */
	private LinkedHashMap<String,ColumnInfo> classColumnInfos = new LinkedHashMap<String, ColumnInfo>();
	
	/**
	 * String是数据库字段名,ColumnInfo是Field属性上的注解
	 */
	private LinkedHashMap<String,ColumnInfo> columnInfos = new LinkedHashMap<String, ColumnInfo>();
	
	/**
	 * String是数据库字段名,Field属性上的Field
	 */
	private LinkedHashMap<String,Field> fields = new LinkedHashMap<String, Field>();
	
	
	/**
	 * 主键的Field
	 */
	private Field idField;
	
	/**
	 * 主键的Field的注解
	 */
	private ColumnInfo idColumnInfo;
	
	/**
	 * 一对多关系中，例如：学生和书的关系，书中的外键studentId的Field
	 * String是数据库字段名,Field属性上的Field
	 */
	private Map<String,Field> one2OneFields = new HashMap<String, Field>();

	/**
	 *  一对多关系中，例如：学生和书的关系，书中的外键studentId的Field上的Column注解
	 * String是是数据库字段名,Column是Field属性上的注解
	 */
	private Map<String,ColumnInfo> one2OneFieldColumnInfos = new HashMap<String, ColumnInfo>();
	
	/**
	 *  一对多关系中，例如：学生和书的关系，书中的外键studentId的Field对应的Book
	 * String是数据库字段名,EntityClass是外键关联的对象的实体信息类
	 */
	private Map<String,EntityClass<?>> one2OneFieldEntityClasses = new HashMap<String, EntityClass<?>>();
	
	/**
	 *  一对多关系中，例如：学生和书的关系，
	 * String是属性名,Field是学生的书（EntitySet<book> books）对应的Field
	 */
	private Map<String,Field> one2ManyFields = new HashMap<String, Field>();
	
	/**
	 *  一对多关系中，例如：学生和书的关系，
	 * String是属性名,Column是学生的书（EntitySet<book> books）对应的Field上的注解
	 */
	private Map<String,ColumnInfo> one2ManyFieldColumnInfos=new HashMap<String, ColumnInfo>();

	/**
	 *  一对多关系中查询多的一端的select 语句，
	 * 参数一是 类的属性名,即 Field.name,参数二是查询语句 
	 */
	private Map<String,String> classOne2ManySelectSQLs=new HashMap<String, String>();

	
	/**
	 *  一对多关系中，例如：学生和书的关系，
	 * String是属性名,EntityClass是学生的书（EntitySet<book> books）对应的Field上的注解的clazz的EntityClass，即book的EntityClass
	 */
	private Map<String,EntityClass<?>> one2ManyFieldEntityClasses=new HashMap<String, EntityClass<?>>();
	
	/**
	 * 中间表信息
	 */
	private Map<String,JoinTableInfo> joinTableInfos = new HashMap<String, JoinTableInfo>();
	/**
	 * 所有字段的名称
	 */
	private String[] properties;
	
	private Field lazyLoadField;
	
	private String insertSql;
	private String deleteSql;
	private String updateSql;
	private String selectSql;
	
	public EntityClass(Class<? extends Entity> clazz) {
		this.clazz = clazz;
		this.className= clazz.getSimpleName();
		if(clazz.getPackage()!=null){
			this.packageName= clazz.getPackage().getName();
		}
		this.entityInfo = this.toEntityInfo(clazz.getAnnotation(ddd.base.annotation.Entity.class));		
	}
	
	/**
	 * 前置初始化，即在postInit之前执行
	 */
	public void preInit(){
		Field[] fields = ClassUtil.getClassNestedFields(this.clazz);
		
		Method[] methods = ClassUtil.getClassNestedMethods(this.clazz);
		
		List<String> names = new ArrayList<String>();
		ClassAssist classAssist = new ClassAssist(this.getClazz());
		for (Field field : fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(Column.class)){
				ColumnInfo columnInfo = toColumnInfo(field,methods,classAssist);
				this.columnInfos.put(columnInfo.getName(),columnInfo );
				this.fields.put(columnInfo.getName(), field);
				
				if(field.getAnnotation(Column.class).Id()){
					this.idField = field;
					this.idColumnInfo = this.columnInfos.get(columnInfo.getName());
					names.add(columnInfo.getName());
				}else if( columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY  ){
					this.one2ManyFields.put(columnInfo.getName(), field);
					this.one2ManyFieldColumnInfos.put(columnInfo.getName(), columnInfo);
				}else if( columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE ){
					this.one2OneFields.put(columnInfo.getName(), field);
					this.one2OneFieldColumnInfos.put(columnInfo.getName(), columnInfo);
					names.add(columnInfo.getName());
				}else{
					names.add(columnInfo.getName());
				}
				this.classFields.put(field.getName(), field);
				this.classColumnInfos.put(field.getName(), columnInfo);	
			}
		}
		properties= new String[names.size()];
		this.properties = names.toArray(properties);
		try {
			if(this.clazz.equals(Entity.class)){
				this.lazyLoadField = this.clazz.getDeclaredField("initialized");
			}else{
				this.lazyLoadField = this.clazz.getSuperclass().getDeclaredField("initialized");
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 后置初始化，即在所有类的preInit执行完成后执行
	 */
	public void postInit()
	{
		initRelationEntityClass();
		this.insertSql = createInsertSql();
		this.updateSql = createUpdateSql();
		this.deleteSql = createDeleteSql();
		this.setSelectSql(createSelectSql());
		this.createOne2ManySelectSql();
	}
	public void initRelationEntityClass()
	{
		EntityClass<?> entityClass = this;
		
		Map<String, Field> one2OneFields = entityClass.getOne2OneFields();
		
		for (String key : one2OneFields.keySet()) {
			Map<String, EntityClass<?>> one2OneEntityClasses = entityClass.getOne2OneFieldEntityClasses();
			
			one2OneEntityClasses.put(key, SessionFactory.getEntityClass(one2OneFields.get(key).getType()));
		}
		
		Map<String, ColumnInfo> one2ManyColumnInfos = entityClass.getOne2ManyFieldColumnInfos();
		
		for (String key : one2ManyColumnInfos.keySet()) {
			Map<String, EntityClass<?>> one2ManyEntityClasses = entityClass.getOne2ManyFieldEntityClasses();
			
			one2ManyEntityClasses.put(key, SessionFactory.getEntityClass(one2ManyColumnInfos.get(key).getClazz()));
		}	
		for (String key : one2ManyColumnInfos.keySet()) {
			ColumnInfo  foreignColumn = one2ManyColumnInfos.get(key);
			//如果是聚合关系（不是组合），则需要建立中间表，下面构建中间表的相关信息
			if(foreignColumn.isComposition() == false)
			{
				EntityInfo hostEntityInfo=entityClass.getEntityInfo();
				EntityInfo foreignEntityInfo=SessionFactory.getEntityClass(foreignColumn.getClazz()).getEntityInfo();
				String foreignTableName=foreignEntityInfo.getName();
				String hostTableName=hostEntityInfo.getName();
				
				String joinTableName=foreignColumn.getJoinTableName();
				String joinTableOneSide=foreignColumn.getJoinTableOneSide();
				String joinTableManySide=foreignColumn.getJoinTableManySide();
				if(joinTableName.equals(""))
				{
					joinTableName=hostTableName+"_"+foreignColumn.getName();
					foreignColumn.setJoinTableName(joinTableName);
				}
				if(joinTableOneSide.equals(""))
				{
					joinTableOneSide=hostTableName+"EId";
					foreignColumn.setJoinTableOneSide(joinTableOneSide);
				}
				if(joinTableManySide.equals(""))
				{
					joinTableManySide=foreignTableName+"EId";
					foreignColumn.setJoinTableManySide(joinTableManySide);
				}
				JoinTableInfo joinTableInfo = new JoinTableInfo();
				joinTableInfo.setJoinTableName(joinTableName);
				joinTableInfo.setJoinTableOneSide(joinTableOneSide);
				joinTableInfo.setJoinTableManySide(joinTableManySide);
				joinTableInfo.setManySideEntityClass(entityClass);
				SessionFactory.getEntityClass(foreignColumn.getClazz()).getJoinTableInfos().put(joinTableName, joinTableInfo);
			}
			else
			{
				//如果是组合关系，则需要JoinColumn来维护关系，如果为空，则以一端的表名加Eid作为JoinColumn
				if( "".equals(foreignColumn.getJoinColumn()))
				{
					foreignColumn.setJoinColumn(this.entityInfo.getName()+"_"+this.idColumnInfo.getName());
				}
			}
		}		
	}
	private String createInsertSql(){
		int size = 0;
		
		StringBuffer sbColumsNames = new StringBuffer();
		for (String key : this.columnInfos.keySet()) {
			//TODO 怎么判断？
			if(this.columnInfos.get(key).getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE
					|| this.columnInfos.get(key).getType() == ColumnInfo.COLUMN_TYPE_PRIMARY){
				size++;
				
				sbColumsNames.append(key+",");
			}
		}
		String columsNames = sbColumsNames.toString().substring(0, sbColumsNames.length()-1);
		
		String paras = createInsertValues(size);
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(this.entityInfo.getName());
		sb.append(" (");
		sb.append(columsNames);
		sb.append(") values(");
		sb.append(paras);
		sb.append(")");
		
		return sb.toString();
	}

	public static String createInsertValues(int count){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<count;i++){
			sb.append("?,");
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	
	private String createUpdateSql(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(this.entityInfo.getName());
		sb.append(" set ");
		
		for (String key : this.columnInfos.keySet()) {
			ColumnInfo columnInfo = this.columnInfos.get(key);
			if( (this.columnInfos.get(key).getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE
					|| this.columnInfos.get(key).getType() == ColumnInfo.COLUMN_TYPE_PRIMARY)
					&& columnInfo.isId() == false){
				sb.append(columnInfo.getName());
				sb.append("=?,");
			}
		}
		sb = sb.delete(sb.length()-1, sb.length());
		sb.append(" where ");
		sb.append(this.idColumnInfo.getName());
		sb.append("=?");
		
		return sb.toString();
	}
	
	private String createDeleteSql(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("delete from ");
		sb.append(this.entityInfo.getName());
		sb.append(" where ");
		sb.append(this.idColumnInfo.getName());
		sb.append("=?");
		
		return sb.toString();
	}
	private String createSelectSql(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from ");
		sb.append(this.entityInfo.getName());
		sb.append(" where ");
		sb.append(this.idColumnInfo.getName());
		sb.append("=?");
		
		return sb.toString();
	}	
	public void createOne2ManySelectSql(){
		EntityClass<T> entityClass = this;
		String sql ="";

		for(ColumnInfo columnInfo : this.one2ManyFieldColumnInfos.values())
		{
			EntityClass<? extends Entity> manySideEntityClass = SessionFactory.getEntityClass(columnInfo.getClazz());
			//如果是组合关系，则从多的一边的外键字段中查询，否则从中间表中查询
			if(columnInfo.isComposition() == true)
			{
					sql = "select * from "+ manySideEntityClass.getEntityInfo().getName() 
							+" where "+columnInfo.getJoinColumn()+"=?";
					if(! "".equals(columnInfo.getOrderBy()))
					{
						sql += " order by "+columnInfo.getOrderBy();
					}
			}
			else
			{
				sql = "select f.* from "+columnInfo.getJoinTableName() +" j"
						+" left outer join " + manySideEntityClass.getEntityInfo().getName() +" f"
						+ " on j."+columnInfo.getJoinTableManySide()+" = "+"f."+entityClass.getIdColumnInfo().getName()
						+" where j."+columnInfo.getJoinTableOneSide()+ " =? " ;
				if(! "".equals(columnInfo.getOrderBy()))
				{
					sql += " order by f."+columnInfo.getOrderBy()+",j.displayIndex ";
				}
				else
				{
					sql += " order by j.displayIndex ";
				}
			}
			this.getClassOne2ManySelectSQLs().put(columnInfo.getField().getName(), sql);
		}
	}	
	
	private Method getMethed(Method[] methods,String methedName){
		for (Method method : methods) {
			if(method.getName().equals(methedName))
			{
				method.setAccessible(true);
				return method;
			}
		}
		throw new MethodNotFoundException("在加载实体类"+this.packageName+"."+this.className+" 的："+methedName+"方法未找到");
	}
	
	private ColumnInfo toColumnInfo(Field field,Method[] methods,ClassAssist classAssist){
		Column column = field.getAnnotation(Column.class);
		ColumnInfo columnInfo = new ColumnInfo();
		
		columnInfo.setId(column.Id());
		
		columnInfo.setName("".equals(column.name().trim()) ? field.getName() : column.name());
		
		columnInfo.setFKName(column.FKName());
		
		columnInfo.setLabel("".equals(column.label().trim()) ? field.getName() : column.label());
		
		columnInfo.setCodeTable(column.codeTable());
		
		columnInfo.setColumnDefinition(column.columnDefinition());
		
		columnInfo.setLength(column.length());
		
		columnInfo.setNullable(column.nullable());
		
		columnInfo.setPrecision(column.precision());
		
		columnInfo.setScale(column.scale());
		
		if(columnInfo.isId() == true)
		{
			columnInfo.setUnique(true);
		}
		else
		{
			columnInfo.setUnique(column.unique());
		}
		columnInfo.setUniqueName(column.uniqueName());
		if(Collection.class.isAssignableFrom(field.getType()))
		{
			columnInfo.setType(ColumnInfo.COLUMN_TYPE_ONE2MANY);
			Class<?> cl = classAssist.getFieldGenericType(field.getName());
			if(cl == null)
			{
				System.err.println("类 "+this.clazz.getName()+" 的属性 "+field.getName()+
						" 是集合类型，但没有配置泛型类");
			}
			else
			{
				columnInfo.setClazz(cl);
			}
		}
		else if(Entity.class.isAssignableFrom(field.getType()))
		{
			columnInfo.setType(ColumnInfo.COLUMN_TYPE_ONE2ONE);
			columnInfo.setClazz(field.getType());
		}
		else if(field.getType() == String.class ||
			java.lang.Boolean.class == field.getType() || 
			java.lang.Character.class == field.getType() || 
			java.lang.Byte.class == field.getType() || 
			java.lang.Short.class == field.getType() || 
			java.lang.Integer.class == field.getType() || 
			java.lang.Long.class == field.getType() || 
			java.lang.Float.class == field.getType() || 
			java.lang.Double.class == field.getType() ||
			java.util.Date.class == field.getType() ||
			java.sql.Date.class == field.getType() ||
			java.sql.Timestamp.class == field.getType() )
			{
			
			columnInfo.setType(ColumnInfo.COLUMN_TYPE_PRIMARY);
			columnInfo.setClazz(field.getType());
		}else if(field.getType().isPrimitive()){
			throw new DDDException("请使用包装类型："+field);
		}
		else
		{
			columnInfo.setType(ColumnInfo.COLUMN_TYPE_INVALID);
			columnInfo.setClazz(field.getType());
		}
 			
		columnInfo.setJoinColumn(column.joinColumn());
		
		columnInfo.setJoinTableName(column.joinTableName());
		
		columnInfo.setJoinTableOneSide(column.joinTableOneSide());
		
		columnInfo.setJoinTableManySide(column.joinTableManySide());
		
		columnInfo.setComposition(column.composition());
		
		columnInfo.setDeleteType(column.deleteType());
		
		columnInfo.setComment(column.comment());
		
		columnInfo.setIndex(column.index());
		
		columnInfo.setFieldType(field.getType());
		
		columnInfo.setOrderBy(column.orderBy());
		
		columnInfo.setField(field);
		
		
		String fieldNameFirstUp = field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		columnInfo.setFieldGetter(this.getMethed(methods, "get"+fieldNameFirstUp));
		columnInfo.setFieldSetter(this.getMethed(methods, "set"+fieldNameFirstUp));

		return columnInfo;
	}
	
	private EntityInfo toEntityInfo(ddd.base.annotation.Entity entity){
		EntityInfo entityInfo = new EntityInfo();
		if(entity.name().equals("")){
			entityInfo.setName(this.clazz.getSimpleName().toLowerCase());
		}else{
			entityInfo.setName(entity.name());
		}
		
		if(entity.label().equals("")){
			entityInfo.setLabel(this.clazz.getSimpleName().toLowerCase());
		}else{
			entityInfo.setLabel(entity.label());
		}
		
		entityInfo.setComment(entity.comment());
		
		return entityInfo;
	}
	
	public static void setPreparedStatement(PreparedStatement preparedStatement,int index,Object value) throws SQLException{
		if(value!=null){
			Class clazz = value.getClass();
			if(java.util.Date.class.equals(clazz)||java.sql.Date.class.equals(clazz)){
				value = new java.sql.Timestamp(((java.util.Date)value).getTime());
			}
		}
		preparedStatement.setObject(index, value);
	}
	
	
	/**
	 * 填充插入语句，支持preparedStatement.addBatch();
	 * @param preparedStatement
	 * @param entity
	 * @return
	 * @throws SQLException
	 * @throws ClassMatchException
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 */
	public PreparedStatement fillInsertStatement(PreparedStatement preparedStatement,Entity entity) throws 
		SQLException, ClassMatchException, IllegalAccessException,IllegalArgumentException, InvocationTargetException {
		if(entity.getClass().equals(this.clazz)){
			int index=1;
			
			Long eid = entity.getEId();
			if(eid==null || eid<=0){
				eid = SessionFactory.getNewPrimarykey(clazz);
				entity.setEId(eid);
			}
			
			for (ColumnInfo columnInfo: this.columnInfos.values()) {
				//Field field = columnInfo.getField();
				//field.setAccessible(true);
				Method methodGetter = columnInfo.getFieldGetter();
				//Object refObject = field.get(entity);
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					Object value = methodGetter.invoke(entity, new Object[]{});
					if(value!=null){
						EntityClass<?> entityClass = SessionFactory.getEntityClass(value.getClass());
						preparedStatement.setObject(index, entityClass.getEid(value));
					}else{
						preparedStatement.setObject(index, null);
					}
					index++;
				}else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_PRIMARY){
					Object value = methodGetter.invoke(entity, new Object[]{});
					setPreparedStatement(preparedStatement, index, value);
					index++;
				}
			}
			preparedStatement.addBatch();//把这条执行语句加到PreparedStatement对象的批处理命令中 
			
		}else{
			throw new ClassMatchException("类型不匹配");
		}
		
		return preparedStatement;
	}
	/**
	 * 填充删除语句，支持preparedStatement.addBatch();
	 * @param preparedStatement
	 * @param entityId
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement fillDeleteSql(PreparedStatement preparedStatement,Long entityId) throws SQLException {
		preparedStatement.setObject(1, entityId);
		preparedStatement.addBatch();
		return preparedStatement;
	}
	/**
	 * 填充删除语句，支持preparedStatement.addBatch();
	 * @param preparedStatement
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement fillDeleteSql(PreparedStatement preparedStatement,Entity entity) throws SQLException {
		if(entity.getClass().equals(this.clazz)){
			preparedStatement.setObject(1, entity.getEId());
			preparedStatement.addBatch();
		}else{
			throw new ClassMatchException("类型不匹配");
		}
		return preparedStatement;
	}
	
	/**
	 * 填充更新语句，
	 * @param preparedStatement
	 * @param entity
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 */
	public PreparedStatement fillUpdateSql(PreparedStatement preparedStatement,Entity entity) throws 
		SQLException, ClassMatchException, IllegalAccessException,IllegalArgumentException, InvocationTargetException{
		if(! entity.getClass().equals(this.clazz))
		{
			throw new ClassMatchException("类型不匹配");
		}
		int index=1;
		
		Long eid = entity.getEId();
		if(eid==null || eid<=0){
			throw new PrimaryKeyException("主键为空");
		}
		for (ColumnInfo columnInfo : this.columnInfos.values()) {
			//Field field = columnInfo.getField();
			//field.setAccessible(true);
			//Object refObject = field.get(entity);
			Method methodGetter = columnInfo.getFieldGetter();
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE){
				Object value = methodGetter.invoke(entity, new Object[]{});
				if(value!=null){
					EntityClass<?> entityClass = SessionFactory.getEntityClass(value.getClass());
					preparedStatement.setObject(index, entityClass.getEid(value));
				}else{
					preparedStatement.setObject(index, null);
				}
				index++;
			}else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_PRIMARY 
					&& columnInfo.isId() == false){
				Object value = methodGetter.invoke(entity, new Object[]{});
				setPreparedStatement(preparedStatement, index, value);
				index++;
			}
		}
		preparedStatement.setObject(index, this.getEid(entity));
		
		preparedStatement.addBatch();//把这条执行语句加到PreparedStatement对象的批处理命令中 

		return preparedStatement;
	}
	
	/**
	 * 填充更新语句，
	 * @param preparedStatement
	 * @param entity
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 */
	public PreparedStatement fillUpdateSql(PreparedStatement preparedStatement,Entity entity,String[] columnNames) throws 
		SQLException, ClassMatchException, IllegalAccessException,IllegalArgumentException, InvocationTargetException{
		if(! entity.getClass().equals(this.clazz))
		{
			throw new ClassMatchException("类型不匹配");
		}
		int index=1;
		
		Long eid = entity.getEId();
		if(eid==null || eid<=0){
			throw new PrimaryKeyException("主键为空");
		}
		for (String columnName : columnNames) {
			ColumnInfo columnInfo = this.columnInfos.get(columnName);
			Method methodGetter = columnInfo.getFieldGetter();
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE){
				Object value = methodGetter.invoke(entity, new Object[]{});
				if(value!=null){
					EntityClass<?> entityClass = SessionFactory.getEntityClass(value.getClass());
					preparedStatement.setObject(index, entityClass.getEid(value));
				}else{
					preparedStatement.setObject(index, null);
				}
				index++;
			}else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_PRIMARY 
					&& columnInfo.isId() == false){
				Object value = methodGetter.invoke(entity, new Object[]{});
				setPreparedStatement(preparedStatement, index, value);
				index++;
			}
		}
		preparedStatement.setObject(index, this.getEid(entity));
		
		preparedStatement.addBatch();//把这条执行语句加到PreparedStatement对象的批处理命令中 

		return preparedStatement;
	}
	
	/**
	 * 数据库的结果集映射成对象
	 * @param rs
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	private <T extends Entity> T toEntity(ResultSet rs,T result,String prefix) throws IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException {
		try {
			String idColumnName = prefix+this.idColumnInfo.getName();
			int idColumnIndex = this.findColumn(rs, idColumnName);
			if(idColumnIndex == -1)
			{
				throw new DDDException("查询的结果集中不存在列名："+idColumnName);
			}
			Long idFieldValue =  rs.getLong(idColumnIndex);
			Entity refObjectCached= SessionFactory.getThreadSession().getEntityCached(this.entityInfo.getName(), idFieldValue); 
			if(refObjectCached != null)
			{
				if(refObjectCached.equals(result) && !refObjectCached.isInitialized()){
					
				}else{
					return (T)refObjectCached;
				}
			}
			for (ColumnInfo columnInfo:this.columnInfos.values() ) 
			{
				//Field field = columnInfo.getField();
				//field.setAccessible(true);
				Method methodSetter = columnInfo.getFieldSetter();
				String columnName = prefix+columnInfo.getName();
				int columnIndex = this.findColumn(rs, columnName);
				if(columnIndex == -1)
				{
					continue;
				}
				//如果是引用字段
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					//如果在数据集中存在形如 teacher_eid的类型的字段，表示需要在这个数据集中加载引用字段的属性,teacher表示在student表中引用字段teacher
					//的名称，eid是指teacher属性的id字段名称
					
					Long fieldValue = rs.getLong(columnIndex);
					
					if(!rs.wasNull()){
						EntityClass<?> entityClass = SessionFactory.getEntityClass(columnInfo.getField().getType());
						Entity refObject=null;
						if(columnInfo.getClazz().equals(result.getClass()) && fieldValue==idFieldValue){
							refObject=result;
						}else{
							refObject= SessionFactory.getThreadSession().getEntityCached(entityClass.entityInfo.getName(), fieldValue); 
							
						}
						if(refObject == null)
						{
							refObject = SessionFactory.createEntity(entityClass.getClazz(),0L);
							
//							String refColumnName =key+"_"+ entityClass.getIdColumnInfo().getName();
//							int refColumnIndex = this.findColumn(rs, refColumnName)	;
//							if(refColumnIndex == -1)
//							{
								/*Field refIdField = entityClass.getIdField();
								refIdField.setAccessible(true);
								refIdField.set(refObject, fieldValue);*/
								refObject.setEId(fieldValue);
								//引用对象可以懒加载(没有初始化)
								refObject.setNewEntity(false);
//							}
//							else
//							{
								//refObject = entityClass.toEntity(rs,refObject,columnName+"_");
//							}
							SessionFactory.getThreadSession().cacheEntity(entityClass.entityInfo.getName(), refObject.getEId(), refObject);
						}
						//field.set(result, refObject);
						methodSetter.invoke(result, refObject);
					}
				}
				else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_PRIMARY)
				{
					//如果是普通字段，排除集合字段
					//field.set(result, fieldValue);
					Object fieldValue = getRealTypeData(columnInfo.getClazz(), rs, columnIndex);
					if(rs.wasNull()){
						methodSetter.invoke(result, new Object[]{null});
					}else{
						methodSetter.invoke(result, new Object[]{fieldValue});
					}
					
				}
			}
			result.setInitialized(true);;
			//缓存实体到一级缓存 
			SessionFactory.getThreadSession().cacheEntity(this.getEntityInfo().getName(), result.getEId(), result);
			

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	

	
	public static Object getRealTypeData(Class clazz,ResultSet rs,int index) throws SQLException{
		if(Long.class.equals(clazz)||long.class.equals(clazz)){
			return rs.getLong(index);
		}else if(Integer.class.equals(clazz)||int.class.equals(clazz)){
			return rs.getInt(index);
		}else  if(Double.class.equals(clazz)||double.class.equals(clazz)){
			return rs.getDouble(index);
		}else if(Float.class.equals(clazz)||float.class.equals(clazz)){
			return rs.getFloat(index);
		}else if(Boolean.class.equals(clazz)||boolean.class.equals(clazz)){
			return rs.getBoolean(index);
		}else if(String.class.equals(clazz)){
			return rs.getString(index);
		}else if(java.util.Date.class.equals(clazz)||java.sql.Date.class.equals(clazz)||java.sql.Timestamp.class.equals(clazz)){
			return rs.getTimestamp(index);
		}else if(java.math.BigDecimal.class.equals(clazz)){
			return rs.getBigDecimal(index);
		}else if(byte[].class.equals(clazz)){
			return rs.getBytes(index);
		}
		return null;
	}
	
	
	
	public <T extends Entity> T toEntity(ResultSet rs,T result) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException 
	{
		return this.toEntity(rs, result,"");
	}
	/**
	 * 数据库的结果集映射成对象
	 * @param rs
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public <T extends Entity> T toEntity(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException {
		T result = null;
		try {
			result = (T) this.clazz.newInstance();
			result = this.toEntity(rs, result,"");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		}  
	
		return result;
		
	}
	private int findColumn(ResultSet rs,String columnLabel) 
	{
		try {
			return rs.findColumn(columnLabel);
		} catch (SQLException e) {
			//e.printStackTrace();
			return -1;
		}
	}	

	public Entity createEntity()
	{
		try {
			Entity entity =  clazz.newInstance();
			entity.setNewEntity(true);
			return entity;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}	
	public void setFieldValue(ColumnInfo columnInfo,Object entity,Object value){
		if(!this.clazz.equals(entity.getClass())){
			throw new ClassMatchException("类型不匹配");
		}
		try {
			columnInfo.getFieldSetter().invoke(entity, new Object[]{value});
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public void setFieldValue(String fieldName,Object entity,Object value){
		if(!this.clazz.equals(entity.getClass())){
			throw new ClassMatchException("类型不匹配");
		}
		ColumnInfo columnInfo = this.classColumnInfos.get(fieldName);
		if(columnInfo == null)
		{
			throw new ClassMatchException("在实体 "+this.entityInfo.getName()+" 中没找到属性 "+fieldName);
		}
		this.setFieldValue(columnInfo, entity, value);
	}
	
	public Object getFieldValue(ColumnInfo columnInfo,Object entity){
		if(!this.clazz.equals(entity.getClass())){
			throw new ClassMatchException("类型不匹配");
		}
		Object result =null;
		try {
			result = columnInfo.getField().get(entity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	
		return result;
	}
	public Object getFieldValue(String fieldName,Object entity){
		if(!this.clazz.equals(entity.getClass())){
			throw new ClassMatchException("类型不匹配");
		}
		ColumnInfo columnInfo = this.classColumnInfos.get(fieldName);
		if(columnInfo == null)
		{
			throw new ClassMatchException("在实体 "+this.entityInfo.getName()+" 中没找到属性 "+fieldName);
		}
		return this.getFieldValue(columnInfo, entity);
	}	
	public void setEid(Object entity,Object value){
		setFieldValue(this.idColumnInfo, entity, value);
	}
	
	public Long getEid(Object entity){
		return (Long) getFieldValue(this.idColumnInfo, entity);
	}
	/**
	 * 把类的属性名映射到表的字体名
	 * @param fieldName
	 * @return
	 */
	public String mapFieldName2ColumnName(String fieldName)
	{
		ColumnInfo columnInfo = this.classColumnInfos.get(fieldName);
		if(columnInfo == null)
		{
			throw new ClassMatchException("在映射类的属性名到表的字体名时出错，原因是在实体 "+this.entityInfo.getName()+" 中没找到属性 "+fieldName);
		}
		return columnInfo.getName();
	}
	
//	public void setLazyLoadField(Object entity,Object value){
//		setFieldValue(this.lazyLoadField, entity, value);
//	}
//	public boolean getLazyLoadFieldValue(Object entity){
//		return (boolean) getFieldValue(this.lazyLoadField, entity);
//	}
//	
	/**
	 * 根据注解上的name获取注解信息
	 * @param name
	 * @return
	 */
	public ColumnInfo getColumnInfo(String name){
		return this.columnInfos.get(name);
	}
	/**
	 * 根据属性名获取注解信息
	 * @param fieldName
	 * @return
	 */
	public ColumnInfo getFieldColumnInfo(String fieldName){
		return this.classColumnInfos.get(fieldName);
	}
	
	public Field getField(String name){
		return this.fields.get(name);
	}
	
	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public Class<? extends Entity> getClazz() {
		return clazz;
	}


	public void setClazz(Class<? extends Entity> clazz) {
		this.clazz = clazz;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public EntityInfo getEntityInfo() {
		return entityInfo;
	}


	public void setEntityInfo(EntityInfo entityInfo) {
		this.entityInfo = entityInfo;
	}


	public LinkedHashMap<String, ColumnInfo> getColumnInfos() {
		return columnInfos;
	}


	public void setColumnInfos(LinkedHashMap<String, ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}


	public LinkedHashMap<String, Field> getFields() {
		return fields;
	}


	public void setFields(LinkedHashMap<String, Field> fields) {
		this.fields = fields;
	}


	public Field getIdField() {
		return idField;
	}


	public void setIdField(Field idField) {
		this.idField = idField;
	}


	public ColumnInfo getIdColumnInfo() {
		return idColumnInfo;
	}


	public void setIdColumnInfo(ColumnInfo idColumnInfo) {
		this.idColumnInfo = idColumnInfo;
	}


	public Map<String, Field> getOne2OneFields() {
		return one2OneFields;
	}


	public void setOne2OneFields(Map<String, Field> one2OneFields) {
		this.one2OneFields = one2OneFields;
	}


	public Map<String, ColumnInfo> getOne2OneFieldColumnInfos() {
		return one2OneFieldColumnInfos;
	}


	public void setOne2OneFieldColumnInfos(
			Map<String, ColumnInfo> one2OneFieldColumnInfos) {
		this.one2OneFieldColumnInfos = one2OneFieldColumnInfos;
	}


	public Map<String, EntityClass<?>> getOne2OneFieldEntityClasses() {
		return one2OneFieldEntityClasses;
	}


	public void setOne2OneFieldEntityClasses(
			Map<String, EntityClass<?>> one2OneFieldEntityClasses) {
		this.one2OneFieldEntityClasses = one2OneFieldEntityClasses;
	}


	public Map<String, Field> getOne2ManyFields() {
		return one2ManyFields;
	}


	public void setOne2ManyFields(Map<String, Field> one2ManyFields) {
		this.one2ManyFields = one2ManyFields;
	}


	public Map<String, ColumnInfo> getOne2ManyFieldColumnInfos() {
		return one2ManyFieldColumnInfos;
	}


	public void setOne2ManyFieldColumnInfos(
			Map<String, ColumnInfo> one2ManyFieldColumnInfos) {
		this.one2ManyFieldColumnInfos = one2ManyFieldColumnInfos;
	}


	public Map<String, EntityClass<?>> getOne2ManyFieldEntityClasses() {
		return one2ManyFieldEntityClasses;
	}


	public void setOne2ManyFieldEntityClasses(
			Map<String, EntityClass<?>> one2ManyFieldEntityClasses) {
		this.one2ManyFieldEntityClasses = one2ManyFieldEntityClasses;
	}


	public String getInsertSql() {
		return insertSql;
	}


	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}


	public String getDeleteSql() {
		return deleteSql;
	}


	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}


	public String getUpdateSql() {
		return updateSql;
	}


	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}


	public Field getLazyLoadField() {
		return lazyLoadField;
	}


	public void setLazyLoadField(Field lazyLoadField) {
		this.lazyLoadField = lazyLoadField;
	}


	public LinkedHashMap<String,ColumnInfo> getClassColumnInfos() {
		return classColumnInfos;
	}


	public void setClassColumnInfos(LinkedHashMap<String,ColumnInfo> classColumnInfos) {
		this.classColumnInfos = classColumnInfos;
	}

	public Map<String,String> getClassOne2ManySelectSQLs() {
		return classOne2ManySelectSQLs;
	}

	public void setClassOne2ManySelectSQLs(Map<String,String> classOne2ManySelectSQLs) {
		this.classOne2ManySelectSQLs = classOne2ManySelectSQLs;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}	
	public Map<String, JoinTableInfo> getJoinTableInfos() {
		return joinTableInfos;
	}

	public void setJoinTableInfos(Map<String, JoinTableInfo> joinTableInfos) {
		this.joinTableInfos = joinTableInfos;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	
}
