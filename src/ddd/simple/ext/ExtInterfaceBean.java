package ddd.simple.ext;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.persistence.ExtInterface;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.dao.model.ModelDao;
import ddd.simple.entity.model.Model;
import ddd.simple.util.model.CreateModelEntityClass;

@Service
public class ExtInterfaceBean implements ExtInterface{
	
	@Resource(name = "modelDaoBean")
	private ModelDao modelDao;
	
	@Override
	public EntityClass<? extends Entity> getEntityClass(String tableName) {
		try {
			Model model = modelDao.findModelByEnglishName(tableName);
			if(model==null){
				return null;
			}
			EntityClass<Entity> entityClass = CreateModelEntityClass.create(model);
			SessionFactory.addDynamicEntityClass(entityClass);
			return entityClass;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
