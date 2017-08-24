
package ddd.simple.service.permission.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.permission.ModuleDao;
import ddd.simple.dao.permission.PermissionDao;
import ddd.simple.dao.permission.RoleDao;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.ModuleTreeNode;
import ddd.simple.entity.permission.Permission;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.permission.ModuleService;
@Service
public class ModuleServiceBean extends BaseService implements ModuleService{

	@Resource(name="moduleDaoBean")
	private ModuleDao moduleDao ;
	
	@Resource(name = "permissionDaoBean")
	private PermissionDao permissionDao;
	
	
	@Resource(name = "roleDaoBean")
	private RoleDao roleDao;
	
	public Module saveModule(Module module) throws Exception {
		try {
			if("否".equals(module.getModuleType()))
			{
				String moduleCode = this.getModuleCode(module.getParent().getEId());
				module.setCode(moduleCode);
			}
			 else if("是".equals(module.getModuleType()))
			{
				EntitySet<Module> systemModules = this.getSystemModules();
				String systemModuleCode = this.getLastFourCode(systemModules);
				module.setCode(systemModuleCode);
			}
			
			Module newModule = this.moduleDao.saveModule(module);
			
			return newModule;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveModule", e.getMessage(), e);
		}
	}
	//递归删除模板和所有子模板
	private int deleteModule(Module module) throws Exception{
		int number = 1;
		EntitySet<Module> childMoudules = module.getChildren();
		if(!childMoudules.isEmpty()){
			for(Module childModule:childMoudules){
				number+=this.deleteModule(childModule);
			}
		}
		EntitySet<Permission> permissions = permissionDao.findPermissionByModule(module.getEId());
		if(!permissions.isEmpty()){
			permissionDao.delete(permissions);
		}
		moduleDao.delete(module);
		return number;
	}
	public int deleteModule(Long moduleId) throws Exception{
		try {
			Module module = moduleDao.findModuleById(moduleId);
			int number = this.deleteModule(module);
			//级联删除
			//删除权限点
			//删除被引用的权限点
			try{
				
			}catch(Exception e){
				return -1;
			}
			return number;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModule", e.getMessage(), e);
		}
		
	}
	public Module updateModule(Module module) throws Exception {
		try {
			Module newModule = new Module();
			
			if("否".equals(module.getModuleType()))
			{
				Module parentModule = module.getParent();
				Module findThisModule = this.findModuleById(module.getEId()); 
				//如果前台传过来的模块的父模块没有变化，那么直接保存
				if(parentModule.getEId().equals(findThisModule.getParent().getEId()))
				{
					newModule = this.moduleDao.updateModule(module);
				}
				else
				{
					Module findParentModule = this.findModuleById(parentModule.getEId());
					Module changCodeModule = this.setUpdateCode(findParentModule, module);
					changCodeModule.setParent(findParentModule);
					newModule = this.moduleDao.updateModule(changCodeModule);
				}
			}
			else if("是".equals(module.getModuleType()))
			{
				Module findThisSystemModule = this.findModuleById(module.getEId());
				if(findThisSystemModule.getParent() == null || findThisSystemModule.getParent().getEId() == null)
				{
					newModule = this.moduleDao.updateModule(module);
				}
				else 
				{
					EntitySet<Module> systemModules = this.getSystemModules();
					String newParentCode = this.getLastFourCode(systemModules);
					module.setCode(newParentCode);
					EntitySet<Module> childModules = this.setUpdateChildCode(findThisSystemModule, newParentCode);
					module.setChildren(childModules);
					
					newModule = this.moduleDao.updateModule(module);
				}
			}
			
		
			return newModule;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateModule", e.getMessage(), e);
		}
	}
	
	public Module findModuleById(Long moduleId) throws Exception{
		try {
			Module module = this.moduleDao.findModuleById(moduleId);
			if(module.getParent() != null)
			{
				module.getParent().getName();
			}
			EntitySet<Module> modules = (EntitySet<Module>) module.getChildren();
			for(Module childModule:modules)
			{
				childModule.getChildren();
			}
			return module;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModuleById", e.getMessage(), e);
		}
	}
	
	public EntitySet<Module> findModules(String where) throws Exception{
		try{
			EntitySet<Module> modules = this.moduleDao.query(where, Module.class);
			for(Module module:modules)
			{
				EntitySet<Module> childModules = (EntitySet<Module>) module.getChildren();
				for (Module childModule : childModules) {
					childModule.getChildren();
				}
			}
			return modules;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModules", e.getMessage(), e);
		}
	}
	
	public EntitySet<Module> findModules() throws Exception{
		try{
			EntitySet<Module> modules = this.moduleDao.findModules();
			for(Module module:modules)
			{
				EntitySet<Module> childModules = (EntitySet<Module>) module.getChildren();
				for (Module childModule : childModules) {
					childModule.getChildren();
				}
			}
			return modules;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModules", e.getMessage(), e);
		}
	}
	
	public EntitySet<Module> findModulesByParentCode(String moduleCode) throws Exception{
		try{
			EntitySet<Module> modules = this.moduleDao.findModulesByParentCode(moduleCode);
			for(Module module:modules)
			{
				module.getParent();
			}
			return modules;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModulesByParentCode", e.getMessage(), e);
		}
	}
	
	public EntitySet<Module> findModulesByOperator(Long operatorId) throws Exception
	{
		try {
			this.moduleDao.findModules();
			EntitySet<Module> modules = (EntitySet<Module>)this.moduleDao.findModulesByOperator(operatorId);
			EntitySet<Module> parentModules = new EntitySet<Module>();
			EntitySet<Module> resultModules = new EntitySet<Module>();
			
			for (Module module : modules)
			{
				module.getParent().getName();
				if(module.getParent().getParent() != null)
				{
					module.getParent().getParent().getName();
				}
			}
			
			for (Module module : modules) {
				Module parentModule = getParentModule(parentModules, module.getParent());
				if(parentModule==null){
					parentModule = module.getParent();
					EntitySet<Module> childModules = new EntitySet<Module>();
					childModules.add(module);
					parentModule.setChildren(childModules);
					parentModules.add(parentModule);
				}else{
					parentModule.getChildren().add(module);
				}
			}
			
			for (Module module : parentModules) {
				Module parentModule = getParentModule(resultModules, module.getParent());
				if(parentModule==null){
					parentModule = module.getParent();
					EntitySet<Module> childModules = new EntitySet<Module>();
					childModules.add(module);
					parentModule.setChildren(childModules);
					resultModules.add(parentModule);
				}else{
					parentModule.getChildren().add(module);
				}
			}
			
			return resultModules;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModulesByOperator", e.getMessage(), e);
		}
	}
	
	public EntitySet<Module> findSystemModulesByOperator(Long operatorId) throws Exception
	{
		try {
			EntitySet<Module> operableModules = this.moduleDao.findOperableModules(operatorId);
			
			Map<String,String> rootModuleCodes = new HashMap<String,String>();
			
			for(Module module:operableModules)
			{
				String moduleCode = module.getCode();
				if(moduleCode.length()>=4)
				{
					String rootCode = moduleCode.substring(0,4);
					if(rootModuleCodes.get(rootCode)==null)
					{
						rootModuleCodes.put(rootCode, rootCode);
					}
				}
			}
			
			String findCodesSql = "";
			
			for(Entry<String, String> rootModuleCode:rootModuleCodes.entrySet()){ 
				findCodesSql+=rootModuleCode.getValue()+",";
			}
			
			findCodesSql = findCodesSql.substring(0,findCodesSql.lastIndexOf(","));
			
			EntitySet<Module> systemModules = this.moduleDao.findSystemModulesByCodes(findCodesSql);
			
			return systemModules;
		}  catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findSystemModulesByOperator", e.getMessage(), e);
		}
	}
	
	
	private EntitySet<Module> dealWithChildModules(EntitySet<Module> operableModules,ArrayList<Long> sytemModulesId)  throws Exception{
		try{
			// 保存父模块
			Map<Long,Module> parents = new LinkedHashMap<Long,Module>();
			
			// 遍历所有可操作模块
			for(Module module:operableModules)
			{
				if(!(sytemModulesId.contains(module.getParent().getEId())))
				{
					this.getParentModuleByPerssion(module, parents,sytemModulesId);
				}
				
			}
			Map<Long,Module> sortedParentsMap = new LinkedHashMap<Long,Module>();
			EntitySet<Module> sortedParents = this.moduleDao.sortParentsModules(parents.values());
			
			Iterator<Module> ite = sortedParents.iterator();
			while(ite.hasNext()){
				Module tempModule = ite.next();
				tempModule.setChildren(parents.get(tempModule.getEId()).getChildren());
				sortedParentsMap.put(tempModule.getEId(), tempModule);
			}
			
			EntitySet<Module> displayModules = new EntitySet<Module>();
			for(Entry<Long, Module> parent:sortedParentsMap.entrySet()){ 
				if(parent.getValue().getParent()!=null&&sytemModulesId.contains(parent.getValue().getParent().getEId()))
				{
					parent.getValue().getParent().setChildren(null);
					displayModules.add(parent.getValue());
				}
			}
			return displayModules;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public ModuleTreeNode findSystemChildModulesNew(Long operatorId,Long systemModuleId)throws Exception{
		EntitySet<Module> modules = this.findSystemChildModules(operatorId, systemModuleId);
		ModuleTreeNode node = new ModuleTreeNode();
		node.setNodes(new ArrayList<ModuleTreeNode>());
		node.setName("mynode");
		this.constructTree(node, modules);
		return node;
	}
	
	
	public void constructTree(ModuleTreeNode node,EntitySet<Module> modules){
		if(modules== null ||modules.size() == 0){
			return ;
		}
		
		Iterator<Module> ite = modules.iterator();
		//循环
		while(ite.hasNext()){
			Module module = ite.next();
			ModuleTreeNode childNode = new ModuleTreeNode();
			childNode.setText(module.getName());
			childNode.setId(module.getEId()+"");
			childNode.setRouteData(module.getRoute());
			EntitySet<Module> childModule =  module.getChildren();
			
			//glyphicon glyphicon-folder-close
			//glyphicon glyphicon-folder-open
			//glyphicon glyphicon-file
			
			String iconClass = module.getIconClass();
			if(iconClass != null && (!("".equals(iconClass)))  ){
				childNode.setIcon(iconClass);
			}else{
				if(childModule == null || childModule.size()==0){
					childNode.setIcon("glyphicon glyphicon-file");
				}else{
					childNode.setIcon("glyphicon glyphicon-folder-open");
				}
			}
					
			
			
			childNode.setNodes(new ArrayList<ModuleTreeNode>());
			node.addNode(childNode);
		    this.constructTree(childNode,childModule);	
		}
		
	}
	
	/**
	 * 查询当前用户的权限下&指定父模块下的模块
	 */
	public EntitySet<Module> findSystemChildModules(Long operatorId,Long systemModuleId) throws Exception
	{
		try {
			
			// 可操作模块
			EntitySet<Module> operableModules =this.moduleDao.findOperableModules(operatorId);
			ArrayList<Long> sytemModulesId = new ArrayList<Long>();
			sytemModulesId.add(systemModuleId);
			return this.dealWithChildModules(operableModules,sytemModulesId);
			/*// 保存父模块
			Map<Long,Module> parents = new LinkedHashMap<Long,Module>();
			
			// 遍历所有可操作模块
			for(Module module:operableModules)
			{
				if(!module.getParent().getEId().equals(systemModuleId))
				{
					this.getParentModuleByPerssion(module, parents,systemModuleId);
				}
				
			}
			Map<Long,Module> sortedParentsMap = new LinkedHashMap<Long,Module>();
			EntitySet<Module> sortedParents = this.moduleDao.sortParentsModules(parents.values());
			Iterator<Module> ite = sortedParents.iterator();
			while(ite.hasNext()){
				Module tempModule = ite.next();
				tempModule.setChildren(parents.get(tempModule.getEId()).getChildren());
				sortedParentsMap.put(tempModule.getEId(), tempModule);
			}
			
			EntitySet<Module> displayModules = new EntitySet<Module>();
			for(Entry<Long, Module> parent:sortedParentsMap.entrySet()){ 
				if(parent.getValue().getParent()!=null&&parent.getValue().getParent().getEId().equals(systemModuleId))
				{
					displayModules.add(parent.getValue());
				}
			}
			
			return displayModules;*/
		}  catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findsystemChildModules", e.getMessage(), e);
		}
	}
	
	
	private void getParentModuleByPerssion(Module module,Map<Long,Module> parents,ArrayList<Long> sytemModulesId)
	{
		if(module.getParent()!=null&&!(sytemModulesId.contains(module.getParent().getEId())))
		{
			if(parents.get(module.getParent().getEId())!=null)
			{
				
				Module tempParent = parents.get(module.getParent().getEId());
				tempParent.getChildren().add(module);
				
				
				parents.put(tempParent.getEId(), tempParent);
			}
			else
			{
				Module tempParent = module.getParent();
				tempParent.setChildren(new EntitySet<Module>());
				tempParent.getChildren().add(module);
				
				
				parents.put(tempParent.getEId(), tempParent);
			}
			
			module = module.getParent();
		}
		else
		{
			return;
		}
		getParentModuleByPerssion(module,parents,sytemModulesId);
		
	}
	
	private Module getParentModule(EntitySet<Module> parentModules,Module parentModule){
		for (Module module : parentModules) {
			if(module.getEId()==parentModule.getEId()){
				return module;
			}
		}
		return null;
	}
	
	public EntitySet<Module> getSystemModules() throws Exception
	{
		try {
			
			EntitySet<Module> modules = this.findModules();
			EntitySet<Module> systemModules = new EntitySet<Module>();
			
			for (Module module : modules)
			{
				if("是".equals(module.getModuleType()))
				{
					systemModules.add(module);
				}
			}
			
			return systemModules;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getSystemModules", e.getMessage(), e);
		}
	}
	
	 public String getModuleCode(Long moduleId) throws Exception
	 {
		 try {
			 	Module parentModule = this.findModuleById(moduleId);
			 	EntitySet<Module> childrenModules = (EntitySet<Module>)parentModule.getChildren();
			 
			 	String maxLastFourCode = this.getLastFourCode(childrenModules); 
			 	
			 	String maxCode = parentModule.getCode()+maxLastFourCode;
			 	
				return maxCode;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getModuleCode", e.getMessage(), e);
		}
	 }
	 
	 public Integer findMaxModuleCode(EntitySet<Module> modules) throws Exception
	 {
		 try {
			EntitySet<Module> getCodeModules = modules;
			ArrayList<String> codeNumberString = new ArrayList<String>();
			for (Module module : getCodeModules)
			{
				String moduleCode = module.getCode();
				String codeNumber = moduleCode.substring(moduleCode.length()-4, moduleCode.length());
				codeNumberString.add(codeNumber);
			};
			
			Collections.sort(codeNumberString);
			
			int maxCode = 0;
			if(codeNumberString.size() != 0)
			{
				maxCode = Integer.parseInt(codeNumberString.get(codeNumberString.size()-1));
			}
			return maxCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findMaxModuleCode", e.getMessage(), e);
		}
	 }
	 
	 public String getLastFourCode(EntitySet<Module> modules) throws Exception
	 {
		 try {
			 Integer maxCode = this.findMaxModuleCode(modules);
			 Integer useCode = maxCode+1;
			 Integer useCodeLength = useCode.toString().length();
			 String finalLastFourCode = "";
			 
			 for(int i=0;i<4-useCodeLength;i++)
			 {
				 finalLastFourCode+="0";
			 }
			 finalLastFourCode+=useCode;
			 
			 return finalLastFourCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getLastFourCode", e.getMessage(), e);
		}
	 }
	 
	 public Module setUpdateCode(Module parentModule,Module module) throws Exception
	 {
		 try {
			 Module newModule = module; 
			 String oldParentCode = parentModule.getCode();
			 String newCode = this.getChangeCode(parentModule,oldParentCode);
		   	 
			 newModule.setCode(newCode);
			 EntitySet<Module> childrenModules = this.setUpdateChildCode(newModule, newCode);
			 newModule.setChildren(childrenModules);
		   	 return newModule;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("setUpdateCode", e.getMessage(), e);
		}
	 }
	  
	 public EntitySet<Module> setUpdateChildCode(Module module,String newParentCode) throws Exception
	 {
		 try {
			 EntitySet<Module> childrenModules = new EntitySet<Module>();
			 if(module.getChildren().size() != 0)
			 {
				childrenModules = (EntitySet<Module>)(this.findModuleById(module.getEId()).getChildren());
				
				for (Module childModule : childrenModules)
				{
					String newCode = this.getChangeChildCode(newParentCode, childModule);
					childModule.setCode(newCode);
				}
			 }
			
			
			EntitySet<Module> newChildrenModules = this.moduleDao.updateChildrenModuleCode(childrenModules);
			
			return newChildrenModules;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("setUpdateChildCode", e.getMessage(), e);
		}
	 }
	 
	 public String getChangeCode(Module parentModule,String parentCode) throws Exception
	 {
		 try {
			
			 String lastFourCode = this.getLastFourCode((EntitySet<Module>)parentModule.getChildren());
			 String newCode = parentCode+lastFourCode;
			 
			 return newCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getChangeCode", e.getMessage(), e);
		}
	 }
	 
	 public String getChangeChildCode(String newParentCode,Module childModule) throws Exception
	 {
		 try {
			 String oldChildCode = childModule.getCode();
			 String lastFourCode = oldChildCode.substring(oldChildCode.length()-4,oldChildCode.length());
			 String newChildCode = newParentCode + lastFourCode;
			 return newChildCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getChangeChildCode", e.getMessage(), e);
		}
	 }
	 /*
     *  查找会员权限下的模块
     **/
		public EntitySet<Module> findMemberModules() throws Exception 
		{
			try {
				Long memberId = this.getLoginUser().getLoginVip().getEId();
				EntitySet<Module> systemModules = this.getSystemModules();
				ArrayList<Long> systemModulesId = new ArrayList<Long>();
				for(Module module : systemModules){
					systemModulesId.add(module.getEId());
				}
				EntitySet<Module> operableModules = this.moduleDao.findMemberOperableModules(memberId);
				EntitySet<Module> module = this.dealWithChildModules(operableModules, systemModulesId);
				return module;
			}  catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("findsystemChildModules", e.getMessage(), e);
			}
		}
		
		@Override
		public int deleteModuleByName(String name) throws Exception
		{
			if(name == null || name.equals(""))
				return 0;
			EntitySet<Module> modules = this.moduleDao.query("name = '"+name+"'", Module.class);
			if(modules.isEmpty())
				return 0;
			permissionDao.deleteByModule(modules);
			return this.moduleDao.deleteByWhere("name = '"+name+"'", Module.class);
		}
		
		public EntitySet<Module> findModulesByName(String name) throws Exception{
			return this.moduleDao.findModulesByName(name);
		}
		@Override
		public EntitySet<Module> findModuleByModelName(String modelName) throws Exception
		{
			return this.moduleDao.findModuleByModelName(modelName);
		}
		
		
}
