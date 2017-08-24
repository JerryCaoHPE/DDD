package ddd.simple.dao.scheduleTask.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.scheduleTask.ScheduleTask;
import ddd.simple.dao.scheduleTask.ScheduleTaskDao;

@Service
public class ScheduleTaskDaoBean extends BaseDao implements ScheduleTaskDao
{
	@Override
	public ScheduleTask saveScheduleTask(ScheduleTask scheduleTask)  throws Exception{
		return this.save(scheduleTask);
	}

	@Override
	public int deleteScheduleTask(Long scheduleTaskId)  throws Exception{
		return this.deleteById(scheduleTaskId,ScheduleTask.class);
	}

	@Override
	public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask)  throws Exception{
		return this.update(scheduleTask);
	}

	@Override
	public ScheduleTask findScheduleTaskById(Long scheduleTaskId)  throws Exception{
		return this.query(scheduleTaskId, ScheduleTask.class);
	}
	
	@Override
	public EntitySet<ScheduleTask> findAllScheduleTask() throws Exception {
		return this.query("1=1",ScheduleTask.class);
	}
	
	@Override
	public EntitySet<ScheduleTask> findStartScheduleTasks() throws Exception {
		return this.query("state='启用'",ScheduleTask.class);
	}
	
}
