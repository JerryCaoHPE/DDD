package  ddd.simple.service.workflow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thoughtworks.xstream.converters.basic.DateConverter;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.DateUtil;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.organization.Employee;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.workflow.CheckOption;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.listview.DataSourceService;
import ddd.simple.service.systemConfig.SystemConfigService;

@Service
public class WorkFlowEngineService extends BaseService{
	@Resource(name = "baseDao")
	private BaseDao baseDao;
	
	@Resource(name = "repositoryService")
	private RepositoryService repositoryService;
	
	@Resource(name = "runtimeService")
	private RuntimeService runtimeService;
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Resource(name = "historyService")
	private HistoryService historyService;
	
	@Resource(name = "formService")
	private FormService formService;
	
	@Resource(name = "identityService")
	private IdentityService identityService;
	
	@Resource(name = "dataSourceServiceBean")
	private DataSourceService dataSourceService;
	
	@Resource(name = "workflowTraceService")
	private WorkflowTraceService workflowTraceService;	

	@Resource(name = "systemConfigServiceBean")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "checkOptionServiceBean")
	private CheckOptionService checkOptionService;
	
	//单据是否有效属性名
	public static final String ATTRIBUTENAME_ISEFFECT = "isEffect";
	//流程紧急状态属性名
	public static final String ATTRIBUTENAME_EMERGENCYSTATUS = "emergencyStatus";
	//审批结果属性名
	public static final String ATTRIBUTENAME_CHECKRESULT = "checkResult";

	//单据是否有效默认值
	public static final String VALUE_DEFAULT_ISEFFECT = "无效";
	public static final String VALUE_ISEFFECT = "有效";
	
	//流程紧急状态默认值
	public static final String VALUE_DEFAULT_EMERGENCYSTATUS = "普通";
	
	//单据审批结果默认值
	public static final String VALUE_DEFAULT_CHECKRESULT = "审核中";
	public static final String VALUE_CHECKRESULT = "通过";
	
	//审核状态
	public static final String ATTRIBUTENAME_AUDITSTATE = "auditState";
	public static final String VALUE_DEFAULT_AUDITSTATE = "已审核";
	
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}



	public WorkflowTraceService getWorkflowTraceService() {
		return workflowTraceService;
	}

	public void setWorkflowTraceService(WorkflowTraceService workflowTraceService) {
		this.workflowTraceService = workflowTraceService;
	}
	
	public String startProcess(String processConfigName,long formId,Class<?> entityClass,String checkResultProcessVariableName,Map<String,Object> params)
	{
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		
		String tableName = SessionFactory.getEntityClass(entityClass).getEntityInfo().getName();
		return this.startProcess(processConfigName, formId, tableName,checkResultProcessVariableName,"",params);
	}
	
	public String startProcess(String processConfigName,long formId,String tableName,String checkResultProcessVariableName,Map<String,Object> params)
	{
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		return this.startProcess(processConfigName, formId, tableName,checkResultProcessVariableName,"",params);
	}
	
	/**
	 * 启动流程
	 * @param processConfigName 系统变量名
	 * @param entityId 实体Id
	 * @param formType 实体类名
	 * @param checkResultProcessVariableName 表示审批结果的流程变量名称
	 * @param description 流程描述
	 * @return
	 */
	public String startProcess(String processConfigName,long formId,String tableName,String checkResultProcessVariableName,String description,Map<String,Object> params)
	{
		String processConfig = this.systemConfigService.findSystemConfigValueBykey(processConfigName);
		if("".equals(processConfig))
		{
			throw new DDDException("没有定义流程相关的系统参数" + processConfigName);
		}
		
		//process;;employeeCode;main.list.workflow.treeWorkFlow;checkResult
		String[] processConfigs = processConfig.split(";");
		if(processConfigs.length != 5 )
		{
			throw new DDDException("系统参数" + processConfig+ "的格式不正确");
		}

		String processDefinitionKey = processConfigs[0];
		String processDefinitionVersion= processConfigs[1];
		String dataSourceCode = processConfigs[2];
		String formRoute = processConfigs[3];
		if(checkResultProcessVariableName == null || checkResultProcessVariableName.length() == 0)
		{
			checkResultProcessVariableName = processConfigs[4];
		}
		
		return this.startProcess(processDefinitionKey,processDefinitionVersion,dataSourceCode,formId,tableName,formRoute,checkResultProcessVariableName,description,params);
	}
	
	/**
	 * 启动流程
	 * @param processKey 流程定义的Key
	 * @param listViewId 视图的Id,结合entityId可以查询实体的信息
	 * @param formAddresss 业务表单地址
	 * @param entityId 实体Id
	 * @param businessKey 业务key
	 * @param operator 流程启动人
	 * @param description 流程启动信息
	 * @return 流程是否启动成功消息
	 */
	private String startProcess(String processDefinitionKey,String processDefinitionVersion,String dataSourceCode
			,long entityId,String tableName,String formRoute,String checkResultProcessVariableName,String description,Map<String,Object> params)
	{
		String returnData = null;
		try
		{
			Map<String, Object> dataSourceData = new HashMap<String, Object>();
			
			if(dataSourceCode != null && !"".equals(dataSourceCode))
			{
				if(params.get("entityId") == null)
				{
					params.put("entityId", entityId);
				}
				DataSource dataSource = this.dataSourceService.findDataSourceByCode(dataSourceCode);
				Set<Map<String, Object>> dataSourceDatas = this.dataSourceService.getDataSourceResult(dataSource, params);
				if(dataSourceDatas == null || dataSourceDatas.size() == 0)
				{
					return null;
				}
				dataSourceData.putAll((Map<String, Object>)dataSourceDatas.toArray()[0]);
			}
			
			//紧急状态设置默认值
			if(dataSourceData.get(ATTRIBUTENAME_EMERGENCYSTATUS) == null)
			{
				dataSourceData.put(ATTRIBUTENAME_EMERGENCYSTATUS,VALUE_DEFAULT_EMERGENCYSTATUS);
			}
			
			//审批结果设置默认值
			if(dataSourceData.get(ATTRIBUTENAME_CHECKRESULT) == null)
			{
				dataSourceData.put(ATTRIBUTENAME_CHECKRESULT,VALUE_DEFAULT_CHECKRESULT);
			}
			
			
			//单据是否有效设置默认值
			if(dataSourceData.get(ATTRIBUTENAME_ISEFFECT) == null)
			{
				dataSourceData.put(ATTRIBUTENAME_ISEFFECT,VALUE_DEFAULT_ISEFFECT);
			}
			
			Member member = this.getLoginUser().getLoginVip();
			Operator operator = this.getLoginUser().getLoginOperator();
			String code = member==null?operator.getCode():member.getName();
			String name = member==null?operator.getName():member.getName();
			
			identityService.setAuthenticatedUserId(code);
			
			//流程启动时的流程变量
			Map<String,Object> initParocessVariables = new HashMap<String,Object>();
			initParocessVariables.put("实体Id",entityId);
			initParocessVariables.put("对应表名",tableName);
			initParocessVariables.put("流程启动人",name);
			initParocessVariables.put("申请人",name);
			Date processStartTime = new Date();
			String dateString = DateUtil.formatDateTime(processStartTime);
			initParocessVariables.put("流程启动时间", dateString);
			initParocessVariables.put("流程定义Key", processDefinitionKey);
			initParocessVariables.put("数据源编码",dataSourceCode);
			initParocessVariables.put("流程描述",description);
			initParocessVariables.put("表单路由",formRoute);
			initParocessVariables.put("表示审批结果的流程变量名称",checkResultProcessVariableName);
			initParocessVariables.putAll(dataSourceData);
			if(params != null)
			{
				initParocessVariables.putAll(params);
			}

			ProcessInstance processInstance = null;
			if(processDefinitionVersion != null && !"".equals(processDefinitionVersion))
			{
				//根据流程key和版本号查询流程定义
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey)
				.processDefinitionVersion(Integer.parseInt(processDefinitionVersion)).singleResult();

				String processDefinitionId = processDefinition.getId();

				//启动流程
				processInstance = runtimeService.startProcessInstanceById(processDefinitionId,initParocessVariables);
			}
			else
			{
				processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,initParocessVariables);
			}
			Map<String,String> processMap = new HashMap<String, String>();
			processMap.put("processInstanceId",processInstance.getProcessInstanceId());
			//将流程id插入对象model中
		    this.insertProcessInstanceIdToModel(tableName, processMap, entityId);
			//保存流程启动信息
			this.saveStartProcessInfo(entityId,tableName,name,processStartTime,processInstance.getProcessInstanceId(),processDefinitionKey);
			
			returnData = "success:" + processInstance.getProcessInstanceId();
		}
		catch (Exception e) 
		{
			returnData = "faild:" + e.getMessage();
			e.printStackTrace();
			if(e.getCause() != null)
			{
				throw new DDDException(e.getCause().getMessage());
			}
			else 
			{
				throw new DDDException(e.getMessage());
			}
		}
		
		return returnData;
	}
	
	/*
	 * 将流程id插入对象model中
	 */
	public void insertProcessInstanceIdToModel(String entityName,Map map,long entityId){
		
		try {
			this.baseDao.update(entityName, map, "eid="+entityId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 流程启动时保存启动信息
	 * @param entityId
	 * @param entity
	 * @param startProcessPeople
	 * @param processStartTime
	 * @param processInstanceId
	 * @param processDefinitionName
	 */
	private void saveStartProcessInfo(Long entityId,String entityName, String startProcessPeople,
			Date processStartTime,String processInstanceId,String processDefinitionKey){
		CheckOption checkOption = new CheckOption();
		checkOption.setTaskName("启动" + processDefinitionKey);
		checkOption.setCheckTime(processStartTime);
		checkOption.setCheckResult("启动" + processDefinitionKey + "成功");
		checkOption.setManagerOption("启动流程");
		checkOption.setCheckPeople(startProcessPeople);
		checkOption.setProcessInstanceId(processInstanceId);
		checkOption.setFormId(entityId);
		checkOption.setFormType(entityName);
		checkOption.setDescription("启动" + processDefinitionKey);
		
		Object orgName = this.runtimeService.getVariable(processInstanceId, "organizationName");
		checkOption.setOrganizationName(orgName!=null?orgName.toString():null);
		
		this.checkOptionService.saveCheckOption(checkOption);
	}
	
	
	/**
	 * 取得所有的流程定义
	 * @return
	 */
	public List<Object[]> processList() {
		/*
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
		List<Object[]> objects = new ArrayList<Object[]>();
		
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			objects.add(new Object[] {processDefinition, deployment});
		}
		
		return objects;
	}
	
	/**
	 * 流程定义部署
	 * @param file
	 * @return
	 */
	public Deployment deployWorkflow(InputStream fileInputStream,String fileName) {
		Deployment deployment = null;
		try {
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}  else if (extension.equals("bpmn")) {
				/*
				 * bpmn扩展名特殊处理，转换为bpmn20.xml
				 */
				String baseName = FilenameUtils.getBaseName(fileName);
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				throw new ActivitiException("no support file type of " + extension);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DDDException dddException = new DDDException("WorkFlowEngineService-handle","部署流程定义出错，原因："+e.getMessage()+"，请检查流程定义文件的格式",e);
			throw dddException;
		}

		return  deployment ;
	}
	
	public Deployment deployWorkflow(byte[] byteArray,String fileName) {
		Deployment deployment = null;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(byteArrayInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, byteArrayInputStream).deploy();
			} else if (extension.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, byteArrayInputStream).deploy();
			}  else if (extension.equals("bpmn")) {
				/*
				 * bpmn扩展名特殊处理，转换为bpmn20.xml
				 */
				String baseName = FilenameUtils.getBaseName(fileName);
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", byteArrayInputStream).deploy();
			} else {
				throw new ActivitiException("no support file type of " + extension);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DDDException dddException = new DDDException("WorkFlowEngineService-handle","部署流程定义出错，原因："+e.getMessage()+"，请检查流程定义文件的格式",e);
			throw dddException;
		}

		return deployment;
	}
	
	
	/**
	 * 输出跟踪流程信息
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
		List<Map<String, Object>> activityInfos = workflowTraceService.traceProcess(processInstanceId);
		return activityInfos;
	}

	/**
	 * 读取资源，通过部署ID
	 * @param deploymentId	流程部署的ID
	 * @param resourceName	资源名称(foo.xml|foo.png)
	 * @param response
	 * @throws Exception
	 */
	public byte[] loadByDeployment( String deploymentId,
			String resourceName) throws Exception {
		InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		
		int len = resourceAsStream.available();
		byte [] bytes = new byte[len];
		resourceAsStream.read(bytes);
		
		return bytes;
	}

	/**
	 * 读取资源，通过流程ID
	 * @param resourceType			资源类型(xml|image)
	 * @param processInstanceId		流程实例ID	
	 * @param response
	 * @throws Exception
	 */
	public byte[] loadByProcessInstance( String resourceType,String processInstanceId) throws Exception {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		
//		ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery()
//				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		
		/*文臻修改*/
		ProcessDefinition singleResult = null;
		if(processInstance == null){
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
					.singleResult();
			singleResult = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
		}
		else{
			singleResult = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		}
		
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = singleResult.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = singleResult.getResourceName();
		}
		
		return loadByDeployment(singleResult.getDeploymentId(),resourceName);
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * @param deploymentId	流程部署ID
	 */
	public void deleteProcessDefinition(String deploymentId) {
		try
		{
			repositoryService.deleteDeployment(deploymentId, false);
		}
		catch (Exception e) 
		{
			throw new DDDException("删除流程定义失败,原因是:" + e.getMessage());
		}
	}

	public String deploy(MultipartFile file) {

		String fileName = file.getOriginalFilename();

		try {
			InputStream fileInputStream = file.getInputStream();

			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.indexOf("bpmn20.xml") != -1) {
				repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}  else if (extension.equals("bpmn")) {
				/*
				 * bpmn扩展名特殊处理，转换为bpmn20.xml
				 */
				String baseName = FilenameUtils.getBaseName(fileName);
				repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				throw new ActivitiException("no support file type of " + extension);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/workflow/process-list";
	}


}
