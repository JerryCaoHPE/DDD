package ddd.simple.entity.member;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.permission.Role;

@ddd.base.annotation.Entity(label="会员类别",name="memberType")
public class MemberType extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="类别名称",nullable=false,comment="")
	private String typeName;

	@Column(label="类别编码",nullable=false,unique=true,comment="")
	private String typeCade;
	
	@Column(name="roleId",label="角色",FKName="MT_FK_R")
	private Role role;
	
	public String getTypeName() {
		lazyLoad();
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCade() {
		lazyLoad();
		return this.typeCade;
	}

	public void setTypeCade(String typeCade) {
		this.typeCade = typeCade;
	}

	public Role getRole() {
		lazyLoad();
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
   
}