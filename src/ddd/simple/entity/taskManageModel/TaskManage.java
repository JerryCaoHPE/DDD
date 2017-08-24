package ddd.simple.entity.taskManageModel;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

import ddd.simple.entity.member.Member;
import ddd.simple.entity.organization.Employee;
import java.util.Date;

@ddd.base.annotation.Entity(label="任务管理",name="taskManage")
public class TaskManage extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="职员",name="employeeId",comment="")
	private Employee employee;

	@Column(label="会员",name="memberId",comment="")
	private Member member;

	@Column(label="开始日期",comment="")
	private Date startDate;
	
	@Column(label="结束日期",comment="")
	private Date endDate;

	@Column(label="描述",comment="")
	private String taskDescribe;

	@Column(label="任务名",comment="")
	private String name;

	@Column(label="路由",comment="")
	private String route;

	@Column(label="状态",comment="")
	private String state;

	@Column(label="类型",comment="")
	private String type;


	public Employee getEmployee() {
		lazyLoad();
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Member getMember() {
		lazyLoad();
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Date getStartDate() {
		lazyLoad();
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		lazyLoad();
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getTaskDescribe()
	{
		return taskDescribe;
	}

	public void setTaskDescribe(String taskDescribe)
	{
		this.taskDescribe = taskDescribe;
	}

	public String getName() {
		lazyLoad();
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoute() {
		lazyLoad();
		return this.route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getState() {
		lazyLoad();
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		lazyLoad();
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
}