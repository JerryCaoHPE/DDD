package ddd.simple.service.operationManual.imp;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.operationManual.ManualDao;
import ddd.simple.entity.operationManual.Manual;
import ddd.simple.service.operationManual.ManualService;

@Service
public class ManualServiceBean implements ManualService{

	@Resource(name="manualDaoBean")
	private ManualDao manualDao;
	
	@Override
	public Manual saveManual(Manual manual) {
		try{
			return this.manualDao.saveManual(manual);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveManual",e.getMessage(),e);
		}
	}

	@Override
	public void deleteManual(Long manualId) {
		try{
			this.manualDao.deleteManual(manualId);
		}catch (Exception e){
			e.printStackTrace();
			throw new DDDException("deleteManual",e.getMessage(),e);
		}
	}

	@Override
	public Manual updateManual(Manual manual) {
		try{
			return this.manualDao.updateManual(manual);
		}catch (Exception e){
			e.printStackTrace();
			throw new DDDException("updateManual",e.getMessage(),e);
		}
	}

	@Override
	public Manual findManualById(Long manualId) {
		try{
			return this.manualDao.findManualById(manualId);
		}catch (Exception e){
			e.printStackTrace();
			throw new DDDException("findManualById",e.getMessage(),e);
		}
	}

	@Override
	public EntitySet<Manual> findManuals() {
		try {
			return this.manualDao.findManuals();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findManuals", e.getMessage(), e);
		}
	}
	public EntitySet<Manual> findManualByCatalogId(Long catalogId){
		try {
			EntitySet<Manual> manual = this.manualDao.findManualByCatalogId(catalogId);
			return manual;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findManualByCatalogId", e.getMessage(), e);
		}
	}

}
