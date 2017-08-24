package ddd.simple.dao.organization.impl;

import java.util.HashMap;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.organization.EntityPropertyDao;
import ddd.simple.entity.organization.EntityProperty;
import ddd.simple.entity.organization.EntityPropertyDefine;

@Service
public class EntityPropertyDaoBean extends BaseDao implements EntityPropertyDao
{
	@Override
	public EntityProperty saveEntityProperty(EntityProperty entityProperty)  throws Exception{
		return this.save(entityProperty);
	}

	@Override
	public int deleteEntityProperty(Long entityPropertyId)  throws Exception{
		return this.deleteById(entityPropertyId,EntityProperty.class);
	}

	@Override
	public EntityProperty updateEntityProperty(EntityProperty entityProperty)  throws Exception{
		return this.update(entityProperty);
	}

	@Override
	public EntityProperty findEntityPropertyById(Long entityPropertyId)  throws Exception{
		return this.query(entityPropertyId, EntityProperty.class);
	}
	
	@Override
	public EntitySet<EntityProperty> findAllEntityProperty() throws Exception {
		return this.query("",EntityProperty.class);
	}
	@Override
	public EntitySet<EntityProperty> findEntityPropertyByIdandType(Long id,String type) throws Exception {
		String where = "entity = "+"'"+type+"' and entityId = "+id;
		return this.query(where,EntityProperty.class);
	}
	@Override
	public EntitySet<EntityProperty> saveEntityProperties(EntitySet<EntityPropertyDefine> entityProperties,String entity,Long entityId){
		EntitySet<EntityProperty> result = new EntitySet<EntityProperty>();
		try{
			java.util.Iterator<EntityPropertyDefine> ite = entityProperties.iterator();
			while(ite.hasNext()){
				EntityPropertyDefine temp = ite.next();
				if(!(temp.getDefaultValue().equals(""))){
					EntityProperty ep = new EntityProperty();
					ep.setEntity(entity);
					ep.setEntityId(entityId);
					ep.setName(temp.getName());
					ep.setType(temp.getType());
					ep.setStringValue(temp.getDefaultValue());
					this.save(ep);
					result.add(ep);
				}
			}
		}catch(Exception e){
			
		}
		return result;
	}

	@Override
	public EntityProperty updataEntityProperty(String where, Object newValue)
			throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("StringValue", newValue);
		int i = this.update("entityproperty",hashMap,where);
		if(i==0){
			return null;
		}else
		return new EntityProperty();
	}


	
}
