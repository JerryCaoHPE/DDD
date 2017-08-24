package ddd.simple.entity.permission;

import java.io.Serializable;
import java.util.Collection;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="role",label="角色")
public class Role extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(label="角色名称")
	private String name;
	
	@Column(label="角色编码")
	private String code;
	
	
	@Column(label="权限点")
	private Collection<Permission> permissions;
	
	@Column(joinColumn="roleId",composition=true)
	private Collection<OperatorAndRole> operatorAndRoles;

	// 前台使用 不生成表结构
	private Boolean isAssigned;
	
	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		lazyLoad();
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsAssigned() {
		lazyLoad();
		return isAssigned;
	}

	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public Collection<Permission> getPermissions() {
		lazyLoad("permissions");
		return permissions;
	}

	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = permissions;
	}

	public Collection<OperatorAndRole> getOperatorAndRoles() {
		lazyLoad("operatorAndRoles");
		return operatorAndRoles;
	}

	public void setOperatorAndRoles(Collection<OperatorAndRole> operatorAndRoles) {
		this.operatorAndRoles = operatorAndRoles;
	}
	
	
}
