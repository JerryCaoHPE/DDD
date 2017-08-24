/**
* @Title: uploadImgAction.java
* @Package ddd.simple.action.upload
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年10月9日 下午7:37:23
* @version V1.0
*//*
package ddd.simple.action.upload;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.fastjson.JSONObject;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.persistence.SessionFactory;
import ddd.cms.entity.gallery.Gallery;
import ddd.cms.service.gallery.GalleryService;
import ddd.simple.service.systemConfig.SystemConfigService;

*//**
 * 项目名称：CMS 类名称：uploadImgAction 类描述： 创建人：Administrator 创建时间：2015年10月9日 下午7:37:23
 * 修改人：胡兴 修改时间：2015年10月9日 下午7:37:23 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 *//*
@Action
@RequestMapping("/uploadAction")
@Controller
public class uploadAction
{
	public void uploadImg(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		response.setHeader("Content-Type", "text/html");
		
		// response.setContentType("text/plain;charset=utf-8");
		
		try
		{
			
			SystemConfigService systemConfigService = (SystemConfigService) ContextLoaderListener.getCurrentWebApplicationContext()
					.getBean("systemConfigServiceBean");
					
			String savePath = systemConfigService.findSystemConfigValueBykey("galleryPath");
			if (savePath == null || "".equals(savePath))
			{
				savePath = "D:\\java\\img";
			}
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			diskFactory.setSizeThreshold(4096);
			
			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			upload.setSizeMax(4 * 1024 * 1024);
			
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			
			while (iter.hasNext())
			{
				FileItem item = (FileItem) iter.next();
				if (item.getName() == null)
				{
					continue;
				}
				String fileName = this.getUploadFileName(item);
				if (!item.isFormField() && fileName != null)
				{
					PrintWriter out = null;
					try
					{
						out = response.getWriter();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					
					String relPath = this.uploadImgFile(item, savePath, fileName);
					saveImgToGallery(item, "ddd", fileName, relPath);
					
					JSONObject imgJson = new JSONObject();
					imgJson.put("state", "SUCCESS");
					imgJson.put("url", relPath);
					imgJson.put("title", fileName);
					imgJson.put("original", fileName);
					out.write(imgJson.toString());
					// out.close();
				}
			}
			
			SessionFactory.commitTransaction();
			
		} catch (Exception e)
		{
			System.out.println("使用 fileupload 包时发生异常 ...");
			e.printStackTrace();
		}
	}
	
	public void saveImgToGallery(FileItem item, String author, String imgName, String url)
	{
		String imgType = "";
		if (imgName != null && !"".equals(imgName))
		{
			String[] imgArrs = imgName.split("\\.");
			imgType = imgArrs[imgArrs.length - 1];
		}
		Long size = item.getSize();
		Date uploadDate = new Date();
		GalleryService galleryService = (GalleryService) ContextLoaderListener.getCurrentWebApplicationContext()
				.getBean("galleryServiceBean");
		Gallery gallery = new Gallery();
		gallery.setAuthor(author);
		gallery.setImgName(imgName);
		gallery.setUrl(url);
		gallery.setImgType(imgType);
		gallery.setPostfix(imgType);
		gallery.setSize(size.toString());
		gallery.setUploadDate(uploadDate);
		Gallery newGallery = galleryService.saveGallery(gallery);
	}
	
	public String uploadImgFile(FileItem item, String savePath, String fileName) throws Exception
	{
		
		Date nowTime = new Date();
		
		String finalFileName = nowTime.getTime() + "_" + fileName;
		String fileTime = "\\" + nowTime.getYear() + "\\" + nowTime.getMonth() + "\\" + nowTime.getDay();
		
		String returnURL = fileTime + "\\" + finalFileName;
		
		savePath += fileTime;
		
		this.uploadFile(item, savePath, finalFileName);
		return returnURL;
	}
	
	public void uploadFile(FileItem item, String savePath, String fileName)
	{
		File uploadFile = new File(savePath);
		
		if (!uploadFile.exists())
		{
			uploadFile.mkdirs();
		}
		
		try
		{
			item.write(new File(uploadFile, fileName));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getUploadFileName(FileItem item)
	{
		String fileName = item.getName();
		long fileSize = item.getSize();
		if ("".equals(fileName) && fileSize == 0)
		{
			System.out.println("文件名为空 ...");
			return null;
		}
		
		System.out.println("完整的文件名：" + fileName);
		int index = fileName.lastIndexOf("\\");
		
		fileName = fileName.substring(index + 1, fileName.length());
		
		return fileName;
	}
}
*/