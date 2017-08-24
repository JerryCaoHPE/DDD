package ddd.simple.dao.operationManual;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Manual;

public interface ManualDao {
	
	public Manual saveManual(Manual manual) throws Exception;
	
	public  void deleteManual( Long manualId) throws Exception;
	
	public Manual updateManual(Manual manual) throws Exception;
	
	public Manual findManualById( Long manualId) throws Exception;

	public EntitySet<Manual> findManuals() throws Exception;

	public EntitySet<Manual> findManualByCatalogId(Long catalogId) throws Exception;

}
