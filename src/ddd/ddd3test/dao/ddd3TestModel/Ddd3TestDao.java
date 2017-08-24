package ddd.ddd3test.dao.ddd3TestModel;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.ddd3test.entity.ddd3TestModel.Ddd3Test;

public interface Ddd3TestDao extends BaseDaoInterface {
	public Ddd3Test saveDdd3Test(Ddd3Test ddd3Test) throws Exception;
	
	public int deleteDdd3Test(Long ddd3TestId) throws Exception;
	
	public Ddd3Test updateDdd3Test(Ddd3Test ddd3Test) throws Exception;
	
	public Ddd3Test findDdd3TestById(Long ddd3TestId) throws Exception;
	
	public EntitySet<Ddd3Test> findAllDdd3Test() throws Exception;
}
