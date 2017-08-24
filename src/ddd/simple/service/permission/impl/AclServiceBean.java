package ddd.simple.service.permission.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddd.simple.dao.permission.AclDao;
import ddd.simple.entity.permission.ACL;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.permission.AclService;

@Service
public class AclServiceBean  implements AclService {

	@Resource(name = "aclDaoBean")
	private AclDao aclDao;

	public ACL addOrUpdatePermission(long roleId, long moduleId,
			int permission, boolean yes) {
		ACL acl = findAcl(roleId, moduleId);
/*
		if (acl == null) {
			acl = new ACL();
			acl.setModule((Module) aclDao.load(Module.class, moduleId));
			acl.setRole((Role) aclDao.load(Role.class, roleId));
			acl.setPermission(permission, yes);
			
			return aclDao.addOrUpdatePermission(acl);;
		} else {
			acl.setPermission(permission, yes);
			return aclDao.update(acl);
		}*/

		return acl;
	}

	public void addPermission(long roleId, long moduleId) {
		/*ACL acl = findAcl(roleId, moduleId);
		if (acl == null) {
			acl = new ACL();
			acl.setModule((Module) aclDao.load(Module.class, moduleId));
			acl.setRole((Role) aclDao.load(Role.class, roleId));
			acl.setState(0);
			aclDao.save(acl);
		}*/
	}

	public void delPermission(long roleId, long moduleId) {
		/*ACL acl = findAcl(roleId, moduleId);
		if (acl == null)
			throw new RuntimeException("没找到相应ACL实例");

		aclDao.delete(acl);*/
	}

	public boolean hasPermission(long roleId, long moduleId, int permission) {
		/*ACL acl = findAcl(roleId, moduleId);
		if (acl == null)
			throw new RuntimeException("没找到相应ACL实例");

		return (acl.getPermission(permission));*/
		
		return false;
	}

	public List<Module> searchModules(long roleId) {
		return null;
	}

	public ACL findAcl(long roleId, long moduleId) {
		/*List<ACL> lists = aclDao
				.find(
						"select acl from ACL acl where acl.role.roleId=? and acl.module.moduleId=?",
						new Long[] { roleId, moduleId });

		if (lists == null || lists.size() == 0)
			return null;
		else
			return lists.get(0);*/
		return null;
	}
}
