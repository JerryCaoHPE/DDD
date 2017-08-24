package ddd.simple.entity.systemConfig;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="systemConfig",label="系统参数")
public class SystemConfig extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="systemConfigKey",label="键")
	private String systemConfigKey;

	@Column(length = 8000, name="systemConfigValue",label="值")
	private String systemConfigValue;

	@Column(name="systemConfigType",label="类型")
	private String systemConfigType;



	public String getSystemConfigKey() {
		return systemConfigKey;
	}

	public void setSystemConfigKey(String systemConfigKey) {
		this.systemConfigKey = systemConfigKey;
	}

	public String getSystemConfigValue() {
		return systemConfigValue;
	}

	public void setSystemConfigValue(String systemConfigValue) {
		this.systemConfigValue = systemConfigValue;
	}

	public String getSystemConfigType() {
		return systemConfigType;
	}

	public void setSystemConfigType(String systemConfigType) {
		this.systemConfigType = systemConfigType;
	}
}