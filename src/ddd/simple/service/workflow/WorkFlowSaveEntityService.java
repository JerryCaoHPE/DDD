package ddd.simple.service.workflow;

import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ddd.simple.dao.base.BaseDao;

@Service
@Transactional
public class WorkFlowSaveEntityService extends BaseDao implements JavaDelegate
{
	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		try 
		{
			String entityName = (String)delegateExecution.getVariable("对应表名");
			Long entityId = (Long)delegateExecution.getVariable("实体Id");
			String checkResultProcessVariableName = (String)delegateExecution.getVariable("表示审批结果的流程变量名称");
			String checkResult = String.valueOf(delegateExecution.getVariable(checkResultProcessVariableName));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
			map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,"未通过");
			
			//设置申请单是否有效
			//map.put(WorkFlowEngineService.ATTRIBUTENAME_ISEFFECT,WorkFlowEngineService.VALUE_ISEFFECT);
			
			//设置审批结果
			if(checkResult != null)
			{
				if("同意".equals(checkResult))
				{
					map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,WorkFlowEngineService.VALUE_CHECKRESULT);
					map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				}else if("不同意".equals(checkResult)){
					map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,"未通过");
					map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				}else if("驳回".equals(checkResult)){
					map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,"驳回");
					map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				}else{
					map.put(WorkFlowEngineService.ATTRIBUTENAME_CHECKRESULT,WorkFlowEngineService.VALUE_DEFAULT_CHECKRESULT);
					map.put(WorkFlowEngineService.ATTRIBUTENAME_AUDITSTATE,WorkFlowEngineService.VALUE_DEFAULT_AUDITSTATE);
				}
				
				this.update(entityName, map, "eid="+entityId);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
	}
}