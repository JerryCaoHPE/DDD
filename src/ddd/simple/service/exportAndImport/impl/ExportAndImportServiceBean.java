package ddd.simple.service.exportAndImport.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.exporter.ExporterEngnine;
import ddd.base.persistence.EntitySet;
import ddd.base.util.FileUtil;
import ddd.simple.dao.exportAndImport.ExportAndImportDao;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.exporterConfig.ExporterConfig;
import ddd.simple.entity.importConfigs.CellError;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.billCode.BillCodeService;
import ddd.simple.service.exportAndImport.ExportAndImportService;
import ddd.simple.service.exporterConfig.ExporterConfigService;
import ddd.simple.service.importData.ImportDataService;
import ddd.simple.service.importData.impl.ImportDataServiceImpl;
import ddd.simple.service.systemConfig.SystemConfigService;

/**
 * 
 * @author Xue Zhou
 * 		
 */
@Service
public class ExportAndImportServiceBean extends BaseService implements ExportAndImportService
{
	@Resource(name = "exportAndImportDaoBean")
	private ExportAndImportDao exportAndImportDao;
	
	@Resource(name = "exporterConfigServiceBean")
	private ExporterConfigService exporterConfigService;
	
	@Resource(name = "systemConfigServiceBean")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "attachmentServiceBean")
	private AttachmentService attachmentService;
	
	@Resource(name="billCodeServiceBean")
	private BillCodeService billCodeService;
	
	private ExporterEngnine exporterEngnine = new ExporterEngnine();
	
	@Override
	public Map<String, String> ImportDataFromExcel(String configContext, String dataFilePath)
	{
		
		String errorMessage = null;
		//设置基础信息
		Map<String, Object> baseInfo=new HashMap<String, Object>();
		baseInfo.put("operateDate", new Date());
		/*baseInfo.put("orgId", this.getOrganization().getEId());*/
		baseInfo.put("operatorCode", this.getLoginUser().getLoginOperator().getCode());
		
		String separator = this.systemConfigService.findSystemConfigValueBykey("Excel导入一对多分割符");
		
		if (separator.length() < 1)
		{
			separator = ";";
		}
		
		ImportDataService importDataService = new ImportDataServiceImpl(baseInfo,exportAndImportDao,billCodeService, configContext, dataFilePath, separator);
		List<CellError> cellErrors = importDataService.importData();
			
		if(cellErrors.size() > 0)
		{
			Map<Integer, String> errorMap=new LinkedHashMap<Integer,String>();
			
			for (int i = 0; i < cellErrors.size(); i++) {
				int rowNum=cellErrors.get(i).getRow();
				if(errorMap.containsKey(rowNum)){
					errorMap.put(rowNum, errorMap.get(rowNum)+"; "+cellErrors.get(i).getError());
				}else{
					errorMap.put(rowNum, cellErrors.get(i).getError());
				}
			}
			
			String html="<table class='table table-striped'><th>行号</th><th>错误原因</th>";
			
			for(Entry<Integer,String> entry : errorMap.entrySet()) 
	        { 
				int rowIndex=entry.getKey()+1;
	            html+="<tr><td>"+rowIndex+"</td><td>"+entry.getValue()+"</td></tr>";
	        }
			
			html+="</table>";
			throw new DDDException(html);
		}
		return null;
	}
	
	
	
	/**
	 * 
	 * @Title: export
	 * @Description：根据配置key和id集直接导出数据成json格式文件
	 * @param configKey
	 *            实体对应配置（指定导出项）
	 * @param ids
	 *            （指定数据EID）
	 * @author huxing
	 * @return { isSuccess:是否成功, message: json字符串 | 失败原因 }
	 */
	@Override
	public Map<String, Object> export(String configKey, ArrayList<Long> ids)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Boolean _isSuccess = false;
		try
		{
			ExporterConfig config = exporterConfigService.findExporterConfigByKey(configKey);
			if (config == null)
			{
				result.put("message", "未找到名称为：【" + configKey + "】的导出配置!");
			} else
			{
				String configText = config.getConfig();
				String jsonStr = "";
				if (config != null && configText != null && !configText.equals(""))
				{
					String randomTemporaryFilePath = FileUtil.randomTemporaryFilePath("text");
					if (!randomTemporaryFilePath.equals(""))
					{
						File temporaryFile = new File(randomTemporaryFilePath);
						if (!temporaryFile.getParentFile().exists())
						{
							temporaryFile.getParentFile().mkdirs();
						}
						jsonStr = exporterEngnine.export(configText, ids, new FileOutputStream(temporaryFile));
						_isSuccess = true;
						result.put("message", jsonStr);
					} else
						result.put("message", "临时路径生成失败!");
				}
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			result.put("message", "指定路径异常");
		} catch (Exception e)
		{
			e.printStackTrace();
			result.put("message", e.getMessage());
		} finally
		{
			result.put("isSuccess", _isSuccess);
			return result;
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
	@Override
	public void importByAttachment(EntitySet<Attachment> attachments)
	{
		try
		{
			for (Attachment attachment : attachments)
			{
				String content = FileUtil.readAsStringByTemporaryPath(attachment);
				importByJson(content);
			}
		} catch (DDDException e)
		{
			throw e;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午12:28:19 修改人：胡兴 修改时间：2015年11月22日 下午12:28:19
	 * 
	 * @Title: importByAttachment
	 * @Description: 根据附件导入数据 附件对应的文件内为json格式
	 * @param associateFormId
	 * @param associateFormName
	 * @return
	 * @return void
	 */
	@Override
	public void importByAttachment(Long associateFormId, String associateFormName)
	{
		try
		{
			EntitySet<Attachment> attachments = attachmentService.findAttachmentByForm(associateFormId, associateFormName);
			importByAttachment(attachments);
		} catch (DDDException e)
		{
			throw e;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 上午11:54:12 修改人：胡兴 修改时间：2015年11月22日 上午11:54:12
	 * 
	 * @Title: importByJson
	 * @Description: 根据附件json字符串导入
	 * @param jsonStr
	 * @return void
	 */
	@Override
	public void importByJson(String jsonStr)
	{
		try
		{
			exporterEngnine.importData(jsonStr);
		} catch (DDDException e)
		{
			throw e;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException(e.getMessage());
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
	@Override
	public void importByFile(String path)
	{
		try{
			String jsonStr = FileUtils.readFileToString(new File(path));
			this.exporterEngnine.importData(jsonStr);
		}catch(Exception e){
			e.printStackTrace();
			throw new DDDException(e.getMessage());
		}
	}
	
}
