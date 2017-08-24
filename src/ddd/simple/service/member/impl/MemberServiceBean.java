package ddd.simple.service.member.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.Config;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.member.MemberType;
import ddd.simple.entity.memberGroup.MemberGroup;
import ddd.simple.entity.memberProjection.MemberProjection;
import ddd.simple.service.member.MemberTypeService;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.permission.LoginUser;
import ddd.simple.entity.permission.Permission;
import ddd.simple.entity.permission.Role;
import ddd.simple.dao.member.MemberDao;
import ddd.simple.dao.memberGroup.MemberGroupDao;
import ddd.simple.dao.memberProjection.MemberProjectionDao;
import ddd.simple.service.member.MemberService;

@Service
public class MemberServiceBean extends BaseService implements MemberService
{
	
	@Resource(name = "memberDaoBean")
	private MemberDao memberDao;
	
	@Resource(name = "memberGroupDaoBean")
	private MemberGroupDao memberGroupDao;
	
	@Resource(name = "memberProjectionDaoBean")
	private MemberProjectionDao memberProjectionDao;
	
	@Resource(name = "memberTypeServiceBean")
	private MemberTypeService memberTypeService;
	
	// 获取默认会员类别
	private MemberType getMemberType()
	{
		try
		{
			Long defultMemberTypeId = Long.parseLong(Config.get("defaultMemberTypeId"));
			return this.memberTypeService.findMemberTypeById(defultMemberTypeId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getMemberType", e.getMessage(), e);
		}
	}
	
	@Override
	public Member saveMember(Member member)
	{
		try
		{
			// MemberType memberType = null;
			// 如果没有指定类型
			// if(member.getMemberType()==null)
			// {
			// memberType =this.getMemberType();
			// member.setMemberType(memberType);
			// }
			Date operatorDate = new Date();
			member.setOperateDate(operatorDate);
			return this.memberDao.saveMember(member);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveMember", e.getMessage(), e);
		}
	}
	
	@Override
	public int deleteMember(Member member)
	{
		try
		{
			this.memberProjectionDao.deleteMemberProjectionByMember(member.getEId());
			return this.memberDao.delete(member);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("deleteMember", e.getMessage(), e);
		}
		
	}
	
	@Override
	public Member updateMember(Member member)
	{
		try
		{
			Date operateDate = new Date();
			member.setOperateDate(operateDate);
			return this.memberDao.updateMember(member);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateMember", e.getMessage(), e);
		}
	}
	
	@Override
	public Member findMemberById(Long memberId)
	{
		try
		{
			return this.memberDao.findMemberById(memberId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findMemberById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<Member> findAllMember()
	{
		try
		{
			return this.memberDao.findAllMember();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAllMember", e.getMessage(), e);
		}
	}
	
	/*
	 * 安全性 后台session保存会员账号名，是否登陆
	 */
	@Override
	public Member findMemberByNameAndPassword(String name, String password)
	{
		try
		{
			return this.memberDao.findMemberByNameAndPassword(name, password);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findMemberByNameAndPassword", e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Object> checkOrganization(String name, String password, MemberGroup currentGroup)
	{
		try
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("isSuccess", false);
			Member member = this.memberDao.findMemberByNameAndPassword(name, password);
			if (member != null && currentGroup != null)
			{
				EntitySet<MemberGroup> groups = this.searchGroup(name);
				if(groups.contains(currentGroup)){
					LoginUser loginUser = this.turnToLoginUser(member, currentGroup);
					result.put("isSuccess", true);
					// 在前台 存入sessionStorage
					result.put("loginUser", loginUser);
					// 在后台 存入session
					super.setLoginUser(loginUser);
				}
				else{
					throw new DDDException("单位选择异常!");
				}
			}
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("checkOrganizationError!", e.getMessage(), e);
		}
	}
	
	private LoginUser turnToLoginUser(Member member, MemberGroup currentGroup) throws Exception
	{
		if (currentGroup == null || member == null)
			return null;
		LoginUser loginUser = new LoginUser();
		loginUser.setCurrentGroup(currentGroup);
		EntitySet<MemberProjection> projections = memberProjectionDao.findProjectionByMember(member.getEId());
		EntitySet<MemberGroup> groups = new EntitySet<MemberGroup>();
		Set<String> permissionCodes = new HashSet<String>();
		EntitySet<Role> roles = new EntitySet<Role>();
		
		Map<Long, MemberType> appearedMembertype = new HashMap<Long, MemberType>();
		
		for (MemberProjection projection : projections)
		{
			// bug：当相同的实体出现在结果集中时，以后不会被查询出来
			// 解决：将第一次查询出来的结果缓存
			MemberType memberType = projection.getMemberType();
			;
			if (!appearedMembertype.containsKey(memberType.getEId()))
			{
				if (memberType.getRole() == null)
				{
					throw new DDDException("会员类型" + memberType.getTypeName() + "未分配角色!");
				}
				appearedMembertype.put(memberType.getEId(), memberType);
			} else
			{
				memberType = appearedMembertype.get(memberType.getEId());
			}
			
			if (projection.getMemberGroup() != null)
			{
				if (projection.getMemberGroup().getEId() == currentGroup.getEId())
				{
					// 在多角色的情况下 会进入多次
					Collection<Permission> permissions = memberType.getRole().getPermissions();
					for (Permission permission : permissions)
					{
						permissionCodes.add(permission.getCode());
					}
				}
				groups.add(projection.getMemberGroup());
			}
			if (projection.getMemberType() != null)
			{
				roles.add(projection.getMemberType().getRole());
			}
			
		}
		
		loginUser.setGroups(groups);
		loginUser.setCurrentOrganization(currentGroup.getOrganization());
		loginUser.setUserName(member.getName());
		loginUser.setLoginVip(member);
		loginUser.setUserPermissionsCode(permissionCodes);
		return loginUser;
	}
	
	@Override
	public Member findMemberInfo(Long memberId)
	{
		Member findMember = this.findMemberById(memberId);
		findMember.setPassword(null);
		return findMember;
	}
	
	@Override
	public Member updateMemberInfo(Member member)
	{
		try
		{
			String password = findMemberById(member.getEId()).getPassword();
			member.setPassword(password);
			return this.memberDao.updateMember(member);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateMemberInfo", e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Object> editPassword(String oldPassword, String newPassword)
	{
		Long memberId = this.getLoginUser().getEId();
		boolean ifEditSuccess = false;
		Map<String, Object> result = new HashMap<String, Object>();
		Member member = this.memberDao.editPassword(memberId, oldPassword, newPassword);
		if (member != null)
		{
			if (member.getEId() != null)
			{
				ifEditSuccess = true;
			}
		}
		result.put("editPasswordSuccess", ifEditSuccess);
		return result;
	}

	public EntitySet<MemberGroup> searchGroup(String memberName) throws DDDException
	{
		
		if (memberName == null || memberName.equals(""))
			return null;
		Member member = null;
		try
		{
			member = memberDao.findMemberByName(memberName);
			if (member == null)
			{
				return null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("查询用户失败!");
		}
		
		try
		{
			EntitySet<MemberProjection> projections = memberProjectionDao.findProjectionByMember(member.getEId());
			if (projections == null || projections.isEmpty())
				return null;
			EntitySet<MemberGroup> groups = new EntitySet<MemberGroup>();
			for (MemberProjection projection : projections)
			{
				MemberGroup group = projection.getMemberGroup();
				group.getName();
				group.getEId();
				group.getOrganization(); // EId
				groups.add(group);
			}
			return groups;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("搜索组失败!");
		}
	}
	
	@Override
	public Map<String, Object> checkMemberIsInGroup(Member member) throws Exception
	{
		String inGroupName = getInGroupName(member);
		Map<String, Object> isInGroupRsult = new HashMap<String, Object>();
		isInGroupRsult.put("res", "init");
		if (inGroupName == null || inGroupName == "")
		{
			isInGroupRsult.put("res", "noInGroup");
		} else
		{
			isInGroupRsult.put("res", "isInGroup");
			isInGroupRsult.put("groups", inGroupName);
		}
		if (member == null)
			return null;
		return isInGroupRsult;
	}
	
	private String getInGroupName(Member member) throws Exception
	{
		try
		{
			String inGroupNames = "";
			
			EntitySet<MemberProjection> memberProjections = memberProjectionDao.findProjectionByMember(member.getEId());
			for (MemberProjection memberProjection : memberProjections)
			{
				if (memberProjection.getMemberGroup() != null)
				{
					if (memberProjection.getMemberGroup().getName() != null)
						inGroupNames += memberProjection.getMemberGroup().getName() + "、";
				}
			}
			return inGroupNames;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getInGroupName", e.getMessage(), e);
		}
	}
	
}
