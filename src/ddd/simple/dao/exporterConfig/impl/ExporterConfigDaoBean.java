package ddd.simple.dao.exporterConfig.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.exporterConfig.ExporterConfig;
import ddd.simple.dao.exporterConfig.ExporterConfigDao;

@Service
public class ExporterConfigDaoBean extends BaseDao implements ExporterConfigDao
{
	@Override
	public ExporterConfig saveExporterConfig(ExporterConfig exporterConfig) throws Exception
	{
		return this.save(exporterConfig);
	}
	
	@Override
	public int deleteExporterConfig(Long exporterConfigId) throws Exception
	{
		return this.deleteById(exporterConfigId, ExporterConfig.class);
	}
	
	@Override
	public ExporterConfig updateExporterConfig(ExporterConfig exporterConfig) throws Exception
	{
		return this.update(exporterConfig);
	}
	
	@Override
	public ExporterConfig findExporterConfigById(Long exporterConfigId) throws Exception
	{
		return this.query(exporterConfigId, ExporterConfig.class);
	}
	
	@Override
	public EntitySet<ExporterConfig> findAllExporterConfig() throws Exception
	{
		return this.query("1=1", ExporterConfig.class);
	}
	
	@Override
	public ExporterConfig findExporterConfigByKey(String key) throws Exception
	{
		EntitySet<ExporterConfig> set = this.query("configName = '" + key + "'", ExporterConfig.class);
		if(set.size()>1){
			throw new Exception("实体ExporterConfig属性configName重复!");
		}
		
		return set.isEmpty() ? null:set.iterator().next();
	}
}
