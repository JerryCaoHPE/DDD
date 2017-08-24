package ddd.simple.dao.taskManageModel.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.taskManageModel.TaskManage;
import ddd.simple.dao.taskManageModel.TaskManageDao;

@Service
public class TaskManageDaoBean extends BaseDao implements TaskManageDao
{
	@Override
	public TaskManage saveTaskManage(TaskManage taskManage)  throws Exception{
		return this.save(taskManage);
	}

	@Override
	public int deleteTaskManage(Long taskManageId)  throws Exception{
		return this.deleteById(taskManageId,TaskManage.class);
	}

	@Override
	public TaskManage updateTaskManage(TaskManage taskManage)  throws Exception{
		return this.update(taskManage);
	}

	@Override
	public TaskManage findTaskManageById(Long taskManageId)  throws Exception{
		return this.query(taskManageId, TaskManage.class);
	}
	
	@Override
	public EntitySet<TaskManage> findAllTaskManage() throws Exception {
		return this.query("1=1",TaskManage.class);
	}

	@Override
	public EntitySet<TaskManage> findAllTaskUnDealed() throws Exception
	{
		return this.query("state != '已处理' ",TaskManage.class);
	}
}
