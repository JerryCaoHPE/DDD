package ddd.simple.entity.log;

import java.io.Serializable;
import java.util.Date;
import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(name="log")
public class Log extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 本次操作的操作人员
	 */
	@Column(name="operator",label="操作人员姓名",length=20)
	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
    

	/**
	 * 操作时间，默认为当前时间
	 */
	@Column(name="logDate",label="操作时间",length=20)
	private Date logDate;

	public Date getLogDate() {	
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = (logDate == null ? new Date() : logDate);
	}
	
	/**
	 * 本次操作所在的机器IP地址
	 */
	@Column(name="ipAddress",label="操作人员IP",length=20)
	private String ipAddress;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * 日志类型，如：系统日志，用户日志
	 */
	@Column(name="logType",label="操作日志类型",length=20)
	private String logType;

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	/**
	 * 本次操作的简单名称，如：操作名称
	 */
	@Column(name="operationName",label="操作名称",length=100)
	private String operationName;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * 操作内容，本次操作的具体详细的内容
	 */
	@Column(name="operationContent",label="操作内容",length=8000)
	private String operationContent;

	public String getOperationContent() {
		return operationContent;
	}

	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}
	

	/**
	 * 实体类名，记录该实体的完整类名
	 */
	@Column(name="entityClassName",label="记录该实体的完整类名",length=100)
	private String entityClassName;

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}
}
