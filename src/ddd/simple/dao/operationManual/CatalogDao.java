package ddd.simple.dao.operationManual;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Catalog;


public interface CatalogDao {
	
	public Catalog saveCatalog(Catalog catalog) throws Exception;
	
	public void deleteCatalog(Long catalogId) throws Exception;
	
	public Catalog updateCatalog(Catalog catalog) throws Exception;
	
	public Catalog findCatalogById(Long catalogId) throws Exception;
	
	public EntitySet<Catalog> findCatalogs() throws Exception;

	public EntitySet<Catalog> updateChildrenCatalogCode(EntitySet<Catalog> childrenCatalogs) throws Exception;
}
