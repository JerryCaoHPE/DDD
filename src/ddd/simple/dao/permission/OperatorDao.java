package ddd.simple.dao.permission;

import java.util.ArrayList;
import java.util.List;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.organization.Organization;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.permission.Role;

public interface OperatorDao
{
	public Operator saveOperator(Operator operator) throws Exception;

	public int deleteOperator(Long operatorId) throws Exception;
	
	public Operator updateOperator(Operator operator) throws Exception;
	
	public Operator findOperatorById(Long operatorId) throws Exception;
	
	public EntitySet<Operator> findAllOperators() throws Exception;
	
	public Operator findOperatorByCode(String operatorCode)throws Exception;

	public Operator checkOperatorLogin(String operatorCode,String operatorPassword) throws Exception;
	
	public  EntitySet<Organization> searchOrganization(String operatorCode)throws Exception;

	public EntitySet<Role> getSourceOperatorRoles(List<Long> sourceEIds)throws Exception;

}
