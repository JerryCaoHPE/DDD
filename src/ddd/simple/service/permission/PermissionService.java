package ddd.simple.service.permission;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.base.BaseServiceInterface;

public interface PermissionService extends BaseServiceInterface{

	public Permission savePermission(Permission permission)throws Exception;
	
	public void savePermissions(EntitySet<Permission> permissions)throws Exception;

	public int deletePermission(Long permissionId)throws Exception;

	public Permission updatePermission(Permission permission)throws Exception;

	public Permission findPermissionById(Long permissionId)throws Exception;
	
	public EntitySet<Permission> findAllPermissions()throws Exception;

	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception;
	
	public EntitySet<Permission> findPermissionByRole(Long roleId) throws Exception;
	
	public EntitySet<Permission> findShowPermissions(Long roleId,Long moduleId) throws Exception;
	
	public Role savePermissionsByRole(Long roleId,Long moduleId,EntitySet<Permission> permissions) throws Exception;
	
	public int deleteByModule(EntitySet<Module> modules) throws Exception;
}