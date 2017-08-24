package ddd.simple.dao.operationManual.imp;


import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.operationManual.CatalogDao;
import ddd.simple.entity.operationManual.Catalog;

@Service
public class CatalogDaoBean extends BaseDao implements CatalogDao{

	public Catalog saveCatalog(Catalog catalog)throws Exception{
		return this.save(catalog);
	}

	public void deleteCatalog(Long catalogId) throws Exception {
		this.deleteById(catalogId,Catalog.class);
	}

	public Catalog updateCatalog(Catalog catalog) throws Exception{
		return this.update(catalog);
	}

	public Catalog findCatalogById(Long catalogId) throws Exception{
		return this.query(catalogId, Catalog.class);
	}

	public EntitySet<Catalog> findCatalogs() throws Exception{
		return (EntitySet<Catalog>) this.query("1=1", Catalog.class);
	}
	
	@Override
	public EntitySet<Catalog> updateChildrenCatalogCode(EntitySet<Catalog> childrenCatalogs) 
	  throws Exception{
		return (EntitySet<Catalog>)this.update(childrenCatalogs);
	}
	
}
