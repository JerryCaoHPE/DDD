package ddd.simple.util;

import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

public class Strings {
	
	public static final String NOTICE_SUCCESS = "success";
	
	public static final String JSONObject_SUCCESS_Tip = "{\"result\":\"success\"}";

	//查询条件根占位符
	public static final String PLACEHOLDER = "queryplace" ;
	
	public static final String 	ERROR = "保存失败!,错误信息:";
	
	public static final String BASETBALE  = "CquqBaseTable";
	
	public static final String BASESQL  = "CquqBaseSQL";
	
	public static final String SERVICE_PACKAGE_URL = "org.cqut.cqutq.service.";
	
	public static final String REPORTVIEW_ADD_URL = "/cquq/reportview.jsp";
	
	public static final String ALL_CATEGORY = "所有类别";
	
	public static final String KEY_CATEGORY = "categoryId";
	
	// 保存动态SQL class 文件目录     D:/angular/workspace/DDD3/src
	public static final String DYNAMICSQLCLASSPATH = "D:/angular/tomcat7/webapps/DDD3/WEB-INF/classes";
	
	public static String getJsonString(Map<String ,String > map){
		JSONArray jsonArray = JSONArray.fromObject(map); 
		String jsonString = jsonArray.toString();
		
		return jsonString;
	}
	
	public static String escapeExprSpecialWord(String keyword) {  
	    if (StringUtils.isNotBlank(keyword)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	}  
}
