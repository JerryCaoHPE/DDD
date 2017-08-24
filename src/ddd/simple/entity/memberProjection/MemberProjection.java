package ddd.simple.entity.memberProjection;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.member.Member;
import ddd.simple.entity.member.MemberType;
import ddd.simple.entity.memberGroup.MemberGroup;



@ddd.base.annotation.Entity(label="会员映射表",name="memberProjection")
public class MemberProjection extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(FKName="MemberP_FK_M",label="会员",name="memberId",nullable=false,comment="")
	private Member Member;

	@Column(FKName="MemberP_FK_MG",label="会员组",name="memberGroupId",nullable=false,comment="")
	private MemberGroup MemberGroup;

	@Column(FKName="MemberP_FK_MT",label="会员类型",name="memberTypeId",nullable=false,comment="")
	private MemberType MemberType;


	public Member getMember() {
		lazyLoad();
		return this.Member;
	}

	public void setMember(Member Member) {
		this.Member = Member;
	}

	public MemberGroup getMemberGroup() {
		lazyLoad();
		return this.MemberGroup;
	}

	public void setMemberGroup(MemberGroup MemberGroup) {
		this.MemberGroup = MemberGroup;
	}

	public MemberType getMemberType() {
		lazyLoad();
		return this.MemberType;
	}

	public void setMemberType(MemberType MemberType) {
		this.MemberType = MemberType;
	}


	
}