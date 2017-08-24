package ddd.simple.dao.memberProjection;

import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.memberProjection.MemberProjection;

public interface MemberProjectionDao extends BaseDaoInterface
{
	public MemberProjection saveMemberProjection(MemberProjection memberProjection) throws Exception;
	
	public int deleteMemberProjection(Long memberProjectionId) throws Exception;
	
	public MemberProjection updateMemberProjection(MemberProjection memberProjection) throws Exception;
	
	public MemberProjection findMemberProjectionById(Long memberProjectionId) throws Exception;
	
	public EntitySet<MemberProjection> findAllMemberProjection() throws Exception;
	
	public EntitySet<MemberProjection> findProjectionByMember(Long memberId) throws Exception;

	public EntitySet<MemberProjection> findMemberProsByGroupId(Long memberGroupId) throws Exception;

	public int deleteMemberProjectionByIds(Long memberId, Long memberGroupId) throws Exception;
	
	public int deleteMemberProjectionByMember(Long memberId) throws Exception;

	public EntitySet<MemberProjection> getMemberProjectIdByIds(Long memberId, Long memberGroupId) throws Exception;

	EntitySet<MemberProjection> findMemberProsByTypeId(Long memberTypeId)throws Exception;
}
