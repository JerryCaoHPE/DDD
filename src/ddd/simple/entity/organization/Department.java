package ddd.simple.entity.organization;


import java.io.Serializable;



import java.util.List;
import java.util.Set;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;



/**
 * 部门
 * @author 龚翔 2015/3/17
 */
@ddd.base.annotation.Entity(name="department",label="部门")
public class Department extends Entity implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	/**部门编码*/
	@Column(label="部门编码")
	private String code;
	/**部门名称*/
	@Column(label="部门名称")
	private String name;
	/**所属单位*/
	@Column(label="所属单位",name="organizationId",FKName="D_FK_O")
	private Organization organization;
	
	
/*	*//**部门类型*//*
	@Column(label="部门类型")
	private CodeTable departmentType;*/
	/**办公电话*/
	@Column(label="办公电话")
	private String officePhoneNumber;
	/**传真*/
	@Column(label="传真")
	private String faxNumber;
	/**地址*/
	@Column(label="地址",length=3000)
	private String address;
	/**是否在用*/
	@Column(label="是否启用")
    private Boolean enable;

	@Column(label="职员集合",joinColumn="departmentId",composition=true)
	private EntitySet<Employee> employees;
	
	public String getCode() {
		lazyLoad();
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		lazyLoad();
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Organization getOrganization() {
		lazyLoad();
		return this.organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public String getOfficePhoneNumber() {
		lazyLoad();
		return officePhoneNumber;
	}
	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}
	public String getFaxNumber() {
		lazyLoad();
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getAddress() {
		lazyLoad();
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Boolean getEnable() {
		lazyLoad();
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public EntitySet<Employee> getEmployees() {
		lazyLoad("employees");
		return employees;
	}
	public void setEmployees(EntitySet<Employee> employees) {
		this.employees = employees;
	}
	
}