package ddd.simple.service.operationManual.imp;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.operationManual.CatalogDao;
import ddd.simple.entity.operationManual.Catalog;
import ddd.simple.service.operationManual.CatalogService;

@Service
public class CatalogServiceBean implements CatalogService{

	@Resource(name="catalogDaoBean")
	private CatalogDao catalogDao;
  
	public Catalog saveCatalog(Catalog catalog) throws Exception {
		try {
			if("否".equals(catalog.getCatalogType()))
			{
				String catalogCode = this.getCatalogCode(catalog.getParent().getEId());
				catalog.setCode(catalogCode);
			}
			 else if("是".equals(catalog.getCatalogType()))
			{
				EntitySet<Catalog> systemCatalogs = this.getSystemCatalogs();
				String systemCatalogCode = this.getLastFourCode(systemCatalogs);
				catalog.setCode(systemCatalogCode);
			}
			
			Catalog newCatalog = this.catalogDao.saveCatalog(catalog);
			
			return newCatalog;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveCatalog", e.getMessage(), e);
		}
	}
	
	public void deleteCatalog(Long catalogId) throws Exception{
		try{
			this.catalogDao.deleteCatalog(catalogId);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteCatalog",e.getMessage(),e);
		}
		
	}

	public Catalog updateCatalog(Catalog catalog) throws Exception {
		try {
			Catalog newCatalog = new Catalog();
			
			if("否".equals(catalog.getCatalogType()))
			{
				Catalog parentCatalog = catalog.getParent();
				Catalog findThisCatalog = this.findCatalogById(catalog.getEId()); 
				//如果前台传过来的模块的父模块没有变化，那么直接保存
				if(parentCatalog.getEId().equals(findThisCatalog.getParent().getEId()))
				{
					newCatalog = this.catalogDao.updateCatalog(catalog);
				}
				else
				{
					Catalog findParentCatalog = this.findCatalogById(parentCatalog.getEId());
					Catalog changCodeCatalog = this.setUpdateCode(findParentCatalog, catalog);
					changCodeCatalog.setParent(findParentCatalog);
					newCatalog = this.catalogDao.updateCatalog(changCodeCatalog);
				}
			}
			else if("是".equals(catalog.getCatalogType()))
			{
				Catalog findThisSystemCatalog = this.findCatalogById(catalog.getEId());
				if(findThisSystemCatalog.getParent()== null)
				{
					newCatalog = this.catalogDao.updateCatalog(catalog);
				}
				else 
				{
					EntitySet<Catalog> systemCatalogs = this.getSystemCatalogs();
					String newParentCode = this.getLastFourCode(systemCatalogs);
					catalog.setCode(newParentCode);
					EntitySet<Catalog> childCatalogs = this.setUpdateChildCode(findThisSystemCatalog, newParentCode);
					catalog.setChildren(childCatalogs);
					
					newCatalog = this.catalogDao.updateCatalog(catalog);
				}
			}
			
		
			return newCatalog;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateCatalog", e.getMessage(), e);
		}
	}
	
	public Catalog findCatalogById(Long catalogId) throws Exception{
		try {
			Catalog catalog = this.catalogDao.findCatalogById(catalogId);
			catalog.getParent();
			EntitySet<Catalog> catalogs = (EntitySet<Catalog>) catalog.getChildren();
			for(Catalog childCatalog:catalogs)
			{
				childCatalog.getChildren();
			}
			return catalog;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findCatalogById", e.getMessage(), e);
		}
	}
	
	public EntitySet<Catalog> findCatalogs() throws Exception{
		try{
			EntitySet<Catalog> catalogs = this.catalogDao.findCatalogs();			
			for(Catalog catalog:catalogs)
			{
				EntitySet<Catalog> childCatalogs = (EntitySet<Catalog>) catalog.getChildren();
				for (Catalog childCatalog : childCatalogs) {
					childCatalog.getChildren();
				}
			}
			return catalogs;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findCatalogs", e.getMessage(), e);
		}
	}	
	
	
	 public String getCatalogCode(Long catalogId) throws Exception
	 {
		 try {
			 Catalog parentCatalog = this.findCatalogById(catalogId);
			 	EntitySet<Catalog> childrenCatalogs = (EntitySet<Catalog>)parentCatalog.getChildren();
			 
			 	String maxLastFourCode = this.getLastFourCode(childrenCatalogs); 
			 	
			 	String maxCode = parentCatalog.getCode()+maxLastFourCode;
			 	
				return maxCode;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getCatalogCode", e.getMessage(), e);
		}
	 }
	
	 public String getLastFourCode(EntitySet<Catalog> catalogs) throws Exception
	 {
		 try {
			 Integer maxCode = this.findMaxCatalogCode(catalogs);
			 Integer useCode = maxCode+1;
			 Integer useCodeLength = useCode.toString().length();
			 String finalLastFourCode = "";
			 if(useCodeLength == 1)
			 {
				 finalLastFourCode+="000"+useCode;
			 }
			 else if(useCodeLength == 2)
			 {
				 finalLastFourCode+="00"+useCode;
			 }
			 else if(useCodeLength == 3)
			 {
				 finalLastFourCode+="0"+useCode;
			 }
			 else if(useCodeLength == 4)
			 {
				 finalLastFourCode+=useCode;
			 }
			 
			 return finalLastFourCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getLastFourCode", e.getMessage(), e);
		}
	 }
	
	 public Integer findMaxCatalogCode(EntitySet<Catalog> catalogs) throws Exception
	 {
		 try {
			EntitySet<Catalog> getCodeCatalogs = catalogs;
			ArrayList<String> codeNumberString = new ArrayList<String>();
			for (Catalog catalog : getCodeCatalogs)
			{
				String catalogCode = catalog.getCode();
				String codeNumber = catalogCode.substring(catalogCode.length()-4, catalogCode.length());
				codeNumberString.add(codeNumber);
			};
			
			Collections.sort(codeNumberString);
			
			int maxCode = 0;
			if(codeNumberString.size() != 0)
			{
				maxCode = Integer.parseInt(codeNumberString.get(codeNumberString.size()-1));
			}
			return maxCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findMaxCatalogCode", e.getMessage(), e);
		}
	 }
	
	 public EntitySet<Catalog> getSystemCatalogs() throws Exception
		{
			try {
				
				EntitySet<Catalog> catalogs = this.findCatalogs();
				EntitySet<Catalog> systemCatalogs = new EntitySet<Catalog>();
				
				for (Catalog catalog : catalogs)
				{
					if("是".equals(catalog.getCatalogType()))
					{
						systemCatalogs.add(catalog);
					}
				}
				return systemCatalogs;
			} catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("getSystemCatalogs", e.getMessage(), e);
			}
		}
	 
	 public Catalog setUpdateCode(Catalog parentCatalog,Catalog catalog) throws Exception
	 {
		 try {
			 Catalog newCatalog = catalog; 
			 String oldParentCode = parentCatalog.getCode();
			 String newCode = this.getChangeCode(parentCatalog,oldParentCode);
		   	 
			 newCatalog.setCode(newCode);
			 EntitySet<Catalog> childrenCatalogs = this.setUpdateChildCode(newCatalog, newCode);
			 newCatalog.setChildren(childrenCatalogs);
		   	 return newCatalog;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("setUpdateCode", e.getMessage(), e);
		}
	 }
	 
	 public String getChangeCode(Catalog parentCatalog,String parentCode) throws Exception
	 {
		 try {
			
			 String lastFourCode = this.getLastFourCode((EntitySet<Catalog>)parentCatalog.getChildren());
			 String newCode = parentCode+lastFourCode;
			 
			 return newCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getChangeCode", e.getMessage(), e);
		}
	 }
	 
	 public EntitySet<Catalog> setUpdateChildCode(Catalog catalog,String newParentCode) throws Exception
	 {
		 try {
			 EntitySet<Catalog> childrenCatalogs = new EntitySet<Catalog>();
			 if(catalog.getChildren().size() != 0)
			 {
				childrenCatalogs = (EntitySet<Catalog>)(this.findCatalogById(catalog.getEId()).getChildren());
				
				for (Catalog childCatalog : childrenCatalogs)
				{
					String newCode = this.getChangeChildCode(newParentCode, childCatalog);
					childCatalog.setCode(newCode);
				}
			 }
			
			
			EntitySet<Catalog> newChildrenCatalogs = this.catalogDao.updateChildrenCatalogCode(childrenCatalogs);
			
			return newChildrenCatalogs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("setUpdateChildCode", e.getMessage(), e);
		}
	 }
	 
	 public String getChangeChildCode(String newParentCode,Catalog childCatalog) throws Exception
	 {
		 try {
			 String oldChildCode = childCatalog.getCode();
			 String lastFourCode = oldChildCode.substring(oldChildCode.length()-4,oldChildCode.length());
			 String newChildCode = newParentCode + lastFourCode;
			 return newChildCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getChangeChildCode", e.getMessage(), e);
		}
	 }
	 
	 
	 
//	public Catalog saveCatalog(Catalog catalog) {
//		try {
//			return this.catalogDao.saveCatalog(catalog);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DDDException("saveCatalog", e.getMessage(), e);
//		}
//	}
//
//	public void deleteCatalog(Long catalogId) {
//		try{
//			System.out.println("到此一有");
//			this.catalogDao.deleteCatalog(catalogId);
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new DDDException("deleteCatalog",e.getMessage(),e);
//		}
//		
//	}
//
//	public Catalog updateCatalog(Catalog catalog) {
//		try {
//			return this.catalogDao.updateCatalog(catalog);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DDDException("updateCatalog", e.getMessage(), e);
//		}
//	}
//
//	public Catalog findCatalogById(Long catalogId) {
//		try {
//			return this.catalogDao.findCatalogById(catalogId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DDDException("findCatalogById", e.getMessage(), e);
//		}
//	}
//
//	public EntitySet<Catalog> findCatalogs() {
//		try {
//			return this.catalogDao.findCatalogs();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DDDException("findCatalogs", e.getMessage(), e);
//		}
//	}
		
}
