package ddd.simple.entity.listview;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="displayattribute")
public class DisplayAttribute extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** 表示第几个字段 */
	@Column
	private Integer columnIndex;

	//属性英文
	@Column
	private String attributeName;

	//属性中文
	@Column
	private String attributeValue;
	
	@Column
	private String cssArrtribute;
	
	@Column
	private String showType;
	
	@Column(name="reportViewId",composition=true,nullable=false,FKName="DA_fk_RV")
	private ReportView reportView;
	
	public ReportView getReportView() {
		return reportView;
	}

	public void setReportView(ReportView reportView) {
		this.reportView = reportView;
	}

	public String generateDisplaySql(){
		String field ;
		if(columnIndex >0){
			field = " , "+attributeName +" " + ""+attributeName +"";
		}else{
			field = attributeName +" " + ""+attributeName +"" ;
		}
		
		return field;
	}
	
	public String getCssArrtribute() {
		return cssArrtribute;
	}

	public void setCssArrtribute(String cssArrtribute) {
		this.cssArrtribute = cssArrtribute;
	}
	


	public Integer getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getAttributeName()
	{
		return this.attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}
	public String getAttributeValue()
	{
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue)
	{
		this.attributeValue = attributeValue;
	}



	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

}