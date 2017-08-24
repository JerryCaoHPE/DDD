package ddd.simple.entity.permission;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.organization.Organization;

@ddd.base.annotation.Entity(name="OperatorAndRole",label="操作员和角色")
public class OperatorAndRole extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="operatorId",label="操作员",nullable=false,FKName="OAR_fk_operator")
	private Operator operator;
	
	@Column(name="roleId",label="角色",nullable=false,FKName="OAR_fk_role")
	private Role role;
	
	@Column(name="organizationId",label="引用机构",nullable=false,FKName="OAR_fk_org")
	private Organization organization;

	public Operator getOperator() {
		lazyLoad();
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Role getRole() {
		lazyLoad();
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Organization getOrganization() {
		lazyLoad();
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
}
