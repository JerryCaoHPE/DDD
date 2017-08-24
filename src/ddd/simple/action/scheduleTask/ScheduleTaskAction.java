package ddd.simple.action.scheduleTask;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.scheduleTask.ScheduleTask;
import ddd.simple.service.scheduleTask.ScheduleTaskService;

@Action
@RequestMapping("/ScheduleTask")
@Controller
public class ScheduleTaskAction
{
	@Resource(name="scheduleTaskServiceBean")
	private ScheduleTaskService scheduleTaskService;
	
	public ScheduleTask saveScheduleTask(ScheduleTask scheduleTask)
	{
		try {
			ScheduleTask saveScheduleTask = this.scheduleTaskService.saveScheduleTask(scheduleTask);
			return saveScheduleTask;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteScheduleTask(Long scheduleTaskId){
		
		try {
			return this.scheduleTaskService.deleteScheduleTask(scheduleTaskId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask) {
		try {
			ScheduleTask updateScheduleTask = this.scheduleTaskService.updateScheduleTask(scheduleTask);
			return updateScheduleTask;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ScheduleTask findScheduleTaskById(Long scheduleTaskId){
		try {
			ScheduleTask findScheduleTask = this.scheduleTaskService.findScheduleTaskById(scheduleTaskId);
			return  findScheduleTask;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ScheduleTask> findAllScheduleTask(){
		try{
			EntitySet<ScheduleTask> allScheduleTask = this.scheduleTaskService.findAllScheduleTask();
			return allScheduleTask;
		} catch (DDDException e) {
			throw e;
		}
	}

}