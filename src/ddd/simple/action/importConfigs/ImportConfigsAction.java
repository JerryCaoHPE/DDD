package ddd.simple.action.importConfigs;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.importConfigs.ConfigItem;
import ddd.simple.entity.importConfigs.ImportConfigs;
import ddd.simple.service.importConfigs.ImportConfigsService;

@Action
@RequestMapping("/ImportConfigs")
@Controller
public class ImportConfigsAction
{
	@Resource(name="importConfigsServiceBean")
	private ImportConfigsService importConfigsService;
	
	public ImportConfigs saveImportConfigs(ImportConfigs importConfigs)
	{
		try {
			ImportConfigs saveImportConfigs = this.importConfigsService.saveImportConfigs(importConfigs);
			return saveImportConfigs;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteImportConfigs(Long importConfigsId){
		
		try {
			return this.importConfigsService.deleteImportConfigs(importConfigsId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ImportConfigs updateImportConfigs(ImportConfigs importConfigs) {
		try {
			ImportConfigs updateImportConfigs = this.importConfigsService.updateImportConfigs(importConfigs);
			return updateImportConfigs;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ImportConfigs findImportConfigsById(Long importConfigsId){
		try {
			ImportConfigs findImportConfigs = this.importConfigsService.findImportConfigsById(importConfigsId);
			String context = findImportConfigs.getConfigContext();
			if(!("}".equals(context.substring(context.length()-1)))){
				context = context.substring(0, context.length()-1);
			}
			findImportConfigs.setConfigContext(context);
			return  findImportConfigs;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ImportConfigs> findAllImportConfigs(){
		try{
			EntitySet<ImportConfigs> allImportConfigs = this.importConfigsService.findAllImportConfigs();
			return allImportConfigs;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public ImportConfigs findImportConfigByKey(String importConfigKey){
		try {
			return this.importConfigsService.findImportConfigByKey(importConfigKey);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	
	public List<ConfigItem> getAllField(String entityName){
		try {
			return this.importConfigsService.getAllField(entityName);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public void createExcelTemplate(ImportConfigs importConfigs){
		try{
			 this.importConfigsService.createExcelTemplate(importConfigs);
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public void createAllExcelTemplate(){
		try{
			 this.importConfigsService.createAllExcelTemplate();
		} catch (DDDException e) {
			throw e;
		}
	}
}