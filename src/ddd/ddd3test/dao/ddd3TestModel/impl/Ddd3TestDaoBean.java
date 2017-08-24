package ddd.ddd3test.dao.ddd3TestModel.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.ddd3test.entity.ddd3TestModel.Ddd3Test;
import ddd.ddd3test.dao.ddd3TestModel.Ddd3TestDao;

@Service
public class Ddd3TestDaoBean extends BaseDao implements Ddd3TestDao
{
	@Override
	public Ddd3Test saveDdd3Test(Ddd3Test ddd3Test)  throws Exception{
		return this.save(ddd3Test);
	}

	@Override
	public int deleteDdd3Test(Long ddd3TestId)  throws Exception{
		return this.deleteById(ddd3TestId,Ddd3Test.class);
	}

	@Override
	public Ddd3Test updateDdd3Test(Ddd3Test ddd3Test)  throws Exception{
		return this.update(ddd3Test);
	}

	@Override
	public Ddd3Test findDdd3TestById(Long ddd3TestId)  throws Exception{
		return this.query(ddd3TestId, Ddd3Test.class);
	}
	
	@Override
	public EntitySet<Ddd3Test> findAllDdd3Test() throws Exception {
		return this.query("1=1",Ddd3Test.class);
	}
}
