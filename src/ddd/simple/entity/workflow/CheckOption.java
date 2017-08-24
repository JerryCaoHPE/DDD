package ddd.simple.entity.workflow;

import java.io.Serializable;
import java.util.Date;


import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

/**
* 审批历史
*/
@ddd.base.annotation.Entity(name="checkOption",label="审批历史")
public class CheckOption extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Column(name="processInstanceId",label="流程实例Id",nullable=false)
	private String processInstanceId;

//	@Column(length = 8000, name="checkResult",label = "办理结果")
	@Column(name="checkResult",label = "办理结果")
	private String checkResult;
	
//	@Column(length = 8000, name="description",label = "任务描述")
	@Column(name="description",label = "任务描述")
	private String description;

	@Column(name="formId",label="单据Id",nullable=false)
	private Long formId;

	@Column(name="formType",label="单据类型",nullable=false)
	private String formType;

//	@Column(length = 8000, name="managerOption",label = "办理人意见")
	@Column(name="description",label = "任务描述")
	private String managerOption;
	
	@Column(name="formRoute",label = "表单地址")
	private String formRoute;

	@Column(name="checkPeople",label="实际任务办理人")
	private String checkPeople;
	
	@Column(name="assigneePeople",label = "任务办理人")
	private String assigneePeople;

	@Column(name="checkTime",label="办理时间")
	private Date checkTime;

	@Column(name="taskName",label="任务名称")
	private String taskName;
	
	@Column(name="organizationName",label="公司名称")
	private String organizationName;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getManagerOption() {
		return managerOption;
	}

	public void setManagerOption(String managerOption) {
		this.managerOption = managerOption;
	}


	public String getCheckPeople() {
		return checkPeople;
	}

	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}

	public String getAssigneePeople() {
		return assigneePeople;
	}

	public void setAssigneePeople(String assigneePeople) {
		this.assigneePeople = assigneePeople;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFormRoute() {
		return formRoute;
	}

	public void setFormRoute(String formRoute) {
		this.formRoute = formRoute;
	}


	
}