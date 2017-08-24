/**
 * 
 */
package ddd.simple.entity.model;


import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="modelItem",label="模块")
public class ModelItem extends Entity implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="modelId",composition=true,FKName="MI_FK_M")
	private Model model;
	
	@Column(label="模型项英文名称")
	private String modelItemEnglishName;//英文名称
	
	@Column(label="模型项中文名称")
	private String modelItemName;//中文名称
	
	@Column(label="字段长度")
	private Integer textSize;//长度
	
	@Column(name="datatype",label="数据类型")
	private String datatype;
	
	@Column(name="datatypeDesc",label="数据类型中文名")
	private String datatypeDesc;

	@Column(name="javatype",label="数据类型英文名")
	private String javatype;

	@Column(name="displayIndex",label="排序")
	private Long displayIndex ;
	
	@Column(name="isSearchCriteria",label="是否作为搜索条件",length=10)
	private String isSearchCriteria;
	
	@Column(name="relatedParameters",label="相关参数",length=8000)
	private String relatedParameters;
	
	@Column(name="isInherited",label="是否为系统模型项",length=10)
	private String isInherited;
	
	@Column(name="isMustItem",label="是否必填",length=10)
	private String isMustItem;
	
	@Column(name="isSort",label="是否排序",length=10)
	private String isSort;
	
	@Column(name="isContribute",label="是否投稿",length=10)
	private String isContribute;
	
	@Column(name="isUse",label="是否启用",length=10)
	private String isUse;
	
	@Column(name="isInput",label="是否录入",length=10)
	private String isInput;
	
	@Column(name="isProcess",label="是否加入流程变量",length=10)
	private String isProcess;
	
	@Column(name="subModelEnglishName",label="子模型英文名")
	private String subModelEnglishName; 
	
	@Column(name="joinTableName",label="中间表表名")
	private String joinTableName;
	
	@Column(name="dynamicFormKey",label="动态表单唯一标示")
	private String dynamicFormKey;
	
	public String getJavatype() {
		lazyLoad();
		return javatype;
	}

	public void setJavatype(String javatype) {
		this.javatype = javatype;
	}

	public Model getModel() {
		lazyLoad();
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getModelItemEnglishName() {
		lazyLoad();
		return modelItemEnglishName;
	}

	public void setModelItemEnglishName(String modelItemEnglishName) {
		this.modelItemEnglishName = modelItemEnglishName;
	}

	public String getModelItemName() {
		lazyLoad();
		return modelItemName;
	}

	public void setModelItemName(String modelItemName) {
		this.modelItemName = modelItemName;
	}

	public Integer getTextSize() {
		lazyLoad();
		return textSize;
	}

	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}

	public String getDatatype() {
		lazyLoad();
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Long getDisplayIndex() {
		lazyLoad();
		return displayIndex;
	}

	public void setDisplayIndex(Long displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getRelatedParameters() {
		lazyLoad();
		return relatedParameters;
	}

	public void setRelatedParameters(String relatedParameters) {
		this.relatedParameters = relatedParameters;
	}

	public String getIsInherited() {
		lazyLoad();
		return isInherited;
	}

	public void setIsInherited(String isInherited) {
		this.isInherited = isInherited;
	}
	
	public String getIsSearchCriteria() {
		lazyLoad();
		return isSearchCriteria;
	}

	public void setIsSearchCriteria(String isSearchCriteria) {
		this.isSearchCriteria = isSearchCriteria;
	}

	public String getIsMustItem() {
		lazyLoad();
		return isMustItem;
	}

	public void setIsMustItem(String isMustItem) {
		this.isMustItem = isMustItem;
	}
	
	public String getIsSort() {
		lazyLoad();
		return isSort;
	}

	public void setIsSort(String isSort) {
		this.isSort = isSort;
	}
	
	public String getIsProcess(){
		lazyLoad();
		return this.isProcess;
	}

	public void setIsProcess(String isProcess){
		this.isProcess = isProcess;
	}

	public String getIsContribute() {
		lazyLoad();
		return isContribute;
	}

	public void setIsContribute(String isContribute) {
		this.isContribute = isContribute;
	}
	
	public String getIsUse() {
		lazyLoad();
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Override
	public ModelItem clone() throws CloneNotSupportedException {
		return (ModelItem) super.clone();
	}

	public String getSubModelEnglishName() {
		lazyLoad();
		return subModelEnglishName;
	}

	public void setSubModelEnglishName(String subModelEnglishName) {
		this.subModelEnglishName = subModelEnglishName;
	}

	public String getJoinTableName() {
		lazyLoad();
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}

	public String getIsInput() {
		lazyLoad();
		return isInput;
	}

	public void setIsInput(String isInput) {
		this.isInput = isInput;
	}

	public String getDynamicFormKey() {
		lazyLoad();
		return dynamicFormKey;
	}

	public void setDynamicFormKey(String dynamicFormKey) {
		this.dynamicFormKey = dynamicFormKey;
	}
	
	public String getDatatypeDesc() {
		lazyLoad();
		return datatypeDesc;
	}

	public void setDatatypeDesc(String datatypeDesc) {
		this.datatypeDesc = datatypeDesc;
	}
}
