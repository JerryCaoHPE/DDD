package ddd.simple.entity.listview;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="ordercondition")
public class OrderCondition extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String columnName ;
	
	@Column
	private Integer columnIndex ;
	
	@Column
	private String orderType ;
	
	@Column(name="reportViewId",composition=true,FKName="OC_FK_RV")
	private ReportView reportView;
	
	public ReportView getReportView() {
		return reportView;
	}

	public void setReportView(ReportView reportView) {
		this.reportView = reportView;
	}

	public String generateOrderSql(String baseSql){
		String orderSql = this.columnName +" "+this.orderType;
		
		return orderSql;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
	

}
