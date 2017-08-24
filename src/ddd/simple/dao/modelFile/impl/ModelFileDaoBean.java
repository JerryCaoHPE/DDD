package ddd.simple.dao.modelFile.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;

import org.springframework.stereotype.Service;

import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.dao.modelFile.ModelFileDao;

@Service
public class ModelFileDaoBean extends BaseDao implements ModelFileDao
{
	@Override
	public ModelFile saveModelFile(ModelFile modelFile)  throws Exception{
		return this.save(modelFile);
	}

	@Override
	public int deleteModelFile(Long modelFileId)  throws Exception{
		return this.deleteById(modelFileId,ModelFile.class);
	}

	@Override
	public ModelFile updateModelFile(ModelFile modelFile)  throws Exception{
		return this.update(modelFile);
	}

	@Override
	public ModelFile findModelFileById(Long modelFileId)  throws Exception{
		return this.query(modelFileId, ModelFile.class);
	}
	
	@Override
	public EntitySet<ModelFile> findAllModelFile() throws Exception {
		return this.query("1=1",ModelFile.class);
	}

	@Override
	public EntitySet<ModelFile> findModelFileByType(String type) throws Exception {
		return this.query("type = '"+type+"'", ModelFile.class);
	}
	
	
	@Override
	public ModelFile findModelFileByKey(String key) throws Exception
	{
		EntitySet<ModelFile> modelFiles =  this.query(" modelKey = '"+ key +"'", ModelFile.class);
		return modelFiles.iterator().next();
	}
}
