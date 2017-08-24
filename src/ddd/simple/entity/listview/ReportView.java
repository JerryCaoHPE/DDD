package ddd.simple.entity.listview;

import java.io.Serializable;
import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="reportview",label="视图")
public class ReportView extends Entity implements Serializable,Cloneable
{

	private static final long serialVersionUID = 1L;
	
	@Column(name="reportViewName",label="名称")
	private String reportViewName;
	
	@Column(name="reportViewKey",label="唯一编码")
	private String reportViewKey;

	@Column(name="reportSql",label="初始sql")
	private String reportSql;

	/** 后台生成 */
	@Column(name="finalSql",label="最终sql",length=8000)
	private String finalSql;
	
	@Column(name="dataSourceId",composition=true,FKName="RV_FK_DS")
	private DataSource dataSource;
	
	/**定义不能通过sql来确定的网页显示格式 */
	@Column(joinColumn="reportViewId",composition=true,orderBy="columnIndex")
	private EntitySet<DisplayAttribute> displayAttributes;
	
	@Column(joinColumn="reportViewId",composition=true)
	private EntitySet<OrderCondition> orderConditions;

	@Column(joinColumn="reportViewId",composition=true)
	private EntitySet<SearchConditions> searchConditions;
	
	@Column(joinColumn="reportViewId",composition=true)
	private EntitySet<ViewTreeCondition> viewTreeConditions;
	
	@Column(name="reportViewDesc",label="说明")
	private String reportViewDesc;
	
	@Column(name="categoryId",label="categoryId")
	private Long categoryId;

	public String getReportViewName()
	{
		lazyLoad();
		return this.reportViewName;
	}

	public void setReportViewName(String reportViewName)
	{
		this.reportViewName = reportViewName;
	}

	public String getReportSql()
	{
		lazyLoad();
		return this.reportSql;
	}
	

	public DataSource getDataSource() {
		lazyLoad();
		return dataSource;
	}
	
	public EntitySet<OrderCondition> getOrderConditions() {
		lazyLoad("orderConditions");
		return orderConditions;
	}

	public void setOrderConditions(EntitySet<OrderCondition> orderConditions) {
		super.LazyFieidsLoaded.put("orderConditions", true);
		this.orderConditions = orderConditions;
	}
	

	public EntitySet<SearchConditions> getSearchConditions() {
		lazyLoad("searchConditions");
		return searchConditions;
	}

	public void setSearchConditions(EntitySet<SearchConditions> searchConditions) {
		super.LazyFieidsLoaded.put("searchConditions", true);
		this.searchConditions = searchConditions;
	}

	
	public EntitySet<ViewTreeCondition> getViewTreeConditions() {
		lazyLoad("viewTreeConditions");
		return viewTreeConditions;
	}

	public void setViewTreeConditions(
			EntitySet<ViewTreeCondition> viewTreeConditions) {
		super.LazyFieidsLoaded.put("viewTreeConditions", true);
		this.viewTreeConditions = viewTreeConditions;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public void setReportSql(String reportSql)
	{
		this.reportSql = reportSql;
	}

	public String getFinalSql()
	{
		lazyLoad();
		return this.finalSql;
	}

	public void setFinalSql(String finalSql)
	{
		this.finalSql = finalSql;
	}

	public EntitySet<DisplayAttribute> getDisplayAttributes()
	{
		lazyLoad("displayAttributes");
		return this.displayAttributes;
	}

	public void setDisplayAttributes(EntitySet<DisplayAttribute> displayAttributes)
	{
		super.LazyFieidsLoaded.put("displayAttributes", true);
		this.displayAttributes = displayAttributes;
	}

	public String getReportViewDesc()
	{
		lazyLoad();
		return this.reportViewDesc;
	}

	public void setReportViewDesc(String reportViewDesc)
	{
		this.reportViewDesc = reportViewDesc;
	}

	public String getReportViewKey() {
		lazyLoad();
		return reportViewKey;
	}

	public void setReportViewKey(String reportViewKey) {
		this.reportViewKey = reportViewKey;
	}

	public Long getCategoryId() {
		lazyLoad();
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}		
	
	public Object clone() {  
        ReportView o = null;  
        try {  
            o = (ReportView) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    } 
}