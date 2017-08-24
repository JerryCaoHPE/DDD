package ddd.simple.service.taskManageModel.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.taskManageModel.TaskManage;
import ddd.simple.dao.taskManageModel.TaskManageDao;
import ddd.simple.service.taskManageModel.TaskManageService;

@Service
public class TaskManageServiceBean extends BaseService implements TaskManageService
{

	@Resource(name="taskManageDaoBean")
	private TaskManageDao taskManageDao;
	
	@Override
	public TaskManage saveTaskManage(TaskManage taskManage) 
	{
		try {
			return this.taskManageDao.saveTaskManage(taskManage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveTaskManage", e.getMessage(), e);
		}
	}

	@Override
	public int deleteTaskManage(Long taskManageId) {
		try {
			return this.taskManageDao.deleteTaskManage(taskManageId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteTaskManage", e.getMessage(), e);
		}
		
	}

	@Override
	public TaskManage updateTaskManage(TaskManage taskManage) {
		try {
			return this.taskManageDao.updateTaskManage(taskManage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateTaskManage", e.getMessage(), e);
		}
	}

	@Override
	public TaskManage findTaskManageById(Long taskManageId) {
		try {
			return this.taskManageDao.findTaskManageById(taskManageId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findTaskManageById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<TaskManage> findAllTaskManage() {
		try{
			return this.taskManageDao.findAllTaskManage();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllTaskManage", e.getMessage(), e);
		}
	}

	@Override
	public EntitySet<TaskManage> findAllTaskUnDealed()
	{
		try{
			return this.taskManageDao.findAllTaskUnDealed();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllTaskManage", e.getMessage(), e);
		}
	}

}
