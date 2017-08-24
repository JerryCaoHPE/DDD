package ddd.simple.dao.billCode.impl;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.billCode.BillCodeDao;
import ddd.simple.entity.billCode.BillCode;

@Service
public class BillCodeDaoBean extends BaseDao implements BillCodeDao
{
	@Override
	public BillCode saveBillCode(BillCode billCode)  throws Exception{
		return this.save(billCode);
	}

	@Override
	public int deleteBillCode(Long billCodeId)  throws Exception{
		return this.deleteById(billCodeId,BillCode.class);
	}

	@Override
	public BillCode updateBillCode(BillCode billCode)  throws Exception{
		return this.update(billCode);
	}

	@Override
	public BillCode findBillCodeById(Long billCodeId)  throws Exception{
		return this.query(billCodeId, BillCode.class);
	}
	
	@Override
	public BillCode findBillCodeByName(String billCodeTypeName) throws Exception
	{
		return this.queryOne(" billCodeTypeName=?", new Object[]{billCodeTypeName}, BillCode.class);
	}	
	
	@Override
	public EntitySet<BillCode> findAllBillCode() throws Exception {
		return this.query("1=1",BillCode.class);
	}
}
