package ddd.simple.dao.codeTable;



import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.codeTable.CodeTable;

public interface CodeTableDao extends BaseDaoInterface
{
	public CodeTable saveCodeTable(CodeTable codeTable) throws Exception;
	
	public int deleteCodeTable(Long codeTableId) throws Exception;
	
	public CodeTable updateCodeTable(CodeTable codeTable) throws Exception;
	
	public CodeTable findCodeTableById(Long codeTableId) throws Exception;
	
	public EntitySet<CodeTable> findAllCodeTable() throws Exception;
	
	public EntitySet<CodeTable> findCodeTablesByKey(String codeTypeKey);
}
