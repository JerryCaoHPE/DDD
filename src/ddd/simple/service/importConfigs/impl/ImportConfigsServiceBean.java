package ddd.simple.service.importConfigs.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.annotation.ColumnInfo;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.JSONUtil;
import ddd.simple.dao.importConfigs.ImportConfigsDao;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.importConfigs.Config;
import ddd.simple.entity.importConfigs.ConfigItem;
import ddd.simple.entity.importConfigs.ImportConfigs;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.importConfigs.ImportConfigsService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service
public class ImportConfigsServiceBean extends BaseService implements ImportConfigsService
{
	
	@Resource(name = "importConfigsDaoBean")
	private ImportConfigsDao importConfigsDao;
	@Resource(name = "attachmentServiceBean")
	private AttachmentService attachmentService;
	private String filePath = "";
	
	@Override
	public ImportConfigs saveImportConfigs(ImportConfigs importConfigs)
	{
		try
		{
			ImportConfigs savedImportConfigs = this.importConfigsDao.saveImportConfigs(importConfigs);
			return savedImportConfigs;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("saveImportConfigs", e.getMessage(), e);
		}
	}
	
	private boolean createExcelForConfigs(ImportConfigs savedImportConfigs){
		try{
			filePath = this.attachmentService.getFilePathFormSystemConfig();
			
			String entityId = String.valueOf(savedImportConfigs.getEId());
			String entityName = savedImportConfigs.getImportConfigKey();
			
			File file = this.createExcelFile(entityName, entityId, savedImportConfigs);
			
			this.attachmentService.createAttachmentFromFile(entityId, entityName, savedImportConfigs.getImportConfigName()+".xls", file.length());
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			throw new DDDException("saveImportConfigs", e.getMessage(), e);
		}
	}
	
	private File createExcelFile(String entityName,String entityId,ImportConfigs importConfigs){
		EntitySet<Attachment>  attachments = this.attachmentService.findAttachmentByForm(Long.parseLong(entityId), entityName);
		
		if(attachments != null && attachments.size() !=0){
			Iterator<Attachment> ite = attachments.iterator();
			while(ite.hasNext()){
				Attachment att = ite.next();
				if(att != null){
					this.attachmentService.deleteAttachment(att.getEId());
				}
			}
		}
		
		String savePath = this.filePath+"\\"+entityName+"\\"+entityId;
		String finalFileName = entityName+"_"+entityId+"_"+(importConfigs.getImportConfigName()+".xls");
		
		try{
			File uploadFile = new File(savePath);
			
			if(!uploadFile.exists())
			{
				uploadFile.mkdirs();
			}
			
			File file = new File(savePath+"\\"+finalFileName);
			WritableWorkbook book = Workbook.createWorkbook(file);
			WritableSheet sheet = book.createSheet("sheet1", 0);
			
			String context = importConfigs.getConfigContext().replace("\r\n", "");
			
			Config config = JSONUtil.fromJSON(context, Config.class);
			List<ConfigItem> configItems  = config.getConfigItems();
			
			for(int i =0;i<configItems.size();i++){
				sheet.setColumnView(i,configItems.get(i).getColumnTitle().length()*2+4); 
				Label label = new Label(i, 0, configItems.get(i).getColumnTitle());
				if(configItems.get(i).getIsNotNull() == true){
					WritableCellFormat cellFormat1 = new WritableCellFormat(NumberFormats.TEXT);
				    cellFormat1.setBackground(Colour.YELLOW);
					label.setCellFormat(cellFormat1);
				}
				sheet.addCell(label);
			}
			
			book.write();
			book.close();
			return file;
		}catch(Exception e){
			e.printStackTrace();
			throw new DDDException("createExcelFile", e.getMessage(), e);
		}
	}
	
	@Override
	public int deleteImportConfigs(Long importConfigsId)
	{
		try
		{
			return this.importConfigsDao.deleteImportConfigs(importConfigsId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("deleteImportConfigs", e.getMessage(), e);
		}
		
	}
	
	@Override
	public ImportConfigs updateImportConfigs(ImportConfigs importConfigs)
	{
		try
		{
			ImportConfigs savedImportConfigs = this.importConfigsDao.updateImportConfigs(importConfigs);
			return savedImportConfigs;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("updateImportConfigs", e.getMessage(), e);
		}
	}
	
	@Override
	public ImportConfigs findImportConfigsById(Long importConfigsId)
	{
		try
		{
			return this.importConfigsDao.findImportConfigsById(importConfigsId);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findImportConfigsById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<ImportConfigs> findAllImportConfigs()
	{
		try
		{
			return this.importConfigsDao.findAllImportConfigs();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAllImportConfigs", e.getMessage(), e);
		}
	}
	
	@Override
	public ImportConfigs findImportConfigByKey(String importConfigKey)
	{
		try
		{
			return this.importConfigsDao.findImportConfigsByKey(importConfigKey);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("findAllImportConfigs", e.getMessage(), e);
		}
	}

	public boolean checkBaseType(Class<?> clazz){
		return false;
	}
	
	@Override
	public List<ConfigItem> getAllField(String entityClassStr)
	{
		try
		{
			List<ConfigItem> result = new ArrayList<ConfigItem>();
			
			EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(entityClassStr));
			LinkedHashMap<String, ColumnInfo> columnsInfos = entityClass.getColumnInfos();
			Set<String> keySet = columnsInfos.keySet();
			Iterator<String> ite = keySet.iterator();
			while (ite.hasNext())
			{
				
				String fieldName = ite.next();
				if(!"EId".equals(fieldName)){
					ConfigItem configItem = new ConfigItem();
					ColumnInfo columnInfo = columnsInfos.get(fieldName);
					
					boolean isUnique = columnInfo.isUnique();
					boolean isNotNull = columnInfo.isNullable();
					
					String showName = columnInfo.getLabel();
					String fieldRealName = columnInfo.getName();
					String FieldType = columnInfo.getFieldType().toString();
					
					if(FieldType.startsWith("class ddd.")){
						configItem.setIsEntity(true);
						configItem.setRelationEntityName(FieldType.substring(6));
					}else{
						configItem.setIsEntity(false);
					}
					configItem.setColumnTitle(showName);
					configItem.setFieldName(fieldRealName == null?fieldName:fieldRealName);
					configItem.setIsUnique(isUnique);
					configItem.setIsNotNull(!isNotNull);
					
					result.add(configItem);
				}
			}
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getAllField", e.getMessage(), e);
		}
	}

	@Override
	public void createExcelTemplate(ImportConfigs importConfigs) {
		try
		{
			if(importConfigs.getEId() != null && importConfigs.getEId() > 0){
				importConfigs=this.updateImportConfigs(importConfigs);
			}else{
				importConfigs=this.saveImportConfigs(importConfigs);
			}
			this.createExcelForConfigs(importConfigs);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("createExcelTemplate", e.getMessage(), e);
		}
	}

	@Override
	public void createAllExcelTemplate() {
		try {
			EntitySet<ImportConfigs> importConfigs=this.findAllImportConfigs();
			
			Iterator<ImportConfigs> iterator=importConfigs.iterator();
			while(iterator.hasNext()){
				this.createExcelTemplate(iterator.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("createAllExcelTemplate", e.getMessage(), e);
		}
	}
	
}
