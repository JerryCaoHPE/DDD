package ddd.simple.service.workflow;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;

@Service
public class WorkflowDeployHandler {
//public class WorkflowDeployHandler implements IFileUploadHandler {

	@Resource(name = "workFlowEngineService")
	private WorkFlowEngineService workFlowEngineService;

	public String handle(List<FileItem> fileItems,Map<?,?> paramMap)   {
		
		Iterator<FileItem> iter = fileItems.iterator();

		while (iter.hasNext()) {
			FileItem fileItem = (FileItem) iter.next();

			// 只处理文件类型的表单，忽略其他不是文件域的所有表单信息
			if (!fileItem.isFormField()) 
			{
				String name = fileItem.getName();
				long size = fileItem.getSize();
				if ((name == null || name.equals("")) && size == 0) {
					continue;
				}
			 
				try {
					this.workFlowEngineService.deployWorkflow(fileItem.getInputStream(), fileItem.getName());
				} catch (IOException e) 
				{
					 DDDException dddException = new DDDException("WorkflowDeployHandler-handle","获取上传的流程定义文件出错，原因是："+e.getMessage()+",请重试，如果错误仍然存在，请成管理员联系",e); 
					 throw dddException; 
				}
			}
		}
		
		return "/CCM/ddd/simple/workflow/processDefinition.jsp";
	}
}
