
package ddd.simple.service.permission.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.permission.OperatorAndRoleDao;
import ddd.simple.dao.permission.RoleDao;
import ddd.simple.entity.permission.OperatorAndRole;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.permission.OperatorService;
import ddd.simple.service.permission.RoleService;

@Service
public class RoleServiceBean  implements RoleService
{

	@Resource(name="roleDaoBean")
	private RoleDao roleDao;
	
	@Resource(name="operatorAndRoleDaoBean")
	private OperatorAndRoleDao operatorAndRoleDao;
	
	@Resource(name="operatorServiceBean")
	private OperatorService operatorService;
	
	public Role saveRole(Role role) throws Exception {
		try {
				Role newRole = this.roleDao.saveRole(role);
				return newRole;
			} catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("saveRole", e.getMessage(), e);
			}
	}

	public int deleteRole(Long roleId) throws Exception {
		try {
			return this.roleDao.deleteRole(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteRole", e.getMessage(), e);
		}
	}

	public Role updateRole(Role role) throws Exception {
		try {
			Role newRole = this.roleDao.updateRole(role);
			return newRole;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateRole", e.getMessage(), e);
		}
	}

	public Role findRoleById(Long roleId) throws Exception {
		try {
			Role role = this.roleDao.findRoleById(roleId);
			role.getPermissions();
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findRoleById", e.getMessage(), e);
		}
	}

	public EntitySet<Role> findAllRoles() throws Exception {
		try {
			EntitySet<Role> roles = this.roleDao.findAllRoles();
			return roles;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllRoles", e.getMessage(), e);
		}
	}
	
	public EntitySet<Role> findShowRoles(Long operatorId,Long organizationId) throws Exception{
		try {
			EntitySet<OperatorAndRole> operatorAndRoles = this.operatorAndRoleDao.findRolesByOperatorIdandOrganizationId(operatorId, organizationId);
			EntitySet<Role> roles = this.findAllRoles();
			
			for (OperatorAndRole operatorAndRole : operatorAndRoles) {
				for (Role role : roles) {
					role.getName();
					role.getCode();
					if(role.getEId().equals(operatorAndRole.getRole().getEId()))
					{
						role.setIsAssigned(true);
					}
				}
			}
			
			return roles;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findShowRoles", e.getMessage(), e);
		}
	}
	
	public Role distributionPermission(Long roleId,EntitySet<Permission> permissions) throws Exception{
		try {
			Role role = this.findRoleById(roleId);
			
			return role;
		} catch(Exception e) {
			e.printStackTrace();
			throw new DDDException("distributionPermission", e.getMessage(), e);
		}
	}
	
	
	public EntitySet<Role> finRoleByEmployeeId(Long employeeId)
	{
		try
		{
			return this.roleDao.finRoleByEmployeeId(employeeId);
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new DDDException("finRoleByEmployeeId", e.getMessage(), e);
		}
	}
}
