package ddd.simple.action.permission;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.permission.PermissionService;


@Action
@RequestMapping("/Permission")
@Controller
public class PermissionAction {

	@Resource(name="permissionServiceBean")
	private PermissionService permissionService;
	
	public Permission savePermission(Permission permission)throws Exception{
		try {
			Permission newPermission = this.permissionService.savePermission(permission);
			return newPermission;
		} catch (DDDException e) {
			throw e;
		}
	};

	public int deletePermission(Long permissionId)throws Exception{
		try {
			return this.permissionService.deletePermission(permissionId);
		} catch (DDDException e) {
			throw e;
		}
	};

	public Permission updatePermission(Permission permission)throws Exception{
		try {
			Permission newPermission = this.permissionService.updatePermission(permission);
			return newPermission;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Permission findPermissionById(Long permissionId)throws Exception{
		try {
			Permission findPermission = this.permissionService.findPermissionById(permissionId);
			return findPermission;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Permission> findAllPermissions()throws Exception{
		try {
			EntitySet<Permission> permissions = this.permissionService.findAllPermissions();
			return permissions;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception{
		try {
			EntitySet<Permission> permissions = this.permissionService.findPermissionByModule(moduleId);
			return permissions;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public EntitySet<Permission> findPermissionByRole(Long roleId) throws Exception{
		try {
			EntitySet<Permission> permissions = this.permissionService.findPermissionByRole(roleId);
			return permissions;
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public EntitySet<Permission> findShowPermissions(Long roleId,Long moduleId) throws Exception{
		try {
			EntitySet<Permission> permissions = this.permissionService.findShowPermissions(roleId, moduleId);
			return permissions;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Role savePermissionsByRole(Long roleId,Long moduleId,EntitySet<Permission> permissions) throws Exception{
		try { 
			return this.permissionService.savePermissionsByRole(roleId, moduleId, permissions);
		} catch (Exception e) {
			throw e;
		}
	}
}
