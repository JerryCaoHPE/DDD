package ddd.simple.action.codeTable;


import javax.annotation.Resource;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.service.codeTable.CodeTableService;

@Action
@RequestMapping("/CodeTable")
@Controller
public class CodeTableAction
{
	@Resource(name="codeTableServiceBean")
	private CodeTableService codeTableService;
	
	public CodeTable saveCodeTable(CodeTable codeTable)
	{
		try {
			CodeTable saveCodeTable = this.codeTableService.saveCodeTable(codeTable);
			return saveCodeTable;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteCodeTable(Long codeTableId){
		
		try {
			return this.codeTableService.deleteCodeTable(codeTableId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public CodeTable updateCodeTable(CodeTable codeTable) {
		try {
			CodeTable updateCodeTable = this.codeTableService.updateCodeTable(codeTable);
			return updateCodeTable;
		} catch (DDDException e) {
			throw e;
		}
	}

	public CodeTable findCodeTableById(Long codeTableId){
		try {
			CodeTable findCodeTable = this.codeTableService.findCodeTableById(codeTableId);
			if(findCodeTable.getCodeType()!=null){
				findCodeTable.getCodeType().getName();
			}
			return  findCodeTable;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<CodeTable> findAllCodeTable(){
		try{
			EntitySet<CodeTable> allCodeTable = this.codeTableService.findAllCodeTable();
			return allCodeTable;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<CodeTable> findCodeTablesByKey(String codeTypeKey) {

		try {
			EntitySet<CodeTable> codeTables = this.codeTableService.findCodeTablesByKey(codeTypeKey);
			return codeTables;
		} catch (DDDException e) {
			throw e;
		}
	}

}