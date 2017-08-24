package ddd.simple.service.exportAndImport;

import java.util.ArrayList;
import java.util.Map;

import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;

public interface ExportAndImportService
{
	/**
	 * 返回状态字协议 return 0 ：导入成功
	 */
	public Map<String, String> ImportDataFromExcel(String configContext, String dataFilePath);
	
	/**
	 * 
	 * @Title: export
	 * @Description：根据配置key和id集直接导出数据成json格式文件
	 * @param configKey
	 *            实体对应配置（指定导出项）
	 * @param ids
	 *            （指定数据EID）
	 * @return { isSuccess:是否成功, result: json字符串 | 失败原因 }
	 */
	public Map<String, Object> export(String configKey, ArrayList<Long> ids);
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午1:48:13 修改人：胡兴 修改时间：2015年11月22日 下午1:48:13
	 * 
	 * @Title: importByAttachment
	 * @Description: 根据附件导入数据 附件对应的文件内为json格式
	 * @param attachments
	 * @return void
	 */
	public void importByAttachment(EntitySet<Attachment> attachments);
	
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
	public void importByAttachment(Long associateFormId, String associateFormName);
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午12:29:36 修改人：胡兴 修改时间：2015年11月22日 下午12:29:36
	 * 
	 * @Title: importByJson
	 * @Description: 根据json字符串导入数据
	 * @param jsonStr
	 * @return
	 * @return void
	 */
	public void importByJson(String jsonStr);
	
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
	public void importByFile(String path);
}
