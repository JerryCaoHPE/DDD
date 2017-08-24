package ddd.simple.action.operationManual;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operationManual.Catalog;
import ddd.simple.service.operationManual.CatalogService;

@Action
@RequestMapping("/Catalog")
@Controller
public class CatalogAction {
	@Resource(name="catalogServiceBean")
	private CatalogService catalogService;
	
	public Catalog saveCatalog(Catalog catalog) throws Exception{
		try {
			Catalog saveCatalog = this.catalogService.saveCatalog(catalog);
			return saveCatalog;
		} catch (DDDException e) {
			throw e;
		}		
	}
	
	public void deleteCatalog(Long catalogId) throws Exception{
		try {
			this.catalogService.deleteCatalog(catalogId);
		} catch (DDDException e) {
			throw e;
		}
		
	}
	
	public Catalog updateCatalog(Catalog catalog) throws Exception {
		try {
			Catalog updateCatalog = this.catalogService.updateCatalog(catalog);
			return updateCatalog;
		}catch (DDDException e) {
			throw e;
		}
	}
	
	public Catalog findCatalogById(Long catalogId) throws Exception {
		try {
			Catalog findCatalogById = this.catalogService.findCatalogById(catalogId);
			return findCatalogById;
		}catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Catalog> findCatalogs() throws Exception {
		try{
			EntitySet<Catalog> findCatalogs = this.catalogService.findCatalogs();
			return findCatalogs;
		} catch (DDDException e) {
			throw e;
		}
	}
	public EntitySet<Catalog> getSystemCatalogs() throws Exception {
		try{
			EntitySet<Catalog> getSystemCatalogs = this.catalogService.getSystemCatalogs();
			return getSystemCatalogs;
		} catch (DDDException e) {
			throw e;
		}
	}
}
