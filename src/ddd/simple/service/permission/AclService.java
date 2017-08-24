package ddd.simple.service.permission;

import java.util.List;

import ddd.simple.entity.permission.ACL;
import ddd.simple.entity.permission.Module;

public interface AclService {
	/**
	 * 授权
	 * 
	 * @param roleId
	 *            主体标识
	 * @param module
	 *            资源标识
	 * @param permission
	 * @param yes
	 *            是否允许，true 表示允许,false表示不允许
	 * @return 
	 */
	public ACL addOrUpdatePermission(long roleId, long moduleId,
			int permission, boolean yes);

	/**
	 *  删除
	 * @param principalTyoe
	 * @param principalSn
	 * @param resourceSn
	 */
	public void delPermission(
			long roleId,
			long moduleId
			);
	
	/**
	 * 启用模块
	 */
	
	public void addPermission(long roleId,long moduleId);
		
	/**
	 * 
	 * 判断角色对某模块的某操作的授权（允许或不允许）
	 */
	public boolean hasPermission(long roleId, long moduleId, int permission);

	/**
	 * 搜索某个角色拥有的权限的模块列表（顶级模块）
	 * 
	 * @return
	 */
	public List<Module> searchModules(long roleId);

	public ACL findAcl(long roleId, long moduleId);

}
