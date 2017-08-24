package ddd.simple.util.file_upload_download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoaderListener;

import ddd.base.exception.DDDException;
import ddd.simple.service.systemConfig.SystemConfigService;

public class ShowImgServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	private SystemConfigService systemConfigService = (SystemConfigService) ContextLoaderListener.getCurrentWebApplicationContext()
			.getBean("systemConfigServiceBean");
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String imgFilePath = systemConfigService.findSystemConfigValueBykey("galleryPath");
		if(imgFilePath == null || "".equals(imgFilePath))
		{
			throw new DDDException("未配置系统参数galleryPath!");
		}
		String imgUrl = new String(request.getParameter("imgUrl").getBytes("ISO8859-1"),"utf-8");


		String finalUrl = "";
		
		if(imgUrl != null)
		{
			finalUrl = imgFilePath+"/"+imgUrl;
		} 
		
		File checkFile = new File(finalUrl);
		if(checkFile.isFile() == false)
		{
			finalUrl = this.getServletContext().getRealPath("ddd/asset/missing.png");
			File imgFile = new File(finalUrl);
			try {
				showImgToFront(response,imgFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				showImgToFront(response,checkFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void showImgToFront(HttpServletResponse response,File imgFile) throws Exception
	{
		System.out.println("请求图片完整路径："+imgFile.getAbsolutePath());
		response.setContentType("application/octet-stream");
		FileInputStream fin = new FileInputStream(imgFile);
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
	}

}
