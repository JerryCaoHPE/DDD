package ddd.simple.dao.billCode;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.billCode.BillCode;

public interface BillCodeDao extends BaseDaoInterface
{
	public BillCode saveBillCode(BillCode billCode) throws Exception;
	
	public int deleteBillCode(Long billCodeId) throws Exception;
	
	public BillCode updateBillCode(BillCode billCode) throws Exception;
	
	public BillCode findBillCodeById(Long billCodeId) throws Exception;
	
	public BillCode findBillCodeByName(String billCodeTypeName) throws Exception;
	
	public EntitySet<BillCode> findAllBillCode() throws Exception;
}
