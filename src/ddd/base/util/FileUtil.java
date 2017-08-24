package ddd.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.io.FileUtils;

import ddd.base.Config;
import ddd.base.exception.DDDException;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.service.systemConfig.SystemConfigService;

public class FileUtil
{
	public static void main(String[] args) throws Exception
	{
		
		// String str = readeString(new
		// File("C:\\Users\\Administrator\\Desktop\\test.txt"));
		// System.out.println(str);
		List<String> filesPath = new ArrayList<String>();
		
		selectDir(new File("D:\\angular\\workspace\\DDD3\\src\\ddd\\simple"), "", filesPath);
		for (int i = 0; i < filesPath.size(); i++)
		{
			String temp = MD5Util.getMD5(new File(filesPath.get(i)));
			System.out.println(filesPath.get(i) + "--" + temp);
		}
	}
	
	public static String readeString(File file) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		FileInputStream fileInputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Config.CHARSET_UTF8);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		int b = 0;
		while ((b = bufferedReader.read()) != -1)
		{
			sb.append((char) b);
		}
		bufferedReader.close();
		return sb.toString();
	}
	
	public static String readeString(InputStream inputStream) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Config.CHARSET_UTF8);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		int b = 0;
		while ((b = bufferedReader.read()) != -1)
		{
			sb.append((char) b);
		}
		bufferedReader.close();
		return sb.toString();
	}
	
	public static void writeToFile(File outputFile, String content) throws IOException
	{
		if (!outputFile.getParentFile().exists())
		{
			outputFile.getParentFile().mkdirs();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Config.CHARSET_UTF8);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		bufferedWriter.write(content);
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	public static void writeToFile(OutputStream outputStream, String content) throws IOException
	{
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Config.CHARSET_UTF8);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		bufferedWriter.write(content);
		bufferedWriter.flush();
	}
	
	public static List<String> getFilePathsBySuffix(File path, String suffix)
	{
		List<String> filePaths = new ArrayList<String>();
		if (path.exists())
		{
			selectDir(path, suffix.toLowerCase(), filePaths);
		}
		return filePaths;
	}
	
	private static void selectDir(File dir, String suffix, List<String> filePaths)
	{
		Queue<File> dirQueue = new LinkedList<File>();
		dirQueue.add(dir);
		
		while (!dirQueue.isEmpty())
		{
			File[] list = dirQueue.poll().listFiles();
			for (File file : list)
			{
				if (file.isDirectory())
				{
					dirQueue.add(file);
				} else
				{
					String fileName = file.getName().toLowerCase();
					if (fileName.endsWith(suffix))
					{
						filePaths.add(file.getAbsolutePath());
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: writeToTemporaryFile
	 * @Description: 写入临时文件
	 * @param content
	 * @param suffix
	 * @return
	 * @return Map<Boolean,String> <true,文件路径> | <false,错误原因>
	 */
	public static Map<Boolean, String> writeToTemporaryFile(String content, String suffix)
	{
		Map<Boolean, String> result = new HashMap<Boolean, String>();
		
		String randomTemporaryFilePath = randomTemporaryFilePath(suffix);
		try
		{
			if (!randomTemporaryFilePath.equals(""))
			{
				
				FileUtils.write(new File(randomTemporaryFilePath), content);
				result.put(true, randomTemporaryFilePath);
			} else
				result.put(false, "获取随机文件路径异常!");
		} catch (IOException e)
		{
			result.put(false, "文件流写入异常!");
		} catch (Exception e)
		{
			result.put(false, e.getMessage());
		} finally
		{
			return result;
		}
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午4:36:11 修改人：胡兴 修改时间：2015年11月22日 下午4:36:11
	 * 
	 * @Title: randomTemporaryFilePath
	 * @Description: 随机临时路径（至文件）
	 * @param suffix
	 * @return
	 * @return String
	 */
	public static String randomTemporaryFilePath(String suffix)
	{
		String orderPath = "";
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH)+1;// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日
		
		String basePath = getTemporaryBasePath();
		if (basePath != null && !basePath.equals(""))
		{
			orderPath = basePath + "/" + year + "/" + month + "/" + day + "/" + random(suffix);
		}
		return orderPath;
	}
	
	/**
	 * 
	 * 创建人：胡兴 创建时间：2015年11月22日 下午4:36:36 修改人：胡兴 修改时间：2015年11月22日 下午4:36:36
	 * 
	 * @Title: randomTemporaryFilePath
	 * @Description: 随机临时路径（至父级）
	 * @return
	 * @return String
	 */
	public static String randomTemporaryFilePath()
	{
		String orderPath = "";
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH)+1;// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日
		
		String basePath = getTemporaryBasePath();
		if (basePath != null && !basePath.equals(""))
		{
			orderPath = basePath + "/" + year + "/" + month + "/" + day + "/";
		}
		return orderPath;
	}
	
	private static String getTemporaryBasePath()
	{
		String basePath = "";
		if (SpringContextUtil.containsBean("systemConfigServiceBean"))
		{
			SystemConfigService configs = (SystemConfigService) SpringContextUtil.getBean("systemConfigServiceBean");
			basePath = configs.findSystemConfigValueBykey("temporaryBasePath");
		}
		return basePath;
	}
	
	private static String random(String suffix)
	{
		return System.currentTimeMillis() + "_" + (int) (Math.random() * 10000) + "." + suffix;
	}
	
	public static String random()
	{
		return System.currentTimeMillis() + "_" + (int) (Math.random() * 10000);
	}
	
	public static String readAsStringByTemporaryPath(Attachment attachment)
	{
		try
		{
			String content = "";
			String basePath = getTemporaryBasePath();
			String relativePath = attachment.getAttachmentAddr();
			if (basePath != null && !basePath.equals("") && relativePath != null && !relativePath.equals(""))
				content = readeString(new File(basePath + "/" + relativePath));
			return content;
		} catch (DDDException e)
		{
			throw e;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("读取失败!");
		}
	}
	
}
