package  ddd.simple.service.member;

import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.memberGroup.MemberGroup;

public interface MemberService extends BaseServiceInterface
{
	public Member saveMember(Member member) throws Exception ;
	
	public int deleteMember(Member member) throws Exception;
	
	public Member updateMember(Member member) throws Exception;
	
	public Member findMemberById(Long memberId) throws Exception;
	
	public EntitySet<Member> findAllMember() throws Exception ;
	
	public Member findMemberByNameAndPassword(String name, String password) throws Exception;
	
	public Map<String, Object> checkOrganization(String name, String password,MemberGroup group) throws Exception;

	public Member findMemberInfo(Long memberId) throws Exception;
 
	public Member updateMemberInfo(Member member) throws Exception ;

	public Map<String, Object> editPassword(String oldPassword,String newPassword) throws Exception;

	public EntitySet<MemberGroup> searchGroup(String memberName) throws Exception;

	public Map<String, Object> checkMemberIsInGroup(Member member) throws Exception;
}