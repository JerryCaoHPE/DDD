package ddd.simple.service.systemConfig.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.systemConfig.SystemConfig;
import ddd.simple.dao.systemConfig.SystemConfigDao;
import ddd.simple.service.systemConfig.SystemConfigService;

@Service
public class SystemConfigServiceBean extends BaseService implements SystemConfigService
{

	@Resource(name="systemConfigDaoBean")
	private SystemConfigDao systemConfigDao;
	
	@Override
	public SystemConfig saveSystemConfig(SystemConfig systemConfig) 
	{
		try {
			return this.systemConfigDao.saveSystemConfig(systemConfig);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveSystemConfig", e.getMessage(), e);
		}
	}

	@Override
	public int deleteSystemConfig(Long systemConfigId) {
		try {
			return this.systemConfigDao.deleteSystemConfig(systemConfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteSystemConfig", e.getMessage(), e);
		}
		
	}

	@Override
	public SystemConfig updateSystemConfig(SystemConfig systemConfig) {
		try {
			return this.systemConfigDao.updateSystemConfig(systemConfig);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateSystemConfig", e.getMessage(), e);
		}
	}

	@Override
	public SystemConfig findSystemConfigById(Long systemConfigId) {
		try {
			return this.systemConfigDao.findSystemConfigById(systemConfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findSystemConfigById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<SystemConfig> findAllSystemConfig() {
		try{
			return this.systemConfigDao.findAllSystemConfig();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllSystemConfig", e.getMessage(), e);
		}
	}
	
	public Map<String,Object> findSystemConfigs()
	{
		Map<String,Object> configMap = new HashMap<String,Object>();
		EntitySet<SystemConfig> configSet = this.findAllSystemConfig();
		
		for (SystemConfig str : configSet) {  
			configMap.put(str.getSystemConfigKey(), str.getSystemConfigValue());
		}  
		
		return configMap;
	}

	@Override
	public SystemConfig findSystemConfigByKey(String key) {
		try {
			return this.systemConfigDao.findSystemConfigByKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findSystemConfigByKey", e.getMessage(), e);
		}
	}
	
	public String findSystemConfigValueBykey(String key)
	{
		try {
			SystemConfig systemConfig = this.systemConfigDao.findSystemConfigByKey(key);
			if(systemConfig!=null){
				return systemConfig.getSystemConfigValue();
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findSystemConfigValueBykey", e.getMessage(), e);
		}
	}

}
