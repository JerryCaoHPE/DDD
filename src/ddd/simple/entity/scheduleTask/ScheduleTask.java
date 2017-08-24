package ddd.simple.entity.scheduleTask;

import java.io.Serializable;
import java.util.Date;
import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

/**
 * 
 * @author ICE
 * 
 */
@ddd.base.annotation.Entity(name = "scheduleTask", label = "计划任务")
public class ScheduleTask extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "scheduleTaskName", label = "名称")
	private String scheduleTaskName;// 要执行的任务名称

	@Column(name = "scheduleTaskPath", label = "文件路径", length = 1000)
	private String scheduleTaskPath;// 执行文件路径

	@Column(name = "fileType", label = "文件类型")
	private String fileType;// 任务文件类型（exe,bat,method)

	@Column(name = "lastStartTime", label = "上次启动时间")
	private Date lastStartTime;// 上次启动时间

	@Column(name = "nextStartTime", label = "下次启动时间")
	private Date nextStartTime;// 下次启动时间

	@Column(name = "scheduledTaskType", label = "计划类型", codeTable = "scheduledTaskType")
	private String scheduledTaskType;// 计划类型（每次服务启动，一次，每天，每周，每月，间隔）

	@Column(name = "startTime", label = "启动时间")
	private Date startTime;// 启动时间

	@Column(name = "endTime", label = "结束时间")
	private Date endTime;// 结束时间

	@Column(name = "dayOfMonth", label = "日期")
	private Integer dayOfMonth;// 一个月的几号

	@Column(name = "dayOfWeek", label = "星期")
	private Integer dayOfWeek;// 星期几

	@Column(name = "period", label = "间隔数值")
	private Integer period;

	@Column(name = "periodUnit", label = "间隔单位")
	private String periodUnit;// 秒,分钟，小时，天，周，

	@Column(name = "state", label = "状态")
	private String state;// 状态（启用，停用）

	@Column(name = "executeResult", label = "执行结果", length = 8000)
	private String executeResult;

	public String getScheduleTaskName() {
		return scheduleTaskName;
	}

	public void setScheduleTaskName(String scheduleTaskName) {
		this.scheduleTaskName = scheduleTaskName;
	}

	public String getScheduleTaskPath() {
		return scheduleTaskPath;
	}

	public void setScheduleTaskPath(String scheduleTaskPath) {
		this.scheduleTaskPath = scheduleTaskPath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public Date getNextStartTime() {
		return nextStartTime;
	}

	public void setNextStartTime(Date nextStartTime) {
		this.nextStartTime = nextStartTime;
	}

	public String getScheduledTaskType() {
		return scheduledTaskType;
	}

	public void setScheduledTaskType(String scheduledTaskType) {
		this.scheduledTaskType = scheduledTaskType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getPeriodUnit() {
		return periodUnit;
	}

	public void setPeriodUnit(String periodUnit) {
		this.periodUnit = periodUnit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExecuteResult() {
		return executeResult;
	}

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}

}