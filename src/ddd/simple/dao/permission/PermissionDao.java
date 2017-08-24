package ddd.simple.dao.permission;


import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;


public interface PermissionDao extends BaseDaoInterface
{

	public Permission savePermission(Permission permission)throws Exception;
	
	public int deletePermission(Long permissionId)throws Exception;
	
	public Permission updatePermission(Permission permission)throws Exception;
	
	public Permission findPermissionById(Long permissionId)throws Exception;
	
	public EntitySet<Permission> findAllPermissions()throws Exception;
	
	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception;

	public int deleteByModule(EntitySet<Module> modules) throws Exception;
}
