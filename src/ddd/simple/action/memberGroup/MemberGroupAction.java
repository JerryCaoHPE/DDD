package ddd.simple.action.memberGroup;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.member.MemberType;
import ddd.simple.entity.memberGroup.MemberGroup;
import ddd.simple.entity.memberProjection.MemberProjection;
import ddd.simple.service.memberGroup.MemberGroupService;

@Action
@RequestMapping("/MemberGroup")
@Controller
public class MemberGroupAction
{
	@Resource(name="memberGroupServiceBean")
	private MemberGroupService memberGroupService;
	
	public MemberGroup saveMemberGroup(MemberGroup memberGroup)
	{
		try {
			MemberGroup saveMemberGroup = this.memberGroupService.saveMemberGroup(memberGroup);
			return saveMemberGroup;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteMemberGroup(Long memberGroupId){
		
		try {
			return this.memberGroupService.deleteMemberGroup(memberGroupId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public MemberGroup updateMemberGroup(MemberGroup memberGroup) {
		try {
			MemberGroup updateMemberGroup = this.memberGroupService.updateMemberGroup(memberGroup);
			return updateMemberGroup;
		} catch (DDDException e) {
			throw e;
		}
	}

	public MemberGroup findMemberGroupById(Long memberGroupId){
		try {
			MemberGroup findMemberGroup = this.memberGroupService.findMemberGroupById(memberGroupId);
			return  findMemberGroup;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<MemberGroup> findAllMemberGroup(){
		try{
			EntitySet<MemberGroup> allMemberGroup = this.memberGroupService.findAllMemberGroup();
			return allMemberGroup;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public Map<String, Object> saveMemberProjection(Member member,MemberGroup memberGroup,EntitySet<MemberType> memberTypes)
	{
		try {
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("isSuccess", this.memberGroupService.saveMemberProjection(member,memberGroup,memberTypes));
			return result;
		} catch (DDDException e) {
			throw e;
			
		}
	}
	public MemberProjection findMemberProjectionById(Long memberProjectionId){
		try {
			MemberProjection findMemberProjection = this.memberGroupService.findMemberProjectionById(memberProjectionId);
			return  findMemberProjection;
		} catch (DDDException e) {
			throw e;
		}
	}
	public EntitySet<Member> getGroupMembersById(Long memberGroupId) throws Exception{
		try {
			EntitySet<Member> groupMembers = this.memberGroupService.getGroupMembersById(memberGroupId);
			return groupMembers;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int deleteMemberProjection(Long memberId,Long memberGroupId){
		try {
			return this.memberGroupService.deleteMemberProjection(memberId,memberGroupId);
		} catch (DDDException e) {
			throw e;
		}	
	}
	
	public Map<String,Object> updateMemberProjection(Member member,MemberGroup memberGroup,EntitySet<MemberType> memberTypes) {
		try {
			Map<String,Object> result = new HashMap<String,Object>();
			int deleteRes = this.deleteMemberProjection(member.getEId(), memberGroup.getEId());
			if(deleteRes > 0){
				result.put("isSuccess", this.memberGroupService.saveMemberProjection(member, memberGroup, memberTypes));
			}
			return result;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<MemberProjection> getMemberProjectByIds(Long memberId,Long memberGroupId)
	{
		try {
			return this.memberGroupService.getMemberProjectByIds(memberId,memberGroupId);
		} catch (DDDException e) {
			throw e;
		}	
	}
}