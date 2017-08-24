package ddd.simple.entity.operatorCongfigModel;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(label="个性化参数",name="operatorConfig")
public class OperatorCongfig extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="操作员类型",name="operatorType",comment="操作员类型：operator:操作员，member:会员")
	private String operatorType;
	
	@Column(label="操作员ID",name="operatorId",nullable=false)
	private Long operatorId;

	@Column(label="个性化参数KEY",name="cCkey")
	private String cCKey;

	@Column(label="值",length=8000)
	private String characterParams;

	public Long getOperatorId() {
		lazyLoad();
		return this.operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getCCKey() {
		lazyLoad();
		return this.cCKey;
	}

	public void setCCKey(String cCKey) {
		this.cCKey = cCKey;
	}

	public String getCharacterParams() {
		lazyLoad();
		return this.characterParams;
	}

	public void setCharacterParams(String characterParams) {
		this.characterParams = characterParams;
	}

	/** 
	* @return operatorType 
	*/ 
	
	public String getOperatorType()
	{
		return operatorType;
	}

	/** 
	* @param operatorType 要设置的 operatorType 
	*/
	
	public void setOperatorType(String operatorType)
	{
		this.operatorType = operatorType;
	}
	
}