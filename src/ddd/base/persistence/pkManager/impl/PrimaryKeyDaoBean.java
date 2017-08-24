package ddd.base.persistence.pkManager.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.base.persistence.pkManager.PrimaryKey;
import ddd.base.persistence.pkManager.PrimaryKeyDao;

@Service
public class PrimaryKeyDaoBean extends BaseDao implements PrimaryKeyDao
{
	@Override
	public PrimaryKey savePrimaryKey(PrimaryKey primaryKey)  throws Exception{
		return this.save(primaryKey);
	}

	@Override
	public int deletePrimaryKey(Long primaryKeyId)  throws Exception{
		return this.deleteById(primaryKeyId,PrimaryKey.class);
	}

	@Override
	public PrimaryKey updatePrimaryKey(PrimaryKey primaryKey)  throws Exception{
		return this.update(primaryKey);
	}

	@Override
	public PrimaryKey findPrimaryKeyById(Long primaryKeyId)  throws Exception{
		return this.query(primaryKeyId, PrimaryKey.class);
	}
	
	@Override
	public EntitySet<PrimaryKey> findAllPrimaryKey() throws Exception {
		return this.query("1=1",PrimaryKey.class);
	}
}
