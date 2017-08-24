package ddd.simple.action.operationManual;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Manual;
import ddd.simple.service.operationManual.ManualService;

@Action
@RequestMapping("/Manual")
@Controller
public class ManualAction {
	
	@Resource(name="manualServiceBean")
	private ManualService manualService;
	
	public Manual saveManual(Manual manual) {
		try {
			Manual saveManual = this.manualService.saveManual(manual);
			return saveManual;
		}catch (DDDException e) {
			throw e;
		}		
	}
	
	public  void deleteManual( Long manualId) {
		try {
			this.manualService.deleteManual(manualId);
		}catch (DDDException e) {
			throw e;
		}		
	}
	
	public Manual updateManual(Manual manual) {
		try {
			Manual updateManual = this.manualService.updateManual(manual);
			return updateManual;
		}catch (DDDException e) {
			throw e;
		}	
	}
	
	public Manual findManualById( Long manualId) {
		try {
			Manual findManualById = this.manualService.findManualById(manualId);
			return findManualById;
		}catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Manual> findManuals() {
		try {
			EntitySet<Manual> findManuals = this.manualService.findManuals();
			return findManuals;
		}catch (DDDException e) {
			throw e;
		}
	}
	public EntitySet<Manual> findManualByCatalogId(Long catalogId) throws Exception{
		try {
			EntitySet<Manual> manuals = this.manualService.findManualByCatalogId(catalogId);
			return manuals;
		} catch (Exception e) {
			throw e;
		}
	}
}
