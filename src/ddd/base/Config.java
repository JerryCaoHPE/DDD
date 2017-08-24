package ddd.base;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ddd.base.persistence.SessionFactory;
import ddd.base.util.Loader;

/**
 *@author 作者:吴桥伟
 *@version 创建时间：2015年1月28日 下午12:52:50
 * 类说明
 */
public class Config {
	public static Map<String, String> config = new HashMap<String, String>();
	
	static{
		Loader loader =Loader.instance();
		try {
			if(config.size()==0){
				config.putAll(loader.load("config.properties"));
				SessionFactory.setDatabase(config.get("dataBase"));
				
				String temp = config.get("applicationPath");
				while(temp.endsWith("/")){
					temp = temp.substring(0, temp.length()-1);
				}
				config.put("applicationName", temp.substring(temp.lastIndexOf("/")+1));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String get(String configName){
		return config.get(configName);
	}
	public static String put(String configName,String value){
		if("tomcatWebContentPath".equals(configName)){
			tomcatWebContentPath=value;
		}
		return config.put(configName,value);
	}
	/**
	 * 动态sql所在的包
	 */
	public static final String DynamicSqlPackages = "ddd.simple.dao";
	/**
	 * 动态sql的后缀
	 */
	public static final String DynamicSqlSuffix = ".py";
	/**
	 * 基础包名,扫描实体时使用
	 */
	public static final String packageName = "ddd";
	/**
	 * 数据库配置
	 */
	public static final String jdbcConfig = "jdbc.properties";

	public static final String CHARSET_UTF8 = config.get("charset_UTF8");
	
	public static final String CHARSET_ISO = config.get("charset_ISO");
	
	/**
	 * 生成java代码的上级包名
	 */
	public static final String basePackage = config.get("basePackage");
	/**
	 * webroot目录
	 */
	public static final String webRoot  = config.get("webRoot");
	/**
	 * 子系统包名
	 */
	public static final String sbuSystem  = basePackage.substring(basePackage.lastIndexOf(".")+1);
	/**
	 * 生成html和js的上级目录
	 */
	public static final String htmlAndjsCodeGenPath = webRoot+"/"+basePackage.replaceAll("[.]", "/")+"/view/";
	/**
	 * 项目根目录
	 */
	public static final String applicationPath = config.get("applicationPath");
	
	/**
	 * 框架根目录
	 */
	public static final String ddd3Path = config.get("ddd3Path");
	/**
	 * 项目名称
	 */
	public static final String applicationName = config.get("applicationName");
	
	/**
	 * 项目代码
	 */
	public static final String applicationCode = config.get("applicationCode");
	
	/**
	 * 项目简码
	 */
	public static final String shortCode = config.get("shortCode");
	
	/**
	 * tomcat的webcontent目录
	 */
	public static  String tomcatWebContentPath;
	
	/**
	 * 默认每次缓存序列号的数量 
	 */
	public static final Long primaryKeyCachedSize = Long.parseLong(config.get("primaryKeyCachedSize"));
	
	/**
	 *  序列号中每个段的大小，如果有多个服务器，为每次每个服务扩展的段的大小 
	 */
	public static final Long primaryKeySegmentSize =  Long.parseLong(config.get("primaryKeySegmentSize"));
	
	/**
	 * Web服務實例的ID，即如果衕一箇APP在多箇tomcat中佈置多箇，每箇tomcat實例有唯一標識
	 */
	public static String serverId = config.get("serverId");
	
	/**
	 * 日志文件存放位置
	 */
	public static String logPath = config.get("logPath");
	
	/**
	 * 系统运行在开发模式
	 */
	public static boolean isDeveloping = "true".equals(config.get("isDeveloping"));
	
	public static String dynamicFormVMPath = config.get("dynamicFormVMPath");
}
