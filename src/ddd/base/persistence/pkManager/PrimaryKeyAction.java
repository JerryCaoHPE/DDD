package ddd.base.persistence.pkManager;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.pkManager.PrimaryKey;
import ddd.base.persistence.pkManager.PrimaryKeyService;

@Action
@RequestMapping("/PrimaryKey")
@Controller
public class PrimaryKeyAction
{
	@Resource(name="primaryKeyServiceBean")
	private PrimaryKeyService primaryKeyService;
	
	public PrimaryKey savePrimaryKey(PrimaryKey primaryKey)
	{
		try {
			PrimaryKey savePrimaryKey = this.primaryKeyService.savePrimaryKey(primaryKey);
			return savePrimaryKey;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deletePrimaryKey(Long primaryKeyId){
		
		try {
			return this.primaryKeyService.deletePrimaryKey(primaryKeyId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public PrimaryKey updatePrimaryKey(PrimaryKey primaryKey) {
		try {
			PrimaryKey updatePrimaryKey = this.primaryKeyService.updatePrimaryKey(primaryKey);
			return updatePrimaryKey;
		} catch (DDDException e) {
			throw e;
		}
	}

	public PrimaryKey findPrimaryKeyById(Long primaryKeyId){
		try {
			PrimaryKey findPrimaryKey = this.primaryKeyService.findPrimaryKeyById(primaryKeyId);
			return  findPrimaryKey;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<PrimaryKey> findAllPrimaryKey(){
		try{
			EntitySet<PrimaryKey> allPrimaryKey = this.primaryKeyService.findAllPrimaryKey();
			return allPrimaryKey;
		} catch (DDDException e) {
			throw e;
		}
	}

}