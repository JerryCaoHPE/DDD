package ddd.simple.service.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.util.MD5Util;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.workflow.CheckOption;
import ddd.simple.service.base.BaseService;

/**
 * 动态FormController
 *
 * @author gx
 */
@Service
public class DynamicFormService extends BaseService
{
	
	@Resource(name = "baseDao")
	private BaseDao baseDao;
	
	@Resource(name = "formService")
	private FormService formService;
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Resource(name = "historyService")
	private HistoryService historyService;
	
	@Resource(name = "runtimeService")
	private RuntimeService runtimeService;
	
	@Resource(name = "managementService")
	private ManagementService managementService;
	
	@Resource(name = "checkOptionServiceBean")
	private CheckOptionService checkOptionService;
	
	/**
	 * 读取Task的表单
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findTaskForm(String taskId)
	{
		try
		{
			Map<String, Object> result = new HashMap<String, Object>();
			Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String processInstanceId = task.getProcessInstanceId();
			Object formRoute = this.runtimeService.getVariable(processInstanceId, "表单路由");
			Object entityId = this.runtimeService.getVariable(processInstanceId, "实体Id");
			Object entityName = this.runtimeService.getVariable(processInstanceId, "对应表名");
			result.put("formId", entityId);
			result.put("formType", entityName);
			result.put("formRoute", formRoute);
			
			TaskFormData taskFormData = this.formService.getTaskFormData(taskId);
			if (taskFormData == null)
			{
				return result;
			} else
			{
				result.put("formKey", taskFormData.getFormKey());
			}
			Map<String, Object> formKeyData = this.baseDao.query(Long.parseLong(entityId.toString()), entityName.toString());
			
			// Map<String, Object> formKeyData =
			// this.modelDataService.findModelDataByContentId(entityName.toString(),
			// Long.parseLong(entityId.toString()));
			result.put("formKeyData", formKeyData);
			
			String executionId = task.getExecutionId();
			List<FormProperty> formProperties = taskFormData.getFormProperties();
			for (int i = 0; i < formProperties.size(); i++)
			{
				FormProperty formProperty = formProperties.get(i);
				this.runtimeService.getVariable(executionId, formProperty.getName());
			}
			
			List<DDDDynamicFormObject> dddDynamicFormObjects = new ArrayList<DynamicFormService.DDDDynamicFormObject>();
			for (FormProperty formProperty : formProperties)
			{
				FormType formType = formProperty.getType();
				if (formType != null)
				{
					DDDDynamicFormObject dddDynamicFormObject = new DDDDynamicFormObject();
					dddDynamicFormObject.name = formProperty.getName();
					dddDynamicFormObject.field = formProperty.getId();
					dddDynamicFormObject.writable = formProperty.isWritable();
					dddDynamicFormObject.readable = formProperty.isReadable();
					dddDynamicFormObject.defauleVaule = formProperty.getValue();
					
					if ("enum".equals(formType.getName()))
					{
						if (formProperty.getName() != null && formProperty.getName().contains("审批结果"))
						{
							dddDynamicFormObject.defauleVaule = "同意";
							dddDynamicFormObject.value = "同意.不同意.驳回";
						}
						
						if (formProperty.getName() != null && formProperty.getName().contains("审批意见"))
						{
							dddDynamicFormObject.defauleVaule = "";
						}
						
						if (formProperty.getName() != null && formProperty.getName().contains("支付处理"))
						{
							dddDynamicFormObject.defauleVaule = "分公司处理";
						}
						if (formProperty.getName() != null && formProperty.getName().contains("密级"))
						{
							dddDynamicFormObject.defauleVaule = "公开";
							dddDynamicFormObject.value = "公开.秘密.机密.绝密";
						}
						/*
						 * Map<String, String> value = (Map<String, String>)
						 * formType.getInformation("values");
						 * dddDynamicFormObject.value = value;
						 * dddDynamicFormObject.value = "同意.不同意.驳回";
						 */
					} else
					{
						String value = formProperty.getValue();
						dddDynamicFormObject.value = value;
					}
					
					dddDynamicFormObject.type = formType.getName();
					
					dddDynamicFormObjects.add(dddDynamicFormObject);
				}
			}
			
			result.put("dddDynamicFormObjects", dddDynamicFormObjects);
			return result;
		} catch (Exception e)
		{
			DDDException dddException = new DDDException("读取任务的表单出错，原因：" + e.getMessage(), e);
			throw dddException;
		}
	}
	
	/**
	 * 读取HistoryTask的表单
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findHistoryTaskInfoForm(String processInstanceId)
	{
		try
		{
			Map<String, Object> result = new HashMap<String, Object>();
			List<HistoricDetail> historicDetails = this.historyService.createHistoricDetailQuery().processInstanceId(processInstanceId)
					.list();
					
			Object formRoute = null, entityId = null, entityName = null;
			
			for (HistoricDetail historicDetail : historicDetails)
			{
				
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				System.out.println("variable: " + variable.getVariableName() + " = " + variable.getValue());
				if (variable.getVariableName().equals("表单路由"))
				{
					formRoute = variable.getValue();
				} else if (variable.getVariableName().equals("实体Id"))
				{
					entityId = variable.getValue();
				} else if (variable.getVariableName().equals("对应表名"))
				{
					entityName = variable.getValue();
				}
			}
			result.put("formId", entityId);
			result.put("formType", entityName);
			result.put("formRoute", formRoute);
			
			Map<String, Object> formKeyData = this.baseDao.query(Long.parseLong(entityId.toString()), entityName.toString());
			result.put("formKeyData", formKeyData);
			
			return result;
		} catch (Exception e)
		{
			DDDException dddException = new DDDException("读取任务的表单出错，原因：" + e.getMessage(), e);
			throw dddException;
		}
	}
	
	/**
	 * 读取Task的表单
	 *//*
		 * @SuppressWarnings("unchecked") public Map<String, Object>
		 * findTaskInfoForm(String processInstanceId){ try { Map<String, Object>
		 * result = new HashMap<String, Object>(); HistoricTaskInstance task =
		 * this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).
		 * singleResult();
		 * 
		 * String processInstanceId = task.getProcessInstanceId(); Object
		 * entityId = this.runtimeService.getVariable(processInstanceId,"实体Id");
		 * Object entityName =
		 * this.runtimeService.getVariable(processInstanceId,"对应表名");
		 * result.put("formId", entityId); result.put("formType", entityName);
		 * 
		 * //StartFormData taskFormData = formService.getStartFormData(
		 * task.getProcessDefinitionId()); //taskFormData.getFormKey(); //
		 * TaskFormData taskFormData = this.formService.getTaskFormData(taskId);
		 * if(taskFormData == null) { return result; } else {
		 * result.put("formKey", taskFormData.getFormKey()); } String tableName
		 * = entityName.toString().toLowerCase(); result.put("formKey",
		 * tableName+"Template"); Map<String, Object> formKeyData =
		 * this.modelDataService.findModelDataByContentId(entityName.toString(),
		 * Long.parseLong(entityId.toString())); result.put("formKeyData",
		 * formKeyData);
		 * 
		 * return result; } catch(Exception e) { DDDException dddException = new
		 * DDDException("读取任务的表单出错，原因："+e.getMessage(),e); throw dddException; }
		 * }
		 */
		
	/**
	 * 提交task的并保存form 提交task并保存审核结果和修改的审核内容
	 */
	public boolean completeTask(String taskId, HashMap parameterMap, HashMap taskDetailInfo)
	{
		Map<String, String> formProperties = new HashMap<String, String>();
		
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 从request中读取参数然后转换
		Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
		
		for (Entry<String, String> entry : entrySet)
		{
			String key = entry.getKey();
			
			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_"))
			{
				String name = key.split("_")[1];
				if (name.contains("审批结果"))
				{
					formProperties.put(task.getName() + "结果", entry.getValue());
					formProperties.put("当前审批人审批结果", entry.getValue());
				}
				if (name.contains("审批意见"))
				{
					formProperties.put(task.getName() + "意见", entry.getValue());
				}
				if (name.contains("密级"))
				{
					formProperties.put("密级", entry.getValue());
				}
			}
		}
		// 审核结果默认值
		String checkedResultName = task.getName() + "结果";
		if (formProperties.get(checkedResultName) == null)
		{
			formProperties.put(checkedResultName, "同意");
		}
		
		String processInstanceId = task.getProcessInstanceId();
		/*
		 * if(taskDetailInfo.get("formKeyData")!=null){ String isAgree =
		 * formProperties.get(checkedResultName); boolean isChanged =
		 * this.checkIsChanged((Map<String,Object>)taskDetailInfo.get(
		 * "formKeyData"), taskDetailInfo.get("formType").toString());
		 * //更新modelData if(
		 * taskDetailInfo.get("formKeyData")!=null&&!isChanged&&!isAgree.equals(
		 * "不同意")) { this.updateModelData(taskDetailInfo); } }
		 */
		
		// 将动态表单数据保存到流程变量里
		saveToProcessVariable(processInstanceId, formProperties);
		// 保存审核结果
		saveCheckOption(processInstanceId, task);
		
		this.taskService.complete(taskId);
		return true;
	}
	
	private String isDDDComponentsDefaultString(Object value)
	{
		if (value == null || value.toString().length() == 0)
		{
			return "";
		}
		String valueString = value.toString();
		
		return valueString;
		
	}
	
	/**
	 * 批量提交task并保存form
	 */
	public boolean completeTasks(List<String> taskIds)
	{
		for (String taskId : taskIds)
		{
			HashMap parameterMap = new HashMap();
			Map<String, Object> taskForm = this.findTaskForm(taskId);
			List<DDDDynamicFormObject> dddDynamicFormObjects = (List<DDDDynamicFormObject>) taskForm.get("dddDynamicFormObjects");
			
			if (dddDynamicFormObjects != null)
			{
				for (int i = 0; i < dddDynamicFormObjects.size(); i++)
				{
					DDDDynamicFormObject dddDynamicFormObject = dddDynamicFormObjects.get(i);
					Object defaultVaule = dddDynamicFormObject.defauleVaule;
					// 如果动态表单不可读，则不显示
					if (dddDynamicFormObject.readable == false)
					{
						continue;
					}
					
					if (dddDynamicFormObject.name.contains("审核结果"))
					{
						parameterMap.put("fp_" + dddDynamicFormObject.name, "同意");
					} else if (dddDynamicFormObject.name.contains("审核意见"))
					{
						parameterMap.put("fp_" + dddDynamicFormObject.name, "同意");
					} else if (dddDynamicFormObject.name.contains("补签领导"))
					{
						continue;
					} else
					{
						String defaultVauleString = this.isDDDComponentsDefaultString(defaultVaule);
						parameterMap.put("fp_" + dddDynamicFormObject.name, defaultVauleString);
					}
				}
			}
			// this.completeTask(taskId, parameterMap);
		}
		return true;
	}
	
	/**
	 * 保存本次审核人意见和审核结果
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 * @param task
	 *            任务
	 */
	private void saveCheckOption(String processInstanceId, Task task)
	{
		String userName = this.getUserName();
		String taskName = task.getName();
		
		Object checkOptionComment = this.runtimeService.getVariable(processInstanceId, taskName + "意见");
		Object checkOptionResult = this.runtimeService.getVariable(processInstanceId, taskName + "结果");
		
		if (checkOptionComment == null)
		{
			checkOptionComment = "同意";
		}
		
		CheckOption checkOption = new CheckOption();
		checkOption.setProcessInstanceId(processInstanceId);
		checkOption.setTaskName(taskName);
		checkOption.setCheckTime(new Date());
		checkOption.setCheckResult(String.valueOf(checkOptionResult));
		checkOption.setManagerOption(String.valueOf(checkOptionComment));
		checkOption.setCheckPeople(userName);
		checkOption.setAssigneePeople(task.getAssignee());
		checkOption.setDescription(task.getDescription());
		
		Object orgName = this.runtimeService.getVariable(processInstanceId, "organizationName");
		checkOption.setOrganizationName(orgName != null ? orgName.toString() : null);
		
		Long entityId = (Long) this.runtimeService.getVariable(processInstanceId, "实体Id");
		String classPath = (String) this.runtimeService.getVariable(processInstanceId, "对应表名");
		String formRoute = (String) this.runtimeService.getVariable(processInstanceId, "表单路由");
		
		checkOption.setFormId(entityId);
		checkOption.setFormType(classPath);
		checkOption.setFormRoute(formRoute);
		
		this.checkOptionService.saveCheckOption(checkOption);
	}
	
	/**
	 * 将动态表单的数据保存到流程变量里
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 * @param formDatas
	 *            表单数据
	 */
	private void saveToProcessVariable(String processInstanceId, Map<String, String> formDatas)
	{
		Set<String> keySet = formDatas.keySet();
		
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();)
		{
			
			String processVariableName = iterator.next();
			String processVariableValue = formDatas.get(processVariableName);
			this.runtimeService.setVariable(processInstanceId, processVariableName, processVariableValue);
		}
	}
	
	private String getUserName(){
		Operator operator = this.getLoginUser().getLoginOperator();
		Member member = this.getLoginUser().getLoginVip();
		String userName = member == null ? operator.getCode() : member.getName();
		return userName;
	}
	/**
	 * 未办理任务列表
	 * 
	 * @param model
	 * @return
	 */
	public Map<String, Object> findNonHandleTaskList(int startPage, int pageSize)
	{
		
		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		String userName = this.getUserName();
		
		List<Task> allTasks = new ArrayList<Task>();
		// 分配到当前登陆用户的任务
		List<Task> assigneeTasks = taskService.createTaskQuery().taskAssignee(userName).listPage(startPage, pageSize);
		
		// 未签收的任务
		List<Task> candidateTasks = taskService.createTaskQuery().taskCandidateUser(userName).listPage(startPage, pageSize);
		List<Task> candidateGroupTasks = taskService.createTaskQuery().taskCandidateGroup(userName).listPage(startPage, pageSize);
		allTasks.addAll(assigneeTasks);
		allTasks.addAll(candidateTasks);
		allTasks.addAll(candidateGroupTasks);
		
		long totalCount = allTasks.size();
		
		for (int i = 0; i < allTasks.size(); i++)
		{
			Task task = allTasks.get(i);
			
			String processInstanceId = task.getProcessInstanceId();
			
			String emergencyStatus = (String) runtimeService.getVariable(processInstanceId, "紧急状态");
			
			if ("普通".equals(emergencyStatus))
			{
				task.setPriority(0);
			} else if ("紧急".equals(emergencyStatus))
			{
				task.setPriority(1);
			}
			
			tasks.add(this.formTaskToMap(task));
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalItems", totalCount);
		result.put("result", tasks);
		return result;
	}
	
	/**
	 * 未办理任务列表
	 * 
	 * @param model
	 * @return
	 */
	// public List<Map<String, String>> findNonHandleTaskList() {
	//
	// Operator operator = this.getLoginUser().getLoginOperator();
	// String userName = operator.getName();
	//
	// List<Map<String, String>> nonHandleTasks = new ArrayList<Map<String,
	// String>>();
	//
	// // 分配到当前登陆用户的未办理任务列表
	// List<Task> tasks =
	// taskService.createTaskQuery().taskAssignee(userName).list();
	// for(int i=0;i<tasks.size();i++)
	// {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("taskId", tasks.get(i).getId());
	// map.put("name", tasks.get(i).getName());
	// map.put("description", tasks.get(i).getDescription());
	// nonHandleTasks.add(map);
	//
	// }
	// return nonHandleTasks;
	// }
	/**
	 * 未签收的任务列表
	 * 
	 * @return 未签收任务列表
	 */
	public Map<String,Object> findNonClaimTaskList(int startPage, int pageSize)
	{
		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		String userName = this.getUserName();
		
		List<Task> allNonClaimTasks = new ArrayList<Task>();
		// 未签收的任务
		List<Task> nonClaimTasks = this.taskService.createTaskQuery().taskCandidateUser(userName).listPage(startPage, pageSize);
		List<Task> nonClaimGroupTasks = this.taskService.createTaskQuery().taskCandidateGroup(userName).listPage(startPage, pageSize);
		allNonClaimTasks.addAll(nonClaimTasks);
		allNonClaimTasks.addAll(nonClaimGroupTasks);
		
		int taskCount = allNonClaimTasks.size();
		for (int i = 0; i < taskCount; i++)
		{
			Task task = allNonClaimTasks.get(i);
			if (null != task.getAssignee())
			{
				allNonClaimTasks.remove(i);
				taskCount--;
				i--;
			}
			else
			{
				tasks.add(this.formTaskToMap(task));
			}
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("totalItems", taskCount);
		result.put("result", tasks);
		return result;
	}
	
	/**
	 * 已完成的任务列表
	 * 
	 * @return 已完成任务列表
	 */
	public Map<String, Object> findCompletedTaskList(int startPage, int pageSize)
	{
		String userName = this.getUserName();
		
		List<Map<String, Object>> historicTaskInstances = new ArrayList<Map<String, Object>>();
		
		List<HistoricTaskInstance> tasks = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userName)
				.listPage(startPage, pageSize);
		long totalTasks = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userName).processFinished().count();
		
		List<HistoricTaskInstance> completedTasks = new ArrayList<HistoricTaskInstance>();
		for (HistoricTaskInstance task : tasks)
		{
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if (pi == null)
			{
				completedTasks.add(task);
			}
		}
		
		for (int i = 0; i < completedTasks.size(); i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("taskId", completedTasks.get(i).getId());
			map.put("name", completedTasks.get(i).getName());
			map.put("description", completedTasks.get(i).getDescription());
			map.put("processInstanceId", completedTasks.get(i).getProcessInstanceId());
			historicTaskInstances.add(map);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalItems", totalTasks);
		result.put("result", historicTaskInstances);
		return result;
	}
	
	/**
	 * 已办任务
	 * 
	 * @return
	 */
	public Map<String, Object> findOperatedTaskList(int startPage, int pageSize)
	{
		String userName = this.getUserName();
		
		List<Map<String, Object>> historicTaskInstances = new ArrayList<Map<String, Object>>();
		List<HistoricTaskInstance> tasks = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userName).finished().listPage(startPage, pageSize);
		long totalTasks = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userName).count();
		
		String procInstId = "";
		for (int i = 0; i < tasks.size(); i++)
		{
			procInstId = tasks.get(i).getProcessInstanceId();
			Task task = this.taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
			
			Map<String, Object> map = new HashMap<String, Object>();
			if (task != null)
			{
				map.put("taskId", task.getId());
			} else
			{
				map.put("taskId", tasks.get(i).getId());
			}
			map.put("name", tasks.get(i).getName());
			map.put("description", tasks.get(i).getDescription());
			map.put("processInstanceId", tasks.get(i).getProcessInstanceId());
			historicTaskInstances.add(map);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalItems", totalTasks);
		result.put("result", historicTaskInstances);
		return result;
	}
	
	/**
	 * 我的任务
	 * 
	 * @param model
	 * @return
	 */
	public List<Map<String, String>> findOwnerTaskList()
	{
		String userName = this.getUserName();
		List<Map<String, String>> ownerTasks = new ArrayList<Map<String, String>>();
		// 当前登陆用户的任务
		List<Task> tasks = taskService.createTaskQuery().taskOwner(userName).list();
		for (int i = 0; i < tasks.size(); i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskId", tasks.get(i).getId());
			map.put("name", tasks.get(i).getName());
			map.put("description", tasks.get(i).getDescription());
			ownerTasks.add(map);
			
		}
		return ownerTasks;
		
	}
	
	/**
	 * 未签收任务
	 * 
	 * @param model
	 * @return
	 */
	public List<Map<String, String>> findCandidateTaskList()
	{
		String userName = this.getUserName();
		List<Map<String, String>> candidateTasks = new ArrayList<Map<String, String>>();
		// taskService.createTaskQuery().taskOwner(arg0) 我的任务
		// 当前登陆用户未签收的任务
		List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(userName).list();
		for (int i = 0; i < tasks.size(); i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskId", tasks.get(i).getId());
			map.put("name", tasks.get(i).getName());
			map.put("description", tasks.get(i).getDescription());
			candidateTasks.add(map);
			
		}
		return candidateTasks;
		
	}
	
	// 获取任务的相关信息
	public Map<String, Object> findTaskDetailById(String taskId)
	{
		
		// List<Task> tasks = taskService.createNativeTaskQuery().sql("SELECT *
		// FROM " + managementService.getTableName(Task.class) + " T WHERE T.ID_
		// = #{taskId}").parameter("taskId", taskId).list();
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		
		if (task == null)
		{
			return null;
		}
		Map<String, Object> map = this.formTaskToMap(task);
		
		Map<String, Object> taskMap = findTaskForm(taskId);
		map.putAll(taskMap);
		// events = this.taskService.getTaskEvents(taskId);
		return map;
		
	}
	
	/**
	 * 已完成任务详情
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Map<String, Object> findCompletedTaskDetailById(String taskId)
	{
		HistoricTaskInstance task = this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (task == null)
		{
			return null;
		}
		
		String processInstanceId = task.getProcessInstanceId();
		String formKey = task.getFormKey();
		
		Map<String, Object> map = this.formTaskInfoToMap(task);
		Map<String, Object> taskMap = this.findHistoryTaskInfoForm(processInstanceId);
		taskMap.put("formKey", formKey);
		map.putAll(taskMap);
		
		return map;
	}
	
	/**
	 * 已办任务详情
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> findOperatedTaskDetailById(String taskId)
	{
		HistoricTaskInstance task = this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Map<String, Object> taskMap = new HashMap<String, Object>();
		
		if (task == null)
		{
			return null;
		}
		if (pi == null)
		{
			taskMap = this.findHistoryTaskInfoForm(processInstanceId);
		} else
		{
			taskMap = this.findTaskForm(taskId);
		}
		
		String formKey = task.getFormKey();
		Map<String, Object> map = this.formTaskInfoToMap(task);
		taskMap.put("formKey", formKey);
		map.putAll(taskMap);
		return map;
	}
	
	/**
	 * 已处理但是未结束的任务列表(未用到)
	 * 
	 * @return
	 */
	public Set<Map<String, Object>> findCompletedAndNoFinishedTaskList(String checkPeople)
	{
		Set<Map<String, Object>> checkOptions = this.checkOptionService.findCheckOptionByCheckPeople(checkPeople);
		return checkOptions;
	}
	
	/**
	 * 签收任务
	 * 
	 * @param taskId
	 *            任务Id
	 */
	public void claim(String taskId)
	{
		String userName = this.getUserName();
		
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		
		Long formId = (Long) this.runtimeService.getVariable(task.getExecutionId(), "实体Id");
		String formType = (String) this.runtimeService.getVariable(task.getExecutionId(), "对应表名");
		
		Integer count = managementService.getTableCount().get("ACT_RU_IDENTITYLINK").intValue();
		List<Map<String, Object>> identityLinks = managementService.createTablePageQuery().tableName("ACT_RU_IDENTITYLINK")
				.listPage(0, count).getRows();
		String assigneePeople = "";
		for (int i = 0; i < identityLinks.size(); i++)
		{
			Map<String, Object> identityLink = identityLinks.get(i);
			String task_id = (String) identityLink.get("TASK_ID_");
			String type = (String) identityLink.get("TYPE_");
			
			if (task_id != null && type != null && task_id.equals(task.getId()) && type.equals("candidate"))
			{
				assigneePeople = assigneePeople + "," + (String) identityLink.get("USER_ID_");
			}
		}
		
		if (assigneePeople.length() > 0)
		{
			assigneePeople = assigneePeople.substring(1);
		}
		
		String processInstanceId = task.getProcessInstanceId();
		CheckOption checkOption = new CheckOption();
		checkOption.setProcessInstanceId(processInstanceId);
		checkOption.setTaskName(task.getName() + "-签收");
		checkOption.setCheckTime(new Date());
		checkOption.setCheckResult("签收成功");
		checkOption.setManagerOption("签收任务");
		checkOption.setCheckPeople(userName);
		checkOption.setAssigneePeople(assigneePeople);
		checkOption.setFormId(formId);
		checkOption.setFormType(formType);
		checkOption.setDescription(task.getDescription());
		
		Object orgName = this.runtimeService.getVariable(processInstanceId, "organizationName");
		checkOption.setOrganizationName(orgName != null ? orgName.toString() : null);
		
		this.checkOptionService.saveCheckOption(checkOption);
		
		this.taskService.claim(taskId, userName);
	}
	
	/**
	 * 签收在队列中的任务
	 * 
	 * @param taskId
	 *            任务Id
	 */
	public void claimAtQueued(String taskId)
	{
		
		String userName = this.getUserName();
		
		this.taskService.claim(taskId, userName);
	}
	
	/**
	 * 运行中的流程实例
	 * 
	 * @param model
	 * @return
	 */
	public List<ProcessInstance> findRunningProcess()
	{
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().list();
		return processInstances;
	}
	
	/**
	 * 根据给定的参数查找运行中的流程实例
	 * 
	 * @param checkPeople
	 * @param startDate
	 * @param endDate
	 * @param organizationName
	 * @param managementItemName
	 * @return
	 */
	public List<ProcessInstance> findRunningProcessByParameters(String checkPeople, Date startDate, Date endDate, String organizationName,
			String managementItemName)
	{
		Set<Map<String, Object>> checkOptions = this.checkOptionService.findCheckOptionByParameters(checkPeople, startDate, endDate,
				organizationName, managementItemName);
		Set<String> sets = new HashSet<String>();
		for (Map<String, Object> checkOption : checkOptions)
		{
			sets.add(checkOption.get("processInstanceId").toString());
		}
		List<ProcessInstance> processInstances;
		if (sets.size() != 0)
		{
			processInstances = this.runtimeService.createProcessInstanceQuery().processInstanceIds(sets).list();
		} else
		{
			processInstances = null;
		}
		
		return processInstances;
	}
	
	/**
	 * 已结束的流程实例
	 * 
	 * @param model
	 * @return
	 */
	public List<HistoricProcessInstance> findFinishedProcess()
	{
		List<HistoricProcessInstance> historicProcessInstances = this.historyService.createHistoricProcessInstanceQuery().finished().list();
		return historicProcessInstances;
	}
	
	/**
	 * 挂起正在运行的流程
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 */
	public void suspendProcess(String processInstanceId)
	{
		this.runtimeService.suspendProcessInstanceById(processInstanceId);
	}
	
	/**
	 * 唤醒被挂起的流程
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 */
	public void activateProcess(String processInstanceId)
	{
		this.runtimeService.activateProcessInstanceById(processInstanceId);
	}
	
	/**
	 * 删除流程实例
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 * @param reason
	 *            删除原因
	 */
	public void deleteProcess(String processInstanceId, String reason)
	{
		this.runtimeService.deleteProcessInstance(processInstanceId, reason);
	}
	
	/**
	 * 根据流程执行Id获取流程变量
	 * 
	 * @param executionId
	 * @return
	 */
	public Map<String, Object> getVariablesByExecutionId(String executionId)
	{
		return this.runtimeService.getVariables(executionId);
	}
	
	/**
	 * 将任务对象转换为map
	 * 
	 * @param task
	 * @return
	 */
	private Map<String, Object> formTaskToMap(Task task)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("taskId", task.getId());
		map.put("assignee", task.getAssignee());
		map.put("description", task.getDescription());
		map.put("name", task.getName());
		map.put("owner", task.getOwner());
		map.put("processDefinitionId", task.getProcessDefinitionId());
		map.put("taskDefinitionKey", task.getTaskDefinitionKey());
		map.put("createTime", task.getCreateTime());
		map.put("dueDate", task.getDueDate());// 到期时间
		map.put("category", task.getCategory());
		map.put("processInstanceId", task.getProcessInstanceId());
		
		return map;
	}
	
	/**
	 * 更新modelData
	 * 
	 * @param taskDetailInfo
	 * @return
	 */
	public String updateModelData(HashMap taskDetailInfo)
	{
		Map<String, Object> modelData = (Map<String, Object>) taskDetailInfo.get("formKeyData");
		modelData.remove("EId");
		String tableName = (String) taskDetailInfo.get("formType");
		
		String andEid = "and EId = " + modelData.get("EId");
		String whereEid = "where EId = " + modelData.get("EId");
		if (!this.checkIsChanged(modelData, tableName))
		{
			try
			{
				this.baseDao.update(tableName, modelData, andEid);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return "success";
	}
	
	/**
	 * 检测modelData是否修改
	 * 
	 * @param formKeyData
	 * @param tableName
	 * @return
	 */
	public boolean checkIsChanged(Map<String, Object> formKeyData, String tableName)
	{
		
		MD5Util md5Util = new MD5Util();
		
		Set<String> keys = formKeyData.keySet();
		String[] properties = new String[keys.size()];
		
		String selectSql = "select * from " + tableName + " " + "where Eid =" + Long.valueOf(formKeyData.get("EId").toString());
		
		Set<Map<String, Object>> content = null;
		try
		{
			content = this.baseDao.query(selectSql, keys.toArray(properties));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Iterator<Map<String, Object>> it = content.iterator();
		Map<String, Object> contentMap = null;
		while (it.hasNext())
		{
			contentMap = it.next();
		}
		for (String key : keys)
		{
			String newValue = formKeyData.get(key).toString();
			String oldValue = contentMap.get(key).toString();
			if (!md5Util.getMD5(newValue).equals(md5Util.getMD5(oldValue)))
			{
				return false;
			}
		}
		return true;
		
	}
	
	private Map<String, Object> formTaskInfoToMap(HistoricTaskInstance task)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("taskId", task.getId());
		map.put("assignee", task.getAssignee());
		map.put("description", task.getDescription());
		map.put("name", task.getName());
		map.put("owner", task.getOwner());
		map.put("processDefinitionId", task.getProcessDefinitionId());
		map.put("taskDefinitionKey", task.getTaskDefinitionKey());
		map.put("createTime", task.getCreateTime());
		map.put("dueDate", task.getDueDate());// 到期时间
		map.put("category", task.getCategory());
		map.put("processInstanceId", task.getProcessInstanceId());
		
		return map;
	}
	
	public Set<Map<String, Object>> getWorkflowProcess()
	{
		try
		{
			String where = "select NAME_,ID_,CREATE_TIME_ from act_re_model";
			return this.baseDao.query(where);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getWorkflowProcess", e.getMessage(), e);
		}
	}
	
	public class DDDDynamicFormObject
	{
		// 动态表单属性名称
		public String name;
		
		public String	field;
		// 动态表单属性值
		public Object	value;
		
		// 动态表单属性值类型（string,enum,date....）
		public String type;
		
		// 是否可读
		public boolean writable;
		
		// 是否可写
		public boolean readable;
		
		// 默认值
		public Object defauleVaule;
	}
	
	/**
	 * 分页查询未办理任务列表
	 * 
	 * @param model
	 * @return
	 */
	public List<Map<String, Object>> findNonHandleTaskListByPaging(int beginSize, int endSize)
	{
		String userName = this.getUserName();
		
		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		List<Task> nonHandleTasks = new ArrayList<Task>();
		
		// 分配到当前登陆用户的任务
		List<Task> assigneeTasks = taskService.createTaskQuery().taskAssignee(userName).list();
		
		// 未签收的任务
		List<Task> candidateTasks = taskService.createTaskQuery().taskCandidateUser(userName).list();
		
		nonHandleTasks.addAll(assigneeTasks);
		nonHandleTasks.addAll(candidateTasks);
		
		for (int i = beginSize; i < endSize && i < nonHandleTasks.size(); i++)
		{
			Task task = nonHandleTasks.get(i);
			
			String processInstanceId = task.getProcessInstanceId();
			
			String emergencyStatus = (String) runtimeService.getVariable(processInstanceId, "紧急状态");
			
			if ("普通".equals(emergencyStatus))
			{
				task.setPriority(0);
			} else if ("紧急".equals(emergencyStatus))
			{
				task.setPriority(1);
			}
			
			tasks.add(this.formTaskToMap(task));
		}
		
		return tasks;
	}
}
