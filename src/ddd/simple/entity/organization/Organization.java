package ddd.simple.entity.organization;


import java.io.Serializable;
import java.util.Date;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;



/**
 * 单位 
 * @author 龚翔
 */
@ddd.base.annotation.Entity(name="organization",label="单位")
public class Organization extends Entity implements Serializable{
 

	private static final long serialVersionUID = 1L;
	

	/**单位编码*/
	@Column(label="单位编码")
	private String code;
    /**单位名称*/
	@Column(label="单位名称")
	private String name;
    /**编码全称*/
	@Column(label="编码全称")
	private String fullCode;
    /**单位简码*/
	@Column(label="单位简码")
	private String shortCode;
    /**单位地址*/
	@Column(label="单位地址",length=3000)
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**单位负责人*/
	@Column(label="单位负责人")
    private String manager;
    /**国际编码*/
	@Column(label="国际编码")	
    private String outGB;
    /**电子邮件*/
	@Column(label="电子邮件")	
    private String email;
    /**单位网址*/
	@Column(label="单位网址")
    private String webSite;
    /**联系电话*/
	@Column(label="联系电话")	
    private String linkTel;
    /**联系人*/
	@Column(label="联系人")	
    private String linkMan;
    /**是否在用*/
	@Column(label="是否启用")	
    private Boolean enable;
    /**备注*/
	@Column(label="备注")	
    private String description;
    /**创建时间*/
	@Column(label="创建时间")
    private Date creatTime;
    /**修改时间*/
	@Column(label="修改时间")	
    private Date modifyTime;  
    /**上级单位*/
	@Column(label="上级单位",name="organizationParentId")
	private Organization parent;
	/**下级单位*/
	@Column(label="下级单位",joinColumn="organizationParentId",composition=true)
	private EntitySet<Organization> children;


	@Column(label = "打印名称")
	private String printName;
	
	@Column(label = "打印英文名称")
	private String printEngishName;


	@Column(label = "报告服务传真")
	private String printServiceFax;

	@Column(label = "报告服务EMail")
	private String printServiceEMail;

	@Column(label = "报告服务网址")
	private String printServiceWeb;
	
	@Column(label = "流程实例Id")
	private Long processInstanceId;
	
	@Column(label = "审批结果",nullable=true)
	private String checkResult;
	
	@Column(label = "审批状态",nullable=true)
	private String auditState;

	public String getCode() {
		lazyLoad();
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullCode() {
		lazyLoad();
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	public String getShortCode() {
		lazyLoad();
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getManager() {
		lazyLoad();
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getOutGB() {
		lazyLoad();
		return outGB;
	}

	public void setOutGB(String outGB) {
		this.outGB = outGB;
	}

	public String getEmail() {
		lazyLoad();
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebSite() {
		lazyLoad();
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getLinkTel() {
		lazyLoad();
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkMan() {
		lazyLoad();
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public Boolean getEnable() {
		lazyLoad();
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getDescription() {
		lazyLoad();
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatTime() {
		lazyLoad();
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getModifyTime() {
		lazyLoad();
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Organization getParent() {
		lazyLoad();
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public EntitySet<Organization> getChildren() {
		lazyLoad("children");
		return children;
	}

	public void setChildren(EntitySet<Organization> children) {
		this.children = children;
	}

	public String getPrintName() {
		lazyLoad();
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public String getPrintEngishName() {
		lazyLoad();
		return printEngishName;
	}

	public void setPrintEngishName(String printEngishName) {
		this.printEngishName = printEngishName;
	}

	public String getPrintServiceFax() {
		lazyLoad();
		return printServiceFax;
	}

	public void setPrintServiceFax(String printServiceFax) {
		this.printServiceFax = printServiceFax;
	}

	public String getPrintServiceEMail() {
		lazyLoad();
		return printServiceEMail;
	}

	public void setPrintServiceEMail(String printServiceEMail) {
		this.printServiceEMail = printServiceEMail;
	}

	public String getPrintServiceWeb() {
		lazyLoad();
		return printServiceWeb;
	}

	public void setPrintServiceWeb(String printServiceWeb) {
		this.printServiceWeb = printServiceWeb;
	}
	
	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
}
