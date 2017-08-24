package ddd.simple.entity.codeTable;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


/**
 * 单位 
 * @author 龚翔
 */
@ddd.base.annotation.Entity(name="codeTable",label="码表")
public class CodeTable extends Entity implements Serializable {

	
	private static final long serialVersionUID = 1392524698901L;


	/**码表代码*/
	@Column(label="码表代码")
	private String code;

	@Column(label="码表名称")
	private String name;
	
	@Column(label="值")
	private String value;

	@Column(label = "码表类型",name="codeTypeId",composition=true,FKName="CTable_FK_CType")
	private CodeType codeType;
    
/*	
	@Column(label="启用标识")
	private Boolean isEnabled = true; //启用标志
*/	
	@Column(label="子系统标识")
	private String subSystemID;       //子系统标识
	
	@Column(label = "父级代号", composition=true,name="parentId",FKName="CTabel_FK_CTabel")
	private CodeTable parent;
    
	@Column(label="显示顺序")
	private Integer displayOrder;     //显示序号
	
/*	*//**下级*//*
	@Column(label="是否有孩子节点")
	private Boolean hasChildren;*/
	
	@Column(name="level_",label = "等级")
	private Integer level;
	
	@Column(label = "修改序号")
	private String modifyOrder = null;       //修改序号  *
	
	@Column(label="所处层次")
	private Integer tier = null;         //代码层次  *
	
	@Column(label="下发标识")
	private String deliverIdentiter = null;  //下发标志 	*    
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CodeType getCodeType() {
		return codeType;
	}

	public void setCodeType(CodeType codeType) {
		this.codeType = codeType;
	}

	public CodeTable getParent() {
		return parent;
	}

	public void setParent(CodeTable parent) {
		this.parent = parent;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	/*public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}*/

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

/*	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}*/

	public String getSubSystemID() {
		return subSystemID;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public String getModifyOrder() {
		return modifyOrder;
	}

	public void setModifyOrder(String modifyOrder) {
		this.modifyOrder = modifyOrder;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public String getDeliverIdentiter() {
		return deliverIdentiter;
	}

	public void setDeliverIdentiter(String deliverIdentiter) {
		this.deliverIdentiter = deliverIdentiter;
	}
    
	
}
