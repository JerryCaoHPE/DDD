package ddd.simple.action.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.util.EntityUtil;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;
import ddd.simple.entity.model.ModelType;
import ddd.simple.service.model.ModelService;

@Action
@RequestMapping("/Model")
@Controller
public class ModelAction
{
	@Resource(name="modelServiceBean")
	private ModelService modelService;
	
	public Model saveModel(Model model)
	{
		try {
			Model saveModel = this.modelService.saveModel(model);
			EntityUtil.loadLazyProperty(saveModel, "modelItems");
			EntityUtil.loadLazyProperty(saveModel, "parentModel");
			EntityUtil.loadLazyProperty(saveModel, "childModel");
			EntityUtil.loadLazyProperty(saveModel, "modelType");
			EntityUtil.loadLazyProperty(saveModel, "modelType.typeName");
			EntitySet<ModelItem> items = saveModel.getModelItems();
			for(ModelItem item : items ){
				item.setModel(null);
			}
			return saveModel;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Model updateModel(Model model)
	{
		try {
			Model updateModel = this.modelService.updateModel(model);
			return updateModel;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public void submitModel(Model model)
	{
		try 
		{
			 this.modelService.submitModel(model);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public int deleteModel(Model model){
		
		try {
			return this.modelService.deleteModel(model);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Map<String,Object> publishModel(Model model)
	{
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			boolean isSuccess = (this.modelService.publishModel(model) != null);
			result.put("isSuccess", isSuccess);
			return result;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	
	public Model findModelById(Long modelId){
		try {
			Model saveModel = this.modelService.findModelById(modelId);
			EntityUtil.loadLazyProperty(saveModel, "modelItems");
			EntityUtil.loadLazyProperty(saveModel, "parentModel");
			EntityUtil.loadLazyProperty(saveModel, "childModel");
			EntityUtil.loadLazyProperty(saveModel, "modelType");
			EntityUtil.loadLazyProperty(saveModel, "modelType.typeName");
			EntitySet<ModelItem> items = saveModel.getModelItems();
			for(ModelItem item : items ){
				item.setModel(null);
			}
			return saveModel;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Model> findAllModel(){
		try{
			EntitySet<Model> allModel = this.modelService.findAllModel();
			return allModel;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Model findModelByEnglishName(String englishName)
	{
		try {
			Model findModel = this.modelService.findModelByEnglishName(englishName);
			return findModel;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Model findBaseModel(){
		try{
//			Model model = this.modelService.findBaseModel();
//			return model;
			return null;
		}catch (DDDException e){
			throw e;
		}
	}
	
	public Map<String,String> previewForm(Model model) throws DDDException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", this.modelService.previewForm(model));
		return map;
	}
	
	public Set<Map<String,Object>> getWorkflowProcess(){
		try{
//			return this.modelService.getWorkflowProcess();
			return null;
		}catch(DDDException e){
			throw e;
		}
	}
	
	public Map<String,Object> cancelPublishModel(Model model)
	{
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			boolean isSuccess = (this.modelService.cancelPublishModel(model) != null);
			result.put("isSuccess", isSuccess);
			return result;
		} catch (DDDException e) {
			throw e;
		}
	}
}