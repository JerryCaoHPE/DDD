package ddd.simple.dao.member.impl;

import javax.annotation.Resource;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.member.MemberType;
import ddd.simple.dao.member.MemberTypeDao;

@Service
public class MemberTypeDaoBean extends BaseDao implements MemberTypeDao
{
	
	@Override
	public MemberType saveMemberType(MemberType memberType)  throws Exception{
		return this.save(memberType);
	}

	@Override
	public int deleteMemberType(MemberType memberType)  throws Exception{
		return this.delete(memberType);
	}

	@Override
	public MemberType updateMemberType(MemberType memberType)  throws Exception{
		return this.update(memberType);
	}

	@Override
	public MemberType findMemberTypeById(Long memberTypeId)  throws Exception{
		return this.query(memberTypeId, MemberType.class);
	}
	
	@Override
	public EntitySet<MemberType> findAllMemberType() throws Exception {
		return this.query("1=1",MemberType.class);
	}
}
