/**
* @Title: JSONCacheEngine.java
* @Package ddd.simple.service.cache
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年12月8日 下午6:32:05
* @version V1.0
*/
package ddd.simple.service.cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import ddd.base.Config;
import ddd.base.util.EntityUtil;
import ddd.base.util.JSONUtil;
import ddd.base.util.SpringContextUtil;
import ddd.base.util.json.JSONResult;
import ddd.simple.entity.codeTable.CodeTable;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.service.codeTable.CodeTableService;
import ddd.simple.service.listview.ReportViewService;

/**
 * 项目名称：DDD3
 * 类名称：JSONCacheEngine
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年12月8日 下午6:32:05
 * 修改人：DDD
 * 修改时间：2015年12月8日 下午6:32:05
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class JSONCacheEngine implements Runnable
{
	private String type;
	private String key;
	private Object object;
	private static Thread thread ;
	public static void  putCache(String type,String key,Object object)
	{
		if(thread != null)
		{	
			long waitTimes = 10000;//等10秒
			while(thread.isAlive() && waitTimes > 0)
			{
				try
				{
					waitTimes -= 1000; 
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
				}
			}
			if(thread.isAlive()) return; //放弃缓存
		}
		long start = System.currentTimeMillis();
		JSONCacheEngine cacheEngine = new JSONCacheEngine();
		thread = new Thread(cacheEngine);
		thread.start();
		if(Config.isDeveloping)
		{
			System.out.println("缓存完成,耗时："+(System.currentTimeMillis()-start));
		}
	}
	public static void initCache()
	{
		Map<String , Map<String,Object>> cached = new HashMap<String, Map<String,Object>>();
		
		cached.put("codeTable", loadCodeTables());
		cached.put("reportView", loadReportViews());
		
		initCache(cached);
	}
	public static Map<String,Object> loadReportViews()
	{
		//以下代码缓
		ReportViewService reportViewService =  (ReportViewService)SpringContextUtil.getBean("reportViewServiceBean");
		try
		{
			Set<ReportView> reportViews = reportViewService.findAllReportViews();
			Iterator<ReportView> iterator = reportViews.iterator();
			Map<String,Object> subCached = new HashMap<String, Object>() ;
			while(iterator.hasNext())
			{
				ReportView reportView = iterator.next();
				EntityUtil.clearProperty(reportView, "dataSource");
				EntityUtil.loadLazyProperty(reportView,"displayAttributes");
				EntityUtil.loadLazyProperty(reportView,"orderConditions");
				EntityUtil.loadLazyProperty(reportView,"searchConditions");
				EntityUtil.loadLazyProperty(reportView,"viewTreeConditions");
				EntityUtil.loadLazyProperty(reportView,"displayAttributes");
				EntityUtil.loadLazyProperty(reportView,"displayAttributes");
				
				EntityUtil.clearProperty(reportView,"displayAttributes.reportView");
				EntityUtil.clearProperty(reportView,"orderConditions.reportView");
				EntityUtil.clearProperty(reportView,"searchConditions.reportView");
				EntityUtil.clearProperty(reportView,"viewTreeConditions.reportView");
				EntityUtil.clearProperty(reportView,"displayAttributes.reportView");
				EntityUtil.clearProperty(reportView,"displayAttributes.reportView");
				
				
				subCached.put(reportView.getReportViewKey(), reportView);
			}
			return subCached;
		} catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("缓存视图(ReportView)出错，原因是："+e.getMessage());
		}
		return null;
	}
	public static Map<String,Object> loadCodeTables()
	{
		//以下代码缓
		CodeTableService codeTableService =  (CodeTableService)SpringContextUtil.getBean("codeTableServiceBean");
		try
		{
			Set<CodeTable> codeTables = codeTableService.findAllCodeTable();
			Iterator<CodeTable> iterator = codeTables.iterator();
			Map<String,Object> subCached = new HashMap<String, Object>() ;
			while(iterator.hasNext())
			{
				CodeTable codeTable = iterator.next();
				List<CodeTable> typeCodeTables ;
				if(codeTable.getCodeType() == null) continue;
				if(subCached.containsKey(codeTable.getCodeType().getCodeTypeKey()))
				{
					typeCodeTables = (List<CodeTable>)subCached.get(codeTable.getCodeType().getCodeTypeKey());
				}
				else
				{
					typeCodeTables = new ArrayList<CodeTable>();
					subCached.put(codeTable.getCodeType().getCodeTypeKey(), typeCodeTables);
				}
				EntityUtil.clearProperty(codeTable, "parent");
				EntityUtil.clearProperty(codeTable, "codeType");
				typeCodeTables.add(codeTable);
			}
			return subCached;
		} catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("缓存视图(CodeTable)出错，原因是："+e.getMessage());
		}
		return null;
	}	
	public static void initCache(Map<String , Map<String,Object>> cached)
	{
		File jsonCacheFile = new File(JSONCacheEngine.getFileName());

		JSONResult jsonResult = JSONUtil.toJSON(cached);
		
		try
		{
			writeJsonCacheFile(jsonCacheFile, jsonResult.getJson());
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("写缓存文件："+jsonCacheFile.getName()+"出错");
		}			
	}
	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		Map<String , Map<String,Object>> cached ;
		File jsonCacheFile = new File(JSONCacheEngine.getFileName());
		if( ! jsonCacheFile.exists())
		{
			cached = new HashMap<String, Map<String,Object>>();
		}
		else
		{
			try
			{
				String cacheJson = readJsonCacheFile(jsonCacheFile);
				cached = JSONUtil.fromJSON(cacheJson, HashMap.class);
			} catch (IOException e)
			{
				e.printStackTrace();
				System.err.println("读缓存文件："+jsonCacheFile.getName()+"出错");
				cached = new HashMap<String, Map<String,Object>>();
			}
			
			Map<String,Object> subCached  ;
			if(cached.containsKey(this.type))
			{
				subCached = cached.get(type);
			}
			else
			{
				subCached = new HashMap<String, Object>();
				cached.put(type, subCached);					
			}
			subCached.put(key, object);
			
			JSONResult jsonResult = JSONUtil.toJSON(cached);
			
			try
			{
				writeJsonCacheFile(jsonCacheFile, jsonResult.getJson());
			} catch (IOException e)
			{
				e.printStackTrace();
				System.err.println("写缓存文件："+jsonCacheFile.getName()+"出错");
			}			
		}
		thread = null;
	}
	private static void writeJsonCacheFile(File jsonCacheFile,String jsonCached) throws IOException
	{
		StringBuilder stringBuilder = new StringBuilder(jsonCached);
		stringBuilder.insert(0,"define(function (){return {cached:  ");
		stringBuilder.append("      };});");
		FileUtils.writeStringToFile(jsonCacheFile, stringBuilder.toString());
	}
	private static String readJsonCacheFile(File jsonCacheFile) throws IOException
	{
		String cacheJson = FileUtils.readFileToString(jsonCacheFile);
		cacheJson = cacheJson.substring(36,cacheJson.length()-7); //去除前后 定义代码：define(function (){return {cached:   };});
		return cacheJson;
	}	
	
	
	private static String getFileName()
	{
		String fileName ;
		if(Config.isDeveloping)
		{
			fileName = Config.applicationPath+"\\WebContent\\ddd\\simple\\view\\cache\\jsonCache.js";
		}
		else
		{
			fileName = System.getProperty("wtp.deploy")+"\\"+Config.applicationCode+"\\ddd\\simple\\view\\cache\\jsonCache.js";
		}
		return fileName;
	}
}
