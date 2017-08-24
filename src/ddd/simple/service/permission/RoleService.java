package ddd.simple.service.permission;


import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Role;


public interface RoleService {
	
	public Role saveRole(Role role) throws Exception;
	
	public int deleteRole( Long roleId) throws Exception;
	
	public Role updateRole(Role role) throws Exception;
	
	public Role findRoleById( Long roleId) throws Exception;

	public EntitySet<Role> findAllRoles() throws Exception;

	public EntitySet<Role> findShowRoles(Long operatorId,Long organizationId) throws Exception;
	
	public EntitySet<Role> finRoleByEmployeeId(Long employeeId);
}
