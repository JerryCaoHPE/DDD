package ddd.simple.dao.outInterface;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.outInterface.OutInterfaceConfig;

public interface OutInterfaceConfigDao extends BaseDaoInterface
{
	public OutInterfaceConfig saveOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) throws Exception;
	
	public int deleteOutInterfaceConfig(Long outInterfaceConfigId) throws Exception;
	
	public OutInterfaceConfig updateOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) throws Exception;
	
	public OutInterfaceConfig findOutInterfaceConfigById(Long outInterfaceConfigId) throws Exception;
	
	public EntitySet<OutInterfaceConfig> findAllOutInterfaceConfig() throws Exception;
}
