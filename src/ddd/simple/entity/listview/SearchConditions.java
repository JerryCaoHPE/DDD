package ddd.simple.entity.listview;

import java.io.Serializable;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="searchconditions")
public class SearchConditions   extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column
	private String paramName ;
	
	@Column 
	private String columnValue;

	@Column
	private String displayName ;
	
	@Column
	private String displayType;
	
	@Column(length=1500)
	private String paramsJson;
	
	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Column(name="reportViewId",composition=true,FKName="searchCds_fk_reportview")
	private ReportView reportView;
	
	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ReportView getReportView() {
		return reportView;
	}

	public void setReportView(ReportView reportView) {
		this.reportView = reportView;
	}

	/** 
	* @return paramsJson 
	*/ 
	
	public String getParamsJson()
	{
		return paramsJson;
	}

	/** 
	* @param paramsJson 要设置的 paramsJson 
	*/
	
	public void setParamsJson(String paramsJson)
	{
		this.paramsJson = paramsJson;
	}

	 
}
