package ddd.simple.action.externalInterface;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.externalInterface.ExternalInterface;
import ddd.simple.service.externalInterface.ExternalInterfaceEngine;
import ddd.simple.service.externalInterface.ExternalInterfaceService;

@Action
@RequestMapping("/DD/ExternalInterface")
@Controller
public class ExternalInterfaceAction
{
	@Resource(name="externalInterfaceServiceBean")
	private ExternalInterfaceService externalInterfaceService;
	
	public ExternalInterface saveExternalInterface(ExternalInterface externalInterface)
	{
		try {
			ExternalInterface saveExternalInterface = this.externalInterfaceService.saveExternalInterface(externalInterface);
			return saveExternalInterface;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteExternalInterface(Long externalInterfaceId){
		
		try {
			return this.externalInterfaceService.deleteExternalInterface(externalInterfaceId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ExternalInterface updateExternalInterface(ExternalInterface externalInterface) {
		try {
			ExternalInterface updateExternalInterface = this.externalInterfaceService.updateExternalInterface(externalInterface);
			return updateExternalInterface;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ExternalInterface findExternalInterfaceById(Long externalInterfaceId){
		try {
			ExternalInterface findExternalInterface = this.externalInterfaceService.findExternalInterfaceById(externalInterfaceId);
			return  findExternalInterface;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ExternalInterface> findAllExternalInterface(){
		try{
			EntitySet<ExternalInterface> allExternalInterface = this.externalInterfaceService.findAllExternalInterface();
			return allExternalInterface;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public boolean executeExternalInterface(ExternalInterface externalInterface){
		try{
			ExternalInterfaceEngine.execute(externalInterface.getExternalInterfaceKey());
			return true;
		} catch (DDDException e) {
			throw e;
		}
	}

}