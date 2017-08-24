package ddd.simple.service.scheduleTask.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.scheduleTask.ScheduleTask;
import ddd.simple.dao.scheduleTask.ScheduleTaskDao;
import ddd.simple.service.scheduleTask.ScheduleService;
import ddd.simple.service.scheduleTask.ScheduleTaskService;

@Service
public class ScheduleTaskServiceBean extends BaseService implements ScheduleTaskService
{

	@Resource(name="scheduleTaskDaoBean")
	private ScheduleTaskDao scheduleTaskDao;
	
	@Resource(name="scheduleServiceBean")
	private ScheduleService scheduleService;
	
	@Override
	public ScheduleTask saveScheduleTask(ScheduleTask scheduleTask) 
	{
		try {
			if(scheduleTask.getStartTime()==null){
				scheduleTask.setStartTime(new Date());
			}
			scheduleTask.setNextStartTime(scheduleTask.getStartTime());
			
			ScheduleTask returnScheduleTask =this.scheduleTaskDao.saveScheduleTask(scheduleTask); 
			scheduleService.addScheduleTask(returnScheduleTask);
			return returnScheduleTask;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveScheduleTask", e.getMessage(), e);
		}
	}

	@Override
	public int deleteScheduleTask(Long scheduleTaskId) {
		try {
			ScheduleTask scheduleTask = this.scheduleTaskDao.findScheduleTaskById(scheduleTaskId);
			int result =this.scheduleTaskDao.deleteScheduleTask(scheduleTaskId);
			this.scheduleService.removeScheduleTask(scheduleTask);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteScheduleTask", e.getMessage(), e);
		}
		
	}

	@Override
	public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask) {
		try {
			if(scheduleTask.getStartTime()==null){
				scheduleTask.setStartTime(new Date());
			}
			if(scheduleTask.getNextStartTime()==null){
				scheduleTask.setNextStartTime(scheduleTask.getStartTime());
			}
			
			
			ScheduleTask returnScheduleTask =this.scheduleTaskDao.updateScheduleTask(scheduleTask);
			scheduleService.rebuildScheduleTask(returnScheduleTask);
			return returnScheduleTask;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateScheduleTask", e.getMessage(), e);
		}
	}

	@Override
	public ScheduleTask findScheduleTaskById(Long scheduleTaskId) {
		try {
			return this.scheduleTaskDao.findScheduleTaskById(scheduleTaskId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findScheduleTaskById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ScheduleTask> findAllScheduleTask() {
		try{
			return this.scheduleTaskDao.findAllScheduleTask();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllScheduleTask", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ScheduleTask> findStartScheduleTasks() {
		try{
			return this.scheduleTaskDao.findStartScheduleTasks();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findStartScheduleTasks", e.getMessage(), e);
		}
	}

}
