package ddd.simple.dao.scheduleTask;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.scheduleTask.ScheduleTask;

public interface ScheduleTaskDao extends BaseDaoInterface
{
	public ScheduleTask saveScheduleTask(ScheduleTask scheduleTask) throws Exception;
	
	public int deleteScheduleTask(Long scheduleTaskId) throws Exception;
	
	public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask) throws Exception;
	
	public ScheduleTask findScheduleTaskById(Long scheduleTaskId) throws Exception;
	
	public EntitySet<ScheduleTask> findAllScheduleTask() throws Exception;
	
	public EntitySet<ScheduleTask> findStartScheduleTasks() throws Exception;
}
