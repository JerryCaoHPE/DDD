package ddd.base.persistence.pkManager;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.base.persistence.pkManager.PrimaryKey;

public interface PrimaryKeyService extends BaseServiceInterface
{
	public PrimaryKey savePrimaryKey(PrimaryKey primaryKey) ;
	
	public int deletePrimaryKey(Long primaryKeyId) ;
	
	public PrimaryKey updatePrimaryKey(PrimaryKey primaryKey) ;
	
	public PrimaryKey findPrimaryKeyById(Long primaryKeyId) ;
	
	public EntitySet<PrimaryKey> findAllPrimaryKey() ;
 
}