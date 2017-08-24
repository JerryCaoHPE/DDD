package ddd.simple.service.workflow.impl;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddd.base.exception.DDDException;
import ddd.simple.dao.workflow.CommonFindTaskAssigneeDao;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.permission.Operator;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.workflow.ICommonFindTaskAssigneeService;

@Service
@Transactional
public class CommonFindTaskAssigneeServiceBean extends BaseService implements
		ICommonFindTaskAssigneeService {
	
	@Resource(name = "commonFindTaskAssigneeDaoBean")
	private CommonFindTaskAssigneeDao commonFindTaskAssigneeDao;
	
	@Override
	public Collection<String> 根据权限点和所属机构查找(String 权限点名称, String 机构名称) 
	{
		
		 Collection<String> operators = this.commonFindTaskAssigneeDao.根据权限点和所属机构查找(权限点名称, 机构名称);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有找到机构名称为 "+机构名称 +" 的机构中有权限点为 "  +权限点名称+"的人员");
		 }
		return operators;
	}

	@Override
	public Collection<String> 根据权限点查找(String 权限点名称) 
	{
		 Collection<String> operators = this.commonFindTaskAssigneeDao.根据权限点查找(权限点名称);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有有权限点为 "  +权限点名称+"的人员");
		 }
		return operators;
	}

	@Override
	public Collection<String> 根据角色和所属机构查找(String 角色名称, String 机构名称) 
	{
		Collection<String> operators = this.commonFindTaskAssigneeDao
				.根据角色和所属机构查找(角色名称, 机构名称);
		if (operators == null || operators.size() == 0) {
			throw new DDDException("没有找到在 " + 机构名称 + "中角色名称为" + 角色名称 + "的人");
		}
		return operators;
	}
	
	@Override
	public Collection<String> 根据角色和所属单位查找(String 角色名称,String 单位名称)
	{
		if(单位名称.length()<1){
			单位名称 = this.getLoginUser().getCurrentOrganization().getParent().getName(); 
		}
		Operator operator = this.getLoginUser().getLoginOperator();
		Member member = this.getLoginUser().getLoginVip();
		
		if(operator!=null)
		{
			Collection<String> operators = this.commonFindTaskAssigneeDao.根据角色和所属机构查找(角色名称, 单位名称);
			if(operators == null || operators.size() == 0)
			{
				throw new DDDException("没有找到在 "  +单位名称+"中角色名称为"+角色名称+"的人");
			}
			return operators;
		}
		else if(member!=null)
		{
			Collection<String> members = this.commonFindTaskAssigneeDao.根据角色和所属单位查找(角色名称, 单位名称);
			if(members == null || members.size() == 0)
			{
				throw new DDDException("没有找到在 "  +单位名称+"中角色名称为"+角色名称+"的人");
			}
			return members;
		}
		return null;
	}
	
	@Override
	public Collection<String> 根据角色查找(String 角色名称) 
	{
		Collection<String> operators = this.commonFindTaskAssigneeDao.根据角色查找(角色名称);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有找到在角色名称为"+角色名称+"的人");
		 }
		return operators;
	}

	@Override
	public Collection<String> 根据部门和所属机构查找(String 部门名称, String 机构名称) 
	{
		Collection<String> operators = this.commonFindTaskAssigneeDao.根据部门和所属机构查找(部门名称, 机构名称);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有找到在机构"+机构名称+"中部门"+部门名称+"的人");
		 }
		return operators;
	}

	@Override
	public Collection<String> 根据部门查找(String 部门名称) 
	{
		Collection<String> operators = this.commonFindTaskAssigneeDao.根据部门查找(部门名称);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有找到部门"+部门名称+"下面的人");
		 }
		return operators;
	}
	
	public Collection<String> 根据人员查找(String 人员Id)
	{
		Collection<String> operators = this.commonFindTaskAssigneeDao.根据人员查找(人员Id);
		 if(operators == null || operators.size() == 0)
		 {
			 throw new DDDException("没有找到人员ID:"+人员Id+"的人");
		 }
		return operators;
	}


}
