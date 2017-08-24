package ddd.simple.dao.systemConfig.impl;

import java.util.Set;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.systemConfig.SystemConfig;
import ddd.simple.dao.systemConfig.SystemConfigDao;

@Service
public class SystemConfigDaoBean extends BaseDao implements SystemConfigDao
{
	@Override
	public SystemConfig saveSystemConfig(SystemConfig systemConfig)  throws Exception{
		return this.save(systemConfig);
	}

	@Override
	public int deleteSystemConfig(Long systemConfigId)  throws Exception{
		return this.deleteById(systemConfigId,SystemConfig.class);
	}

	@Override
	public SystemConfig updateSystemConfig(SystemConfig systemConfig)  throws Exception{
		return this.update(systemConfig);
	}

	@Override
	public SystemConfig findSystemConfigById(Long systemConfigId)  throws Exception{
		return this.query(systemConfigId, SystemConfig.class);
	}
	
	@Override
	public EntitySet<SystemConfig> findAllSystemConfig() throws Exception {
		return this.query("",SystemConfig.class);
	}

	@Override
	public SystemConfig findSystemConfigByKey(String key) throws Exception {
		SystemConfig systemConfig = null;
		String whereSql =" systemConfigKey = '"+key+"'";
		
		Set<SystemConfig> systemConfigSet = this.query(whereSql, SystemConfig.class);
		if(systemConfigSet != null && systemConfigSet.size()>0)
		{
			systemConfig = (SystemConfig)systemConfigSet.toArray()[0];
		}
		return systemConfig;
	}
}
