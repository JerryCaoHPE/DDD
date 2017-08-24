package ddd.simple.action.organization;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.organization.EntityPropertyDefine;
import ddd.simple.service.organization.EntityPropertyDefineService;

@Action
@RequestMapping("/EntityPropertyDefine")
@Controller
public class EntityPropertyDefineAction
{
	@Resource(name="entityPropertyDefineServiceBean")
	private EntityPropertyDefineService entityPropertyDefineService;
	
	public boolean saveEntityPropertyDefine(EntitySet<EntityPropertyDefine> propertiesDefine)
	{
		try {
			 if(this.entityPropertyDefineService.saveEntityPropertyDefine(propertiesDefine)!=null){
				 return true;
			 }else{
				 return false;
			 }
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine){
		try {
			int i = this.entityPropertyDefineService.deleteEntityPropertyDefine(entityPropertyDefine);
			return i;
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public EntityPropertyDefine updateEntityPropertyDefine(EntityPropertyDefine entityPropertyDefine) {
		try {
			EntityPropertyDefine updateEntityPropertyDefine = this.entityPropertyDefineService.updateEntityPropertyDefine(entityPropertyDefine);
			return updateEntityPropertyDefine;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntityPropertyDefine findEntityPropertyDefineById(Long entityPropertyDefineId){
		try {
			EntityPropertyDefine findEntityPropertyDefine = this.entityPropertyDefineService.findEntityPropertyDefineById(entityPropertyDefineId);
			return  findEntityPropertyDefine;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<EntityPropertyDefine> findAllEntityPropertyDefine(){
		try{
			EntitySet<EntityPropertyDefine> allEntityPropertyDefine = this.entityPropertyDefineService.findAllEntityPropertyDefine();
			return allEntityPropertyDefine;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	
	//通过实体类型查找实体的附加字段
	public EntitySet<EntityPropertyDefine> findEntityPropertyDefineByType(String type){
		try{
			EntitySet<EntityPropertyDefine> allEntityPropertyDefine = this.entityPropertyDefineService.findEntityPropertyDefineByType(type);
			if(allEntityPropertyDefine == null)
				return new EntitySet<EntityPropertyDefine>();
			return allEntityPropertyDefine;
		} catch (DDDException e) {
			throw e;
		}
	}

}