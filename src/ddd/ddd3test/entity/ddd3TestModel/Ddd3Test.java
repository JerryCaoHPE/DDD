package ddd.ddd3test.entity.ddd3TestModel;

import ddd.base.annotation.Column;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.codeTable.CodeType;
import ddd.simple.entity.organization.Department;
import ddd.simple.entity.organization.Organization;

import java.sql.Timestamp;
import java.util.Date;

@ddd.base.annotation.Entity(label="框架测试",name="ddd3Test")
public class Ddd3Test extends Entity
{
	private static final long serialVersionUID = 1L;
	

	@Column(label="姓名拼音",comment="",unique=true,uniqueName="dddd")
	private String nameSpelling;

	@Column(label="曾用名",comment="")
	private String usedName;

	@Column(label="健康状况",comment="")
	private String health;

	@Column(label="文化程度(学历)",comment="")
	private String degree;
	
	/**所属单位*/
	@Column(label="所属单位",name="organizationId")
	private Organization organization;
	
	/**码表类型*/
	@Column(label="码表类型")
	private EntitySet<CodeType> codeTypes;
	
	@Column(label="监理职级",comment="codeTable")
	private String supervision;

	@Column(label="工作单位（科级）",comment="")
	private String departmentUnits;
	

	public String getNameSpelling() {
		lazyLoad();
		return this.nameSpelling;
	}

	public void setNameSpelling(String nameSpelling) {
		this.nameSpelling = nameSpelling;
	}

	public String getUsedName() {
		lazyLoad();
		return this.usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}

	public String getHealth() {
		lazyLoad();
		return this.health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getDegree() {
		lazyLoad();
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}


	public String getSupervision() {
		lazyLoad();
		return this.supervision;
	}

	public void setSupervision(String supervision) {
		this.supervision = supervision;
	}

	public String getDepartmentUnits() {
		lazyLoad();
		return this.departmentUnits;
	}

	public void setDepartmentUnits(String departmentUnits) {
		this.departmentUnits = departmentUnits;
	}

	public EntitySet<CodeType> getCodeTypes()
	{
		lazyLoad("codeTypes");
		return codeTypes;
	}

	public void setCodeTypes(EntitySet<CodeType> codeTypes)
	{
		super.LazyFieidsLoaded.put("codeTypes", true);
		this.codeTypes = codeTypes;
	}
	public Organization getOrganization() {
		lazyLoad();
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
    
}