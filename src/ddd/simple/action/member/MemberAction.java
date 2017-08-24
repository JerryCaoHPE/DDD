package ddd.simple.action.member;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.helper.Img;
import ddd.base.persistence.EntitySet;
import ddd.simple.action.util.ValidateCodeUtil;
import ddd.simple.entity.member.Member;
import ddd.simple.service.member.MemberService;

@Action
@RequestMapping("/Member")
@Controller
public class MemberAction
{
	@Resource(name="memberServiceBean")
	private MemberService memberService;
	
	@Autowired 
	private HttpServletRequest request;
	
	public Img validateCode()
	{
		String code = ValidateCodeUtil.getCodeStr();
		byte[] bytes = ValidateCodeUtil.genCodePicBtye(code);
		request.getSession().setAttribute("code", code);
		Img img = new Img(bytes);
		return img;
	}
	
	public Object returnValidate(String inputCode)
	{
		String code = (String) request.getSession().getAttribute("code");
		JSONObject result = new JSONObject(); 
		if(code.equalsIgnoreCase(inputCode)||code==inputCode)
		{
			result.put("result", true);
			
		}else{
			result.put("result", false);
		}
		return result;
	}
	public Member saveMember(Member member) throws Exception
	{
		try {
			Member saveMember = this.memberService.saveMember(member);
			return saveMember;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteMember(Member member) throws Exception{
		
		try {
			return this.memberService.deleteMember(member);
		} catch (DDDException e) {
			throw e;
		}
	}
	public Map<String,Object> checkMemberIsInGroup(Member member) throws Exception{
		try {
			return this.memberService.checkMemberIsInGroup(member);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Member updateMember(Member member) throws Exception {
		try {
			Member updateMember = this.memberService.updateMember(member);
			return updateMember;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Member findMemberById(Long memberId) throws Exception{
		try {
			Member findMember = this.memberService.findMemberById(memberId);
			return  findMember;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Member> findAllMember() throws Exception{
		try{
			EntitySet<Member> allMember = this.memberService.findAllMember();
			return allMember;
		} catch (DDDException e) {
			throw e;
		}
	}
	public Member findMemberInfo(Long memberId) throws Exception{
		try {
			Member member = this.memberService.findMemberInfo(memberId);
			return  member;
		} catch (DDDException e) {
			throw e;
		}
	}
	public Member updateMemberInfo(Member member) throws Exception{
		try {
			Member updateMember = this.memberService.updateMemberInfo(member);
			return  updateMember;
		} catch (DDDException e) {
			throw e;
		}
	}
	public Map<String,Object> editPassword(String oldPassword,String newPassword) throws Exception{
		try {
			Map<String,Object> result = this.memberService.editPassword(oldPassword,newPassword);
			return  result;
		} catch (DDDException e) {
			throw e;
		}
	}
}