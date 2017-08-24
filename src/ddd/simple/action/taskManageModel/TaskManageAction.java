package ddd.simple.action.taskManageModel;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.taskManageModel.TaskManage;
import ddd.simple.service.taskManageModel.TaskManageService;

@Action
@RequestMapping("/TaskManage")
@Controller
public class TaskManageAction
{
	@Resource(name="taskManageServiceBean")
	private TaskManageService taskManageService;
	
	public TaskManage saveTaskManage(TaskManage taskManage)
	{
		try {
			TaskManage saveTaskManage = this.taskManageService.saveTaskManage(taskManage);
			return saveTaskManage;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteTaskManage(Long taskManageId){
		
		try {
			return this.taskManageService.deleteTaskManage(taskManageId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public TaskManage updateTaskManage(TaskManage taskManage) {
		try {
			TaskManage updateTaskManage = this.taskManageService.updateTaskManage(taskManage);
			return updateTaskManage;
		} catch (DDDException e) {
			throw e;
		}
	}

	public TaskManage findTaskManageById(Long taskManageId){
		try {
			TaskManage findTaskManage = this.taskManageService.findTaskManageById(taskManageId);
			return  findTaskManage;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<TaskManage> findAllTaskManage(){
		try{
			EntitySet<TaskManage> allTaskManage = this.taskManageService.findAllTaskManage();
			return allTaskManage;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<TaskManage> findAllTaskUnDealed(){
		try{
			EntitySet<TaskManage> allTaskUndealed = this.taskManageService.findAllTaskUnDealed();
			return allTaskUndealed;
		} catch (DDDException e) {
			throw e;
		}
	}

}