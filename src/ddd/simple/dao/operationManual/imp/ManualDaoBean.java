package ddd.simple.dao.operationManual.imp;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.operationManual.ManualDao;
import ddd.simple.entity.operationManual.Manual;

@Service
public class ManualDaoBean extends BaseDao implements ManualDao{

	public Manual saveManual(Manual manual) throws Exception{
		return this.save(manual);
	}

	public void deleteManual(Long manualId) throws Exception{
		this.deleteById(manualId, Manual.class);
	}

	public Manual updateManual(Manual manual) throws Exception{
		return this.update(manual);
	}

	public Manual findManualById(Long manualId) throws Exception{
		return this.query(manualId, Manual.class);
	}

	public EntitySet<Manual> findManuals() throws Exception{
		return (EntitySet<Manual>) this.query("1=1", Manual.class);
	}
		
	public EntitySet<Manual> findManualByCatalogId(Long catalogId)throws Exception {
		String findSql = "catalogId="+catalogId;
		return (EntitySet<Manual>) this.query(findSql,Manual.class);
	}
}
