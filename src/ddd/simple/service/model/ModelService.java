package ddd.simple.service.model;

import java.util.Map;
import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.dynamicForm.DynamicForm;
 
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelType;

public interface ModelService  
{
	public Model saveModel(Model model) ;
	
	public Model updateModel(Model model);
	
	public int deleteModel(Model model) ;

	public Model publishModel(Model model) ;
	
	public Model cancelPublishModel(Model model) ;
	
	public Model findModelById(Long modelId) ;
	
	public EntitySet<Model> findAllModel() ;
	
	public Model findModelByEnglishName(String englishName);
	
	public String[] findPropertiesName(String tableName);
	
	public String previewForm(Model model);
	
	public void synchronousModelItem(DynamicForm dynamicForm);
	
	public void submitModel(Model model);
}