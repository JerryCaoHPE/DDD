package ddd.ddd3test.service.ddd3TestModel.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.ddd3test.entity.ddd3TestModel.Ddd3Test;
import ddd.ddd3test.dao.ddd3TestModel.Ddd3TestDao;
import ddd.ddd3test.service.ddd3TestModel.Ddd3TestService;

@Service
public class Ddd3TestServiceBean extends BaseService implements Ddd3TestService
{

	@Resource(name="ddd3TestDaoBean")
	private Ddd3TestDao ddd3TestDao;
	
	@Override
	public Ddd3Test saveDdd3Test(Ddd3Test ddd3Test) 
	{
		try {
			return this.ddd3TestDao.saveDdd3Test(ddd3Test);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveDdd3Test", e.getMessage(), e);
		}
	}

	@Override
	public int deleteDdd3Test(Long ddd3TestId) {
		try {
			return this.ddd3TestDao.deleteDdd3Test(ddd3TestId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteDdd3Test", e.getMessage(), e);
		}
		
	}

	@Override
	public Ddd3Test updateDdd3Test(Ddd3Test ddd3Test) {
		try {
			return this.ddd3TestDao.updateDdd3Test(ddd3Test);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateDdd3Test", e.getMessage(), e);
		}
	}

	@Override
	public Ddd3Test findDdd3TestById(Long ddd3TestId) {
		try {
			return this.ddd3TestDao.findDdd3TestById(ddd3TestId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findDdd3TestById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<Ddd3Test> findAllDdd3Test() {
		try{
			return this.ddd3TestDao.findAllDdd3Test();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllDdd3Test", e.getMessage(), e);
		}
	}

}
