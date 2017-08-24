/**
 * @Title: ProjectDDD3Jar.java
 * @Package ddd.develop
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年11月17日 上午2:09:05
 * @version V1.0
 */
package ddd.develop;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import ddd.base.Config;

/**
 * 项目名称：DDD3 类名称：ProjectDDD3Jar 类描述： 创建人：DDD 创建时间：2015年11月17日 上午2:09:05 修改人：DDD
 * 修改时间：2015年11月17日 上午2:09:05 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class ProjectDDD3Jar
{
	
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param args
	 * @return void
	 * @throws IOException
	 */
	public static void main(String[] args)  
	{
		start();
	}
	public static void start( )  
	{
		try
		{
			if( execute())
			{
				System.out.println("打包成功");
			}
			else
			{
				System.err.println("打包失败");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	private static boolean execute() throws Exception
	{
		StringBuffer bat = new StringBuffer();
		String sourcePath = Config.ddd3Path+"build/classes/";
		
		//删除白名单中的文件 
		handleWhiteList(sourcePath);
		
		bat.append("cd ").append(sourcePath).append("\n");
		bat.append(Config.ddd3Path.substring(0, 2)).append("\n");
		
		String jarSource = Config.ddd3Path+"/build/DDD3.jar";
		bat.append("D://angular//jdk7//bin/jar cvfm ").append(jarSource).append("  ").append(Config.ddd3Path)
				.append("src/MANIFEST.MF  *	\n");
		bat.append("exit\n");
//		bat.append("copy /Y ").append(Config.ddd3Path).append("/build/DDD3.jar  ").append(Config.applicationName)
//				.append("WebContent/WEB-INF/lib/DDD3.jar \n");
		
		String batFileName = Config.applicationName + "src/ddd/develop/jar.bat";
		FileUtils.writeStringToFile(new File(batFileName), bat.toString());
		
		File jarSourceFile = new File(jarSource);
		FileUtils.deleteQuietly(jarSourceFile);
		
		openExe(batFileName);

		String jarTarget = Config.applicationName+"WebContent/WEB-INF/lib/DDD3.jar";
		File jarTargetFile = new File(jarTarget);

		if(jarTargetFile.exists())
		{
			FileUtils.deleteQuietly(jarTargetFile);
		}
		
	 
		
		int seconds = 40;
		while(! jarSourceFile.exists())
		{
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			seconds--;
			if(seconds <=0) break;
		}
		
		if(! jarSourceFile.exists())
		{
			System.err.println("打包失败，请检查 jar.bat文件 ");
			return false ;
		}
		if(Config.ddd3Path.equals(Config.packageName)) return true;
		
		FileUtils.copyFile(jarSourceFile, jarTargetFile);
		return true; 
	}
		
	public static void openExe(String cmd)
	{
		   Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象  
	        try {  
	            Process p = run.exec(cmd);// 启动另一个进程来执行命令  
	            BufferedInputStream in = new BufferedInputStream(p.getInputStream());  
	            BufferedReader inBr = new BufferedReader(new InputStreamReader(in,"GBK"));  
	            String lineStr;  
	            while ((lineStr = inBr.readLine()) != null)  
	                //获得命令执行后在控制台的输出信息  
	                System.out.println(lineStr);// 打印输出信息  
	            //检查命令是否执行失败。  
	            if (p.waitFor() != 0) {  
	                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束  
	                    System.err.println("命令执行失败!");  
	            }  
	            inBr.close();  
	            in.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	
	private static List<String> whiteList;
	public static void handleWhiteList(String sourcePath) throws Exception
	{
		whiteList = getWhiteList(Config.applicationPath+"src\\ddd\\develop\\jarWhiteList.txt");
		List<String> whiteFileNames = listWhiteFileNames(sourcePath);
		deleteWhiteFiles(sourcePath,whiteFileNames);
	}
	public static void deleteWhiteFiles(String sourcePath,List<String> whiteFileNames)
	{
		for (String whiteFileName : whiteFileNames)
		{
			FileUtils.deleteQuietly(new File(sourcePath+whiteFileName));
		}
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
	public static  List<String> listWhiteFileNames(String sourcePath)
	{
		final int basePathLength= sourcePath.length();
		final List<String> whiteFileNames = new ArrayList<String>();
		File file = new File(sourcePath);
		if(file.isFile())
		{
			String fileName = file.getPath();
			fileName = fileName.substring(basePathLength);
			if(isWhiteFile(fileName) == true)
			{
				whiteFileNames.add(fileName);
				System.out.println(fileName);
			}			 
		}
		else
		{
			@SuppressWarnings("unused")
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
						System.out.println(fileName);
						if(isWhiteFile(fileName) == true)
						{
							whiteFileNames.add(fileName);
							System.out.println(fileName);
						}
					}
					return true;
				}
			}, TrueFileFilter.INSTANCE);
		}
		return whiteFileNames;
	}
	public static List<String> getWhiteList(String whileListFile) throws Exception{
		List<String> whiteList  = FileUtils.readLines(new File(whileListFile));
		for(int i = whiteList.size()-1;i >= 0 ;i--){
			if(whiteList.get(i) == null || whiteList.get(i).trim().equals(""))
			{
				whiteList.remove(i);
			}
			else
			{
				whiteList.set(i, whiteList.get(i).trim().toLowerCase());
			}
		}
		return whiteList;
	}
}
