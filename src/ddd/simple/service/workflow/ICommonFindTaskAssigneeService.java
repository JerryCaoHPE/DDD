package ddd.simple.service.workflow;

import java.util.Collection;

/**
 * 通用查找任务的参与者服务
 * @author luozeliang
 *
 */
public interface ICommonFindTaskAssigneeService {
	public Collection<String> 根据权限点查找(String 权限点名称);
	public Collection<String> 根据角色查找(String 角色名称);
	public Collection<String> 根据部门查找(String 部门名称);
	public Collection<String> 根据人员查找(String 人员Id);
	
	public Collection<String> 根据权限点和所属机构查找(String 权限点名称,String 机构名称);
	public Collection<String> 根据角色和所属机构查找(String 角色名称,String 机构名称);
	public Collection<String> 根据角色和所属单位查找(String 角色名称,String 单位名称);
	public Collection<String> 根据部门和所属机构查找(String 部门名称,String 机构名称);
	
	
}
