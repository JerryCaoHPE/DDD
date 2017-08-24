package ddd.simple.dao.taskManageModel;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.taskManageModel.TaskManage;

public interface TaskManageDao extends BaseDaoInterface
{
	public TaskManage saveTaskManage(TaskManage taskManage) throws Exception;
	
	public int deleteTaskManage(Long taskManageId) throws Exception;
	
	public TaskManage updateTaskManage(TaskManage taskManage) throws Exception;
	
	public TaskManage findTaskManageById(Long taskManageId) throws Exception;
	
	public EntitySet<TaskManage> findAllTaskManage() throws Exception;

	public EntitySet<TaskManage> findAllTaskUnDealed() throws Exception;
}
