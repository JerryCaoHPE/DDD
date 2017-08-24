package ddd.simple.action.permission;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.memberGroup.MemberGroup;
import ddd.simple.service.member.MemberService;

@Action
@RequestMapping("/VipLogin")
@Controller
public class VipLoginAction
{
	
	@Resource(name = "memberServiceBean")
	private MemberService memberService;
	
	public Map<String, Object> vipLogin(String name, String password) throws Exception
	{
		try
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("isSuccess", false);
			//采用md5加密
			if (password.length() != 32) {
				return new HashMap<String, Object>();
			}
			Member member = this.memberService.findMemberByNameAndPassword(name, password);
			if (member != null)
			{
				result.put("isSuccess", true);
				result.put("groups", this.memberService.searchGroup(name));
			}
			return result;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public Map<String, Object> checkOrganization(String name, String password, MemberGroup group) throws Exception
	{
		return this.memberService.checkOrganization(name, password, group);
	}
	
	public EntitySet<MemberGroup> searchGroup(String memberName) throws Exception
	{
		try
		{
			return this.memberService.searchGroup(memberName);
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public void vipLoginOut(HttpServletRequest request) throws Exception
	{
		this.memberService.removeLoginUser();
	}
}
