package ddd.simple.dao.systemConfig;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.systemConfig.SystemConfig;

public interface SystemConfigDao extends BaseDaoInterface
{
	public SystemConfig saveSystemConfig(SystemConfig systemConfig) throws Exception;
	
	public int deleteSystemConfig(Long systemConfigId) throws Exception;
	
	public SystemConfig updateSystemConfig(SystemConfig systemConfig) throws Exception;
	
	public SystemConfig findSystemConfigById(Long systemConfigId) throws Exception;
	
	public EntitySet<SystemConfig> findAllSystemConfig() throws Exception;

	public SystemConfig findSystemConfigByKey(String key) throws Exception;
}
