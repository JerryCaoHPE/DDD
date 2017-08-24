package ddd.simple.dao.codeTable.impl;


import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.dao.codeTable.CodeTableDao;

@Service
public class CodeTableDaoBean extends BaseDao implements CodeTableDao
{
	@Override
	public CodeTable saveCodeTable(CodeTable codeTable)  throws Exception{
		return this.save(codeTable);
	}

	@Override
	public int deleteCodeTable(Long codeTableId)  throws Exception{
		return this.deleteById(codeTableId,CodeTable.class);
	}

	@Override
	public CodeTable updateCodeTable(CodeTable codeTable)  throws Exception{
		return this.update(codeTable);
	}

	@Override
	public CodeTable findCodeTableById(Long codeTableId)  throws Exception{
		return this.query(codeTableId, CodeTable.class);
	}
	
	@Override
	public EntitySet<CodeTable> findAllCodeTable() throws Exception {
		return this.query("",CodeTable.class);
	}
	
	@Override
	public EntitySet<CodeTable> findCodeTablesByKey(String codeTypeKey) {
		
	String where = "codeTypeId in (SELECT EId from codetype d where d.codeTypeKey ='"+codeTypeKey+"') order by displayOrder";
	   
		EntitySet<CodeTable> codeTableSet = null;
		try {
			codeTableSet = this.query(where, CodeTable.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DDDException("findCodeTablesByKey", e.getMessage(), e);
		}
		
		
		return codeTableSet;
	}
}
