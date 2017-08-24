package ddd.simple.service.permission.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.organization.OrganizationDao;
import ddd.simple.dao.permission.OperatorAndRoleDao;
import ddd.simple.dao.permission.OperatorDao;
import ddd.simple.dao.permission.RoleDao;
import ddd.simple.entity.organization.Organization;
import ddd.simple.entity.permission.LoginUser;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.permission.OperatorAndRole;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.permission.OperatorService;
import ddd.simple.service.permission.RoleService;

@Service
public class OperatorServiceBean extends BaseService implements OperatorService {

	@Resource(name = "operatorDaoBean")
	private OperatorDao operatorDao;

	@Resource(name = "roleDaoBean")
	private RoleDao roleDao;

	@Resource(name = "organizationDaoBean")
	private OrganizationDao organizationDao;

	@Resource(name = "operatorAndRoleDaoBean")
	private OperatorAndRoleDao operatorAndRoleDao;

	@Resource(name = "roleServiceBean")
	private RoleService roleService;

	/*
	 * @Resource(name="moduleServiceBean") private ModuleService moduleService;
	 */

	private Set<String> permissionCodes;

	public Operator saveOperator(Operator operator) {
		try {
			Operator newOperator = this.operatorDao.saveOperator(operator);
			return newOperator;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveOperator", e.getMessage(), e);
		}

	}

	public int deleteOperator(Long operatorId) {
		try {
			return this.operatorDao.deleteOperator(operatorId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteOperator", e.getMessage(), e);
		}
	}

	public Operator updateOperator(Operator operator) {
		try {
			Operator newOperator = this.operatorDao.updateOperator(operator);
			return newOperator;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateOperator", e.getMessage(), e);
		}
	}

	public Operator findOperatorById(Long operatorId) {
		try {
			Operator findOperator = this.operatorDao
					.findOperatorById(operatorId);
			if (findOperator.getEmployee() != null) {
				findOperator.getEmployee().getName();
			}
			return findOperator;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOperatorById", e.getMessage(), e);
		}
	}

	public EntitySet<Operator> findAllOperators() {
		try {
			EntitySet<Operator> operators = this.operatorDao.findAllOperators();
			return operators;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAlloOperators", e.getMessage(), e);
		}
	}

	public Operator findOperatorByCode(String oeratorCode) throws Exception {
		try {
			Operator operator = this.operatorDao
					.findOperatorByCode(oeratorCode);
			return operator;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOperatorByCode", e.getMessage(), e);
		}
	}

	public Map<String, Object> checkLoginUser(String operatorCode,
			String operatorPassword, Organization organization)
			throws Exception {
		try {
			// 密码的md5值一定是32位值,长度不等,直接跳过
			if (operatorPassword.length() != 32) {
				return new HashMap<String, Object>();
			}
			Operator operator = this.operatorDao.checkOperatorLogin(
					operatorCode, operatorPassword);
			Map<String, Object> result = new HashMap<String, Object>();
			if (operator == null) {
				result.put("isLogin", false);
				return result;
			}
			if (operator.getEmployee() != null) {
				operator.getEmployee().getName();
			}

			LoginUser loginUser = this.setLoginUser(operator);
			super.setLoginUser(loginUser);
			loginUser.setCurrentOrganization(organization);
			result.put("isLogin", true);
			result.put("loginUser", loginUser);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("checkLoginUser", e.getMessage(), e);
		}
	}

	private LoginUser setLoginUser(Operator userOperator) throws Exception {
		try {
			EntitySet<OperatorAndRole> operatorAndRoles = (EntitySet<OperatorAndRole>) userOperator
					.getOperatorAndRoles();
			EntitySet<Role> userRoles = new EntitySet<Role>();

			for (OperatorAndRole operatorAndRole : operatorAndRoles) {
				operatorAndRole.getRole().getName();
				userRoles.add(operatorAndRole.getRole());
			}

			this.permissionCodes = new HashSet<String>();

			for (Role role : userRoles) {
				this.setUserPermissionCodes((EntitySet<Permission>) role
						.getPermissions());
			}

			LoginUser loginUser = new LoginUser();
			loginUser.setLoginOperator(userOperator);
			loginUser.setUserRoles(userRoles);
			loginUser.setUserPermissionsCode(permissionCodes);

			return loginUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("setLoginUser", e.getMessage(), e);
		}
	}

	private void setUserPermissionCodes(EntitySet<Permission> permissions) {
		for (Permission permission : permissions) {
			if (this.permissionCodes.contains(permission.getCode()))
				continue;

			this.permissionCodes.add(permission.getCode());

		}
	}

	public Integer distributionRole(Long operatorId, EntitySet<Role> roles,
			Long organizationId) throws Exception {
		if (operatorId != null && organizationId != null) {
			this.operatorAndRoleDao.deleteOperatorAndRoleBysql(operatorId,
					organizationId);
		}

		EntitySet<OperatorAndRole> operatorAndRoles = new EntitySet<OperatorAndRole>();
		Operator operator = new Operator();
		operator.setEId(operatorId);
		Organization organization = new Organization();
		organization.setEId(organizationId);

		for (Role role : roles) {
			OperatorAndRole operatorAndRole = new OperatorAndRole();
			operatorAndRole.setOperator(operator);
			operatorAndRole.setOrganization(organization);
			operatorAndRole.setRole(role);
			operatorAndRoles.add(operatorAndRole);
		}

		return this.operatorAndRoleDao.saveOperatorAndRole(operatorAndRoles);
	}

	@Override
	public ArrayList<Organization> searchOrganization(String operatorCode)
			throws Exception {
		EntitySet<Organization> result = this.operatorDao
				.searchOrganization(operatorCode);
		Iterator<Organization> ite = result.iterator();
		ArrayList<Organization> orgs = new ArrayList<Organization>();
		while (ite.hasNext()) {
			orgs.add(ite.next());
		}
		return orgs;
	}

	@Override
	public Integer copyOperator(List<Long> sourceEIds, List<Long> targetEIds)
			throws Exception {
		// 得到源操作员的所有角色
		EntitySet<OperatorAndRole> operatorAndRoles = new EntitySet<OperatorAndRole>();
		EntitySet<OperatorAndRole> operatorAndRolesTarget = new EntitySet<OperatorAndRole>();
		EntitySet<Role> sourceRoles = new EntitySet<Role>();
		EntitySet<Organization> targetOrganizations = new EntitySet<Organization>();

		for (Long EId : sourceEIds) {
			operatorAndRoles = this.operatorAndRoleDao
					.findRolesByOperatorId(EId);

			for (OperatorAndRole operatorAndRole : operatorAndRoles) {
				sourceRoles.add(operatorAndRole.getRole());
			}
		}
		// 遍历目标操作员，给每个操作员设置角色
		EntitySet<Role> targetRoles = new EntitySet<Role>();

		for (Long EId : targetEIds) {
			operatorAndRoles = this.operatorAndRoleDao
					.findRolesByOperatorId(EId);
			Operator operator = this.operatorDao.findOperatorById(EId);

			for (OperatorAndRole operatorAndRole : operatorAndRoles) {
				targetOrganizations.add(operatorAndRole.getOrganization());
			}

			for (Role role : sourceRoles) {
				for (Organization organization : targetOrganizations) {
					OperatorAndRole operatorAndRole = new OperatorAndRole();
					operatorAndRole.setOperator(operator);
					operatorAndRole.setRole(role);
					operatorAndRole.setOrganization(organization);
					operatorAndRolesTarget.add(operatorAndRole);
				}

			}
			targetOrganizations.clear();
		}

		return this.operatorAndRoleDao
				.saveOperatorAndRole(operatorAndRolesTarget);
	}

}
