package ddd.simple.service.permission.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.permission.PermissionDao;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.permission.PermissionService;
import ddd.simple.service.permission.RoleService;

@Service
public class PermissionServiceBean extends BaseService implements PermissionService {

	@Resource(name="permissionDaoBean")
	private PermissionDao permissionDao;
	
	@Resource(name="roleServiceBean")
	private RoleService roleService;
	
	public Permission savePermission(Permission permission) {
		try {
			Permission newPermission = this.permissionDao.savePermission(permission);
			return newPermission;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("savePermission", e.getMessage(), e);
		}
	}
	
	public void savePermissions(EntitySet<Permission> permissions){
		try {
			 this.permissionDao.save(permissions);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("savePermission", e.getMessage(), e);
		}
	}

	public int deletePermission(Long permissionId) {
		try {
			return this.permissionDao.deletePermission(permissionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deletePermission", e.getMessage(), e);
		}
	}

	public Permission updatePermission(Permission permission) {
		try {
			Permission newPermission = this.permissionDao.updatePermission(permission);
			return newPermission;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updatePermission", e.getMessage(), e);
		}
	}

	public Permission findPermissionById(Long permissionId) {
		try {
			Permission findPermission = this.permissionDao.findPermissionById(permissionId);
			if(findPermission.getModule() != null)
			{
				findPermission.getModule().getName();
			}
			return findPermission;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findPermissionById", e.getMessage(), e);
		}
	}

	public EntitySet<Permission> findAllPermissions() throws Exception {
		try {
			EntitySet<Permission> permissions = this.permissionDao.findAllPermissions();
			return permissions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllPermissions", e.getMessage(), e);
		}
	}

	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception{
		try {
			EntitySet<Permission> permissions = this.permissionDao.findPermissionByModule(moduleId);
			return permissions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findPermissionByModule", e.getMessage(), e);
		}
	}
	
	public EntitySet<Permission> findPermissionByRole(Long roleId) throws Exception{
		try {
			Role role = this.roleService.findRoleById(roleId);
			EntitySet<Permission> permissions = (EntitySet<Permission>)role.getPermissions();
			return permissions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findPermissionByRole", e.getMessage(), e);
		}
	}
	
	public EntitySet<Permission> findShowPermissions(Long roleId,Long moduleId) throws Exception{
		try {
			
			EntitySet<Permission> modulePermissions = this.findPermissionByModule(moduleId);
			EntitySet<Permission> rolePermissions = this.findPermissionByRole(roleId);
			
			for (Permission rolePermission : rolePermissions)
			{
				for (Permission modulePermission : modulePermissions)
				{
					if((rolePermission.getEId()).equals(modulePermission.getEId()))
					{
						modulePermission.setIsAssigned(true);
					}
				}
			}
			return modulePermissions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findShowPermissions", e.getMessage(), e);
		}
		
	}
	
	public Role savePermissionsByRole(Long roleId,Long moduleId,EntitySet<Permission> permissions) throws Exception{
		try {
			Role role = this.roleService.findRoleById(roleId);
			EntitySet<Permission> rolePermissions = (EntitySet<Permission>)role.getPermissions();
			EntitySet<Permission> delPermissions = new EntitySet<Permission>();
			
			for (Permission permission : rolePermissions)
			{
				if(permission.getModule() != null)
				{
					Long roleModuleId = permission.getModule().getEId();
					if(roleModuleId.equals(moduleId))
					{
						delPermissions.add(permission);
					}
				}
			}
			
			rolePermissions.removeAll(delPermissions);
			
			for (Permission permission : permissions)
			{
				rolePermissions.add(permission);
			}
			role.setPermissions(rolePermissions);
			Role newRole = this.roleService.updateRole(role);
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("savePermissionsByRole", e.getMessage(), e);
		}
	}

	@Override
	public int deleteByModule(EntitySet<Module> modules) throws Exception
	{
		return this.permissionDao.deleteByModule(modules);
	}

}
