package ddd.simple.action.operatorCongfigModel;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.operatorCongfigModel.OperatorCongfig;
import ddd.simple.service.operatorCongfigModel.OperatorCongfigService;

@Action
@RequestMapping("/OperatorCongfig")
@Controller
public class OperatorCongfigAction
{
	@Resource(name="operatorCongfigServiceBean")
	private OperatorCongfigService operatorCongfigService;
	
	public OperatorCongfig saveOperatorCongfig(OperatorCongfig operatorCongfig)
	{
		try {
			OperatorCongfig saveOperatorCongfig = this.operatorCongfigService.saveOperatorCongfig(operatorCongfig);
			return saveOperatorCongfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteOperatorCongfig(Long operatorCongfigId){
		
		try {
			return this.operatorCongfigService.deleteOperatorCongfig(operatorCongfigId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public OperatorCongfig updateOperatorCongfig(OperatorCongfig operatorCongfig) {
		try {
			OperatorCongfig updateOperatorCongfig = this.operatorCongfigService.updateOperatorCongfig(operatorCongfig);
			return updateOperatorCongfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	public OperatorCongfig findOneOperatorCongfig(String uniqueInfo){
		try {
			OperatorCongfig findOperatorCongfig = this.operatorCongfigService.findOneOperatorCongfig(uniqueInfo);
			return  findOperatorCongfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	public OperatorCongfig findOperatorCongfigById(Long operatorCongfigId){
		try {
			OperatorCongfig findOperatorCongfig = this.operatorCongfigService.findOperatorCongfigById(operatorCongfigId);
			return  findOperatorCongfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<OperatorCongfig> findAllOperatorCongfig(){
		try{
			EntitySet<OperatorCongfig> allOperatorCongfig = this.operatorCongfigService.findAllOperatorCongfig();
			return allOperatorCongfig;
		} catch (DDDException e) {
			throw e;
		}
	}

}