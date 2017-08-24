package ddd.simple.service.entityTips.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.annotation.ColumnInfo;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.entityTips.EntityTips;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.dao.entityTips.EntityTipsDao;
import ddd.simple.service.entityTips.EntityTipsService;

@Service
public class EntityTipsServiceBean extends BaseService implements EntityTipsService
{

	@Resource(name="entityTipsDaoBean")
	private EntityTipsDao entityTipsDao;
	
	@Override
	public EntityTips saveEntityTips(EntityTips entityTips) 
	{
		try {
			return this.entityTipsDao.saveEntityTips(entityTips);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEntityTips", e.getMessage(), e);
		}
	}

	@Override
	public int deleteEntityTips(Long entityTipsId) {
		try {
			return this.entityTipsDao.deleteEntityTips(entityTipsId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteEntityTips", e.getMessage(), e);
		}
		
	}

	@Override
	public EntityTips updateEntityTips(EntityTips entityTips) {
		try {
			return this.entityTipsDao.updateEntityTips(entityTips);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateEntityTips", e.getMessage(), e);
		}
	}

	@Override
	public EntityTips findEntityTipsById(Long entityTipsId) {
		try {
			return this.entityTipsDao.findEntityTipsById(entityTipsId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findEntityTipsById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<EntityTips> findAllEntityTips() {
		try{
			return this.entityTipsDao.findAllEntityTips();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEntityTips", e.getMessage(), e);
		}
	}

	@Override
	public List<Map<String, String>> getAllField(String entityClassStr)
	{
		try
		{
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			
			EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(entityClassStr));
			LinkedHashMap<String, ColumnInfo> columnsInfos = entityClass.getColumnInfos();
			Set<String> keySet = columnsInfos.keySet();
			Iterator<String> ite = keySet.iterator();
			while (ite.hasNext())
			{
				HashMap<String, String> temp = new HashMap<String, String>();
				String fieldName = ite.next();
				ColumnInfo columnInfo = columnsInfos.get(fieldName);
				String showName = columnInfo.getLabel();
				temp.put(showName, fieldName);
				result.add(temp);
			}
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getAllField", e.getMessage(), e);
		}
	}

	@Override
	public EntitySet<EntityTips> findEntityTipsByName(String name) {

			try{
				return this.entityTipsDao.findEntityTipsByName(name);
			}catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("findAllEntityTips", e.getMessage(), e);
			}
		
	}

}
