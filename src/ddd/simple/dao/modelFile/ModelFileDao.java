package ddd.simple.dao.modelFile;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.modelFile.ModelFile;

public interface ModelFileDao extends BaseDaoInterface
{
	public ModelFile saveModelFile(ModelFile modelFile) throws Exception;
	
	public int deleteModelFile(Long modelFileId) throws Exception;
	
	public ModelFile updateModelFile(ModelFile modelFile) throws Exception;
	
	public ModelFile findModelFileById(Long modelFileId) throws Exception;
	
	public EntitySet<ModelFile> findAllModelFile() throws Exception;

	public ModelFile findModelFileByKey(String key) throws Exception;

	public EntitySet<ModelFile> findModelFileByType(String type)throws Exception;

}
