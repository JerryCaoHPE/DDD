package ddd.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class EntityUtil {
	/**
	 * 深度克隆实体
	 * @param entity
	 * @return
	 */
	public static <T> T clone(T entity){
		T result = null;
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream arrayInputStream;
		ObjectInputStream objectInputStream;
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(arrayOutputStream);
			objOut.writeObject(entity);
			
			arrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
			objectInputStream = new ObjectInputStream(arrayInputStream);
			result = (T) objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return result;
	}
	/**
	 * 获取T类型的主键值，Long是主键的值，Object主键对应的实体
	 * @param list
	 * @return
	 */
	public static Map<Long, Entity> getEIds(Set<Entity> list,Connection connection){
		Map<Long, Entity> result = new HashMap<Long, Entity>();
		if(list.size()==0){
			return result;
		}
		Iterator<Entity> iterator = list.iterator();
		Entity entity = iterator.next();
		EntityClass<?> entityClass = SessionFactory.getEntityClass((Class<Entity>)entity.getClass());
		iterator = list.iterator();
		while(iterator.hasNext())
		{
			entity = iterator.next();
			Long eid = entityClass.getEid(entity);
			if(eid==null){
				eid = SessionFactory.getNewPrimarykey(entityClass.getClazz(),connection,false);
				entityClass.setEid(entity, eid);
			}
			result.put(eid,entity);
		}
		
		return result;
	}
	
//	public static Object clearProperty(Object bean,String name)
//	{
//		try
//		{
//			PropertyUtils.setNestedProperty(bean, name, null);
//		} catch (Exception e)
//		{
//			throw new DDDException("clearProperty","清除实体的属性出错，原因是："+e.getMessage(),e);
//		}
//		
//		return bean;
//	}
	/**
	 * 清除对象属性，即设为null,主要用于数据传输到前端前，消除循环引用
	 * @param bean
	 * @param   names 所指属性对应的对象的所有属性 ,清除对象的固定的级联对象，例如传入student.class.college。如果中间的属性为集合，也是可以的
	 */
	public static void setProperty(Object bean, String names,Object newValue)
	{
		if (bean == null) {
			return ;
		}
 	 
		try {
			String name = StringUtils.substringBefore(names, ".");
			//如果是要设置的目标属性，则设备为null,并返回
			if(names.equals(name))
			{
				PropertyUtils.setSimpleProperty(bean, name,newValue);
				return ;
			}			
			Object value = PropertyUtils.getSimpleProperty(bean, name);
			if (value == null) {
				return ;
			}

			names = StringUtils.substringAfter(names, ".");
			if(value instanceof Collection)
			{
				@SuppressWarnings("rawtypes")
				Iterator iterator = ((Collection)value).iterator();
				while(iterator.hasNext())
				{
					setProperty(iterator.next(), names,newValue);
				}
			}
			else
			{
				clearProperty(value,names);
			}
			
		 
		} catch (Exception e) {
			DDDException dddException = new DDDException("clearProperty","清除属性"+names+"出错，原因是："+e.getMessage(), e);
			throw dddException;
		}
	}	
	/**
	 * 清除对象属性，即设为null,主要用于数据传输到前端前，消除循环引用
	 * @param bean
	 * @param   names 所指属性对应的对象的所有属性 ,清除对象的固定的级联对象，例如传入student.class.college。如果中间的属性为集合，也是可以的
	 */
	public static void clearProperty(Object bean, String names)
	{
		setProperty(bean,names,null);
	}
	/**
	 * 加载对象属性，主要用于数据传输到前端前，加载需要传输的数据
	 * @param bean
	 * @param   names 所指属性对应的对象的所有属性 ,对象的固定的级联对象，例如传入student.class.college。如果中间的属性为集合，也是可以的
	 */
	public static void loadLazyProperty(Object bean, String names)
	{
		if (bean == null) {
			return ;
		}
 	 
		try {
			String name = StringUtils.substringBefore(names, ".");
			Object value = PropertyUtils.getSimpleProperty(bean, name);
			if (value == null) {
				return ;
			}
			//如果是要设置的目标属性，则设备为null,并返回
			if(names.equals(name))
			{
				return ;
			}
			names = StringUtils.substringAfter(names, ".");
			if(value instanceof Collection)
			{
				@SuppressWarnings("rawtypes")
				Iterator iterator = ((Collection)value).iterator();
				while(iterator.hasNext())
				{
					loadLazyProperty(iterator.next(), names);
				}
			}
			else
			{
				loadLazyProperty(value,names);
			}
			
		 
		} catch (Exception e) {
			DDDException dddException = new DDDException("clearProperty","清除属性"+names+"出错，原因是："+e.getMessage(), e);
			throw dddException;
		}
	}	
}
