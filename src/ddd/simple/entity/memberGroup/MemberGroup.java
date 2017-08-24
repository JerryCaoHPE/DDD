package ddd.simple.entity.memberGroup;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.organization.Organization;



@ddd.base.annotation.Entity(label="会员组",name="memberGroup")
public class MemberGroup extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="所属单位",name="organizationId",nullable=false,comment="",FKName="MG_FK_O")
	private Organization organization;

	@Column(label="名称",nullable=false,unique=true,comment="")
	private String name;


	public Organization getOrganization() {
		lazyLoad();
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getName() {
		lazyLoad();
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}