package ddd.simple.entity.listview;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.util.Strings;

@ddd.base.annotation.Entity(name="querycondition")
public class QueryCondition extends Entity implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String columnName;
	
	//是否是下拉框
	@Column
	private Boolean isCombox;

	@Column
	private Integer columnIndex;
	
	/** 等于、大于、小于、之间...... */
	@Column
	private String queryConditionType;
	
	//固定值,输入框
	@Column
	private String inputType;
	
	//并且或者
	@Column
	private String linkType;

	/**只输入一个查询值时默认用这个属性 */
	@Column
	private String conditionValue;
	
	@Column(name="reportViewId",composition=true,FKName="QC_FK_RV")
	private ReportView reportView;
	
	//后期处理
	public void postProcess(){
		
		if(linkType.equals("并且")){
			this.linkType = " and " ;
		}else if(linkType.equals("或者")){
			this.linkType = " or ";
		}
		
	}
	
	public String generateWhereSql(){
		String whereSql = " " + this.columnName ;
		
		whereSql = whereSql +" "+ queryConditionType;
		if(inputType.equals("V") || inputType.equals("U")){//固定值
			
			if(!conditionValue.equals("NULL")){
				whereSql = whereSql + " '" + conditionValue +"'";
			}else{
				whereSql = whereSql + " " + conditionValue +"";
			}
		}else if(inputType.equals("I")){//输入框
			whereSql = whereSql + " '" + Strings.PLACEHOLDER +"'" ;
		}
		
		whereSql = whereSql + " " + this.linkType ;
		
		return whereSql;
	}
	
	
	public Integer getColumnIndex()
	{
		return this.columnIndex;
	}
	
	public void setColumnIndex(Integer columnIndex)
	{
		this.columnIndex = columnIndex;
	}

	public String getQueryConditionType()
	{
		return this.queryConditionType;
	}

	public void setQueryConditionType(String queryConditionType)
	{
		this.queryConditionType = queryConditionType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}




	public ReportView getReportView() {
		return reportView;
	}

	public void setReportView(ReportView reportView) {
		this.reportView = reportView;
	}

	public Boolean getIsCombox() {
		return isCombox;
	}

	public void setIsCombox(Boolean isCombox) {
		this.isCombox = isCombox;
	}




	
	
	
}