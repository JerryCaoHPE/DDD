package ddd.simple.dao.outInterface.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.outInterface.OutInterfaceConfig;
import ddd.simple.dao.outInterface.OutInterfaceConfigDao;

@Service
public class OutInterfaceConfigDaoBean extends BaseDao implements OutInterfaceConfigDao
{
	@Override
	public OutInterfaceConfig saveOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig)  throws Exception{
		return this.save(outInterfaceConfig);
	}

	@Override
	public int deleteOutInterfaceConfig(Long outInterfaceConfigId)  throws Exception{
		return this.deleteById(outInterfaceConfigId,OutInterfaceConfig.class);
	}

	@Override
	public OutInterfaceConfig updateOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig)  throws Exception{
		return this.update(outInterfaceConfig);
	}

	@Override
	public OutInterfaceConfig findOutInterfaceConfigById(Long outInterfaceConfigId)  throws Exception{
		return this.query(outInterfaceConfigId, OutInterfaceConfig.class);
	}
	
	@Override
	public EntitySet<OutInterfaceConfig> findAllOutInterfaceConfig() throws Exception {
		return this.query("1=1",OutInterfaceConfig.class);
	}
}
