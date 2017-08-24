package ddd.simple.entity.reportForm;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.dynamicForm.DynamicForm;

@ddd.base.annotation.Entity(label="报表测试",name="reportTest")
public class ReportTest extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="类型",comment="")
	private String reportType;

	@Column(label="名称",unique=true,comment="")
	private String reportName;

	@Column(label="Key",unique=true,comment="")
	private String reportKey;

	@Column(label="附件",joinColumn="reportTestId",comment="")
	private EntitySet<Attachment> excelFile;

	@Column(label="动态表单",name="dynamicFormId",comment="")
	private DynamicForm dynamicForm;


	public String getReportType() {
		lazyLoad();
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportName() {
		lazyLoad();
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportKey() {
		lazyLoad();
		return this.reportKey;
	}

	public void setReportKey(String reportKey) {
		this.reportKey = reportKey;
	}

	public EntitySet<Attachment> getExcelFile() {
		lazyLoad("excelFile");
		return this.excelFile;
	}

	public void setExcelFile(EntitySet<Attachment> excelFile) {
		super.LazyFieidsLoaded.put("excelFile", true);
		this.excelFile = excelFile;
	}

	public DynamicForm getDynamicForm() {
		lazyLoad();
		return this.dynamicForm;
	}

	public void setDynamicForm(DynamicForm dynamicForm) {
		this.dynamicForm = dynamicForm;
	}
}