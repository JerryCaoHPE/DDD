package ddd.simple.action.permission;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.ModuleTreeNode;
import ddd.simple.service.permission.ModuleService;

@Action
@RequestMapping("/Module")
@Controller
public class ModuleAction {

	@Resource(name="moduleServiceBean")
	private ModuleService moduleService;
	
	public Module saveModule(Module module)  throws Exception
	{
		try {
			Module newModule = this.moduleService.saveModule(module);
			return newModule;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteModule(Long moduleId) throws Exception{
		
		try {
			int deleteNumber = this.moduleService.deleteModule(moduleId);
			return deleteNumber;
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public Module updateModule(Module module) throws Exception{
		try {
			Module newModule = this.moduleService.updateModule(module);
			return newModule;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Module findModuleById(Long moduleId) throws Exception{
		try {
			Module findModule = this.moduleService.findModuleById(moduleId);
			this.clearModuleParent(findModule.getChildren());
			
			findModule.getParent();
			return  findModule;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	
	public EntitySet<Module> findModulesByParentCode(String moduleCode) throws Exception{
		try{
			EntitySet<Module> findModules = this.moduleService.findModulesByParentCode(moduleCode);
			return findModules;
		}catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Module> findModules() throws Exception{
		try{
			EntitySet<Module> findModules = this.moduleService.findModules();
			return findModules;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Module> findModulesByOperator(Long operatorId) throws Exception{
		try {
			EntitySet<Module> modules = this.moduleService.findModulesByOperator(operatorId);
			return modules;
		}catch (DDDException e) {
			throw e;
		}
	}
	
	public ModuleTreeNode findSystemChildModulesNew(Long operatorId,Long systemModuleId)throws Exception{
		return this.moduleService.findSystemChildModulesNew(operatorId,systemModuleId);
	}
	
	//加载模块
	public EntitySet<Module> findSystemChildModules(Long operatorId,Long systemModuleId)throws Exception{
		try {
			EntitySet<Module> modules = this.moduleService.findSystemChildModules(operatorId, systemModuleId);
			//把对象的parent设为null，避免循环引用
			clearModuleParent(modules);
			return modules;
		} catch (DDDException e) {
			throw e;
		}
	}
	/**
	 * 
	* @Title: clearModuleParen 
	* @Description: 把对象的parent设为null，避免循环引用
	* @param modules 
	* @return void
	 */
	private void clearModuleParent(EntitySet<Module> modules)
	{
		Iterator<Module> iterator = modules.iterator();
		while(iterator.hasNext())
		{
			Module module = iterator.next();
			module.setParent(null);
			if(module.getChildren() != null && module.getChildren().size() >0)
			{
				clearModuleParent(module.getChildren());
			}
		}
	}
	
	public EntitySet<Module> findSystemModulesByOperator(Long operatorId) throws Exception{
		try {
			EntitySet<Module> modules = this.moduleService.findSystemModulesByOperator(operatorId);
			Iterator<Module> ite = modules.iterator();
			while(ite.hasNext()){
				ite.next().getName();
			}
			return modules;
		} catch (DDDException e) {
			throw e;
		}
	}
	/**
	 * 
	* @Title: findMemberModules 
	* @Description: 查找会员权限对应的模块 从session中取 若session没有 则登陆失效(安全)
	* @return 
	* @throws Exception 
	* @return Map<String,Object>
	 */
	public Map<String,Object> findMemberModules() throws Exception
	{
		try{
			Map<String,Object> result = new HashMap<String,Object>();
			EntitySet<Module> modules = this.moduleService.findMemberModules();
			if(modules == null){
				result.put("isSuccess", false);
				result.put("isLogin",false);
			}
			else{
				result.put("isSuccess",true);
				result.put("modules", modules);
			}
		    return result;
		}catch(DDDException e){
			throw e;
		}
	}
}
