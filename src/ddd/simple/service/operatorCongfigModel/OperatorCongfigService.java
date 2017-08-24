package ddd.simple.service.operatorCongfigModel;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.operatorCongfigModel.OperatorCongfig;

public interface OperatorCongfigService extends BaseServiceInterface
{
	public OperatorCongfig saveOperatorCongfig(OperatorCongfig operatorCongfig) ;
	
	public int deleteOperatorCongfig(Long operatorCongfigId) ;
	
	public OperatorCongfig updateOperatorCongfig(OperatorCongfig operatorCongfig) ;
	
	public OperatorCongfig findOperatorCongfigById(Long operatorCongfigId) ;
	
	public OperatorCongfig findOneOperatorCongfig(String where);
	
	public EntitySet<OperatorCongfig> findAllOperatorCongfig() ;
 
}