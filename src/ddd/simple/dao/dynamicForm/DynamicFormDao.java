package ddd.simple.dao.dynamicForm;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.dynamicForm.DynamicForm;

public interface DynamicFormDao extends BaseDaoInterface
{
	public DynamicForm saveDynamicForm(DynamicForm dynamicForm) throws Exception;
	
	public int deleteDynamicForm(Long dynamicFormId) throws Exception;
	
	public DynamicForm updateDynamicForm(DynamicForm dynamicForm) throws Exception;
	
	public DynamicForm findDynamicFormById(Long dynamicFormId) throws Exception;
	
	public EntitySet<DynamicForm> findAllDynamicForm() throws Exception;

	public DynamicForm findDynamicFormByKey(String dynamicFormKey) throws Exception;
}
