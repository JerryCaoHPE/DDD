package ddd.base.exporter;

import ddd.base.persistence.baseEntity.Entity;

public class ConfigEntity {
	
	private Config config;
	
	private Entity  entity;

	public ConfigEntity(Config config, Entity entity) {
		this.config = config;
		this.entity = entity;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	

	

}
