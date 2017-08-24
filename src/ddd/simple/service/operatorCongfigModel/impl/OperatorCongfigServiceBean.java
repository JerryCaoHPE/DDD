package ddd.simple.service.operatorCongfigModel.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.operatorCongfigModel.OperatorCongfigDao;
import ddd.simple.entity.operatorCongfigModel.OperatorCongfig;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.operatorCongfigModel.OperatorCongfigService;

@Service
public class OperatorCongfigServiceBean extends BaseService implements OperatorCongfigService
{

	@Resource(name="operatorCongfigDaoBean")
	private OperatorCongfigDao operatorCongfigDao;
	
	@Override
	public OperatorCongfig saveOperatorCongfig(OperatorCongfig operatorCongfig) 
	{
		try {
			if(this.getLoginUser() == null || this.getLoginUser().getOperatorType().equals("other") )
			{
				//如果没有登陆，则查不到个性化参数
				return null;
			}
			
			OperatorCongfig operatorCongfigExisting = findOneOperatorCongfig(operatorCongfig.getCCKey());
			if(operatorCongfigExisting != null)
			{
				//修改
				operatorCongfigExisting.setCharacterParams(operatorCongfig.getCharacterParams());
				this.operatorCongfigDao.update(operatorCongfigExisting);
				return operatorCongfigExisting;
			}
			else
			{	
				operatorCongfig.setOperatorType(this.getLoginUser().getOperatorType());
				operatorCongfig.setOperatorId(this.getLoginUser().getEId());
				this.operatorCongfigDao.save(operatorCongfig);
				return operatorCongfig;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveOperatorCongfig", e.getMessage(), e);
		}
	}

	@Override
	public int deleteOperatorCongfig(Long operatorCongfigId) {
		try {
			return this.operatorCongfigDao.deleteOperatorCongfig(operatorCongfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteOperatorCongfig", e.getMessage(), e);
		}
		
	}

	@Override
	public OperatorCongfig updateOperatorCongfig(OperatorCongfig operatorCongfig) {
		try {
			return this.operatorCongfigDao.updateOperatorCongfig(operatorCongfig);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateOperatorCongfig", e.getMessage(), e);
		}
	}
	public OperatorCongfig findOne(String where){
		try {
			return this.operatorCongfigDao.findOneByCondition(where);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOperatorCongfigById", e.getMessage(), e);
		}
	}
	@Override
	public OperatorCongfig findOperatorCongfigById(Long operatorCongfigId) {
		try {
			return this.operatorCongfigDao.findOperatorCongfigById(operatorCongfigId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOperatorCongfigById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<OperatorCongfig> findAllOperatorCongfig() {
		try{
			return this.operatorCongfigDao.findAllOperatorCongfig();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllOperatorCongfig", e.getMessage(), e);
		}
	}

	@Override
	public OperatorCongfig findOneOperatorCongfig(String key) {
		if(this.getLoginUser() == null || this.getLoginUser().getOperatorType().equals("other") )
		{
			//如果没有登陆，则查不到个性化参数
			return null;
		}
		String where ="operatorType = '"+ this.getLoginUser().getOperatorType() 
				+"' and operatorId = "+ this.getLoginUser().getEId()+" and cCKey = '"+key+"'";
		return this.findOne(where);
	}

}
