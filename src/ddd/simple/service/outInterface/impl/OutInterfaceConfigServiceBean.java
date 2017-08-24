package ddd.simple.service.outInterface.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.outInterface.OutInterfaceConfigDao;
import ddd.simple.entity.outInterface.OutInterfaceConfig;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.outInterface.OutInterfaceConfigService;

@Service
public class OutInterfaceConfigServiceBean extends BaseService implements OutInterfaceConfigService
{

	@Resource(name="outInterfaceConfigDaoBean")
	private OutInterfaceConfigDao outInterfaceConfigDao;
	
	@Override
	public OutInterfaceConfig saveOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) 
	{
		try {
			return this.outInterfaceConfigDao.saveOutInterfaceConfig(outInterfaceConfig);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveOutInterfaceConfig", e.getMessage(), e);
		}
	}

	@Override
	public int deleteOutInterfaceConfig(Long outInterfaceConfigId) {
		try {
			return this.outInterfaceConfigDao.deleteOutInterfaceConfig(outInterfaceConfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteOutInterfaceConfig", e.getMessage(), e);
		}
		
	}

	@Override
	public OutInterfaceConfig updateOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) {
		try {
			return this.outInterfaceConfigDao.updateOutInterfaceConfig(outInterfaceConfig);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateOutInterfaceConfig", e.getMessage(), e);
		}
	}

	@Override
	public OutInterfaceConfig findOutInterfaceConfigById(Long outInterfaceConfigId) {
		try {
			return this.outInterfaceConfigDao.findOutInterfaceConfigById(outInterfaceConfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOutInterfaceConfigById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<OutInterfaceConfig> findAllOutInterfaceConfig() {
		try{
			return this.outInterfaceConfigDao.findAllOutInterfaceConfig();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllOutInterfaceConfig", e.getMessage(), e);
		}
	}

	/* (非 Javadoc) 
	* <p>Title: findOutInterfaceConfigByName</p> 
	* <p>Description: </p> 
	* @param configName
	* @return 
	* @see ddd.simple.service.outInterface.OutInterfaceConfigService#findOutInterfaceConfigByName(java.lang.String) 
	*/
	@Override
	public OutInterfaceConfig findOutInterfaceConfigByName(String configName)
	{
		return null;
	}

}
