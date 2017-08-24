package ddd.base.dynamicSql;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import ddd.base.persistence.SessionFactory;
import ddd.base.util.SQLUtil;
import ddd.base.util.SpringContextUtil;
import ddd.base.util.VelocityUtil;
import ddd.simple.dao.listview.ListViewDao;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.base.InternalParamHandler;
import ddd.simple.service.base.InternalParamHandlerInterface;
import ddd.simple.service.listview.DataSourceService;

public class DynamicService {
	
	public static InternalParamHandler internalParamHandler =(InternalParamHandler)SpringContextUtil.getBean("internalParamHandler");
	
	
	public static String getDynamicSql(String stringTemplate,Map<String, Object> map){
		if(stringTemplate == null || "".equals(stringTemplate.trim()))return stringTemplate;
		
		map = internalParamHandler.addInternalParam(map);
		
		String dysql = VelocityUtil.generateString(stringTemplate, map);
		String temp = dysql;
		if(temp.toLowerCase().trim().startsWith("select")){
			String[] properties = SQLUtil.getProperties(temp);
			
			int start = temp.indexOf("_Limit(");
			if(start!=-1){
				int end = temp.indexOf(")", start);
				String limit = temp.substring(start+7, end);
				String[] limits = SQLUtil.split(limit, "[,]");
				temp = temp.substring(0, start)+temp.substring(end+1, temp.length());
				dysql = SessionFactory.getSqlHandler().limit(temp,properties,limits[0], limits[1]);
			}
		}
		
		return dysql;
	}
	/**
	 * 获取动态sql中需要传的参数
	 * @param dynamicSql
	 * @return
	 */
	public static Set<String> getDynamicSqlParams(String dynamicSql){
		Set<String> params = new HashSet<String>();
		int index = dynamicSql.indexOf('$');
		int endIndex=0;
		while(index!=-1){
			String param = null;
			index=findStartSignIndex(dynamicSql, index);
			endIndex = findEndSignIndex(dynamicSql, index);
			if(endIndex==-1){
				endIndex=dynamicSql.length();
			}
			param = dynamicSql.substring(index, endIndex);
			params.add(param);
			index = dynamicSql.indexOf('$',endIndex);
		}
		return params;
	}
	private static int findStartSignIndex(String dynamicSql, int fromIndex){
		char sign;
		while(++fromIndex <dynamicSql.length()){
			sign = dynamicSql.charAt(fromIndex);
			if((sign>'a'&&sign<'z')||(sign>'A'&&sign<'Z')||(sign>'0'&&sign<'9')||sign=='_'){
				return fromIndex;
			}
		}
		return -1;
	}
	private static int findEndSignIndex(String dynamicSql, int fromIndex){
		char sign;
		while(++fromIndex <dynamicSql.length()){
			sign = dynamicSql.charAt(fromIndex);
			if((sign<'a'||sign>'z')&&(sign<'A'||sign>'Z')&&(sign<'0'||sign>'9')&&sign!='_'){
				return fromIndex;
			}
		}
		return -1;
	}
}
