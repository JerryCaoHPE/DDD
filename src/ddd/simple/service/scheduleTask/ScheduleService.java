package ddd.simple.service.scheduleTask;

import ddd.simple.entity.scheduleTask.ScheduleTask;


public interface ScheduleService {
	
	/**
	 * 执行计划任务
	 */
	public void executeScheduleTask();
	
	public void addScheduleTask(ScheduleTask task);
	
	public void removeScheduleTask(ScheduleTask task);
	
	public void rebuildScheduleTask(ScheduleTask task);
	
	public void rebuildScheduleTask();
}
