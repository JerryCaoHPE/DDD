package ddd.simple.entity.permission;

import java.io.Serializable;
import java.util.Collection;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.organization.Employee;

@ddd.base.annotation.Entity(name="operator",label="操作员")
public class Operator extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(label="操作员名称")
	private String name;
	
	@Column(label="编码")
	private String code;
	
	@Column(label="密码")
	private String passWord;
	
	@Column(name="employeeId",label="职员编码",FKName="O_FK_E")
	private Employee employee;
	
	@Column(joinColumn="operatorId",composition=true)
	private Collection<OperatorAndRole> operatorAndRoles;

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

	public String getPassWord() {
		lazyLoad();
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Employee getEmployee() {
		lazyLoad();
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Collection<OperatorAndRole> getOperatorAndRoles() {
		lazyLoad("operatorAndRoles");
		return operatorAndRoles;
	}

	public void setOperatorAndRoles(Collection<OperatorAndRole> operatorAndRoles) {
		this.operatorAndRoles = operatorAndRoles;
	}
	
	
}
