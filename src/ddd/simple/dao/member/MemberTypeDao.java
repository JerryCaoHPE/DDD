package ddd.simple.dao.member;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.member.MemberType;

public interface MemberTypeDao extends BaseDaoInterface
{
	public MemberType saveMemberType(MemberType memberType) throws Exception;
	
	public int deleteMemberType(MemberType memberType) throws Exception;
	
	public MemberType updateMemberType(MemberType memberType) throws Exception;
	
	public MemberType findMemberTypeById(Long memberTypeId) throws Exception;
	
	public EntitySet<MemberType> findAllMemberType() throws Exception;
}
