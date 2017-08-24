package ddd.simple.dao.memberProjection.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.memberProjection.MemberProjectionDao;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.memberProjection.MemberProjection;
import net.sf.json.JSONObject;

@Service
public class MemberProjectionDaoBean extends BaseDao implements MemberProjectionDao
{
	@Override
	public MemberProjection saveMemberProjection(MemberProjection memberProjection)  throws Exception{
		return this.save(memberProjection);
	}

	@Override
	public int deleteMemberProjection(Long memberProjectionId)  throws Exception{
		return this.deleteById(memberProjectionId,MemberProjection.class);
	}

	@Override
	public MemberProjection updateMemberProjection(MemberProjection memberProjection)  throws Exception{
		return this.update(memberProjection);
	}

	@Override
	public MemberProjection findMemberProjectionById(Long memberProjectionId)  throws Exception{
		return this.query(memberProjectionId, MemberProjection.class);
	}
	
	@Override
	public EntitySet<MemberProjection> findAllMemberProjection() throws Exception {
		return this.query("1=1",MemberProjection.class);
	}

	@Override
	public EntitySet<MemberProjection> findProjectionByMember(Long memberId) throws Exception {
		if(memberId == null || memberId.equals("0")){
			return null;
		}
		String sql = "1 != 1 or memberId = '"+memberId+"'";
		return this.query(sql, MemberProjection.class);
	}

	@Override
	public EntitySet<MemberProjection> findMemberProsByGroupId(Long memberGroupId) throws Exception {
		String sqlCheck = "memberGroupId = "+memberGroupId+"";
		return this.query(sqlCheck, MemberProjection.class);
	}
	
	@Override
	public EntitySet<MemberProjection> findMemberProsByTypeId(Long memberTypeId) throws Exception {
		String sqlCheck = "memberTypeId = "+memberTypeId+"";
		return this.query(sqlCheck, MemberProjection.class);
	}
	@Override
	public int deleteMemberProjectionByIds(Long memberId, Long memberGroupId) throws Exception {
		String where = "memberId = "+memberId+" and memberGroupId = "+memberGroupId+"";
		return this.deleteByWhere(where,MemberProjection.class);
	}

	@Override
	public int deleteMemberProjectionByMember(Long memberId) throws Exception {
		// TODO Auto-generated method stub
		if(memberId == null || memberId.equals("0")){
			return 0;
		}
		String where = "memberId = ( select m.EId from member m where 1 != 1 or memberId = '"+memberId+"'";
		return this.deleteByWhere(where+")", MemberProjection.class);
	}
	@Override
	public EntitySet<MemberProjection> getMemberProjectIdByIds(Long memberId, Long memberGroupId) throws Exception {
		String where = "memberId = "+memberId+" and memberGroupId = "+memberGroupId+"";
		return this.query(where, MemberProjection.class);
	}
}
