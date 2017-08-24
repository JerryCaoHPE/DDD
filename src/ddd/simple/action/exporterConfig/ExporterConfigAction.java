package ddd.simple.action.exporterConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import ddd.base.exception.DDDException;
import ddd.base.exporter.ExporterEngnine;
import ddd.base.persistence.EntitySet;
import ddd.base.util.FileUtil;
import ddd.simple.entity.exporterConfig.ExporterConfig;
import ddd.simple.service.exporterConfig.ExporterConfigService;

@Action
@RequestMapping("/ExporterConfig")
@Controller
public class ExporterConfigAction
{
	@Resource(name="exporterConfigServiceBean")
	private ExporterConfigService exporterConfigService;
	
	public ExporterConfig saveExporterConfig(ExporterConfig exporterConfig)
	{
		try {
//			String configString = exporterConfig.getConfig();
//			
//			List<Long> ids = new ArrayList<Long>();
//			ids.add(Long.parseLong(exporterConfig.getRemark()));
//			
//			FileOutputStream out = null;
//			try {
//				out = new FileOutputStream("D:\\test.json");
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
			
//			new ExporterEngnine().export(configString, ids, out);
			
			ExporterConfig saveExporterConfig = this.exporterConfigService.saveExporterConfig(exporterConfig);
			return saveExporterConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteExporterConfig(Long exporterConfigId){
		
		try {
			return this.exporterConfigService.deleteExporterConfig(exporterConfigId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ExporterConfig updateExporterConfig(ExporterConfig exporterConfig) {
		try {
			ExporterConfig updateExporterConfig = this.exporterConfigService.updateExporterConfig(exporterConfig);
			return updateExporterConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ExporterConfig findExporterConfigById(Long exporterConfigId){
		try {
//			FileInputStream in = null;
//			try {
//				in = new FileInputStream("D:\\test.json");
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			new ExporterEngnine().importData(in);
			
			ExporterConfig findExporterConfig = this.exporterConfigService.findExporterConfigById(exporterConfigId);
			return  findExporterConfig;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ExporterConfig> findAllExporterConfig(){
		try{
			EntitySet<ExporterConfig> allExporterConfig = this.exporterConfigService.findAllExporterConfig();
			return allExporterConfig;
		} catch (DDDException e) {
			throw e;
		}
	}

}