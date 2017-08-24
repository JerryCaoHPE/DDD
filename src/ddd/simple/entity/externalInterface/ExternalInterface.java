package ddd.simple.entity.externalInterface;

import ddd.base.annotation.Column;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="外部接口",name="DD_externalInterface")
public class ExternalInterface extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="配置",name="configJson",length=2000,nullable=false,unique=false,comment="json格式")
	private String configJson;

	@Column(label="唯一标识",name="externalInterfaceKey",length=50,nullable=false,unique=true,uniqueName="externalInterfaceKey",comment="必须填")
	private String externalInterfaceKey;

	@Column(label="执行类",name="executeClass",length=200,nullable=false,unique=false,comment="精确到包")
	private String executeClass;


	public String getConfigJson() {
		lazyLoad();
		return this.configJson;
	}

	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}

	public String getExternalInterfaceKey() {
		lazyLoad();
		return this.externalInterfaceKey;
	}

	public void setExternalInterfaceKey(String key) {
		this.externalInterfaceKey = key;
	}

	public String getExecuteClass() {
		lazyLoad();
		return this.executeClass;
	}

	public void setExecuteClass(String executeClass) {
		this.executeClass = executeClass;
	}


	
}