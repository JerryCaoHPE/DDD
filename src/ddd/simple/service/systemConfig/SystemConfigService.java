package ddd.simple.service.systemConfig;

import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.systemConfig.SystemConfig;

public interface SystemConfigService extends BaseServiceInterface
{
	public SystemConfig saveSystemConfig(SystemConfig systemConfig) ;
	
	public int deleteSystemConfig(Long systemConfigId) ;
	
	public SystemConfig updateSystemConfig(SystemConfig systemConfig) ;
	
	public SystemConfig findSystemConfigById(Long systemConfigId) ;
	
	public EntitySet<SystemConfig> findAllSystemConfig() ;

	public SystemConfig findSystemConfigByKey(String key);

	public String findSystemConfigValueBykey(String key);
	
	public Map<String,Object> findSystemConfigs();
}