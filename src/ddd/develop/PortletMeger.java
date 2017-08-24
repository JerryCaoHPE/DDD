/**
 * @Title: PortletMeger.java
 * @Package ddd.develop
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年12月22日 下午3:53:20
 * @version V1.0
 */
package ddd.develop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ddd.base.util.FileUtil;

/**
 * 项目名称：DDD3 类名称：PortletMeger 类描述： 创建人：AnotherTen 创建时间：2015年12月22日 下午3:53:20
 * 修改人：胡均 修改时间：2015年12月22日 下午3:53:20 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class PortletMeger
{
	public static void main(String[] args)
	{
		
		// 将所有html合并到portletsMeger中的megerHtml.html中
		megerHtml();
		// 将所有js合并到portletsMeger中的megerJS.js中
		megerJS();
		// 将所有的配置文件中需要的外部js加入到welcome.route.js中
		megerExtraJS();
		
		ddd.simple.util.RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\ddd\\routerList.txt","src\\ddd\\develop\\mergeConfig\\ddd\\whiteRouterList.txt","WebContent\\ddd\\extends\\js\\appRouter.js");
		ProjectCompile.merge();
	}
	
	public static void megerHtml()
	{
		
		// 读取extends文件夹下的portlets和portlets_sub中的所有html文件
		File megerHtml = new File("D:/angular/workspace/DDD3/WebContent/ddd/simple/view/main/html/main/Welcome.html");
		// 清空megerHtml中的内容
		clearnFileContext(megerHtml);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"margin-top: 10px;\">\n\n");
		List<String> filesPath = getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets", "html");
		
		List<String> filesPath_sub = getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets_sub", "html");
		filesPath.addAll(filesPath_sub);
		//对filesPath进行排序，得到port排序效果
		
		
		
		megerFilesContext(filesPath, sb);
		sb.append("</div>");
		writeContextToFile(megerHtml, sb.toString());
		
		System.out.println("合并html完成");
	}
	
	public static void megerJS()
	{
		// 读取extends文件夹下的portlets和portlets_sub中的所有html文件
		File megerJS = new File("D:/angular/workspace/DDD3/WebContent/ddd/extends/portletsMeger/megerJS.js");
		// 清空megerHtml中的内容
		clearnFileContext(megerJS);
		StringBuilder sb = new StringBuilder();
		
		megerFilesContext(getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets", "js"), sb);
		
		megerFilesContext(getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets_sub", "js"), sb);
		writeContextToFile(megerJS, sb.toString());
		
		System.out.println("合并JS完成");
	}
	
	public static void megerExtraJS()
	{
		// 读取extends文件夹下的portlets和portlets_sub中的所有html文件
		File megerRoute = new File("D:/angular/workspace/DDD3/WebContent/ddd/extends/portletsMeger/megerRoute.js");
		// 清空megerHtml中的内容
		clearnFileContext(megerRoute);
		StringBuilder sb = new StringBuilder();
		
		megerFilesContext(getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets", "route"), sb);
		
		megerFilesContext(getFilesPathBySuffix("D:/angular/workspace/DDD3/WebContent/ddd/extends/portlets_sub","route"), sb);
		writeContextToFile(megerRoute, sb.toString());
		
		System.out.println("合并Route完成");
	}
	
	private static void clearnFileContext(File file)
	{
		try
		{
			FileUtil.writeToFile(file, "");
		} catch (IOException e1)
		{
			System.out.println("清除" + file.getName() + "文件中的内容时出错，原因是 " + e1.toString());
		}
	}
	
	private static void writeContextToFile(File file, String context)
	{
		// 向megerHtml中写读取到的全部的html的内容
		try
		{
			FileUtil.writeToFile(file, context);
		} catch (IOException e1)
		{
			System.out.println(" 向" + file.getName() + "中写读取到的全部的html的内容时出错，原因是 " + e1.toString());
		}
	}
	
	private static List<String> getFilesPathBySuffix(String direc, String suffix)
	{
		List<String> filesPath = FileUtil.getFilePathsBySuffix(new File(direc), suffix);
		return filesPath;
	}
	
	private static void megerFilesContext(List<String> filesPath, StringBuilder sb)
	{
		for (int i = 0; i < filesPath.size(); i++)
		{
			try
			{
				String context = FileUtil.readeString(new File(filesPath.get(i)));
				sb.append(context + "\n");
			} catch (Exception e)
			{
				System.out.println("读取文件 " + filesPath.get(i) + "中的内容时发生异常 原因是" + e.toString());
			}
			
		}
	}
}
