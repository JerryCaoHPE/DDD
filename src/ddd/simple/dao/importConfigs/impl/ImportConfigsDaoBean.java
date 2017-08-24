package ddd.simple.dao.importConfigs.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.importConfigs.ImportConfigs;
import ddd.simple.dao.importConfigs.ImportConfigsDao;

@Service
public class ImportConfigsDaoBean extends BaseDao implements ImportConfigsDao
{
	@Override
	public ImportConfigs saveImportConfigs(ImportConfigs importConfigs)  throws Exception{
		return this.save(importConfigs);
	}

	@Override
	public int deleteImportConfigs(Long importConfigsId)  throws Exception{
		return this.deleteById(importConfigsId,ImportConfigs.class);
	}

	@Override
	public ImportConfigs updateImportConfigs(ImportConfigs importConfigs)  throws Exception{
		return this.update(importConfigs);
	}

	@Override
	public ImportConfigs findImportConfigsById(Long importConfigsId)  throws Exception{
		return this.query(importConfigsId, ImportConfigs.class);
	}
	
	@Override
	public EntitySet<ImportConfigs> findAllImportConfigs() throws Exception {
		return this.query("1=1",ImportConfigs.class);
	}

	@Override
	public ImportConfigs findImportConfigsByKey(String importConfigKey)
			throws Exception {
		ImportConfigs importConfigs =  (ImportConfigs)this.query("importConfigKey = '"+importConfigKey+"'", ImportConfigs.class).toArray()[0];
		return importConfigs;
	}
}
