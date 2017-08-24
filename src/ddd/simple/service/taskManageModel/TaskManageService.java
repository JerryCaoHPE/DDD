package ddd.simple.service.taskManageModel;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.taskManageModel.TaskManage;

public interface TaskManageService extends BaseServiceInterface
{
	public TaskManage saveTaskManage(TaskManage taskManage) ;
	
	public int deleteTaskManage(Long taskManageId) ;
	
	public TaskManage updateTaskManage(TaskManage taskManage) ;
	
	public TaskManage findTaskManageById(Long taskManageId) ;
	
	public EntitySet<TaskManage> findAllTaskManage() ;

	public EntitySet<TaskManage> findAllTaskUnDealed();
 
}