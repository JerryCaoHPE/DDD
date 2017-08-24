package ddd.simple.service.operationManual;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Catalog;

public interface CatalogService {
	
	public Catalog saveCatalog(Catalog catalog) throws Exception;
	
	public void deleteCatalog(Long catalogId) throws Exception;
	
	public Catalog updateCatalog(Catalog catalog) throws Exception;
	
	public Catalog findCatalogById(Long catalogId) throws Exception;
	
	public EntitySet<Catalog> findCatalogs() throws Exception;

	public EntitySet<Catalog> getSystemCatalogs() throws Exception;
}
