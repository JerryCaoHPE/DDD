package ddd.simple.service.organization.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.organization.EntityPropertyDao;
import ddd.simple.entity.organization.EntityProperty;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.organization.EntityPropertyService;

@Service
public class EntityPropertyServiceBean extends BaseService implements EntityPropertyService
{

	@Resource(name="entityPropertyDaoBean")
	private EntityPropertyDao entityPropertyDao;
	
	@Override
	public EntityProperty saveEntityProperty(EntityProperty entityProperty) 
	{
		try {
			return this.entityPropertyDao.saveEntityProperty(entityProperty);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEntityProperty", e.getMessage(), e);
		}
	}

	@Override
	public int deleteEntityProperty(Long entityPropertyId) {
		try {
			return this.entityPropertyDao.deleteEntityProperty(entityPropertyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteEntityProperty", e.getMessage(), e);
		}
		
	}
	
	@Override
	public int deleteEntityProperties(String uniqueInfo){
		try{
			String [] infos = uniqueInfo.split("#");
			String where = "entityId = '"+infos[0]+"' and entity = '"+infos[1]+"'";
			this.entityPropertyDao.deleteByWhere(where, EntityProperty.class);
		}catch(Exception e){
			e.printStackTrace();
			throw new DDDException("deleteEntityProperties", e.getMessage(), e);
		}
		return 0;
	}
	
	@Override
	public EntityProperty updateEntityProperty(EntityProperty entityProperty) {
		try {
			return this.entityPropertyDao.updateEntityProperty(entityProperty);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateEntityProperty", e.getMessage(), e);
		}
	}

	@Override
	public EntityProperty findEntityPropertyById(Long entityPropertyId) {
		try {
			return this.entityPropertyDao.findEntityPropertyById(entityPropertyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findEntityPropertyById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<EntityProperty> findAllEntityProperty() {
		try{
			return this.entityPropertyDao.findAllEntityProperty();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEntityProperty", e.getMessage(), e);
		}
	}
	@Override
	public EntitySet<EntityProperty> findEntityPropertyByIdandType(Long id,String type){
		try{
			return this.entityPropertyDao.findEntityPropertyByIdandType(id,type);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEntityProperty", e.getMessage(), e);
		}
	}
	@Override
	public EntitySet<EntityProperty> saveEntityProperties(EntitySet<EntityPropertyDefine> entityProperties,String entity,Long entityId) 
	{
		try {
			return this.entityPropertyDao.saveEntityProperties(entityProperties,entity,entityId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEntityProperty", e.getMessage(), e);
		}
	}

	@Override
	public EntityProperty updataEntityProperty(String uniqueInfo, Object newValue) {
		try {
			String [] infos = uniqueInfo.split("#");
			String where = "entityId = '"+infos[0]+"' and name ='"+infos[1]+"' and entity = "+"'"+infos[2]+"'";
			return this.entityPropertyDao.updataEntityProperty(where,newValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEntityProperty", e.getMessage(), e);
		}
	}
	
	
}
