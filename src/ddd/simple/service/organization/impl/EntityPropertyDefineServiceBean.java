package ddd.simple.service.organization.impl;

import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.dao.organization.EntityPropertyDao;
import ddd.simple.dao.organization.EntityPropertyDefineDao;
import ddd.simple.service.organization.EntityPropertyDefineService;

@Service
public class EntityPropertyDefineServiceBean extends BaseService implements EntityPropertyDefineService
{

	@Resource(name="entityPropertyDefineDaoBean")
	private EntityPropertyDefineDao entityPropertyDefineDao;
	@Resource(name="entityPropertyDaoBean")
	private EntityPropertyDao entityPropertyDao;
	@Override
	public EntitySet<EntityPropertyDefine> saveEntityPropertyDefine(EntitySet<EntityPropertyDefine> propertiesDefine) 
	{
		try {
			EntitySet<EntityPropertyDefine> re = new EntitySet<EntityPropertyDefine>();
			Iterator<EntityPropertyDefine> ite = propertiesDefine.iterator();
			while(ite.hasNext()){
				EntityPropertyDefine temp = ite.next();
				re.add(this.entityPropertyDefineDao.saveEntityPropertyDefine(temp));
			}
			return re;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEntityPropertyDefine", e.getMessage(), e);
		}
	}

	@Override
	public int deleteEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) {
		try {
			//删除一个附加属性定义的时候同时需要删除附加属性表中的与该附加属性相关的记录
			String tableName = "entityproperty";
			String where = " entity = '"+entityPropertyDefine.getEntity()+"' and "+"name = '"+entityPropertyDefine.getName()+"'";
			entityPropertyDao.delete(tableName, where);
			return this.entityPropertyDefineDao.deleteEntityPropertyDefine(entityPropertyDefine.getEId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteEntityPropertyDefine", e.getMessage(), e);
		}
		
	}

	@Override
	public EntityPropertyDefine updateEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) {
		try {
			return this.entityPropertyDefineDao.updateEntityPropertyDefine(entityPropertyDefine);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateEntityPropertyDefine", e.getMessage(), e);
		}
	}

	@Override
	public EntityPropertyDefine findEntityPropertyDefineById(Long entityPropertyDefineId) {
		try {
			return this.entityPropertyDefineDao.findEntityPropertyDefineById(entityPropertyDefineId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findEntityPropertyDefineById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<EntityPropertyDefine> findAllEntityPropertyDefine() {
		try{
			return this.entityPropertyDefineDao.findAllEntityPropertyDefine();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEntityPropertyDefine", e.getMessage(), e);
		}
	}
	@Override
	public EntitySet<EntityPropertyDefine> findEntityPropertyDefineByType(String type) {
		try{
			return this.entityPropertyDefineDao.findByType(type);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEntityPropertyDefine", e.getMessage(), e);
		}
	}
	

}
