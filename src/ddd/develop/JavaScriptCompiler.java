/**
* @Title: JavaScriptCompiler.java
* @Package ddd.develop
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月17日 下午11:20:29
* @version V1.0
*/
package ddd.develop;

import org.apache.commons.io.FilenameUtils;

import com.google.javascript.jscomp.AbstractCommandLineRunner;

/**
 * 项目名称：DDD3
 * 类名称：JavaScriptCompiler
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月17日 下午11:20:29
 * 修改人：DDD
 * 修改时间：2015年11月17日 下午11:20:29
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class JavaScriptCompiler
{
	
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param args
	 * @return void
	 */
	public static void main(String[] args)
	{
		String command = "";
		command = genCommand("D:\\angular\\workspace\\DDD3\\WebContent\\ddd\\js\\uigrid\\3.0.7\\ui-grid-xcy.js");
		//command = genCommand("D:\\angular\\workspace\\DDD3\\WebContent\\ddd\\js\\ui-router\\0.2.13\\angular-ui-router-xcy.js");
		
		
		args = command.split(" ");
		DDDCommandLineRunner runner = new DDDCommandLineRunner(args);
		
		if (runner.shouldRunCompiler())
		{
			int result = 0;
			try
			{
				result = runner.doRun();
			} catch (AbstractCommandLineRunner.FlagUsageException e)
			{
				System.err.println(e.getMessage());
				result = -1;
			} catch (Throwable t)
			{
				t.printStackTrace();
				result = -2;
			}
			
		}
		if (runner.hasErrors()) 
		{
			System.exit(-1);
		}
		else
		{
			System.out.println("成功压缩！！！！");
		}
		
	}
	private static String genCommand(String sourceName)
	{
		String targetName = FilenameUtils.removeExtension(sourceName)+".min.js";
		String command = "--compilation_level SIMPLE_OPTIMIZATIONS "
				+ "--js_output_file="+targetName+" "+sourceName;
		
		return command;
	}
	
}
