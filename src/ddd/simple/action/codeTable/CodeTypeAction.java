package ddd.simple.action.codeTable;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.codeTable.CodeType;
import ddd.simple.service.codeTable.CodeTypeService;

@Action
@RequestMapping("/CodeType")
@Controller
public class CodeTypeAction
{
	@Resource(name="codeTypeServiceBean")
	private CodeTypeService codeTypeService;
	
	public CodeType saveCodeType(CodeType codeType)
	{
		try {
			CodeType saveCodeType = this.codeTypeService.saveCodeType(codeType);
			return saveCodeType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteCodeType(Long codeTypeId){
		
		try {
			return this.codeTypeService.deleteCodeType(codeTypeId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public CodeType updateCodeType(CodeType codeType) {
		try {
			CodeType updateCodeType = this.codeTypeService.updateCodeType(codeType);
			return updateCodeType;
		} catch (DDDException e) {
			throw e;
		}
	}

	public CodeType findCodeTypeById(Long codeTypeId){
		try {
			CodeType findCodeType = this.codeTypeService.findCodeTypeById(codeTypeId);
			return  findCodeType;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<CodeType> findAllCodeType(){
		try{
			EntitySet<CodeType> allCodeType = this.codeTypeService.findAllCodeType();
			return allCodeType;
		} catch (DDDException e) {
			throw e;
		}
	}

}