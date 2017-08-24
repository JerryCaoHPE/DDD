package ddd.simple.dao.workflow.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.workflow.CommonFindTaskAssigneeDao;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.permission.Operator;

@Service
public class CommonFindTaskAssigneeDaoBean extends BaseDao implements
		CommonFindTaskAssigneeDao {

	public Collection<String> 根据权限点和所属机构查找(String 权限点名称, String 机构名称) {
		String sql =  "EId in("+
					  "select oandr.operatorId from operatorAndRole oandr where oandr.roleId in("+
					  "select r.roleEId from permission p left join role_permissions r on p.EId = r.permissionEId "+
					  "where p.name='"+权限点名称+"')"+
					  "and oandr.organizationId in("+
					  "select org.EId from organization org where org.name='"+机构名称+"'))";
		
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}

	public Collection<String> 根据权限点查找(String 权限点名称) {
		String sql = "EId in("+
					 "select oandr.operatorId from OperatorAndRole oandr where oandr.roleId in("+
					 "select r.roleEId from permission p left join role_permissions r on p.EId = r.permissionEId "+
					 "where p.name='"+权限点名称+"'))";
		
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}

	public Collection<String> 根据角色和所属机构查找(String 角色名称, String 机构名称) {
		String sql = "EId in("+
					 "select oandr.operatorId from operatorAndRole oandr where oandr.roleId in("+
					 "select r.EId from role r where r.name='"+角色名称+"') and oandr.organizationId in("+
					 "select org.EId from organization org where org.name='"+机构名称+"'))";
		
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}

	public Collection<String> 根据角色查找(String 角色名称) {
	
		String sql = "EId in("+
					 "select oandr.operatorId from operatorAndRole oandr where oandr.roleId in("+
					 "select r.EId from role r where r.name='"+角色名称+"'))";
		
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}

	@Override
	public Collection<String> 根据角色和所属单位查找(String 角色名称,String 单位名称){
		String sql = "EId in ("+
					 "select memberprojection.memberId from memberprojection where memberTypeId in ("+
					 "select membertype.EId FROM membertype where roleId in("+
					 "select role.EId from role where name='"+角色名称+"')) and memberGroupId in("+
					 "select EId from membergroup where organizationId in("+
					 "select EId from organization where name='"+单位名称+"')))";
	
		List<String> memberNames = this.getMemberNamessBySql(sql);
		return memberNames;
	}
	
	public Collection<String> 根据部门和所属机构查找(String 部门名称, String 机构名称) {
		String sql = "employeeId in("+
					 "select e.EId from employee e where e.departmentId in("+
					 "select d.EId from department d where d.name='"+部门名称+"') and e.organizationId in("+
					 "select org.EId from organization org where org.name='"+机构名称+"'))";

		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}

	public Collection<String> 根据部门查找(String 部门名称) {
		String sql = "employeeId in("+
					 "select e.EId from employee e where e.departmentId in("+
					 "select d.EId from department d where d.name='"+部门名称+"'))";
		
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}
	
	private List<String> getOperatorNamesBySql(String sql)
	{
		List<String> operatorNames = null;
		try 
		{
			EntitySet<Operator> operators = this.query(sql,Operator.class);
			operatorNames = new ArrayList<String>();
			for (Operator operator : operators)
			{
				operatorNames.add(operator.getCode());
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}

		return operatorNames;
	}
	
	private List<String> getMemberNamessBySql(String sql)
	{
		List<String> memberNames = null;
		try 
		{
			EntitySet<Member> members = this.query(sql,Member.class);
			memberNames = new ArrayList<String>();
			for (Member member : members)
			{
				memberNames.add(member.getName());
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		return memberNames;
	}

	/* (非 Javadoc) 
	* <p>Title: 根据人员查找</p> 
	* <p>Description: </p> 
	* @param 人员Id
	* @return 
	* @see ddd.simple.dao.workflow.CommonFindTaskAssigneeDao#根据人员查找(java.lang.String) 
	*/
	@Override
	public Collection<String> 根据人员查找(String 人员Id)
	{
		String sql = "EId in("
				+ " select o.eid from OPERATOR o "
				+ " LEFT JOIN EMPLOYEE e on o.EMPLOYEEID=e.EID"
				+ " where e.EID ="+人员Id+""
				+ ")";
		List<String> operatorNames = this.getOperatorNamesBySql(sql);
		return operatorNames;
	}
}
