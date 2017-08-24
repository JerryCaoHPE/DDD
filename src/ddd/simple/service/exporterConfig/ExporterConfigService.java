package ddd.simple.service.exporterConfig;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.exporterConfig.ExporterConfig;

public interface ExporterConfigService extends BaseServiceInterface
{
	public ExporterConfig saveExporterConfig(ExporterConfig exporterConfig) ;
	
	public int deleteExporterConfig(Long exporterConfigId) ;
	
	public ExporterConfig updateExporterConfig(ExporterConfig exporterConfig) ;
	
	public ExporterConfig findExporterConfigById(Long exporterConfigId) ;
	
	public EntitySet<ExporterConfig> findAllExporterConfig() ;
	
	public ExporterConfig findExporterConfigByKey(String key);
 
}