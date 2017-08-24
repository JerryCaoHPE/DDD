package ddd.simple.service.model.extend;

import java.util.Map;

import ddd.simple.entity.model.Model;

public interface ModelExtInterface
{

	public void beforeSubmitModel(Model model, Map<String,Object> publicParams);

	public void afterSubmitModel(Model model,Map<String,Object> publicParams);

	public String screenModelDataSql(Model model);
	
	public void beforeDeleteModel(Model model,Map<String,Object> publicParams);
	
	public void afterDeleteModel(Model model,Map<String,Object> publicParams);
}
