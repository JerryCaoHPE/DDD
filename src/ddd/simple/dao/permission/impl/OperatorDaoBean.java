package ddd.simple.dao.permission.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.permission.OperatorDao;
import ddd.simple.entity.organization.Organization;
import ddd.simple.entity.permission.Operator;
import ddd.simple.entity.permission.Role;
@Service
public class OperatorDaoBean extends BaseDao implements OperatorDao {
	
	public Operator saveOperator(Operator operator) throws Exception {
		return this.save(operator);
	}
	
	public int deleteOperator(Long operatorId) throws Exception {
		return this.deleteById(operatorId, Operator.class);
	}

	public Operator updateOperator(Operator operator) throws Exception {
		return this.update(operator);
	}

	public Operator findOperatorById(Long operatorId) throws Exception {
		return this.query(operatorId, Operator.class);
	}

	public EntitySet<Operator> findAllOperators() throws Exception {
		return (EntitySet<Operator>) this.query("1=1",Operator.class);
	}


	public Operator findOperatorByCode(String operatorCode) throws Exception{
		return this.queryOne("code = '"+operatorCode+"'",Operator.class);
	}
	
	public Operator checkOperatorLogin(String operatorCode,
			String operatorPassword) throws Exception {

		String sqlCheck = "code = '" + operatorCode + "'";
		EntitySet<Operator> operators = (EntitySet<Operator>) this.query(
				sqlCheck, Operator.class);
		Operator loginOperator = null;
		if (operators != null && operators.size() != 0) {
			for (Operator operator : operators) {
				loginOperator = operator;
				break;
			}
		}
		if( ! operatorPassword.equals(loginOperator.getPassWord()))
		{
			return null;
		}
		return loginOperator;
	}
	@Override
	public EntitySet<Organization> searchOrganization(String operatorCode)
			throws Exception {
		// TODO Auto-generated method stub
		String searchSql = ""
				+ "select * FROM organization "
				+ "where organization.EId in "
				+ "(select organizationId from operatorandrole,operator "
				+ "where operator.EId = operatorandrole.operatorId and operator.code = '"
				+ operatorCode + "')";
		return this.queryBySql(searchSql, Organization.class);
	}
	
	
	@Override
	public EntitySet<Role> getSourceOperatorRoles(List<Long> sourceEIds) throws Exception {
		Iterator<Long> ite = sourceEIds.iterator(); 
		String condition = "operatorId in (";
		while(ite.hasNext()){
			condition += ite.next() +",";
		}
		condition = condition.substring(0,condition.length()-1);
		condition += ")";
		
		String queryCondition = "EId in  (slect roleId form operatorandrole where "+condition+")";
		
		this.query(queryCondition, Role.class);
		
		return null;
	}

}
