package ddd.simple.entity.reportForm;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="报表模版",name="reportForm")
public class ReportForm extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="报表模版名称",nullable=false)
	private String reportFormName;

	@Column(label="报表模版路径")
	private String reportFormUrl;

	@Column(label="报表模版类型",comment="报表信息")
	private String reportFormType;


	public String getReportFormName() {
		lazyLoad();
		return this.reportFormName;
	}

	public void setReportFormName(String reportFormName) {
		this.reportFormName = reportFormName;
	}

	public String getReportFormUrl() {
		lazyLoad();
		return this.reportFormUrl;
	}

	public void setReportFormUrl(String reportFormUrl) {
		this.reportFormUrl = reportFormUrl;
	}

	public String getReportFormType() {
		lazyLoad();
		return this.reportFormType;
	}

	public void setReportFormType(String reportFormType) {
		this.reportFormType = reportFormType;
	}


	
}