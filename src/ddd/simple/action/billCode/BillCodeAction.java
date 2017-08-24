package ddd.simple.action.billCode;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.billCode.BillCode;
import ddd.simple.service.billCode.BillCodeService;

@Action
@RequestMapping("/BillCode")
@Controller
public class BillCodeAction
{
	@Resource(name="billCodeServiceBean")
	private BillCodeService billCodeService;
	
	public BillCode saveBillCode(BillCode billCode)
	{
		try {
			BillCode saveBillCode = this.billCodeService.saveBillCode(billCode);
			return saveBillCode;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteBillCode(Long billCodeId){
		
		try {
			return this.billCodeService.deleteBillCode(billCodeId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public BillCode updateBillCode(BillCode billCode) {
		try {
			BillCode updateBillCode = this.billCodeService.updateBillCode(billCode);
			return updateBillCode;
		} catch (DDDException e) {
			throw e;
		}
	}

	public BillCode findBillCodeById(Long billCodeId){
		try {
			BillCode findBillCode = this.billCodeService.findBillCodeById(billCodeId);
			return  findBillCode;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<BillCode> findAllBillCode(){
		try{
			EntitySet<BillCode> allBillCode = this.billCodeService.findAllBillCode();
			return allBillCode;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public String genNewBillCode(String billCodeTypeName,Map<String,Object> variables)
	{
		try{
			String code = this.billCodeService.genNewBillCode(billCodeTypeName, variables);
			return code;
		} catch (DDDException e) {
			throw e;
		}		
	}
	

}