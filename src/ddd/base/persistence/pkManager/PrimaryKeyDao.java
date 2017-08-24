package ddd.base.persistence.pkManager;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.base.persistence.pkManager.PrimaryKey;

public interface PrimaryKeyDao extends BaseDaoInterface
{
	public PrimaryKey savePrimaryKey(PrimaryKey primaryKey) throws Exception;
	
	public int deletePrimaryKey(Long primaryKeyId) throws Exception;
	
	public PrimaryKey updatePrimaryKey(PrimaryKey primaryKey) throws Exception;
	
	public PrimaryKey findPrimaryKeyById(Long primaryKeyId) throws Exception;
	
	public EntitySet<PrimaryKey> findAllPrimaryKey() throws Exception;
}
