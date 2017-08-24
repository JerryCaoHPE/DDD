package ddd.simple.service.member.impl;

import java.sql.BatchUpdateException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import ddd.base.Config;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.member.MemberType;
import ddd.simple.entity.memberProjection.MemberProjection;
import ddd.simple.dao.member.MemberTypeDao;
import ddd.simple.dao.memberProjection.MemberProjectionDao;
import ddd.simple.service.member.MemberTypeService;

@Service
public class MemberTypeServiceBean extends BaseService implements MemberTypeService
{

	@Resource(name="memberTypeDaoBean")
	private MemberTypeDao memberTypeDao;
	@Resource(name = "memberProjectionDaoBean")
	private MemberProjectionDao memberProjectionDao;
	
	@Override
	public MemberType saveMemberType(MemberType memberType) 
	{
		try {
			Date operateDate = new Date();
			memberType.setOperateDate(operateDate);
			return this.memberTypeDao.saveMemberType(memberType);
		}catch (BatchUpdateException e){
			e.printStackTrace();
			throw new DDDException("saveMemberType", "会员类型编码已存在", e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveMemberType", e.getMessage(), e);
		}
	}

	@Override
	public int deleteMemberType(MemberType memberType) {
		try {
			if(memberType==null)
				return -1;
			Long defaultMemberTypeId = Long.parseLong(Config.get("defaultMemberTypeId"));
			int result = 0;
			if(defaultMemberTypeId!=null){
				EntitySet<MemberProjection> memberProjections = memberProjectionDao.findMemberProsByTypeId(memberType.getEId());
				MemberType defalutMemberType = memberTypeDao.findMemberTypeById(defaultMemberTypeId);
				this.reValueMemberProjection(memberProjections,defalutMemberType);
				result = this.memberTypeDao.deleteMemberType(memberType);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteMemberType", e.getMessage(), e);
		}
	}

	private void reValueMemberProjection(EntitySet<MemberProjection> memberProjections,MemberType defalutMemberType) {
		if(memberProjections.size()<=0)
			return;
		for (MemberProjection memberProjection:memberProjections) {
			memberProjection.setMemberType(defalutMemberType);
			try {
				memberProjectionDao.updateMemberProjection(memberProjection);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DDDException("reValueMemberProjection", e.getMessage(), e);
			}
		}
	}

	@Override
	public MemberType updateMemberType(MemberType memberType) {
		try {
			Date operateDate = new Date();
			memberType.setOperateDate(operateDate);
			return this.memberTypeDao.updateMemberType(memberType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateMemberType", e.getMessage(), e);
		}
	}

	@Override
	public MemberType findMemberTypeById(Long memberTypeId) {
		try {
			MemberType memberType = this.memberTypeDao.findMemberTypeById(memberTypeId);
			if(memberType.getRole()!= null)
			{
				memberType.getRole().getName();
			}
			return memberType;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findMemberTypeById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<MemberType> findAllMemberType() {
		try{
			return this.memberTypeDao.findAllMemberType();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllMemberType", e.getMessage(), e);
		}
	}
}
