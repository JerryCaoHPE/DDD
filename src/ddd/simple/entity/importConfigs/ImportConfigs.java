package ddd.simple.entity.importConfigs;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(label="导入配置",name="importConfigs")
public class ImportConfigs extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="唯一标识",comment="")
	private String importConfigKey;

	@Column(label="名称",comment="")
	private String importConfigName;

	@Column(label="配置",length=8000,comment="")
	private String configContext;


	public String getImportConfigKey() {
		lazyLoad();
		return this.importConfigKey;
	}

	public void setImportConfigKey(String importConfigKey) {
		this.importConfigKey = importConfigKey;
	}

	public String getConfigContext() {
		lazyLoad();
		return this.configContext;
	}

	public void setConfigContext(String configContext) {
		this.configContext = configContext;
	}

	public String getImportConfigName() {
		lazyLoad();
		return importConfigName;
	}

	public void setImportConfigName(String importConfigName) {
		this.importConfigName = importConfigName;
	}
	
	
}