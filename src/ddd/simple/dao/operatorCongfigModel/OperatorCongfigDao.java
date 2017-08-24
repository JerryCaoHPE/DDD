package ddd.simple.dao.operatorCongfigModel;

import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.operatorCongfigModel.OperatorCongfig;

public interface OperatorCongfigDao extends BaseDaoInterface
{
	public OperatorCongfig saveOperatorCongfig(OperatorCongfig operatorCongfig) throws Exception;
	
	public int deleteOperatorCongfig(Long operatorCongfigId) throws Exception;
	
	public OperatorCongfig updateOperatorCongfig(OperatorCongfig operatorCongfig) throws Exception;
	
	public OperatorCongfig findOperatorCongfigById(Long operatorCongfigId) throws Exception;
	
	public EntitySet<OperatorCongfig> findAllOperatorCongfig() throws Exception;
	public OperatorCongfig findOneByCondition(String where);
	
	public int updateOperatorCongfig(Long operatorId,String cCKey,Map<String,Object> operatorcongfigMap)  throws Exception;
}
