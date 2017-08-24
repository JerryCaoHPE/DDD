package ddd.simple.service.workflow;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ddd.simple.entity.workflow.CheckOption;
import ddd.simple.service.base.BaseService; 
import ddd.simple.service.base.BaseServiceInterface;

public interface CheckOptionService extends BaseServiceInterface
{
	public  void deleteCheckOptionById( Long checkOptionId) ;
	
	public CheckOption findCheckOptionById( Long checkOptionId);
	 
	public CheckOption saveCheckOption(CheckOption checkOptionId) ;

	public CheckOption updateCheckOption(CheckOption checkOptionId) ;

	public void deleteCheckOption(CheckOption checkOptionId) ;

	public Set<Map<String, Object>> findCheckOptionsByCheckOptionIdAndFormId(Long checkOptionId,Long formId);
	
	public Set<Map<String, Object>> findCheckOptionsByFormId(Long formId);
	
	//查询某一个审批流程中某一步审批
	public Set<Map<String, Object>> findCheckOptionsByFormTypeAndAuditStep(String formType,String taskName);

	public Set<Map<String, Object>> findCheckOptionByFormIdAndFormTypeAndProcessId(Long formId,String formType,Long processInstanceId);
	public Set<Map<String, Object>> findCheckOptionByFormIdAndFormType(Long formId,String formType);
	
	public Set<Map<String, Object>> findCheckOptionByProcessInstanceId(Long processInstanceId);
	
	public Set<Map<String, Object>> findCheckOptionByCheckPeople(String checkPeople);
	
	public Set<Map<String, Object>> findCheckOptionByParameters(String checkPeople,Date startDate,
			Date endDate,String organizationName,String managementItemName);
}