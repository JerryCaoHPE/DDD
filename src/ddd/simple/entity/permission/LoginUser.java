package ddd.simple.entity.permission;

import java.io.Serializable;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.memberGroup.MemberGroup;
import ddd.simple.entity.organization.Employee;
import ddd.simple.entity.organization.Organization;
/**
 * 
 * @author hx
 * userRoles 此次登陆人对应的角色
 * EId 登陆人id member|operator
 * loginVip 后台登陆 该值为空
 * loginOperator 前台登陆 该值为空
 * userName 登陆人名称 
 * userPermissionsCode 登陆人所有的权限编码
 */
public class LoginUser implements Serializable{

	private static final long serialVersionUID = 4510107943850149711L;

	// 前台登陆有效，多角色
	private EntitySet<Role> userRoles;
	
	// 在后台登陆有效 前台登陆为多角色,该字段无效
	private Role currentRole;

	private Set<String> userPermissionsCode;
	
	private Operator loginOperator;
	
	private Member loginVip;
	
	private String userName;
	
	private Organization currentOrganization;
	
	private EntitySet<MemberGroup> groups;
	
	private MemberGroup currentGroup;
	
	private Long EId;
	
	public EntitySet<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(EntitySet<Role> userRoles) {
		this.userRoles = userRoles;
	}
	
	public Set<String> getUserPermissionsCode() {
		return userPermissionsCode;
	}

	public void setUserPermissionsCode(Set<String> userPermissionsCode) {
		this.userPermissionsCode = userPermissionsCode;
	}

	public Operator getLoginOperator() {
		return loginOperator;
	}

	public void setLoginOperator(Operator loginOperator) {
		this.loginOperator = loginOperator;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getEId() {
		if(this.loginOperator != null)
		{
			return this.loginOperator.getEId();
		}
		else if(this.loginVip != null)
		{
			return this.loginVip.getEId();
		}
		else
		{
			return 0l;
		}
	}

 

	public Member getLoginVip() {
		return loginVip;
	}

	public void setLoginVip(Member loginVip) {
		this.loginVip = loginVip;
	}

	public Role getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(Role currentRole) {
		this.currentRole = currentRole;
	}

	public Organization getCurrentOrganization() {
		return currentOrganization;
	}

	public void setCurrentOrganization(Organization currentOrganization) {
		this.currentOrganization = currentOrganization;
	}
	
	public Employee findCurrentEmployee(){
		if(this.loginOperator != null){
			return this.loginOperator.getEmployee();
		}
		return null;
	}

	public EntitySet<MemberGroup> getGroups() {
		return groups;
	}

	public void setGroups(EntitySet<MemberGroup> groups) {
		this.groups = groups;
	}

	public MemberGroup getCurrentGroup()
	{
		return currentGroup;
	}

	public void setCurrentGroup(MemberGroup currentGroup)
	{
		this.currentGroup = currentGroup;
	}
	
	
	public String getOperatorType()
	{
		if(this.loginOperator != null)
		{
			return "operator";
		}
		else if(this.loginVip != null)
		{
			return "member";
		}
		else
		{
			return "other";
		}
	}	
	
}
