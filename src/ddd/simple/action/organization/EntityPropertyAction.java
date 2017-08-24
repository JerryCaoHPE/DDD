package ddd.simple.action.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;

import com.sun.xml.internal.stream.Entity;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.Department;
import ddd.simple.entity.organization.Employee;
import ddd.simple.entity.organization.EntityProperty;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.entity.organization.Organization;
import ddd.simple.service.organization.DepartmentService;
import ddd.simple.service.organization.EmployeeService;
import ddd.simple.service.organization.EntityPropertyService;
import ddd.simple.service.organization.OrganizationService;

@Action
@RequestMapping("/EntityProperty")
@Controller
public class EntityPropertyAction {
	@Resource(name = "entityPropertyServiceBean")
	private EntityPropertyService entityPropertyService;

	public static String type = "";

	public EntityProperty saveEntityProperty(EntityProperty entityProperty) {
		try {
			EntityProperty saveEntityProperty = this.entityPropertyService
					.saveEntityProperty(entityProperty);
			return saveEntityProperty;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public int deleteEntityPropertise(Long id,String type) {
		try {
			String uniqueinfo = id+"#"+type;
			return this.entityPropertyService.deleteEntityProperties(uniqueinfo);
		} catch (DDDException e) {
			throw e;
		}

	}

	public EntityProperty updateEntityProperty(EntityProperty entityProperty) {
		try {
			EntityProperty updateEntityProperty = this.entityPropertyService
					.updateEntityProperty(entityProperty);
			return updateEntityProperty;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntityProperty findEntityPropertyById(Long entityPropertyId) {
		try {
			EntityProperty findEntityProperty = this.entityPropertyService
					.findEntityPropertyById(entityPropertyId);
			return findEntityProperty;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<EntityProperty> findAllEntityProperty() {
		try {
			EntitySet<EntityProperty> allEntityProperty = this.entityPropertyService
					.findAllEntityProperty();
			return allEntityProperty;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	//同时保存多个附加字段的值
	public boolean  saveAllProperty(EntitySet<EntityPropertyDefine> properties,String type,Long id){
		if(this.entityPropertyService.saveEntityProperties(properties,type,id)!=null){
			return true;
		}
		return false;
	}
	//通过实体类别和ID 查找一个实体的实例的附加字段
	public EntitySet<EntityProperty> findEntityPropertyByIdandType(Long id,String type) {
		try {
			EntitySet<EntityProperty> allEntityProperty = this.entityPropertyService.findEntityPropertyByIdandType(id, type);
			return allEntityProperty;
		} catch (DDDException e) {
			throw e;
		}
	}
	//更新
	// 更新附加属性
	public boolean updateEntityPropertyByIdAndType(EntitySet<EntityPropertyDefine> properties, String type, Long id) {
		EntitySet<EntityProperty> result = new EntitySet<EntityProperty>();
		Iterator<EntityPropertyDefine> ite = properties.iterator();
		while (ite.hasNext()) {
			EntityPropertyDefine propertiyWithNewValue = ite.next();
			String fieldName = propertiyWithNewValue.getName();
			String nuiqueInfo = id + "#" + fieldName + "#" + type;
			String newValue = propertiyWithNewValue.getDefaultValue();
			EntityProperty ep = this.entityPropertyService.updataEntityProperty(nuiqueInfo, newValue);
			if (ep == null) {
				// 表示更新失败需要插入
				EntityProperty tempep = new EntityProperty();
				tempep.setEntity(type);
				tempep.setEntityId(id);
				tempep.setName(fieldName);
				tempep.setStringValue(newValue);
				tempep.setType(propertiyWithNewValue.getType());
				this.saveEntityProperty(tempep);
				result.add(tempep);
			}else{
				result.add(ep);
			}
		}
		if(result==null)return false;
		return true;
	}
}
