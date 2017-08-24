package ddd.simple.service.workflow;

import java.util.Map;

import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.base.BaseServiceInterface;

public interface WorkflowExternalService extends BaseServiceInterface{
	
	public String findNonHandleTasks(Map<String, String> param);/*查询待办任务*/
	public String claim(Map<String, String> param);/*签收任务*/
//	public String handle(Map<String, String> param);/*办理任务*/
	public String handleTask(Map<String, String> param);
	public String findCompletedTasks(Map<String, String> param);/*查询已完成任务*/
	public String findCheckOptionByProcessInstanceId(Map<String, String> param);/*查看申请单*/

}
