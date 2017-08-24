package ddd.simple.service.externalInterface.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.externalInterface.ExternalInterface;
import ddd.simple.dao.externalInterface.ExternalInterfaceDao;
import ddd.simple.service.externalInterface.ExternalInterfaceService;

@Service
public class ExternalInterfaceServiceBean extends BaseService implements ExternalInterfaceService
{

	@Resource(name="externalInterfaceDaoBean")
	private ExternalInterfaceDao externalInterfaceDao;
	
	@Override
	public ExternalInterface saveExternalInterface(ExternalInterface externalInterface) 
	{
		try {
			return this.externalInterfaceDao.saveExternalInterface(externalInterface);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveExternalInterface", e.getMessage(), e);
		}
	}

	@Override
	public int deleteExternalInterface(Long externalInterfaceId) {
		try {
			return this.externalInterfaceDao.deleteExternalInterface(externalInterfaceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteExternalInterface", e.getMessage(), e);
		}
		
	}

	@Override
	public ExternalInterface updateExternalInterface(ExternalInterface externalInterface) {
		try {
			return this.externalInterfaceDao.updateExternalInterface(externalInterface);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateExternalInterface", e.getMessage(), e);
		}
	}

	@Override
	public ExternalInterface findExternalInterfaceById(Long externalInterfaceId) {
		try {
			return this.externalInterfaceDao.findExternalInterfaceById(externalInterfaceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findExternalInterfaceById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ExternalInterface> findAllExternalInterface() {
		try{
			return this.externalInterfaceDao.findAllExternalInterface();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllExternalInterface", e.getMessage(), e);
		}
	}

	@Override
	public ExternalInterface findExternalInterfaceByKey(String key)
	{
		try
		{
			return this.externalInterfaceDao.findExternalInterfaceByKey(key);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findExternalInterfaceByKey", e.getMessage(), e);
		}
	}

}
