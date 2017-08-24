package ddd.simple.service.exporterConfig.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.exporterConfig.ExporterConfig;
import ddd.simple.dao.exporterConfig.ExporterConfigDao;
import ddd.simple.service.exporterConfig.ExporterConfigService;

@Service
public class ExporterConfigServiceBean extends BaseService implements ExporterConfigService
{
	
	@Resource(name = "exporterConfigDaoBean")
	private ExporterConfigDao exporterConfigDao;
	
	@Override
	public ExporterConfig saveExporterConfig(ExporterConfig exporterConfig)
	{
		try
		{
			return this.exporterConfigDao.saveExporterConfig(exporterConfig);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveExporterConfig", e.getMessage(), e);
		}
	}
	
	@Override
	public int deleteExporterConfig(Long exporterConfigId)
	{
		try
		{
			return this.exporterConfigDao.deleteExporterConfig(exporterConfigId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("deleteExporterConfig", e.getMessage(), e);
		}
		
	}
	
	@Override
	public ExporterConfig updateExporterConfig(ExporterConfig exporterConfig)
	{
		try
		{
			return this.exporterConfigDao.updateExporterConfig(exporterConfig);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateExporterConfig", e.getMessage(), e);
		}
	}
	
	@Override
	public ExporterConfig findExporterConfigById(Long exporterConfigId)
	{
		try
		{
			return this.exporterConfigDao.findExporterConfigById(exporterConfigId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findExporterConfigById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ExporterConfig> findAllExporterConfig()
	{
		try
		{
			return this.exporterConfigDao.findAllExporterConfig();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAllExporterConfig", e.getMessage(), e);
		}
	}
	
	@Override
	public ExporterConfig findExporterConfigByKey(String key)
	{
		try
		{
			return this.exporterConfigDao.findExporterConfigByKey(key);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findExporterConfigByKey", e.getMessage(), e);
		}
	}
	
}
