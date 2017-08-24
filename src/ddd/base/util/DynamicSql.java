package ddd.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ddd.base.Config;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.service.listview.DataSourceService;

public class DynamicSql {

	private static List<File> dynamicSqlFile=new ArrayList<File>();
	//private static DynamicSQLService dynamicSQLService = DynamicSQLService.getInsance();
	
	private static DataSourceService dataSourceService = (DataSourceService) SpringContextUtil.getBean("dataSourceServiceBean");
	
	public static void main(String[] args) {
		String basePath = "D:\\angular\\tomcat7\\webapps\\DDD3\\WEB-INF\\classes";
		initDynamicSql(basePath);
	}
	
	public static String getDynamicSql(Class clazz,String sqlKey,Map values){
		
		
		
		return null;
	}
	
	
	public static void initDynamicSql(String basePath){
		String[] packages = Config.DynamicSqlPackages.replaceAll(" ", "").split(",");
		for (String packageName : packages) {
			String path = packageName.replaceAll("[.]", "/");
			File dir = new File(basePath+"/"+path);
			if(dir.exists()){
				selectDir(dir);
			}
		}
		initDynamicSqls();
	}
	private static void selectDir(File dir){
		File[] list =  dir.listFiles();
		for (File file : list) {
			if(file.isDirectory()){
				selectDir(file);
			}else{
				String fileName =  file.getName();
				int index = fileName.lastIndexOf(".");
				String suffix = fileName.substring(index);
				if(Config.DynamicSqlSuffix.equalsIgnoreCase(suffix)){
					dynamicSqlFile.add(file);
					System.out.println("找到文件："+file.getAbsolutePath());
				}
			}
		}
	}

	private static void initDynamicSqls() {
		long start = System.currentTimeMillis();
		// 遍历文件列表
		for (int i = 0; i < dynamicSqlFile.size(); i++) {
			String fileString = FileIO.readToString(dynamicSqlFile.get(i));
			fileString = dealWithFileString(fileString);
			String[] strings = fileString.split("\\$sql");
			for(int j =0,count = strings.length;j<count;j++)
			{
				if(!strings[j].startsWith("#"))
				{
					String firstLine = strings[j].substring(0,strings[j].indexOf("\n")).replaceAll("\r", "").trim();
					String dynamicSqlName = firstLine.split(" ")[0];
					String dynamicSql = strings[j].substring(strings[j].indexOf("\n")+1);
					//dynamicSQLService.addDynamicSQL(dynamicSqlName,dynamicSql);
					//System.out.println(dynamicSQLService.genSQL(dynamicSqlName, new HashMap<String,Object>()));
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("py文件动态sql生成成功,用时"+(end-start)+"ms");
	}
	private static String dealWithFileString(String fileString) {
		String[] temp = fileString.split("\n");
		String returnString ="";
		for(int i=0;i<temp.length;i++)
		{
			if(temp[i].startsWith("\t"))
				returnString += temp[i].substring(1)+"\n";
			else
				returnString += temp[i]+"\n";
		}
		return returnString;
	}
	
	public static void initDataSourceDynamicSql() {
		long start = System.currentTimeMillis();
		// 获取数据源
		EntitySet<DataSource> dataSources = dataSourceService.findDataSources();
		
		// 遍历  // 生成动态SQL
		for (DataSource dataSource : dataSources) {  
			String key = dataSource.getDataSourceCode();
			String sql = dataSource.getReportSql();
			//dynamicSQLService.addDynamicSQL(key,sql);
			//System.out.println(dynamicSQLService.genSQL(key, new HashMap<String,Object>()));
		}  
		long end = System.currentTimeMillis();
		System.out.println("数据源动态sql生成成功,用时"+(end-start)+"ms");
	}
}
