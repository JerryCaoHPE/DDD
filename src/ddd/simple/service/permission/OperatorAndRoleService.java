package ddd.simple.service.permission;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.OperatorAndRole;

public interface OperatorAndRoleService {

	public EntitySet<OperatorAndRole> findRolesByOperatorIdandOrganizationId(Long operatorId,Long organizationId) throws Exception;

	public Integer saveOperatorAndRole(EntitySet<OperatorAndRole> operatorAndRoles) throws Exception;

	public void deleteOperatorAndRole(Long operatorId,Long organizationId) throws Exception;
}
