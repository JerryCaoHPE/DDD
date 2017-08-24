package ddd.simple.dao.permission.impl;


import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.permission.PermissionDao;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;
@Service
public class PermissionDaoBean extends BaseDao implements PermissionDao {

	public Permission savePermission(Permission permission) throws Exception {
		return this.save(permission);
	}

	public int deletePermission(Long permissionId) throws Exception {
		return this.deleteById(permissionId, Permission.class);
	}

	public Permission updatePermission(Permission permission) throws Exception {
		return this.update(permission);
	}

	public Permission findPermissionById(Long permissionId) throws Exception {
		return this.query(permissionId, Permission.class);
	}

	public EntitySet<Permission> findAllPermissions() throws Exception {
		return (EntitySet<Permission>) this.query("1=1",Permission.class);
	}

	public EntitySet<Permission> findPermissionByModule(Long moduleId) throws Exception{
		String findSql = "moduleId="+moduleId;
		return (EntitySet<Permission>) this.query(findSql,Permission.class);
	}
	
	public int deleteByModule(EntitySet<Module> modules) throws Exception{
		String sql = "moduleId in (";
		for(Module module : modules){
			sql += module.getEId() + ",";
		}
		sql = sql.substring(0,sql.length()-1);
		sql += ")";
		return this.deleteByWhere(sql, Permission.class);
	}
}
