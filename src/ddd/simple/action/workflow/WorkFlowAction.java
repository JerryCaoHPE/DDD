package ddd.simple.action.workflow;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.simple.entity.workflow.CheckOption;
import ddd.simple.service.workflow.CheckOptionService;
import ddd.simple.service.workflow.DynamicFormService;
import ddd.simple.service.workflow.WorkFlowEngineService;
import ddd.simple.service.workflow.WorkflowTraceService;


@Action
@RequestMapping("/Workflow")
@Controller
public class WorkFlowAction {
	
	@Resource(name = "dynamicFormService")
	private DynamicFormService dynamicFormService;
	
	@Resource(name = "workFlowEngineService")
	private WorkFlowEngineService workFlowEngineService;
	
	@Resource(name = "workflowTraceService")
	private WorkflowTraceService workflowTraceService;
	
	@Resource(name = "checkOptionServiceBean")
	private CheckOptionService checkOptionService;
	
	/**
	 * 未办理任务列表
	 * @param model
	 * @return
	 */
	public Map<String, Object> findNonHandleTaskList(int startPage,int pageSize) {
		return dynamicFormService.findNonHandleTaskList(startPage,pageSize);
	}
	/**
	 * 我的任务
	 * @param model
	 * @return
	 */
	public List<Map<String, String>> findOwnerTaskList() {
		return dynamicFormService.findOwnerTaskList();
	}
	
	/**
	 * 未签收任务
	 * @param model
	 * @return
	 */
	public List<Map<String, String>> findCandidateTaskList() {
		return  dynamicFormService.findCandidateTaskList();
	}
	
	/**
	 * 已完成的任务列表
	 * @return 已完成任务列表
	 */
	public Map<String, Object> findCompletedTaskList(int startPage,int pageSize){
		return dynamicFormService.findCompletedTaskList(startPage,pageSize);
	}
	
	/**
	 * 已办任务
	 * @return
	 */
	public Map<String,Object> findOperatedTaskList(int startPage,int pageSize){
		return dynamicFormService.findOperatedTaskList(startPage,pageSize);
	}
	
	/*
	 * 签收在队列中的任务
	 */
	public void claimAtQueued(String taskId){
		dynamicFormService.claimAtQueued(taskId);;
	}
	
	/*
	 * 
	 * 获取任务的相关信息
	 */
	public Map<String, Object> findTaskDetailById(String taskId){
		return dynamicFormService.findTaskDetailById(taskId);
	}
	
	/**
	 * 已完成任务详情
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> findCompletedTaskDetailById(String taskId)
	{
		return dynamicFormService.findCompletedTaskDetailById(taskId);
	}
	
	/**
	 * 已办任务详情
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> findOperatedTaskDetailById(String taskId)
	{
		return dynamicFormService.findOperatedTaskDetailById(taskId);
	}
	
   /**
	 * 提交task并保存审批结果和修改的审批内容
	 */
	public boolean completeTask(String taskId, HashMap parameterMap,HashMap taskDetailInfo ) {
		return dynamicFormService.completeTask(taskId, parameterMap, taskDetailInfo);
	}

	/**
	 * 读取Task的表单
	 */
	public Map<String, Object> findTaskForm(String taskId){
		return dynamicFormService.findTaskForm(taskId);
	}
	
	public List<Map<String, Object>> traceProcess(String processInstanceId)
	{
		try
		{
			List<Map<String, Object>> processDatas = this.workflowTraceService.traceProcess(processInstanceId);
			return processDatas;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getProcessImage(String processInstanceId)
	{
		try
		{
			byte[] images = this.workFlowEngineService.loadByProcessInstance("image",processInstanceId);
			return images;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Set<Map<String, Object>> findCheckOptionsByForm(Long formId,String formType)
	{
		try
		{
			return this.checkOptionService.findCheckOptionByFormIdAndFormType(formId, formType);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取流程名
	 * @return
	 */
	public Set<Map<String,Object>> getWorkflowProcess(){
		try{
			return this.dynamicFormService.getWorkflowProcess();
		}catch(DDDException e){
			e.printStackTrace();
		}
		return null; 
	}

}
