package ddd.simple.service.codeTable.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.codegenerator.generator.CodeTableConfig;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.codeTable.CodeTypeDao;
import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.entity.codeTable.CodeType;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.codeTable.CodeTableService;
import ddd.simple.service.codeTable.CodeTypeService;

@Service
public class CodeTypeServiceBean extends BaseService implements CodeTypeService
{

	@Resource(name="codeTypeDaoBean")
	private CodeTypeDao codeTypeDao;
	
	@Resource(name="codeTableServiceBean")
	private CodeTableService codeTableService;
	
	@Override
	public CodeType saveCodeType(CodeType codeType) 
	{
		try {
			return this.codeTypeDao.saveCodeType(codeType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveCodeType", e.getMessage(), e);
		}
	}

	@Override
	public int deleteCodeType(Long codeTypeId) {
		try {
			return this.codeTypeDao.deleteCodeType(codeTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteCodeType", e.getMessage(), e);
		}
		
	}

	@Override
	public CodeType updateCodeType(CodeType codeType) {
		try {
			return this.codeTypeDao.updateCodeType(codeType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateCodeType", e.getMessage(), e);
		}
	}

	@Override
	public CodeType findCodeTypeById(Long codeTypeId) {
		try {
			return this.codeTypeDao.findCodeTypeById(codeTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findCodeTypeById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<CodeType> findAllCodeType() {
		try{
			return this.codeTypeDao.findAllCodeType();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllCodeType", e.getMessage(), e);
		}
	}
	@Override
	public CodeType findCodeTypeByType(String type){
		try
		{
			return this.codeTypeDao.findCodeTypeByType(type);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findCodeTypeByType", e.getMessage(), e);
		}
	}
	
	public boolean checkCodeTableExist(String type){
		CodeType codeType = this.findCodeTypeByType(type);
		if(codeType == null)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean createCodeTable(ArrayList<CodeTableConfig> codeTableConfigs){
		for(int i =0;i<codeTableConfigs.size();i++){
			CodeTableConfig codeTableConfig = codeTableConfigs.get(i);
			String type = codeTableConfig.getCodeTypeKey();
			if(this.checkCodeTableExist(type)){
				//不存在
				CodeType codeType = new CodeType();
				codeType.setName(codeTableConfig.getCodeTypeName());
				codeType.setCodeTypeKey(codeTableConfig.getCodeTypeKey());
				codeType = this.saveCodeType(codeType);
				List<String> codeTables = codeTableConfig.getCodeTables();
				for(int j=0;j<codeTables.size();j++){
					String temp = codeTables.get(j);
					CodeTable codeTable = new CodeTable();
					codeTable.setName(temp);
					codeTable.setValue(temp);
					codeTable.setCodeType(codeType);
					this.codeTableService.saveCodeTable(codeTable);
				}
			}
		}
		return false;
	}

}
