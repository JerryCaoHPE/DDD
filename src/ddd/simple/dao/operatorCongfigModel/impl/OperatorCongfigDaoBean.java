package ddd.simple.dao.operatorCongfigModel.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.operatorCongfigModel.OperatorCongfigDao;
import ddd.simple.entity.operatorCongfigModel.OperatorCongfig;

@Service
public class OperatorCongfigDaoBean extends BaseDao implements OperatorCongfigDao
{
	@Override
	public OperatorCongfig saveOperatorCongfig(OperatorCongfig operatorCongfig)  throws Exception{
		return this.save(operatorCongfig);
	}

	@Override
	public int deleteOperatorCongfig(Long operatorCongfigId)  throws Exception{
		return this.deleteById(operatorCongfigId,OperatorCongfig.class);
	}

	@Override
	public OperatorCongfig updateOperatorCongfig(OperatorCongfig operatorCongfig)  throws Exception{
		return this.update(operatorCongfig);
	}
	
	public int updateOperatorCongfig(Long operatorId,String cCkey,Map<String,Object> operatorcongfigMap)  throws Exception{
		String whereSql = "operatorId = "+operatorId+" and cCkey = '"+cCkey+"'";
		return this.update("operatorcongfig",operatorcongfigMap,whereSql);
	}
	
	@Override
	public OperatorCongfig findOneByCondition(String where){
		try {
			SessionFactory.getThreadSession().clearByEntityName(OperatorCongfig.class);
			return this.queryOne(where, OperatorCongfig.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public OperatorCongfig findOperatorCongfigById(Long operatorCongfigId)  throws Exception{
		return this.query(operatorCongfigId, OperatorCongfig.class);
	}
	
	@Override
	public EntitySet<OperatorCongfig> findAllOperatorCongfig() throws Exception {
		return this.query("",OperatorCongfig.class);
	}
}
