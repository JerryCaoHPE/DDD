package ddd.simple.action.entityTips;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.entityTips.EntityTips;
import ddd.simple.service.entityTips.EntityTipsService;

@Action
@RequestMapping("/EntityTips")
@Controller
public class EntityTipsAction
{
	@Resource(name="entityTipsServiceBean")
	private EntityTipsService entityTipsService;
	
	public EntityTips saveEntityTips(EntityTips entityTips)
	{
		try {
			EntityTips saveEntityTips = this.entityTipsService.saveEntityTips(entityTips);
			return saveEntityTips;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteEntityTips(Long entityTipsId){
		
		try {
			return this.entityTipsService.deleteEntityTips(entityTipsId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public EntityTips updateEntityTips(EntityTips entityTips) {
		try {
			EntityTips updateEntityTips = this.entityTipsService.updateEntityTips(entityTips);
			return updateEntityTips;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntityTips findEntityTipsById(Long entityTipsId){
		try {
			EntityTips findEntityTips = this.entityTipsService.findEntityTipsById(entityTipsId);
			return  findEntityTips;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<EntityTips> findAllEntityTips(){
		try{
			EntitySet<EntityTips> allEntityTips = this.entityTipsService.findAllEntityTips();
			return allEntityTips;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<EntityTips> findEntityTipsByName(String name){
		try{
			EntitySet<EntityTips> allEntityTips = this.entityTipsService.findEntityTipsByName(name);
			return allEntityTips;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public List<Map<String,String>> getAllField(String entityClassStr){
		return this.entityTipsService.getAllField(entityClassStr);
	}

}