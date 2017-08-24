package ddd.simple.dao.permission.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.permission.ModuleDao;
import ddd.simple.entity.permission.LoginUser;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;
import ddd.simple.service.base.BaseServiceInterface;
@Service
public class ModuleDaoBean extends BaseDao implements ModuleDao{

	
	@Resource(name = "baseService")
	private BaseServiceInterface baseService;
	
	public Module saveModule(Module module) throws Exception {
		return this.save(module);
	}

	public void deleteModule(Long moduleId) throws Exception {
		this.deleteById(moduleId,Module.class);
	}

	public Module updateModule(Module module) throws Exception {
		return this.update(module);
	}

	public Module findModuleById(Long moduleId) throws Exception {
		return this.query(moduleId, Module.class);
	}
	/**
	 * 查询系统模块
	 */
	public EntitySet<Module> findSystemModulesByOperator(Long operatorId) throws Exception
	{
		String sql = "select * from module where  EId in (select parentId from module where EId in "
					+ "(select parentId from module where Eid in "
					+ "(select p.moduleId from permission p "
					+ "left join role_permissions r on r.permissionEId = p.EId "  
					+ "left join operatorandrole o on o.roleId = r.roleEId "  
					+ "where o.operatorId = "+operatorId+")))";
		
		EntitySet<Module> modules = this.query(sql,Module.class);
		return modules;
	}
	
	public EntitySet<Module> findSystemModulesByCodes(String findICodesSql) throws Exception {
		String sql = "Code in ("+findICodesSql+") ORDER BY displayIndex ";
		
		return this.query(sql, Module.class);
	}
public int cascaseDeletePermission(Long moduleId){
		
		//得到需要被删除的权限点
		try
		{
			//删除分配的权限点
			EntitySet<Permission> permissions = this.query("EId = "+moduleId, Permission.class);
			String idString = "0";
			Iterator<Permission> ite = permissions.iterator();
			while(ite.hasNext()){
				idString = " , "+ ite.next().getEId();
			}
			this.delete("role_permission", "permissionEId in ("+idString+")");
			
			//删除权限点
			this.delete("permission","moduleId = " + moduleId);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public EntitySet<Module> sortParentsModules(Collection<Module> parents) throws Exception{
		String sql = "eid in ( ";
		Iterator<Module> ite = parents.iterator();
		while(ite.hasNext()){
			sql += ite.next().getEId()+",";
		}
		sql = sql.substring(0,sql.length()-1);
		sql += " ) order by displayIndex desc";
		return this.query(sql,Module.class);
	}
	
	public EntitySet<Module> findOperableModules(Long operatorId) throws Exception
	{
		Long orgId = 0l;
		if(this.baseService.getLoginUser() != null){
			LoginUser loginUser = this.baseService.getLoginUser();
			if(loginUser.getCurrentOrganization() != null){
				orgId = loginUser.getCurrentOrganization().getEId();
			}
			
		}
		String sql = "eid in(select DISTINCT p.moduleId as moduleId from permission p  "
				+ "left join role_permissions r on r.permissionEId = p.EId "
				+ "left join operatorandrole o on o.roleId = r.roleEId  and o.organizationId = "+orgId
				+ " where o.operatorId = "+operatorId+") ORDER BY displayIndex DESC";
		return this.query(sql, Module.class);
	}
	
	/**
	 * 查询系统模块的子模块
	 */
	public EntitySet<Module> findSystemChildByOpeaIdndParentId(Long operatorId,Long parentId)throws Exception
	{
		String sql = " parentId = "+parentId+" and EId in (select parentId from module where Eid in "
					+" (select p.moduleId from permission p "
					+" left join role_permissions r on r.permissionEId = p.EId"
					+" left join operatorandrole o on o.roleId = r.roleEId " 
					+" where o.operatorId = "+operatorId+")) ";
		EntitySet<Module> modules = this.query(sql,Module.class);
		return modules;
	}
	/**
	 * 查询系统模块的子模块的子模块
	 */
	public EntitySet<Module> findSystemChildChildByOpeaIdndParentId(Long operatorId,Long parentId)throws Exception
	{
		String sql = " Eid in (select p.moduleId from permission p"
				+ " left join role_permissions r on r.permissionEId = p.EId"
				+ " left join operatorandrole o on o.roleId = r.roleEId"
				+ " where o.operatorId = "+operatorId+") AND parentId in("
				+ " select EId from module where parentId = "+parentId+")";
		EntitySet<Module> modules = this.query(sql,Module.class);
		return modules;
	}
	
	public EntitySet<Module> findModulesByOperator(Long operatorId) throws Exception{
		String sql = "Eid in (select p.moduleId from permission p "
					+ "left join role_permissions r on r.permissionEId = p.EId "
					+ "left join operatorandrole o on o.roleId = r.roleEId  "
					+ "where o.operatorId = "+operatorId+")";
		EntitySet<Module> modules = this.query(sql,Module.class);
		return modules;
	}
	
	public EntitySet<Module> findModules() throws Exception {
		return (EntitySet<Module>) this.query("1=1",Module.class);
	}
	
	public EntitySet<Module> findModulesByParentCode(String moduleCode)
			throws Exception {
		return (EntitySet<Module>) this.query("code like '"+moduleCode+"%'",Module.class);
	}
	
	public EntitySet<Module> updateChildrenModuleCode(EntitySet<Module> childrenModules)
			throws Exception{
		return (EntitySet<Module>)this.update(childrenModules);
	}

	@Override
	public Map<String, Object> findOperableModulesByOperator(Long operatorId)
			throws Exception {
		String sql = "select DISTINCT p.moduleId as moduleId from permission p  "
				+ "left join role_permissions r on r.permissionEId = p.EId "
				+ "left join operatorandrole o on o.roleId = r.roleEId  "
				+ "where o.operatorId = "+operatorId+"";
		Set<Map<String, Object>> operableModules = this.query(sql);
		
		Map<String, Object> operableModuleMap = new HashMap<String,Object>();
		
		for(Map<String, Object> temp :operableModules)
		{
			operableModuleMap.put(temp.get("moduleId").toString(), temp.get("moduleId"));
		}
		
		return operableModuleMap;
	}

	@Override
	public EntitySet<Module> findSystemChildByParentId(Long parentId)
			throws Exception {
		String sql = " parentId = "+parentId;
		
		return this.query(sql, Module.class);
	}
	/**
	 * 会员拥有的模块(权限绑定)
	 */
	@Override
	public EntitySet<Module> findMemberOperableModules(Long memberId) throws Exception
	{
		String where = "EId in"
					+"( select p.moduleId from permission p  where p.EId in "
						+"( select rp.permissionEId from role_permissions rp where rp.roleEId in "
							+"(select mt.roleId from membertype mt where mt.EId in"
								+"( select mp.memberTypeId from memberprojection mp where mp.memberId = '"+memberId+"')"
							+")"
						+")"
					+")";
		return this.query(where, Module.class);
	}

	@Override
	public EntitySet<Module> findModulesByName(String name) throws Exception
	{
		if(name == null || name.equals(""))
			return null;
		String where = "name = '"+name+"'";
		return this.query(where, Module.class);
	}
	
	@Override
	public EntitySet<Module> findModuleByModelName(String modelName) throws Exception
	{
		String where = "name = '" + modelName + "'";
		return this.query(where, Module.class);
	}
}
