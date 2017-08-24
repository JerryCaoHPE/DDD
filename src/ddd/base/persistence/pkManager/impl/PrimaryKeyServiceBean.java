package ddd.base.persistence.pkManager.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.base.persistence.pkManager.PrimaryKey;
import ddd.base.persistence.pkManager.PrimaryKeyDao;
import ddd.base.persistence.pkManager.PrimaryKeyService;

@Service
public class PrimaryKeyServiceBean extends BaseService implements PrimaryKeyService
{

	@Resource(name="primaryKeyDaoBean")
	private PrimaryKeyDao primaryKeyDao;
	
	@Override
	public PrimaryKey savePrimaryKey(PrimaryKey primaryKey) 
	{
		try {
			return this.primaryKeyDao.savePrimaryKey(primaryKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("savePrimaryKey", e.getMessage(), e);
		}
	}

	@Override
	public int deletePrimaryKey(Long primaryKeyId) {
		try {
			return this.primaryKeyDao.deletePrimaryKey(primaryKeyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deletePrimaryKey", e.getMessage(), e);
		}
		
	}

	@Override
	public PrimaryKey updatePrimaryKey(PrimaryKey primaryKey) {
		try {
			return this.primaryKeyDao.updatePrimaryKey(primaryKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updatePrimaryKey", e.getMessage(), e);
		}
	}

	@Override
	public PrimaryKey findPrimaryKeyById(Long primaryKeyId) {
		try {
			return this.primaryKeyDao.findPrimaryKeyById(primaryKeyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findPrimaryKeyById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<PrimaryKey> findAllPrimaryKey() {
		try{
			return this.primaryKeyDao.findAllPrimaryKey();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllPrimaryKey", e.getMessage(), e);
		}
	}

}
