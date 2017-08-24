package ddd.simple.dao.permission;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.OperatorAndRole;


public interface OperatorAndRoleDao{
	
	public EntitySet<OperatorAndRole> findRolesByOperatorIdandOrganizationId(Long operatorId,Long organizationId) throws Exception;

	public Integer saveOperatorAndRole(EntitySet<OperatorAndRole> operatorAndRoles) throws Exception;

	public void deleteOperatorAndRoleBysql(Long operatorId,Long organizationId) throws Exception;
	
	public EntitySet<OperatorAndRole> findRolesByOperatorId(Long operatorId) throws Exception;
}
