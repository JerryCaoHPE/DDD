package ddd.simple.service.memberGroup;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.member.MemberType;
import ddd.simple.entity.memberGroup.MemberGroup;
import ddd.simple.entity.memberProjection.MemberProjection;

public interface MemberGroupService extends BaseServiceInterface
{
	public MemberGroup saveMemberGroup(MemberGroup memberGroup) ;
	
	public int deleteMemberGroup(Long memberGroupId) ;
	
	public MemberGroup findMemberGroupById(Long memberGroupId) ;
	
	public EntitySet<MemberGroup> findAllMemberGroup() ;

	public EntitySet<Member> getGroupMembersById(Long memberGroupId);
	
	public int deleteMemberProjection(Long memberId, Long memberGroupId);

	public MemberProjection findMemberProjectionById(Long memberProjectionId);

	public EntitySet<MemberProjection> getMemberProjectByIds(Long memberId, Long memberGroupId);

	public boolean saveMemberProjection(Member member,MemberGroup memberGroup, EntitySet<MemberType> memberTypes);

	public MemberGroup updateMemberGroup(MemberGroup memberGroup);
}