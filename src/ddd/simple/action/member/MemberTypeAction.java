package ddd.simple.action.member;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.member.MemberType;
import ddd.simple.service.member.MemberTypeService;

@Action
@RequestMapping("/MemberType")
@Controller
public class MemberTypeAction
{
	@Resource(name="memberTypeServiceBean")
	private MemberTypeService memberTypeService;
	
	public MemberType saveMemberType(MemberType memberType)
	{
		try {
			MemberType saveMemberType = this.memberTypeService.saveMemberType(memberType);
			return saveMemberType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteMemberType(MemberType memberType){
		
		try {
			return this.memberTypeService.deleteMemberType(memberType);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public MemberType updateMemberType(MemberType memberType) {
		try {
			MemberType updateMemberType = this.memberTypeService.updateMemberType(memberType);
			return updateMemberType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public MemberType findMemberTypeById(Long memberTypeId){
		try {
			MemberType findMemberType = this.memberTypeService.findMemberTypeById(memberTypeId);
			return  findMemberType;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<MemberType> findAllMemberType(){
		try{
			EntitySet<MemberType> allMemberType = this.memberTypeService.findAllMemberType();
			return allMemberType;
		} catch (DDDException e) {
			throw e;
		}
	}

}