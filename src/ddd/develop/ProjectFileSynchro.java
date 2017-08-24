package ddd.develop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import ddd.base.Config;
import ddd.base.util.FileUtil;

public class ProjectFileSynchro {
	/**
	 * 同步DDD3框架文件到业务系统的文件中
	 * 1. 在config.properties文件中配置框架项目的根目录：ddd3Path=D://angular/workspace/DDD3/
	 * 2. 在config.properties文件中配置业务系统项目的根目录：applicationPath=D://angular/workspace/CMS/
	 * 3. 在业务系统中文件 src\\ddd\\develop\\synchroPath.txt配置要同步的文件和目录,文件相对于项目的根目录，不包含“\”
	 * 4. 在业务系统中文件 src\\ddd\\develop\\whiteList.txt配置不需要同步的文件和目标,文件相对于项目的根目录，不包含“\”
	 * 5. 路径中统一使用右撇号（\）
	 * */

	public static List<String> whiteList = new ArrayList<String>();
	
	public static void main(String[] args){
		
		synchro();
		 
	}
	public static void synchro(){
		try{
			//获取白名单中的所有文件的绝对路径
			//;
			if(Config.applicationPath.equals(Config.ddd3Path))
			{
				System.err.println("框架和业务系统的目录相同，不能同步。请检查文件synchroPath.txt中的配置：applicationPath，ddd3Path");
			}
			whiteList = getWhiteList(Config.applicationPath+"src\\ddd\\develop\\whiteList.txt");
			
			String [] directories = readSynchroPath(Config.applicationPath+"src\\ddd\\develop\\synchroPath.txt");
			
			List<String> sourceFiles = listDDD3Files(directories,Config.ddd3Path);
		 
			copyFiles(sourceFiles,Config.ddd3Path,Config.applicationPath);
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}
	public static void copyFiles(List<String> sourceFiles,String sourcePath,String targetPath)
	{
		System.out.println("需要同步的文件数量："+sourceFiles.size());
		System.out.println("  源目录为："+sourcePath);
		System.out.println("目标目录为："+targetPath);
		int failCount = 0;
		for (String fileName : sourceFiles)
		{
			try
			{
				FileUtils.copyFile(new File(sourcePath+fileName), new File(targetPath+fileName),false);
				System.out.println("copy:"+sourcePath+fileName+"   ==>  "+targetPath+fileName);
			} catch (Exception e)
			{
				System.err.println("复制文件失败："+fileName+",原因是："+e.getMessage());
				failCount++;
			}
		}
		if(failCount == 0 )
		{
			System.out.println("同步成功");
		}
		else
		{
			System.err.println("同步失败，失败文件数为："+failCount);
		}
	}
 
	//读取需要同步的文件夹的路径
	public static String []  readSynchroPath(String synchroDirectory) throws Exception{
		String temp = FileUtil.readeString(new File(synchroDirectory));
		String [] temps = temp.split("\r\n");
//		for(int i =0;i<temps.length;i++){
//			temps[i]= temps[i];
//		}
		return temps;
	}
	public static boolean isWhiteFile(String fileName)
	{
		for(int i=0;i<whiteList.size();i++)
		{
			if(fileName.toLowerCase().startsWith(whiteList.get(i)))
			{
		     
				return true;
			}
		}
		return false;
	}
	public static List<String> getWhiteList(String whileListFile) throws Exception{
		String temp = FileUtil.readeString(new File(whileListFile));
		String [] temps = temp.split("\r\n");
		List<String> whiteList = new ArrayList<String>();
		for(int i=0;i<temps.length;i++){
			if(temps[i] != null && ! temps[i].trim().equals(""))
			{
				whiteList.add(temps[i].trim().toLowerCase());
			}
		}
		return whiteList;
	}
	public static  List<String> listDDD3Files(String [] directories,String basePath)
	{
		final int basePathLength= basePath.length();
		final List<String> fileNames = new ArrayList<String>();
		for (int i = 0; i < directories.length; i++)
		{
			File file = new File(basePath+directories[i]);
			if(file.isFile())
			{
				String fileName = file.getPath();
				fileName = fileName.substring(basePathLength);
				if(isWhiteFile(fileName) == false)
				{
					fileNames.add(fileName);
					System.out.println(fileName);
				}			 
			}
			else
			{
				Collection<File> files1 = FileUtils.listFiles(file, new IOFileFilter()
				{
					
					@Override
					public boolean accept(File dir, String name)
					{
						System.err.println(name);
						return false;
					}
					
					@Override
					public boolean accept(File file)
					{
						if(file.isFile())
						{
							String fileName = file.getPath();
							fileName = fileName.substring(basePathLength);
							if(isWhiteFile(fileName) == false)
							{
								fileNames.add(fileName);
								System.out.println(fileName);
							}
						}
						return true;
					}
				}, TrueFileFilter.INSTANCE);
			}
		}
		return fileNames;
	}
}
