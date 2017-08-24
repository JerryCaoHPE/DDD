package ddd.simple.service.modelFile;

import java.util.List;
import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.entity.modelFile.StatsReport;

public interface ModelFileService extends BaseServiceInterface
{
	public ModelFile saveModelFile(ModelFile modelFile) ;
	
	public int deleteModelFile(Long modelFileId) ;
	
	public ModelFile updateModelFile(ModelFile modelFile) ;
	
	public ModelFile findModelFileById(Long modelFileId) ;
	
	public EntitySet<ModelFile> findAllModelFile() ;

	public ModelFile findModelFileByKey(String key);

	public List<StatsReport> getAllReportFormGroupByType();
 
}