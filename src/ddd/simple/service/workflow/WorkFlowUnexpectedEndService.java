package ddd.simple.service.workflow;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;


@Service
public class WorkFlowUnexpectedEndService extends BaseDao implements JavaDelegate{

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		try 
		{
			String entityName = (String)delegateExecution.getVariable("对应表名");
			Long entityId = (Long)delegateExecution.getVariable("实体Id");
			String currentCheckResult = String.valueOf(delegateExecution.getVariable("当前审批人审批结果"));
			String checkResultProcessVariableName = (String)delegateExecution.getVariable("表示审批结果的流程变量名称");
			delegateExecution.setVariable(checkResultProcessVariableName, currentCheckResult);
		
			Map<String, Object> map = new HashMap<String, Object>();
			if (currentCheckResult.equals("不同意")) {
				map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,"未通过");
			}else if(currentCheckResult.equals("驳回")){
				map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,"驳回");
			}
		
			this.update(entityName, map, "eid="+entityId);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
	}
}