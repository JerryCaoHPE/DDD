package ddd.simple.service.externalInterface;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.externalInterface.ExternalInterface;

public interface ExternalInterfaceService extends BaseServiceInterface
{
	public ExternalInterface saveExternalInterface(ExternalInterface externalInterface);
	
	public int deleteExternalInterface(Long externalInterfaceId);
	
	public ExternalInterface updateExternalInterface(ExternalInterface externalInterface);
	
	public ExternalInterface findExternalInterfaceById(Long externalInterfaceId);
	
	public EntitySet<ExternalInterface> findAllExternalInterface();
	
	public ExternalInterface findExternalInterfaceByKey(String key);
	
}