package ddd.simple.action.permission;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.permission.RoleService;

@Action
@RequestMapping("/Role")
@Controller
public class RoleAction {

	@Resource(name="roleServiceBean")
	private RoleService roleService;
	
	public Role saveRole(Role role) throws Exception {
		try {
			Role newRole = this.roleService.saveRole(role);
			return newRole;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteRole(Long roleId) throws Exception {
		try {
			return this.roleService.deleteRole(roleId);
		} catch (DDDException e) {
			throw e;
		}
	}

	public Role updateRole(Role role) throws Exception {
		try {
			Role newRole = this.roleService.updateRole(role);
			return newRole;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Role findRoleById(Long roleId) throws Exception {
		try {
			Role role = this.roleService.findRoleById(roleId);
			return role;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<Role> findAllRoles() throws Exception {
		try {
			EntitySet<Role> roles = this.roleService.findAllRoles();
			return roles;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Role> findShowRoles(Long operatorId,Long organizationId) throws Exception{
		try {
			EntitySet<Role> roles = this.roleService.findShowRoles(operatorId, organizationId);
			return roles;
		}  catch (DDDException e) {
			throw e;
		}
	}

}
