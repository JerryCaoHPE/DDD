package ddd.simple.service.permission.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.permission.*;
import ddd.simple.service.permission.*;
import ddd.simple.entity.permission.*;

@Service
public class OperatorAndRoleServiceBean  implements OperatorAndRoleService
{
	@Resource(name="operatorAndRoleDaoBean")
	private OperatorAndRoleDao operatorAndRoleDao;

	public EntitySet<OperatorAndRole> findRolesByOperatorIdandOrganizationId(Long operatorId,Long organizationId) throws Exception{
		try {
			EntitySet<OperatorAndRole> operatorAndRoles = this.operatorAndRoleDao.findRolesByOperatorIdandOrganizationId(operatorId, organizationId);
			return operatorAndRoles;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findRolesByOperatorIdandOrganizationId", e.getMessage(), e);
		}
	}
	
	public Integer saveOperatorAndRole(EntitySet<OperatorAndRole> operatorAndRoles) throws Exception{
		try {
			if(operatorAndRoles == null || operatorAndRoles.isEmpty()){
				return 0;
			}
			Integer count = this.operatorAndRoleDao.saveOperatorAndRole(operatorAndRoles);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveOperatorAndRole", e.getMessage(), e);
		}
	}
	
	public void deleteOperatorAndRole(Long operatorId,Long organizationId) throws Exception{
		try {
			this.operatorAndRoleDao.deleteOperatorAndRoleBysql(operatorId,organizationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteOperatorAndRole", e.getMessage(), e);
		}
	}
	
}
