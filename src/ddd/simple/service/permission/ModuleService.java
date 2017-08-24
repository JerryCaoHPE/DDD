package ddd.simple.service.permission;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.ModuleTreeNode;


public interface ModuleService {

	public Module saveModule(Module module) throws Exception;
	
	public int deleteModule(Long moduleId) throws Exception;
	
	public Module updateModule(Module module) throws Exception;
	
	public Module findModuleById(Long moduleId) throws Exception;

	public EntitySet<Module> findModulesByParentCode(String moduleCode) throws Exception;
	
	public EntitySet<Module> findModules() throws Exception;
	
	public EntitySet<Module> findModules(String where) throws Exception;
	
	public EntitySet<Module> findModulesByOperator(Long operatorId) throws Exception;
	
	public EntitySet<Module> findSystemChildModules(Long operatorId,Long systemModuleId) throws Exception;
	
	public EntitySet<Module> findSystemModulesByOperator(Long operatorId) throws Exception;

	public EntitySet<Module> findMemberModules() throws Exception;
	
	public int deleteModuleByName(String name) throws Exception;
	
	public EntitySet<Module> findModulesByName(String name) throws Exception;
	
	public EntitySet<Module> findModuleByModelName(String modelName) throws Exception;

	public ModuleTreeNode findSystemChildModulesNew(Long operatorId, Long systemModuleId) throws Exception;
}
