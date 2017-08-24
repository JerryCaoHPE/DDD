package ddd.simple.util.file_upload_download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoaderListener;

import ddd.simple.entity.attachment.Attachment;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.systemConfig.SystemConfigService;

public class DownLoadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//public static String filePath = "D:\\java\\upload\\";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/octet-stream");
		try {
			String attachmentId = request.getParameter("attachmentId");
			
			Object attachmentServiceBean = ContextLoaderListener.getCurrentWebApplicationContext().getBean("attachmentServiceBean");
			AttachmentService attachmentService = (AttachmentService)attachmentServiceBean;
			Attachment attachment = attachmentService.findAttachmentById(Long.parseLong(attachmentId));
			String basePath = attachmentService.getFilePathFormSystemConfig();
			//String basePath = this.filePath;
			String downLoadPath = basePath+attachment.getAttachmentAddr();
			
			File file = new File(downLoadPath);
			
			String fileName = URLEncoder.encode(attachment.getAttachmentRealName(), "UTF-8");
			
			response.addHeader("Content-Disposition", "attachment;filename=" +  fileName);
		    response.addHeader("Content-Length", "" + file.length());
			
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bufferedIn = new BufferedInputStream(fin);
			int len = 1024;
			byte [] bytes = new byte[len];
			int size=0;
			
			OutputStream outputStream = response.getOutputStream();
			while((size=bufferedIn.read(bytes))>=0)
			{
				outputStream.write(bytes, 0, size);
			}
			bufferedIn.close();
			outputStream.flush();
			outputStream.close();
			
			
		} catch (Exception e) {
			System.out.println("下载文件发生错误!");
			e.printStackTrace();
		}
	}

}
