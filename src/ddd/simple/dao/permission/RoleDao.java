package ddd.simple.dao.permission;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;

public interface RoleDao
{
	public Role saveRole(Role role) throws Exception;
	
	public int deleteRole( Long roleId) throws Exception;
	
	public Role updateRole(Role role) throws Exception;
	
	public Role findRoleById( Long roleId) throws Exception;

	public EntitySet<Role> findAllRoles() throws Exception;

	public EntitySet<Role> finRoleByEmployeeId(Long employeeId) throws Exception;
	

}
