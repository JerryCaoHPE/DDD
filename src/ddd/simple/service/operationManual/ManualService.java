package ddd.simple.service.operationManual;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Manual;

public interface ManualService {
	
	public Manual saveManual(Manual manual);
	
	public  void deleteManual( Long manualId);
	
	public Manual updateManual(Manual manual);
	
	public Manual findManualById( Long manualId);

	public EntitySet<Manual> findManuals();

	public EntitySet<Manual> findManualByCatalogId(Long catalogId);

}
