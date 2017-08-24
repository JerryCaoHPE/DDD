package ddd.simple.action.outInterface;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.outInterface.OutInterfaceConfig;
import ddd.simple.service.outInterface.OutInterfaceConfigService;

@Action
@RequestMapping("/OutInterfaceConfig")
@Controller
public class OutInterfaceConfigAction
{
	@Resource(name="outInterfaceConfigServiceBean")
	private OutInterfaceConfigService outInterfaceConfigService;
	
	public OutInterfaceConfig saveOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig)
	{
		try {
			OutInterfaceConfig saveOutInterfaceConfig = this.outInterfaceConfigService.saveOutInterfaceConfig(outInterfaceConfig);
			return saveOutInterfaceConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteOutInterfaceConfig(Long outInterfaceConfigId){
		
		try {
			return this.outInterfaceConfigService.deleteOutInterfaceConfig(outInterfaceConfigId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public OutInterfaceConfig updateOutInterfaceConfig(OutInterfaceConfig outInterfaceConfig) {
		try {
			OutInterfaceConfig updateOutInterfaceConfig = this.outInterfaceConfigService.updateOutInterfaceConfig(outInterfaceConfig);
			return updateOutInterfaceConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public OutInterfaceConfig findOutInterfaceConfigById(Long outInterfaceConfigId){
		try {
			OutInterfaceConfig findOutInterfaceConfig = this.outInterfaceConfigService.findOutInterfaceConfigById(outInterfaceConfigId);
			return  findOutInterfaceConfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<OutInterfaceConfig> findAllOutInterfaceConfig(){
		try{
			EntitySet<OutInterfaceConfig> allOutInterfaceConfig = this.outInterfaceConfigService.findAllOutInterfaceConfig();
			return allOutInterfaceConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

}