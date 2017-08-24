package ddd.base.exporter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import ddd.base.annotation.ColumnInfo;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.Session;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.FileUtil;
import ddd.base.util.JSONUtil;

public class ExporterEngnine {
	private static final Object[] getterParam = new Object[]{};

	private Session session ;
	public String export(String configString,List<Long> ids,OutputStream out)
	{
		try {
			this.session = SessionFactory.getThreadSession();
			
			configString = beforeDecodeConfig(configString);
			
			Config config = JSON.parseObject(configString, Config.class);
			
			config = afterDecodeConfig(config);
			
			EntitySet<Entity> entitys = this.findData(config.getEntityName(),ids);
			
			Queue<ConfigEntity> queue = new LinkedList<ConfigEntity>();
			for (Entity entity : entitys) {
				queue.add(new ConfigEntity(config, entity));
				
				while(!queue.isEmpty()){
					loadentityFields(queue);
				}
			}
			
			
			String datas = JSONUtil.entityToJSON(entitys, true).getJson().toString();
			
			Map<String,String> data = new HashMap<String, String>();
			data.put("config", JSON.toJSONString(config));
			data.put("datas", datas);
			String jsonStr = JSON.toJSONString(data);
			FileUtil.writeToFile(out, jsonStr);
			out.close();
			return jsonStr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("导出错误", e.getMessage(), e);
		}
	}
	
	private void loadentityFields(Queue<ConfigEntity> queue)
	{
		ConfigEntity configEntity = queue.poll();
		
		Entity entity = configEntity.getEntity();
		Config config = configEntity.getConfig();
		if(config.getEntityFields()==null){
			return;
		}
		EntityClass<?> entityClass = SessionFactory.getEntityClass(config.getEntityName());
		for(String fieldName: config.getEntityFields().keySet())
		{
			Config fieldEntityConfig =  config.getEntityFields().get(fieldName);
			ColumnInfo columnInfo = entityClass.getFieldColumnInfo(fieldName);
			if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
			{
				Entity fieldEntity = (Entity) entityClass.getFieldValue(columnInfo, entity);
				if(fieldEntity!=null){
					//利用懒加载，加载该实体
					fieldEntity.getInputCode();
					queue.add(new ConfigEntity(fieldEntityConfig, fieldEntity));
				}
			}else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
				//利用懒加载，加载该集合
				this.invokeGetter(columnInfo.getFieldGetter(),entity);
				
				EntitySet<Entity> fieldEntities = (EntitySet<Entity>) entityClass.getFieldValue(columnInfo, entity);
				for (Entity fieldEntity : fieldEntities) {
					queue.add(new ConfigEntity(fieldEntityConfig, fieldEntity));
				}
			}
		}
	}
	
	private void invokeGetter(Method getMethod,Entity entity){
		try {
			getMethod.invoke(entity, getterParam);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private  EntitySet<Entity> findData(String entityName, List<Long> ids) throws Exception {
		EntityClass<?> entityClass = SessionFactory.getEntityClass(entityName);
		StringBuilder where = new StringBuilder("EId in( ");
		
		for (Long eid : ids) {
			where.append(eid);
			where.append(',');
		}
		where.deleteCharAt(where.length()-1);
		where.append(')');
		
		return (EntitySet<Entity>) this.session.getByWhere(where.toString(), null, entityClass.getClazz());
	}
	
	
	public String beforeDecodeConfig(String configString){
		return configString;
	}
	
	public Config afterDecodeConfig(Config config){
		return config;
	}
	
	
	
	public void importData(InputStream in){
		try {
			
			String dataString = FileUtil.readeString(in);
			importData(dataString);
		}catch(DDDException e){
			throw e;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("导入错误", e.getMessage(), e);
		}
	}
	
	public void importData(String dataString){
		try {
			this.session = SessionFactory.getThreadSession();
			
			Map<String,String> data = JSON.parseObject(dataString, new TypeReference<Map<String,String>>(){});
			
			Config config = JSON.parseObject(data.get("config"), Config.class);
			
			EntityClass<?> entityClass = SessionFactory.getEntityClass(config.getEntityName());
			
			List<? extends Entity> datas = JSON.parseArray(data.get("datas"), entityClass.getClazz());
			
			//清除所有的EId
			for (Entity entity : datas) {
				clearAllEId(new ConfigEntity(config, entity));
			}
			
			Stack<ConfigEntity> entitiesStack = new Stack<ConfigEntity>();
			for (Entity entity : datas) {
				entitiesStack.push(new ConfigEntity(config, entity));
				
				while (!entitiesStack.empty()) {
					saveEntityField(entitiesStack);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("导入错误", e.getMessage(), e);
		}
	}
	
	private void clearAllEId(ConfigEntity configEntity){
		
		Entity entity = configEntity.getEntity();
		entity.setEId(null);
		entity.setNewEntity(true);
		
		Config config = configEntity.getConfig();
		
		if(config.getEntityFields()==null){
			return;
		}
		EntityClass<?> entityClass = SessionFactory.getEntityClass(config.getEntityName());
		for(String fieldName: config.getEntityFields().keySet())
		{
			Config fieldEntityConfig =  config.getEntityFields().get(fieldName);
			
			ColumnInfo columnInfo = entityClass.getFieldColumnInfo(fieldName);
			
			if(!fieldEntityConfig.getOnlyExportPk())
			{
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					Entity fieldEntity = (Entity) entityClass.getFieldValue(columnInfo, entity);
					if(fieldEntity!=null){
						clearAllEId(new ConfigEntity(fieldEntityConfig, fieldEntity));
					}
				}else if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
					
					EntitySet<Entity> fieldEntities = (EntitySet<Entity>) entityClass.getFieldValue(columnInfo, entity);
					for (Entity fieldEntity : fieldEntities) {
						clearAllEId(new ConfigEntity(fieldEntityConfig, fieldEntity));
					}
				}
			}
			
		}
	}
	
	private void saveEntityField(Stack<ConfigEntity> entitiesStack) throws Exception {
		
		ConfigEntity configEntity = entitiesStack.pop();
		
		Entity entity = configEntity.getEntity();
		
		Config config = configEntity.getConfig();
		if(config.getEntityFields()==null){
			return;
		}
		EntityClass<?> entityClass = SessionFactory.getEntityClass(config.getEntityName());
		
		//保存实体类型的属性
		//只处理1对1的情况
		for(String fieldName: config.getEntityFields().keySet())
		{
			Config fieldEntityConfig =  config.getEntityFields().get(fieldName);
			
			ColumnInfo columnInfo = entityClass.getFieldColumnInfo(fieldName);
			//只导出唯一标识列的情况，从数据库中查询建立外键
			if(fieldEntityConfig.getOnlyExportPk())
			{
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					Entity fieldEntity = (Entity) entityClass.getFieldValue(columnInfo, entity);
					if(fieldEntity==null){
						continue;
					}
					Object pkFieldValue = this.session.getFieldValue(fieldEntityConfig.getPkFieldName(), fieldEntity);
					if(pkFieldValue==null){
						continue;
					}
					Entity dbFieldEntity = this.queryOne(fieldEntityConfig.getPkFieldName()+"=?",new Object[]{pkFieldValue}, fieldEntity.getClass());
					
					entityClass.setFieldValue(columnInfo, entity, dbFieldEntity);
				}

			}
			else
			{
				//数据全部导出的情况
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2ONE)
				{
					Entity fieldEntity = (Entity) entityClass.getFieldValue(columnInfo, entity);
					if(fieldEntity!=null){
						entitiesStack.push(new ConfigEntity(fieldEntityConfig, fieldEntity));
						saveEntityField(entitiesStack);
					}
				}
			}
		}
		
		//保存自己
		if(entity.isNewEntity()){
			session.saveEntity(entity, false);
		}
		
		//保存集合类型的属性
		//只处理1对多的情况
		for(String fieldName: config.getEntityFields().keySet())
		{
			Config fieldEntityConfig =  config.getEntityFields().get(fieldName);
			
			ColumnInfo columnInfo = entityClass.getFieldColumnInfo(fieldName);
			//只导出唯一标识列的情况，从数据库中查询建立外键
		/*	if(fieldEntityConfig.getOnlyExportPk())
			{
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
					
				}
			}
			else
			{*/
				//数据全部导出的情况
				if(columnInfo.getType() == ColumnInfo.COLUMN_TYPE_ONE2MANY){
					
					EntitySet<Entity> fieldEntities = (EntitySet<Entity>) entityClass.getFieldValue(columnInfo, entity);
					for (Entity fieldEntity : fieldEntities) {
						entitiesStack.push(new ConfigEntity(fieldEntityConfig, fieldEntity));
						saveEntityField(entitiesStack);
					}
				}
//			}
		}
		
	}
	
	
	private <T extends Entity> T queryOne(String where,Object[] params,Class<T> clazz) throws Exception{
		EntitySet<T> entitySet = this.session.getByWhere(where, params, clazz);
		for (T entity : entitySet) {
			return entity;
		}
		return null;
	}
}
