package ddd.simple.dao.externalInterface.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.externalInterface.ExternalInterface;
import ddd.simple.dao.externalInterface.ExternalInterfaceDao;

@Service
public class ExternalInterfaceDaoBean extends BaseDao implements ExternalInterfaceDao
{
	@Override
	public ExternalInterface saveExternalInterface(ExternalInterface externalInterface)  throws Exception{
		return this.save(externalInterface);
	}

	@Override
	public int deleteExternalInterface(Long externalInterfaceId)  throws Exception{
		return this.deleteById(externalInterfaceId,ExternalInterface.class);
	}

	@Override
	public ExternalInterface updateExternalInterface(ExternalInterface externalInterface)  throws Exception{
		return this.update(externalInterface);
	}

	@Override
	public ExternalInterface findExternalInterfaceById(Long externalInterfaceId)  throws Exception{
		return this.query(externalInterfaceId, ExternalInterface.class);
	}
	
	@Override
	public EntitySet<ExternalInterface> findAllExternalInterface() throws Exception {
		return this.query("1=1",ExternalInterface.class);
	}

	@Override
	public ExternalInterface findExternalInterfaceByKey(String key) throws Exception
	{
		return this.queryOne("externalInterfaceKey = '"+key+"'",ExternalInterface.class);
	}
}
