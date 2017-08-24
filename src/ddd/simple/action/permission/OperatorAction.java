package ddd.simple.action.permission;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.permission.Role;
import ddd.simple.service.permission.OperatorService;

@Action
@RequestMapping("/Operator")
@Controller
public class OperatorAction {
	
	@Resource(name="operatorServiceBean")
	private OperatorService operatorService;
	
	public Operator saveOperator(Operator operator)throws Exception{
		try {
			Operator newOperator = this.operatorService.saveOperator(operator);
			return newOperator;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteOperator(Long operatorId)throws Exception{
		try {
			return this.operatorService.deleteOperator(operatorId);
		} catch (DDDException e) {
			throw e;
		}
	}

	public Operator updateOperator(Operator operator)throws Exception{
		try {
			Operator newOperator = this.operatorService.updateOperator(operator);
			return newOperator;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Operator findOperatorById(Long operatorId)throws Exception{
		try {
			Operator findOperator = this.operatorService.findOperatorById(operatorId);
			return findOperator;
		} catch (DDDException e) {
			throw e;
		}
	}

	public EntitySet<Operator> findAllOperators()throws Exception{
		try {
			EntitySet<Operator> operators = this.operatorService.findAllOperators();
			return operators;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Integer distributionRole(Long operatorId,EntitySet<Role> roles,Long organizationId) throws Exception{
		try {
			if(organizationId == null)
				return 0;
			return this.operatorService.distributionRole(operatorId, roles, organizationId);
		}  catch (DDDException e) {
			throw e;
		}
	}
	
	public Integer copyOperator(List<Long> sourceEIds,List<Long> targetEIds) throws Exception{
		try
		{
			return this.operatorService.copyOperator(sourceEIds, targetEIds);
		} catch (DDDException e)
		{
			throw e;
		}
	}
}
