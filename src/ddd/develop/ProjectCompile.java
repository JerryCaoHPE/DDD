package ddd.develop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.sun.star.uno.Exception;

import ddd.base.Config;

public class ProjectCompile {
 
	/**
	 * 替换文件中中的 dddinclude 内容，dddinclude 必须成对出现
	 * 		<!--dddinclude start ddd.html -->
	*    <!--dddinclude end ddd.html -->
	* @Title: main 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param args 
	* @return void
	 */
	public static void main(String[] args) {
		merge();
	}
	public static void merge() {
		try
		{
			
			List<String> compileSources = getCompileSources(Config.applicationPath+"src\\ddd\\develop\\mergeConfig\\compiles.txt");
			mergeFiles(compileSources);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}	
	public static void mergeFiles(List<String> compileSources) throws IOException, Exception
	{
		for (String compileSource : compileSources)
		{
			String fileType = FilenameUtils.getExtension(compileSource).toLowerCase();
			if("html".equals(fileType))
			{
				System.out.print("合并Html："+compileSource);
				mergeHtmlFiles(compileSource);
				System.out.println("         OK");
			}
			else if("js".equals(fileType))
			{
				System.out.print("合并JS："+compileSource);
				mergeJavascriptFiles(compileSource);
				System.out.println("         OK");
			}
		}
	}
	public static void mergeHtmlFiles(String fileName) throws IOException, Exception
	{
		mergeFiles(fileName,"\\<!\\-\\-","\\-\\->");
	}
	public static void mergeJavascriptFiles(String fileName) throws IOException, Exception
	{
		
		mergeFiles(fileName,"/\\*","\\*/");
		
	}	
	public static void mergeFiles(String fileName,String tagPrefix,String tagSuffix) throws IOException, Exception
	{
		Pattern pattern =  null;
		Matcher matcher = null;

		File sourceFile = new File(Config.applicationPath+fileName);
		String source = FileUtils.readFileToString(sourceFile);
		List<Include> includes = new ArrayList<Include>();
		
		//<!--dddinclude start ddd.html -->
		//<!--dddinclude end ddd.html -->
		pattern = Pattern.compile(tagPrefix+"\\s*dddinclude\\s*+(\\S*?)\\s+(\\S*?)\\s*"+tagSuffix);
		matcher = pattern.matcher(source);
		while(matcher.find())
		{
			String dddinclude = matcher.group();
			Include include = new Include();
			include.startIndex = matcher.start();
			include.endIndex = matcher.end();
			include.command = matcher.group(1).trim();
			include.fileName = matcher.group(2).trim();
			includes.add(include);
			//System.out.println(include.toString());
		}
		for(int i =0; i< includes.size();)
		{
			int endIndex = findEndInclude(includes,i);
			if(endIndex == -1)
			{
				throw new Exception("dddinclude 指令没有找到 end,不匹配："+includes.get(i).toString());
			}
			for(int j =i+1; j< endIndex;j++)
			{
				includes.get(j).command = "sub";
			}
			i = endIndex+1;
		}
		for(int i =includes.size()-1; i >= 0;i--)
		{
			if(includes.get(i).command.equals("sub"))
			{
				includes.remove(i);
			}
		}
		
		for(int i = includes.size()-2; i >= 0 ;i--,i--)
		{
			
			File subFile = new File(Config.applicationPath+includes.get(i).fileName);
			String subContent = FileUtils.readFileToString(subFile);
			source = replaceInclude(source,includes.get(i).endIndex,includes.get(i+1).startIndex,subContent);
		}
		FileUtils.writeStringToFile(sourceFile, source);
		 
	}
	 
	private static String replaceInclude(String source,int startIndex,int endIndex,String replace)
	{
		int intentIndex = inverseIndexofChar(source,"\n",endIndex);
		String intent = "";
		if(intentIndex >= 0)
		{
			intent = source.substring(intentIndex, endIndex);
		}
		
		String result = source.substring(0,startIndex)+"\n"+replace+"\n"+intent+source.substring(endIndex);
		return result;
	}
	/**
	 * 从指定位置反向查询字串
	* @Title: inverseIndexofChar 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param str
	* @param searchStr
	* @param startPos
	* @return 
	* @return int
	 */
	private static int inverseIndexofChar(String str,String searchStr, int startPos)
	{
		startPos = startPos - searchStr.length();
		  for(int i=startPos;i >=0;i-- )
		  {
			  String subStr = str.substring(i, i+searchStr.length());
			  if(subStr.equals(searchStr))
			  {
				  return startPos;
			  }
		  }
		  return -1;
	}
	private static int findEndInclude(List<Include> includes,int startIndex)
	{
		boolean oddFlag = true;
		for(int i =startIndex+1; i< includes.size();i++)
		{
			if(includes.get(i).command.equals("end") &&
					includes.get(i).fileName.equals(includes.get(startIndex).fileName))
			{
				return i;
			}
		}
		return -1;
	}
	public static List<String> getCompileSources(String compileFile) throws Exception, IOException{
		List<String> compiles = new ArrayList<String>();
		
		compiles = FileUtils.readLines(new File(compileFile));
		for(int i=compiles.size()-1;i > 0;i-- ){
			if(compiles.get(i) == null ||  compiles.get(i).trim().equals(""))
			{
				compiles.remove(i);
			}
		}
		return compiles;
	}	
}


