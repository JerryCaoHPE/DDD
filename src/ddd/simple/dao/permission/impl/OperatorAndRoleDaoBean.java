package ddd.simple.dao.permission.impl;


import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.permission.OperatorAndRoleDao;
import ddd.simple.entity.permission.OperatorAndRole;
@Service
public class OperatorAndRoleDaoBean extends BaseDao implements OperatorAndRoleDao {

	public EntitySet<OperatorAndRole> findRolesByOperatorIdandOrganizationId(Long operatorId,Long organizationId) throws Exception{
		String sql = "operatorId = "+operatorId+" and organizationId = "+organizationId;
		return (EntitySet<OperatorAndRole>)this.query(sql, OperatorAndRole.class);
	}
	
	public Integer saveOperatorAndRole(EntitySet<OperatorAndRole> operatorAndRoles) throws Exception{
		if(operatorAndRoles == null || operatorAndRoles.isEmpty()){
			return 0;
		}
		
		EntitySet<OperatorAndRole> newOperatorAndRoles = (EntitySet<OperatorAndRole>) this.save(operatorAndRoles);
		
		return newOperatorAndRoles.size();
	}
	
	public void deleteOperatorAndRoleBysql(Long operatorId,Long organizationId) throws Exception{
		String sql = "operatorId = " + operatorId + " and organizationId = "+organizationId;
		this.deleteByWhere(sql, OperatorAndRole.class);
	}


	public EntitySet<OperatorAndRole> findRolesByOperatorId(Long operatorId) throws Exception {
		String sql = "operatorId = "+operatorId;
		return (EntitySet<OperatorAndRole>)this.query(sql, OperatorAndRole.class);
		
	}
}
