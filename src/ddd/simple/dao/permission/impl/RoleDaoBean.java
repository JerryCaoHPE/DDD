
package ddd.simple.dao.permission.impl;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.permission.RoleDao;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
@Service
public class RoleDaoBean extends BaseDao implements RoleDao {


	public Role saveRole(Role role) throws Exception {
		return  this.save(role);
	}

	public int deleteRole(Long roleId) throws Exception {
		return this.deleteById(roleId,Role.class);
	}

	public Role updateRole(Role role) throws Exception {
		return this.update(role);
	}

	public Role findRoleById(Long roleId) throws Exception {
		return this.query(roleId, Role.class);
	}
	
	public EntitySet<Role> findAllRoles() throws Exception {
		return (EntitySet<Role>) this.query("1=1",Role.class);
	}
	
	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception{
		String findSql = "moduleId="+moduleId;
		return (EntitySet<Permission>) this.query(findSql,Permission.class);
	}
	
	public EntitySet<Role> finRoleByEmployeeId(Long employeeId) throws Exception{
		String sql = "select * from role r where r.eid in "
				+ "(select distinct oar.roleid from OperatorAndRole oar where oar.operatorid in "
				+ "(select o.eid from operator o left join employee e on o.employeeId = e.eid where e.eid ="+employeeId+"))";
		return this.queryBySql(sql, Role.class);
	}

}
