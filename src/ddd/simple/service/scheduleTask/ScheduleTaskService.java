package ddd.simple.service.scheduleTask;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.scheduleTask.ScheduleTask;

public interface ScheduleTaskService extends BaseServiceInterface
{
	public ScheduleTask saveScheduleTask(ScheduleTask scheduleTask) ;
	
	public int deleteScheduleTask(Long scheduleTaskId) ;
	
	public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask) ;
	
	public ScheduleTask findScheduleTaskById(Long scheduleTaskId) ;
	
	public EntitySet<ScheduleTask> findAllScheduleTask() ;
	public EntitySet<ScheduleTask> findStartScheduleTasks();
}