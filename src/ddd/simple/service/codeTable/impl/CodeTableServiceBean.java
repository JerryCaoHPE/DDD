package ddd.simple.service.codeTable.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.codeTable.CodeTableDao;
import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.cache.JSONCacheEngine;
import ddd.simple.service.codeTable.CodeTableService;

@Service
public class CodeTableServiceBean extends BaseService implements CodeTableService
{

	@Resource(name="codeTableDaoBean")
	private CodeTableDao codeTableDao;
	
	@Override
	public CodeTable saveCodeTable(CodeTable codeTable) 
	{
		try {
			
			codeTable =  this.codeTableDao.saveCodeTable(codeTable);
			this.refreshCache(codeTable);
			return codeTable;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveCodeTable", e.getMessage(), e);
		}
	}

	private void refreshCache(CodeTable codeTable)
	{
		EntitySet<CodeTable> codeTables = this.findCodeTablesByKey(codeTable.getCodeType().getName());
		JSONCacheEngine.putCache("codeTable", codeTable.getCodeType().getName(), codeTables);
	}
	@Override
	public int deleteCodeTable(Long codeTableId) {
		try {
			return this.codeTableDao.deleteCodeTable(codeTableId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteCodeTable", e.getMessage(), e);
		}
		
	}

	@Override
	public CodeTable updateCodeTable(CodeTable codeTable) {
		try {
			codeTable = this.codeTableDao.updateCodeTable(codeTable);
			this.refreshCache(codeTable);
			return codeTable;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateCodeTable", e.getMessage(), e);
		}
	}

	@Override
	public CodeTable findCodeTableById(Long codeTableId) {
		try {
			return this.codeTableDao.findCodeTableById(codeTableId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findCodeTableById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<CodeTable> findAllCodeTable() {
		try{
			return this.codeTableDao.findAllCodeTable();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllCodeTable", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<CodeTable> findCodeTablesByKey(String codeTypeKey) {
		try {
			return this.codeTableDao.findCodeTablesByKey(codeTypeKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findCodeTables", e.getMessage(), e);

		}
	}

}
