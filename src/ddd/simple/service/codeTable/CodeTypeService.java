package ddd.simple.service.codeTable;

import java.util.ArrayList;

import ddd.base.codegenerator.generator.CodeTableConfig;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.codeTable.CodeType;

public interface CodeTypeService extends BaseServiceInterface
{
	public CodeType saveCodeType(CodeType codeType) ;
	
	public int deleteCodeType(Long codeTypeId) ;
	
	public CodeType updateCodeType(CodeType codeType) ;
	
	public CodeType findCodeTypeById(Long codeTypeId) ;
	
	public EntitySet<CodeType> findAllCodeType() ;

	public boolean createCodeTable(ArrayList<CodeTableConfig> codeTableConfigs);

	public CodeType findCodeTypeByType(String type);
 
}