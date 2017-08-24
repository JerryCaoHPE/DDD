package ddd.simple.entity.listview;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="viewTreeCondition")
public class ViewTreeCondition   extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column
	private String viewTreeKey ;
	
	@Column
	private String viewTreeName ;
	
	@Column 
	private Integer displayIndex;
	
	@Column(name="reportViewId",composition=true,FKName="VTC_fk_RP")
	private ReportView reportView;

	public String getViewTreeKey() {
		return viewTreeKey;
	}

	public void setViewTreeKey(String viewTreeKey) {
		this.viewTreeKey = viewTreeKey;
	}

	public Integer getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(Integer displayIndex) {
		this.displayIndex = displayIndex;
	}

	public ReportView getReportView() {
		return reportView;
	}

	public void setReportView(ReportView reportView) {
		this.reportView = reportView;
	}

	public String getViewTreeName() {
		return viewTreeName;
	}

	public void setViewTreeName(String viewTreeName) {
		this.viewTreeName = viewTreeName;
	}
	
}
