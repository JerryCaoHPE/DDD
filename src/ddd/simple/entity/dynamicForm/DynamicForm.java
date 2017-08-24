package ddd.simple.entity.dynamicForm;

import java.io.Serializable;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;

/**
* 动态表单
*/
@ddd.base.annotation.Entity(name="dynamicForm",label="动态表单")
public class DynamicForm extends Entity implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	
	@Column(name="dynamicFormKey",label="唯一标示",nullable=false,unique=true)
	private String dynamicFormKey;

	@Column(name="dynamicFormName",label = "名称")
	private String dynamicFormName;
	
	@Column(length=8000,name="dynamicFormData",label = "数据项")
	private String dynamicFormData;
	
	@Column(length=8000,name="dynamicFormOldHtml",label = "设计原始模板")
	private String dynamicFormOldHtml;
	
	@Column(length=8000,name="dynamicFormNewHtml",label = "模板html")
	private String dynamicFormNewHtml;
	
	@Column(length=8000,name="dynamicFormDisplayHtml",label = "模板查看展示html")
	private String dynamicFormDisplayHtml;
	
	@Column(length=100,name="dynamicFormType",label = "动态表单类型")
	private String dynamicFormType;
	
	@Column(length=100,name="dynamicFormStatus",label = "动态表单状态")
	private String dynamicFormStatus;
	
	@Column(length=100,name="dynamicFormCategory",label = "动态表单类别")
	private String dynamicFormCategory;

	@Column(name="totalCount",label = "标签总数")
	private Integer totalCount;
	
	public String getDynamicFormKey() {
		lazyLoad();
		return dynamicFormKey;
	}

	public void setDynamicFormKey(String dynamicFormKey) {
		this.dynamicFormKey = dynamicFormKey;
	}

	public String getDynamicFormName() {
		lazyLoad();
		return dynamicFormName;
	}

	public void setDynamicFormName(String dynamicFormName) {
		this.dynamicFormName = dynamicFormName;
	}

	public String getDynamicFormData() {
		lazyLoad();
		return dynamicFormData;
	}

	public void setDynamicFormData(String dynamicFormData) {
		this.dynamicFormData = dynamicFormData;
	}

	public String getDynamicFormOldHtml() {
		lazyLoad();
		return dynamicFormOldHtml;
	}

	public void setDynamicFormOldHtml(String dynamicFormOldHtml) {
		this.dynamicFormOldHtml = dynamicFormOldHtml;
	}

	public String getDynamicFormNewHtml() {
		lazyLoad();
		return dynamicFormNewHtml;
	}

	public void setDynamicFormNewHtml(String dynamicFormNewHtml) {
		this.dynamicFormNewHtml = dynamicFormNewHtml;
	}

	public Integer getTotalCount() {
		lazyLoad();
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getDynamicFormDisplayHtml() {
		lazyLoad();
		return dynamicFormDisplayHtml;
	}

	public void setDynamicFormDisplayHtml(String dynamicFormDisplayHtml) {
		this.dynamicFormDisplayHtml = dynamicFormDisplayHtml;
	}

	public String getDynamicFormType() {
		lazyLoad();
		return dynamicFormType;
	}

	public void setDynamicFormType(String dynamicFormType) {
		this.dynamicFormType = dynamicFormType;
	}

	public String getDynamicFormStatus() {
		lazyLoad();
		return dynamicFormStatus;
	}

	public void setDynamicFormStatus(String dynamicFormStatus) {
		this.dynamicFormStatus = dynamicFormStatus;
	}

	public String getDynamicFormCategory() {
		lazyLoad();
		return dynamicFormCategory;
	}

	public void setDynamicFormCategory(String dynamicFormCategory) {
		this.dynamicFormCategory = dynamicFormCategory;
	}
	
	 @Override  
    public Object clone() {  
        DynamicForm dyanmicForm = null;  
        try{  
        	dyanmicForm = (DynamicForm)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return dyanmicForm;  
    }  
	
}