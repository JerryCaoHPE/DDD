package ddd.simple.dao.importConfigs;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.importConfigs.ImportConfigs;

public interface ImportConfigsDao extends BaseDaoInterface
{
	public ImportConfigs saveImportConfigs(ImportConfigs importConfigs) throws Exception;
	
	public int deleteImportConfigs(Long importConfigsId) throws Exception;
	
	public ImportConfigs updateImportConfigs(ImportConfigs importConfigs) throws Exception;
	
	public ImportConfigs findImportConfigsById(Long importConfigsId) throws Exception;
	
	public EntitySet<ImportConfigs> findAllImportConfigs() throws Exception;

	public ImportConfigs findImportConfigsByKey(String importConfigKey) throws Exception;
}
