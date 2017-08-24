/**
 * 
 */
package ddd.simple.entity.model;

import java.io.Serializable;

import ddd.base.annotation.Column;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name = "model", label = "模型")
public class Model extends Entity implements Serializable
{
	
	private static final long	serialVersionUID	= 1L;
	public static final String	STATE_SAVE			= "已保存";
	public static final String	STATE_SUBMIT		= "已提交";
	public static final String	STATE_PUBLISH		= "已发布";
	
	@Column(label = "模型名称")
	private String modelName;
	
	@Column(label = "模型英文名称", nullable = false, unique = true)
	private String modelEnglishName;
	
	@Column(label = "模型编辑表单")
	private String modelForm;
	
	@Column(label = "审批流程标识")
	private String processKey;
	
	@Column(label = "模型状态")
	private String state;// 已保存、已发布
	
	@Column(name = "modelTypeId", label = "模型类型")
	private ModelType ModelType;
	
	@Column(joinColumn = "modelId", composition = true, orderBy = "displayIndex")
	private EntitySet<ModelItem> modelItems;
	
	@Column(name = "parentId", composition = true, FKName = "MD_FK_MD")
	private Model parentModel;
	
	@Column(joinColumn = "parentId", composition = true)
	private EntitySet<Model> childModel;
	
	public ModelType getModelType()
	{
		lazyLoad();
		return ModelType;
	}
	
	public void setModelType(ModelType modelType)
	{
		ModelType = modelType;
	}
	
	public Model getParentModel()
	{
		lazyLoad();
		return parentModel;
	}
	
	public void setParentModel(Model parentModel)
	{
		this.parentModel = parentModel;
	}
	
	public EntitySet<Model> getChildModel()
	{
		lazyLoad();
		return childModel;
	}
	
	public void setChildModel(EntitySet<Model> childModel)
	{
		super.LazyFieidsLoaded.put("childModel", true);
		this.childModel = childModel;
	}
	
	public String getModelName()
	{
		lazyLoad();
		return modelName;
	}
	
	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}
	
	public String getModelEnglishName()
	{
		lazyLoad();
		return modelEnglishName;
	}
	
	public void setModelEnglishName(String modelEnglishName)
	{
		this.modelEnglishName = modelEnglishName;
	}
	
	public String getProcessKey()
	{
		lazyLoad();
		return processKey;
	}
	
	public void setProcessKey(String processKey)
	{
		this.processKey = processKey;
	}
	
	public String getState()
	{
		lazyLoad();
		return state;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public EntitySet<ModelItem> getModelItems()
	{
		lazyLoad("modelItems");
		return modelItems;
	}
	
	public void setModelItems(EntitySet<ModelItem> modelItems)
	{
		super.LazyFieidsLoaded.put("modelItems", true);
		this.modelItems = modelItems;
	}
	
	public String getModelForm()
	{
		lazyLoad();
		return modelForm;
	}
	
	public void setModelForm(String modelForm)
	{
		this.modelForm = modelForm;
	}
	
}
