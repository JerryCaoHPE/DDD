package  ddd.simple.dao.member.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.member.Member;
import ddd.simple.dao.member.MemberDao;

@Service
public class MemberDaoBean extends BaseDao implements MemberDao
{
	@Override
	public Member saveMember(Member member)  throws Exception{
		return this.save(member);
	}

	@Override
	public int deleteMember(Long memberId)  throws Exception{
		return this.deleteById(memberId,Member.class);
	}

	@Override
	public Member updateMember(Member member)  throws Exception{
		return this.update(member);
	}

	@Override
	public Member findMemberById(Long memberId)  throws Exception{
		return this.query(memberId, Member.class);
	}
	
	@Override
	public EntitySet<Member> findAllMember() throws Exception {
		return this.query("1=1",Member.class);
	}

	@Override
	public Member findMemberByNameAndPassword(String name, String password) throws Exception {
		String sqlCheck = "name = '"+name+"' and password = '"+password+"'";
		return this.queryOne(sqlCheck, Member.class);
	}
	
	@Override
	public Member checkOrganization(String name, String password) throws Exception {
		String sqlCheck = "name = '"+name+"' and password = '"+password+"'";
		return this.queryOne(sqlCheck, Member.class);
	}

	@Override
	public Member editPassword(Long memberId, String oldPassword,String newPassword) {
		Member loginMember = checkOldPassword(memberId,oldPassword);
		if(loginMember!=null){
			loginMember.setPassword(newPassword);
			try {
				this.updateMember(loginMember);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loginMember;
	}
	public Member checkOldPassword(Long memberId, String oldPassword) {
		String sqlCheck = "eid = '"+memberId+"' and password = '"+oldPassword+"'";
		EntitySet<Member> members;
		Member loginMember = new Member();
		loginMember = null;
		try {
			members = (EntitySet<Member>)this.query(sqlCheck, Member.class);
			if(members != null && members.size() != 0)
			{
				for (Member member : members)
				{
					loginMember = member;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginMember;
	}

	public Member findMemberByName(String name) throws Exception {
		return this.queryOne("name = '"+name+"'", Member.class);
	}
}
