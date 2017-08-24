package ddd.simple.entity.exporterConfig;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="exporterConfig",label="数据导入导出配置")
public class ExporterConfig extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;


	@Column(label="名称")
	private String configName;
	
	@Column(length = 8000, label="配置详细")
	private String config;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
	
	
}
