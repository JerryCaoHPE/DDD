package ddd.simple.dao.codeTable.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.codeTable.CodeType;
import ddd.simple.dao.codeTable.CodeTypeDao;

@Service
public class CodeTypeDaoBean extends BaseDao implements CodeTypeDao
{
	@Override
	public CodeType saveCodeType(CodeType codeType)  throws Exception{
		return this.save(codeType);
	}

	@Override
	public int deleteCodeType(Long codeTypeId)  throws Exception{
		return this.deleteById(codeTypeId,CodeType.class);
	}

	@Override
	public CodeType updateCodeType(CodeType codeType)  throws Exception{
		return this.update(codeType);
	}

	@Override
	public CodeType findCodeTypeById(Long codeTypeId)  throws Exception{
		return this.query(codeTypeId, CodeType.class);
	}
	
	@Override
	public EntitySet<CodeType> findAllCodeType() throws Exception {
		return this.query("",CodeType.class);
	}
	
	@Override
	public CodeType findCodeTypeByType(String type) throws Exception{
		return this.queryOne("codeTypeKey = '"+type+"'", CodeType.class);
	}
}
