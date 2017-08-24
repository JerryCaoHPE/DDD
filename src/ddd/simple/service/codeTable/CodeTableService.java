package ddd.simple.service.codeTable;



import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.codeTable.CodeTable;

public interface CodeTableService extends BaseServiceInterface
{
	public CodeTable saveCodeTable(CodeTable codeTable) ;
	
	public int deleteCodeTable(Long codeTableId) ;
	
	public CodeTable updateCodeTable(CodeTable codeTable) ;
	
	public CodeTable findCodeTableById(Long codeTableId) ;
	
	public EntitySet<CodeTable> findAllCodeTable() ;
	
	public EntitySet<CodeTable> findCodeTablesByKey(String codeTypeKey);

 
}