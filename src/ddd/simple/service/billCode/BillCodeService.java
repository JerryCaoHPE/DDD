package ddd.simple.service.billCode;

import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.billCode.BillCode;
import ddd.simple.service.base.BaseServiceInterface;

public interface BillCodeService extends BaseServiceInterface
{
	public BillCode saveBillCode(BillCode billCode) ;
	
	public int deleteBillCode(Long billCodeId) ;
	
	public BillCode updateBillCode(BillCode billCode) ;
	
	public BillCode findBillCodeById(Long billCodeId) ;
	
	public EntitySet<BillCode> findAllBillCode() ;
	
	public String genNewBillCode(String billCodeTypeName,Map<String,Object> variables);
	
	public String genNewBillCode(String billCodeTypeName);

	public String replaceSequenceVariable(BillCode billCode,String baseCode,Long sequence);
 
}