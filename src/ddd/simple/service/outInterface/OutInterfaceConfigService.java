package ddd.simple.service.outInterface;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.outInterface.OutInterfaceConfig;
import ddd.simple.service.base.BaseServiceInterface;

public interface OutInterfaceConfigService extends BaseServiceInterface
{
	public OutInterfaceConfig saveOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) ;
	
	public int deleteOutInterfaceConfig(Long outInterfaceConfigId) ;
	
	public OutInterfaceConfig updateOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) ;
	
	public OutInterfaceConfig findOutInterfaceConfigById(Long outInterfaceConfigId) ;
	
	public OutInterfaceConfig findOutInterfaceConfigByName(String configName) ;
	
	public EntitySet<OutInterfaceConfig> findAllOutInterfaceConfig() ;
 
}