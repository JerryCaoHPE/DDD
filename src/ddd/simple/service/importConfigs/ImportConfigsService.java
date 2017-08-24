package ddd.simple.service.importConfigs;

import java.util.List;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.importConfigs.ConfigItem;
import ddd.simple.entity.importConfigs.ImportConfigs;
import ddd.simple.service.base.BaseServiceInterface;

public interface ImportConfigsService extends BaseServiceInterface
{
	public ImportConfigs saveImportConfigs(ImportConfigs importConfigs) ;
	
	public int deleteImportConfigs(Long importConfigsId) ;
	
	public ImportConfigs updateImportConfigs(ImportConfigs importConfigs) ;
	
	public ImportConfigs findImportConfigsById(Long importConfigsId) ;
	
	public EntitySet<ImportConfigs> findAllImportConfigs() ;

	public ImportConfigs findImportConfigByKey(String importConfigKey);
	
	public List<ConfigItem> getAllField(String entityClassStr);
	
	public void createExcelTemplate(ImportConfigs importConfigs);
	
	public void createAllExcelTemplate();
}