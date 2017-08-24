package ddd.ddd3test.service.ddd3TestModel;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.ddd3test.entity.ddd3TestModel.Ddd3Test;

public interface Ddd3TestService extends BaseServiceInterface
{
	public Ddd3Test saveDdd3Test(Ddd3Test ddd3Test) ;
	
	public int deleteDdd3Test(Long ddd3TestId) ;
	
	public Ddd3Test updateDdd3Test(Ddd3Test ddd3Test) ;
	
	public Ddd3Test findDdd3TestById(Long ddd3TestId) ;
	
	public EntitySet<Ddd3Test> findAllDdd3Test() ;
 
}