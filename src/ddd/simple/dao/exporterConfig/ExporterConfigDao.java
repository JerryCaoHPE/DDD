package ddd.simple.dao.exporterConfig;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.exporterConfig.ExporterConfig;

public interface ExporterConfigDao extends BaseDaoInterface
{
	public ExporterConfig saveExporterConfig(ExporterConfig exporterConfig) throws Exception;
	
	public int deleteExporterConfig(Long exporterConfigId) throws Exception;
	
	public ExporterConfig updateExporterConfig(ExporterConfig exporterConfig) throws Exception;
	
	public ExporterConfig findExporterConfigById(Long exporterConfigId) throws Exception;
	
	public EntitySet<ExporterConfig> findAllExporterConfig() throws Exception;
	
	public ExporterConfig findExporterConfigByKey(String key) throws Exception;
}
