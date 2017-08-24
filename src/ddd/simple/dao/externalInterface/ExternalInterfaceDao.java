package ddd.simple.dao.externalInterface;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.externalInterface.ExternalInterface;

public interface ExternalInterfaceDao extends BaseDaoInterface {
	public ExternalInterface saveExternalInterface(ExternalInterface externalInterface) throws Exception;
	
	public int deleteExternalInterface(Long externalInterfaceId) throws Exception;
	
	public ExternalInterface updateExternalInterface(ExternalInterface externalInterface) throws Exception;
	
	public ExternalInterface findExternalInterfaceById(Long externalInterfaceId) throws Exception;
	
	public EntitySet<ExternalInterface> findAllExternalInterface() throws Exception;
	
	public ExternalInterface findExternalInterfaceByKey(String key) throws Exception;
}
