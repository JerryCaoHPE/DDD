package ddd.simple.action.codeGenerator;

import java.util.List;


public class PermissionMap
{
	private String permissionName;
	private String permissionCode;
	private List<String> actionPaths;
	public String getPermissionName()
	{
		return permissionName;
	}
	public void setPermissionName(String permissionName)
	{
		this.permissionName = permissionName;
	}
	public String getpermissionCode()
	{
		return permissionCode;
	}
	public void setpermissionCode(String permissionCode)
	{
		this.permissionCode = permissionCode;
	}
	public List<String> getActionPaths()
	{
		return actionPaths;
	}
	public void setActionPaths(List<String> actionPaths)
	{
		this.actionPaths = actionPaths;
	}
	
	
}