package ddd.base.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

/**
* 类名称：ListMapHander
* 类描述： 实现<code>ResultSetHander</code>接口,用于将结果集每一行里面的的放入Map<String,Object>中，然后在将这个Map存入List中。
 */
public class OREntityMapper<T extends Entity>  implements ORMapper<T> {

	private EntityClass<? extends Entity> entityClass ;
	private T entity;
	
	public OREntityMapper(EntityClass<? extends Entity> manySideEntityClass) {
		super();
		this.entityClass = manySideEntityClass;
	}
	public OREntityMapper(EntityClass<? extends Entity> entityClass,T entity) {
		super();
		this.entityClass = entityClass;
		this.entity = entity;
	}	
	public OREntityMapper(Class<T> clazz) {
		super();
		this.entityClass = SessionFactory.getEntityClass(clazz);
	}
	@Override
	public EntitySet<T> maps(ResultSet rs) throws SQLException {
		EntitySet<T> resultList = new EntitySet<T>(); 
        
        while(rs.next()){
        	T entity = this.map(rs);
        	resultList.add(entity);
        }
		return resultList;
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		T entity = null;
		try {
			if(this.entity != null)
			{
				entity = this.entityClass.toEntity(rs,this.entity);
			}
			else
			{
				entity = this.entityClass.toEntity(rs);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

}
