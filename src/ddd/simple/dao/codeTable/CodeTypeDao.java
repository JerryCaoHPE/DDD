package ddd.simple.dao.codeTable;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.codeTable.CodeType;

public interface CodeTypeDao extends BaseDaoInterface
{
	public CodeType saveCodeType(CodeType codeType) throws Exception;
	
	public int deleteCodeType(Long codeTypeId) throws Exception;
	
	public CodeType updateCodeType(CodeType codeType) throws Exception;
	
	public CodeType findCodeTypeById(Long codeTypeId) throws Exception;
	
	public EntitySet<CodeType> findAllCodeType() throws Exception;

	public CodeType findCodeTypeByType(String type) throws Exception;
}
