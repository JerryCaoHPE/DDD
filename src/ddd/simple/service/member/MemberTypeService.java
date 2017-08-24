package ddd.simple.service.member;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.member.MemberType;

public interface MemberTypeService extends BaseServiceInterface
{
	public MemberType saveMemberType(MemberType memberType) ;
	
	public int deleteMemberType(MemberType memberType) ;
	
	public MemberType updateMemberType(MemberType memberType) ;
	
	public MemberType findMemberTypeById(Long memberTypeId) ;
	
	public EntitySet<MemberType> findAllMemberType() ;
 
}