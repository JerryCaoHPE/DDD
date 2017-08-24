package ddd.base.persistence.baseEntity;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ddd.base.annotation.Column;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.JSONUtil;
import ddd.base.util.json.JSONResult;
@ddd.base.annotation.Entity()
public abstract class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7585208276137832646L;
	
	/**
	 * 用于json序列化
	 */
	private final static ThreadLocal<Boolean> lazyLoad = new ThreadLocal<Boolean>();
	
	@Column(name="EId",Id=true,nullable=false,label="主键")
	private Long EId;
	
	@Column(name="inputCode",label="输入码",length=20)
	private  String inputCode;
	
	@Column(name="operatorCode",label="操作人编码",length=20)
	private  String operatorCode;
	
	@Column(name="operateDate",label="操作日期")
	private  Date operateDate;
	
	@Column(name="orgId",label="单位主键")
	private  Long orgId;
	
	@Column(name="remark",length=8000,label="备注")
	private  String remark;
	
	/**
	 * 标识该对象是否被初始化过
	 */
	private  boolean initialized = false;
	//是否是新创建的对像
	private  boolean newEntity = true;
	
	public final boolean isNewEntity() {
		return newEntity;
	}

	public final void setNewEntity(boolean newEntity) {
		this.newEntity = newEntity;
		if(this.newEntity == true)
		{
			this.initialized = false;
		}
	}
	protected Map<String,Boolean> LazyFieidsLoaded = new HashMap<String,Boolean>();
	
	public final Long getEId() {
		return EId;
	}

	public final void setEId(Long eId) {
		EId = eId;
	}

	public final String getInputCode() {
		lazyLoad();
		return inputCode;
	}

	public final void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public final String getOperatorCode() {
		lazyLoad();
		return operatorCode;
	}

	public final void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public final Date getOperateDate() {
		lazyLoad();
		return operateDate;
	}

	public final void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public final Long getOrgId() {
		lazyLoad();
		return orgId;
	}

	public final void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public final String getRemark() {
		lazyLoad();
		return remark;
	}

	public final void setRemark(String remark) {
		this.remark = remark;
	}
	public final void setInitialized(boolean initialized) {
		this.initialized = initialized;
		if(this.initialized == true)
		{
			this.newEntity = false;
		}
	}

	/**
	 * 利用弱键引用加载该类型的对象
	 */
	public final void lazyLoad(){
		if(lazyLoad.get()==null || lazyLoad.get()==true){
			this.lazyLoad(this);
		}
	}
	/**
	 * 利用弱键引用加载该this.Field的引用类型的对象
	 * @param listField
	 */
	private final <T extends Entity> void lazyLoad(T entity){
		if(entity == null) return;
		if(entity.isNewEntity() == true) return;
		if(entity.isInitialized() == false  && entity.getEId() !=null){
			SessionFactory.getThreadSession().lazyLoad(entity);
			entity.setInitialized(true);
		}
	}
	/**
	 * 利用弱键引用加载该this.listField的引用类型的对象
	 * @param listField
	 */
	public final void lazyLoad(String listField){
		if(lazyLoad.get()!=null && lazyLoad.get()==false){return;}
		if(this.isNewEntity() == true)
		{
			if(SessionFactory.getThreadSession().getEntityFieldValue(this, listField) == null)
			{
				SessionFactory.getThreadSession().setEntityFieldValue(this, listField,new EntitySet<Entity>());
			}
		}
		else if( this.isFieldLazyLoaded(listField) ==false)
		{
			if(this.EId != null)
			{
				SessionFactory.getThreadSession().lazyLoad(listField,this);
			}
		}
		this.LazyFieidsLoaded.put(listField, true);
	}
	public final boolean isFieldLazyLoaded(String listField){
		Boolean lazyFieldLoaded = this.LazyFieidsLoaded.get(listField);
		if( lazyFieldLoaded == null || lazyFieldLoaded == false)
		{
			 return false;
		}
		else
		{
			return true;
		}
	}
 
	public final Map<String, Boolean> getLazyFieidsLoaded() {
		return LazyFieidsLoaded;
	}

	public final void setLazyFieidsLoaded(Map<String, Boolean> lazyFieidsLoaded) {
		LazyFieidsLoaded = lazyFieidsLoaded;
	}

	public final boolean isInitialized() {
		return initialized;
	}

	public final boolean equals(Object entity)
	{
		if(this == entity) return true;
		if( !(entity instanceof Entity)) return false;
		if(this.getEId() == null) return false;
		return this.getEId().equals(((Entity)entity).getEId()) && this.getClass().equals(entity.getClass());
	}
	public final int hashCode()
	{
		if(this.getEId()  == null)
		{
			return System.identityHashCode(this);
		}
		else
		{
			return this.getEId().hashCode();
		}
	}
	@Override
	public final String toString() {
		JSONResult jsonResult ;
		jsonResult = JSONUtil.entityToJSON(this,true);
		return jsonResult.getJson();
	}

	public final static void setLazyLoad(boolean lazyLoad) {
		Entity.lazyLoad.set(lazyLoad);
	}	
	
}
