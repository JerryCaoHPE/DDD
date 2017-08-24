package ddd.simple.action.systemConfig;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.systemConfig.SystemConfig;
import ddd.simple.service.systemConfig.SystemConfigService;

@Action
@RequestMapping("/SystemConfig")
@Controller
public class SystemConfigAction
{
	@Resource(name="systemConfigServiceBean")
	private SystemConfigService systemConfigService;
	
	public SystemConfig saveSystemConfig(SystemConfig systemConfig)
	{
		try {
			SystemConfig saveSystemConfig = this.systemConfigService.saveSystemConfig(systemConfig);
			return saveSystemConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteSystemConfig(Long systemConfigId){
		
		try {
			return this.systemConfigService.deleteSystemConfig(systemConfigId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public SystemConfig updateSystemConfig(SystemConfig systemConfig)
	{
		try
		{
			SystemConfig updateSystemConfig = this.systemConfigService.updateSystemConfig(systemConfig);
			return updateSystemConfig;
		}
		catch (DDDException e)
		{
			throw e;
		}
	}

	public SystemConfig findSystemConfigById(Long systemConfigId){
		try {
			SystemConfig findSystemConfig = this.systemConfigService.findSystemConfigById(systemConfigId);
			return  findSystemConfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public SystemConfig findSystemConfigValueBykey(String key){
		try {
			SystemConfig findSystemConfig = this.systemConfigService.findSystemConfigByKey(key);
			return  findSystemConfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Map<String,String> findAllSystemConfig(){
		try{
			EntitySet<SystemConfig> allSystemConfig = this.systemConfigService.findAllSystemConfig();
			
			Map<String,String> systemConfigs = new HashMap<String,String>();
			
			for(SystemConfig config:allSystemConfig)
			{
				systemConfigs.put(config.getSystemConfigKey(),config.getSystemConfigValue());
			}
			
			return systemConfigs;
		} catch (DDDException e) {
			throw e;
		}
	}

}