package ddd.simple.service.modelFile.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.modelFile.ModelFileDao;
import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.entity.codeTable.CodeType;
import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.entity.modelFile.StatsReport;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.codeTable.CodeTableService;
import ddd.simple.service.codeTable.CodeTypeService;
import ddd.simple.service.modelFile.ModelFileService;

@Service
public class ModelFileServiceBean extends BaseService implements ModelFileService
{

	@Resource(name="modelFileDaoBean")
	private ModelFileDao modelFileDao;
	
	@Resource(name="codeTypeServiceBean")
	private CodeTypeService codeTypeService;
	
	@Resource(name="codeTableServiceBean")
	private CodeTableService codeTableService;
	
	@Override
	public ModelFile saveModelFile(ModelFile modelFile) 
	{
		try {
			return this.modelFileDao.saveModelFile(modelFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveModelFile", e.getMessage(), e);
		}
	}

	@Override
	public int deleteModelFile(Long modelFileId) {
		try {
			return this.modelFileDao.deleteModelFile(modelFileId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteModelFile", e.getMessage(), e);
		}
		
	}

	@Override
	public ModelFile updateModelFile(ModelFile modelFile) {
		try {
			return this.modelFileDao.updateModelFile(modelFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateModelFile", e.getMessage(), e);
		}
	}

	@Override
	public ModelFile findModelFileById(Long modelFileId) {
		try {
			return this.modelFileDao.findModelFileById(modelFileId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelFileById", e.getMessage(), e);
		}
	}
	
	@Override
	public ModelFile findModelFileByKey(String key) {
		try {
			return this.modelFileDao.findModelFileByKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelFileById", e.getMessage(), e);
		}
	}
	
	public EntitySet<ModelFile> findModelFileByType(String type){
		try {
			return this.modelFileDao.findModelFileByType(type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findModelFileByType", e.getMessage(), e);
		}
		
	}
	
	
	@Override
	public EntitySet<ModelFile> findAllModelFile() {
		try{
			return this.modelFileDao.findAllModelFile();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllModelFile", e.getMessage(), e);
		}
	}
	
	@Override
	public List<StatsReport> getAllReportFormGroupByType(){
		//Map<String,EntitySet<ModelFile>> result = new HashMap<String,EntitySet<ModelFile>>();
		List<StatsReport> result = new ArrayList<StatsReport>();
		CodeType  codeType = this.codeTypeService.findCodeTypeByType("modelFileType");
		if(codeType == null){
			return null;
		}
		EntitySet<CodeTable> codeTables = this.codeTableService.findCodeTablesByKey(codeType.getCodeTypeKey());
		Iterator<CodeTable> ite = codeTables.iterator();
		while(ite.hasNext()){
			CodeTable temp = ite.next();
			StatsReport statsReport = new StatsReport();
			statsReport.setName(temp.getName());
			statsReport.setModelFiles(this.findModelFileByType(temp.getValue()));
			result.add(statsReport);
		}
		return result;
	}

}
