package ddd.simple.entity.organization;


import java.io.Serializable;
import java.util.Date;
import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

/**
 * 职员
 * 
 */
@ddd.base.annotation.Entity(name="employee",label="职员")
public class Employee extends Entity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	
	
	/**职员编码*/
	@Column(label="职员编码")
	private String code;
		
	/**姓名*/
	@Column(label="姓名")	
	private String name;
	
	/**属性*/
	@Column(label="属性")	
	private String property;
	
	
	/**职称*/
	@Column(label="职称")
	private String jobName;
	
	/**工作日模版**/
	@Column(label="考勤方案")
	private String workDayTemplate ;
	
	/**毕业学校*/
	@Column(label="毕业学校")
	private String graduateSchool;
	
	/**专业*/
	@Column(label="专业")	
	private String major;
	
	
	/**联系电话*/
	@Column(label="联系电话")	
	private String linkTel;
	
	
	/**传真*/
	@Column(label="传真")	
	private String faxNumber;
	
	/**电子邮件*/
	@Column(label="电子邮件")	
	private String email;
	
	/**电子邮件*/
	@Column(label="站内邮箱")	
	private String innerEmail;
	
	/**特长*/
	@Column(label="特长")	
	private String specialAbility;
	
	/**是否在用*/
	@Column(label="是否启用")	
    private Boolean enable;
    
	/**备注*/
	@Column(label="备注")    
	private String description;
	
	/**岗位*/
	@Column(label = "岗位")
	private String workPosition;
	
	/**工种*/
	@Column(label = "工种")
	private String workMode;
	
	/**职位*/
	@Column(label = "职位")
	private String employeePosition;
	
	/**所属单位*/
	@Column(label="所属单位",name="organizationId")
	private Organization organization;
	
	/**所属部门*/
	@Column(label="所属部门",name="departmentId")
	private Department department;

	
	/**职员类型*/
	@Column(label = "职员类型")
	private String employeeType;
	
	/*@Column(label="签名",isRef=true)
	private FileInfo signature;*/
	
	
	/**离职原因	 **/
	@Column(label="离职原因")
	private String dimissionReason;

	/**性别	 **/
	@Column(label = "性别")
	private String sex;

	/**紧急联系电话	如父母、妻子或丈夫的电话 **/
	@Column(label="紧急联系电话")
	private String emergencyTel;

	/**婚姻状况	 **/
	@Column(label = "婚姻状况")
	private String maritalStatus;

	/**银行号码	 **/
	@Column(label="银行号码")
	private String bankCardNo;

	/**离职时间	 **/
	@Column(label="离职时间")
	private Date dimissionTime;

	/**毕业时间	 **/
	@Column(label="毕业时间")
	private Date graduateTime;

	/**紧急联系人地址	 **/
	@Column(label="紧急联系人地址")
	private String emergencyAddress;

	/**开户行地址	 **/
	@Column(label="开户行地址")
	private String bankAddr;

	/**合同开始时间	 **/
	@Column(label="合同开始时间")
	private Date contractBeginTime;

	/**出生日期	 **/
	@Column(label="出生日期")
	private Date birthDate;

	/**入司方式	如“社招”、“校招”等 **/
	@Column(label = "入司方式")
	private String joinCompanyType;

	/**银行卡开户行	 **/
	@Column(label="银行卡开户行")
	private String cardOfBank;

	/**转正时间	 **/
	@Column(label="转正时间")
	private Date regularizationTime;

	/**籍贯	 **/
	@Column(label="籍贯")
	private String nativePlace;

	/**结婚时间	 **/
	@Column(label="结婚时间")
	private Date marriedTime;

	/**在职状态	 **/
	@Column(label = "在职状态")
	private String onJobState;

	/**身份证号码	 **/
	@Column(label="身份证号码")
	private String identityCardNo;

	/**入司时间	 **/
	@Column(label="入司时间")
	private Date joinCompanyTime;

	/**民族	 **/
	@Column(label="民族")
	private String nation;

	/**合同签订日期	 **/
	@Column(label="合同签订日期")
	private Date contractSignTime;

	/**住址	 **/
	@Column(label="住址")
	private String address;

	/**合同结束时间	 **/
	@Column(label="合同结束时间")
	private Date contractEndTime;

	/**政治面貌	 **/
	@Column(label = "政治面貌")
	private String politicStatus;

	/**离职类型	 **/
	@Column(label = "离职类型")
	private String dimissionType;

	/**最高学历	 **/
	@Column(label = "最高学历")
	private String lastedEducationBackground;

	/**驾照类型	 **/
	@Column(label = "驾照类型")
	private String drivingLicense;
    
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

	public String getProperty() {
		lazyLoad();
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getJobName() {
		lazyLoad();
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getWorkDayTemplate() {
		lazyLoad();
		return workDayTemplate;
	}

	public void setWorkDayTemplate(String workDayTemplate) {
		this.workDayTemplate = workDayTemplate;
	}

	public String getGraduateSchool() {
		lazyLoad();
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getMajor() {
		lazyLoad();
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getLinkTel() {
		lazyLoad();
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getFaxNumber() {
		lazyLoad();
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmail() {
		lazyLoad();
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getInnerEmail() {
		lazyLoad();
		return innerEmail;
	}

	public void setInnerEmail(String innerEmail) {
		this.innerEmail = innerEmail;
	}

	public String getSpecialAbility() {
		lazyLoad();
		return specialAbility;
	}

	public void setSpecialAbility(String specialAbility) {
		this.specialAbility = specialAbility;
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

	public String getWorkPosition() {
		lazyLoad();
		return workPosition;
	}

	public void setWorkPosition(String workPosition) {
		this.workPosition = workPosition;
	}

	public String getWorkMode() {
		lazyLoad();
		return workMode;
	}

	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}

	public String getEmployeePosition() {
		lazyLoad();
		return employeePosition;
	}

	public void setEmployeePosition(String employeePosition) {
		this.employeePosition = employeePosition;
	}

	public Organization getOrganization() {
		lazyLoad();
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Department getDepartment() {
		lazyLoad();
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getEmployeeType() {
		lazyLoad();
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getDimissionReason() {
		lazyLoad();
		return dimissionReason;
	}

	public void setDimissionReason(String dimissionReason) {
		this.dimissionReason = dimissionReason;
	}

	public String getSex() {
		lazyLoad();
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmergencyTel() {
		lazyLoad();
		return emergencyTel;
	}

	public void setEmergencyTel(String emergencyTel) {
		this.emergencyTel = emergencyTel;
	}

	public String getMaritalStatus() {
		lazyLoad();
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getBankCardNo() {
		lazyLoad();
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public Date getDimissionTime() {
		lazyLoad();
		return dimissionTime;
	}

	public void setDimissionTime(Date dimissionTime) {
		this.dimissionTime = dimissionTime;
	}

	public Date getGraduateTime() {
		lazyLoad();
		return graduateTime;
	}

	public void setGraduateTime(Date graduateTime) {
		this.graduateTime = graduateTime;
	}

	public String getEmergencyAddress() {
		lazyLoad();
		return emergencyAddress;
	}

	public void setEmergencyAddress(String emergencyAddress) {
		this.emergencyAddress = emergencyAddress;
	}

	public String getBankAddr() {
		lazyLoad();
		return bankAddr;
	}

	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}

	public Date getContractBeginTime() {
		lazyLoad();
		return contractBeginTime;
	}

	public void setContractBeginTime(Date contractBeginTime) {
		this.contractBeginTime = contractBeginTime;
	}

	public Date getBirthDate() {
		lazyLoad();
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getJoinCompanyType() {
		return joinCompanyType;
	}

	public void setJoinCompanyType(String joinCompanyType) {
		this.joinCompanyType = joinCompanyType;
	}

	public String getCardOfBank() {
		return cardOfBank;
	}

	public void setCardOfBank(String cardOfBank) {
		this.cardOfBank = cardOfBank;
	}

	public Date getRegularizationTime() {
		return regularizationTime;
	}

	public void setRegularizationTime(Date regularizationTime) {
		this.regularizationTime = regularizationTime;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public Date getMarriedTime() {
		return marriedTime;
	}

	public void setMarriedTime(Date marriedTime) {
		this.marriedTime = marriedTime;
	}

	public String getOnJobState() {
		return onJobState;
	}

	public void setOnJobState(String onJobState) {
		this.onJobState = onJobState;
	}

	public String getIdentityCardNo() {
		return identityCardNo;
	}

	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}

	public Date getJoinCompanyTime() {
		return joinCompanyTime;
	}

	public void setJoinCompanyTime(Date joinCompanyTime) {
		this.joinCompanyTime = joinCompanyTime;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public Date getContractSignTime() {
		return contractSignTime;
	}

	public void setContractSignTime(Date contractSignTime) {
		this.contractSignTime = contractSignTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getContractEndTime() {
		return contractEndTime;
	}

	public void setContractEndTime(Date contractEndTime) {
		this.contractEndTime = contractEndTime;
	}

	public String getPoliticStatus() {
		return politicStatus;
	}

	public void setPoliticStatus(String politicStatus) {
		this.politicStatus = politicStatus;
	}

	public String getDimissionType() {
		return dimissionType;
	}

	public void setDimissionType(String dimissionType) {
		this.dimissionType = dimissionType;
	}

	public String getLastedEducationBackground() {
		return lastedEducationBackground;
	}

	public void setLastedEducationBackground(String lastedEducationBackground) {
		this.lastedEducationBackground = lastedEducationBackground;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
