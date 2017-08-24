package ddd.simple.dao.permission;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.permission.Module;

public interface ModuleDao extends BaseDaoInterface{

	public Module saveModule(Module module) throws Exception;
	
	public void deleteModule(Long moduleId) throws Exception;
	
	public Module updateModule(Module module) throws Exception;
	
	public Module findModuleById(Long moduleId) throws Exception;
	
	public EntitySet<Module> findSystemModulesByOperator(Long operatorId) throws Exception;
	
	public EntitySet<Module> findSystemChildByOpeaIdndParentId(Long operatorId,Long parentId)throws Exception;
	
	public EntitySet<Module> findSystemChildChildByOpeaIdndParentId(Long operatorId,Long parentId)throws Exception;
	
	public EntitySet<Module> findModulesByParentCode(String moduleCode) throws Exception;
	
	public EntitySet<Module> findModules() throws Exception;
	
	public EntitySet<Module> findModulesByOperator(Long operatorId) throws Exception;
	
	public EntitySet<Module> updateChildrenModuleCode(EntitySet<Module> childrenModules)throws Exception;
	
	public Map<String, Object> findOperableModulesByOperator(Long operatorId) throws Exception;
	
	public EntitySet<Module> findSystemChildByParentId(Long parentId)throws Exception;
	
	public EntitySet<Module> findOperableModules(Long operatorId) throws Exception;
	
	public EntitySet<Module> sortParentsModules(Collection<Module> collection) throws Exception;

	public EntitySet<Module> findSystemModulesByCodes(String findCodesSql)  throws Exception;
	
	public EntitySet<Module> findMemberOperableModules(Long memberId) throws Exception;
	
	public EntitySet<Module> findModulesByName(String name) throws Exception;
	
	public EntitySet<Module> findModuleByModelName(String modelName) throws Exception;
	
	
}
