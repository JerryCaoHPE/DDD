package ddd.simple.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import ddd.base.Config;
import ddd.base.util.FileUtil;

public class RouteMerge
{
	private static String			lineBreak	= "\r\n";
	private static String			insertRow	= "//\\\\insertRow\\\\//";
	private static String			from		= "////from=";
	private static SimpleDateFormat	dateFormat	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) throws IOException
	{
		// merge();
	}
	
	public static void merge(String routerSourceFile, String whiteRouterSourceFile, String outputFileName)
	{
		// String appFileName
		// =Config.applicationPath+"\\WebContent\\ddd\\extends\\js\\appRouter.js";
		
		// 清除原来的路由
		try
		{
			routerSourceFile = Config.applicationPath + routerSourceFile;
			List<String> sourcePaths = FileUtils.readLines(new File(routerSourceFile));
			
			whiteRouterSourceFile = Config.applicationPath + whiteRouterSourceFile;
			List<String> whiteSourcePaths = FileUtils.readLines(new File(whiteRouterSourceFile));
			
			outputFileName = Config.applicationPath + outputFileName;
			mergeAll(sourcePaths, whiteSourcePaths, outputFileName);
		} catch (IOException e)
		{
			System.err.println("清除原路由出错，原因是：" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void mergeAll(List<String> sourcePaths,List<String> whiteSourcePaths,String outputFileName){
		StringBuilder appRouters = new StringBuilder();
		
		HashMap<String,String> whiteSourePathMap = new HashMap<String,String>();
		for(String whiteSourcePath : whiteSourcePaths){
			whiteSourcePath = Config.applicationPath + whiteSourcePath;
			List<String> whiteRoutes = FileUtil.getFilePathsBySuffix(new File(whiteSourcePath), ".route.js");
			for(String whiteRoute : whiteRoutes){
				whiteSourePathMap.put(whiteRoute, whiteRoute);
			}
		}	
		
		for (String sourcePath : sourcePaths)
		{
			sourcePath = Config.applicationPath + sourcePath;
			List<String> insertRoutes = FileUtil.getFilePathsBySuffix(new File(sourcePath), ".route.js");
			merge(insertRoutes,whiteSourePathMap,appRouters);
		}
		
		try
		{
			FileUtils.writeStringToFile(new File(outputFileName),appRouters.toString() );
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void merge(List<String> insertRoutes,HashMap<String,String> whiteRoutes, StringBuilder appRouters)
	{
		if (insertRoutes == null || insertRoutes.size() == 0)
		{
			return;
		}
		long start = System.currentTimeMillis();
		try
		{
			for (String fileName : insertRoutes)
			{
				if(whiteRoutes.containsKey(fileName)){
					continue;
				}
				String insertRoute = FileUtils.readFileToString(new File(fileName));
				appRouters.append("//start====").append(fileName).append("\n");
				appRouters.append(insertRoute).append("\n");
				appRouters.append("//end  ====").append(fileName).append("\n");
				System.out.println("插入文件域：" + fileName);
			}
			System.out.println("合并路由用时：" + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
