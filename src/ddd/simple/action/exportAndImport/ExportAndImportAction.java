package ddd.simple.action.exportAndImport;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.importConfigs.ImportConfigs;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.exportAndImport.ExportAndImportService;
import ddd.simple.service.importConfigs.ImportConfigsService;
import ddd.simple.service.systemConfig.SystemConfigService;

//import ddd.simple.service.importConfig.ImportConfigService;

/**
 * @author Cao JianLin
 * @date 创建时间：2015年7月30日 上午10:48:14
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
@Action
@RequestMapping("/ExportAndImport")
@Controller
public class ExportAndImportAction
{
	
	// 加载模版文件 和 数据文件
	@Resource(name = "exportAndImportServiceBean")
	private ExportAndImportService exportAndImportService;
	
	@Resource(name = "importConfigsServiceBean")
	private ImportConfigsService importConfigsService;
	
	@Resource(name = "systemConfigServiceBean")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "attachmentServiceBean")
	private AttachmentService attachmentService;
	
	public Map<String, String> importDataFile(String importConfigKey, Long formId)
	{
		try
		{
			ImportConfigs importConfig = this.importConfigsService.findImportConfigByKey(importConfigKey);
			
			if (importConfig == null)
			{
				throw new DDDException("未找到key为：" + importConfigKey + "的配置，请先到导入配置模块进行配置！");
			}
			
			String configContext = importConfig.getConfigContext();
			
			EntitySet<Attachment> attachments = this.attachmentService.findAttachmentByForm(formId, importConfig.getImportConfigKey());
			
			if (attachments == null || attachments.size() == 0)
			{
				throw new DDDException("网络出错，请先重新上传excel文件！");
			}
			
			Attachment attachment = (Attachment) attachments.toArray()[0];
			
			String attachmentLastPath = attachment.getAttachmentAddr();
			String attachmentFirstPath = this.attachmentService.getFilePathFormSystemConfig();
			
			String dataFilePath = attachmentFirstPath + attachmentLastPath;
			
			return this.importDataFromFile(configContext, dataFilePath);
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public void importEntityByImportConfig(String importConfigKey)
	{
		try
		{
			ImportConfigs importConfig = this.importConfigsService.findImportConfigByKey(importConfigKey);
			String configContext = importConfig.getConfigContext();
			EntitySet<Attachment> attachments = this.attachmentService.findAttachmentByForm(importConfig.getEId(),
					importConfig.getImportConfigKey());
			Attachment attachment = (Attachment) attachments.toArray()[0];
			
			String attachmentLastPath = attachment.getAttachmentAddr();
			String attachmentFirstPath = this.attachmentService.getFilePathFormSystemConfig();
			
			String dataFilePath = attachmentFirstPath + attachmentLastPath;
			
			this.importDataFromFile(configContext, dataFilePath);
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	// 导入
	public Map<String, String> importDataFromFile(String configContext, String dataFilePath)
	{
		try
		{
			// 加载模版
			File dataFile = new File(dataFilePath);
			if (dataFile.exists())
			{// 判断数据文件是否存在
				// 判断数据文件的合法性(只支持excel文件)
				if (dataFilePath.endsWith(".xlsx") || dataFilePath.endsWith(".xls"))
				{
					// 调用Excel的导入服务
					long t1 = System.currentTimeMillis();
					Map<String, String> message = this.exportAndImportService.ImportDataFromExcel(configContext, dataFilePath);
					long t2 = System.currentTimeMillis();
					System.out.println("导入用时" + (t2 - t1) + "毫秒");
					return message;
				} else
				{
					throw new DDDException("数据文件格式不对");
				}
			} else
			{
				throw new DDDException("数据文件不存在");
			}
		} catch (DDDException e)
		{
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * 
	 * @Title: exportByConfigkeyAndIds
	 * @Description：根据配置key和id集直接导出数据成json格式文件
	 * @param configKey
	 *            实体对应配置（指定导出项）
	 * @param ids
	 *            （指定数据EID）
	 * @return { isSuccess:是否成功, message: json字符串 | 失败原因 }
	 */
	public Map<String, Object> exportByConfigkeyAndIds(String configKey, ArrayList<Long> ids)
	{
		try
		{
			return this.exportAndImportService.export(configKey, ids);
		} catch (DDDException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午1:48:13 修改人：胡兴 修改时间：2015年11月22日 下午1:48:13
	 * 
	 * @Title: importByAttachment
	 * @Description: 根据附件导入数据 附件对应的文件内为json格式
	 * @param attachments
	 * @return void
	 */
	public void importByAttachment(Long associateFormId, String associateFormName)
	{
		try
		{
			exportAndImportService.importByAttachment(associateFormId, associateFormName);
		} catch (DDDException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 上午11:54:12 修改人：胡兴 修改时间：2015年11月22日 上午11:54:12
	 * 
	 * @Title: importByJson
	 * @Description: 根据json字符串导入
	 * @param jsonStr
	 * @return void
	 */
	public void importByJson(String jsonStr)
	{
		try
		{
			exportAndImportService.importByJson(jsonStr);
		} catch (DDDException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	* 创建人：胡兴
	* 创建时间：2015年11月22日 下午4:59:08
	* 修改人：胡兴
	* 修改时间：2015年11月22日 下午4:59:08
	* @Title: importByFile 
	* @Description: 通过服务器文件绝对路径
	* @param path 
	* @return void
	 */
	public void importByFile(String path){
		try{
			exportAndImportService.importByFile(path);
		}catch(DDDException e){
			e.printStackTrace();
			throw e;
		}
	}
}
