package ddd.simple.util.model;

import java.util.HashMap;
import java.util.Map;

public class ModelItemTypeTojavaType {
	
	private static Map<String, String> map = new HashMap<String, String>();
	static{
		map.put("datetime", "java.util.Date");
	}
	/**
	 * 不做配置的，默认返回String类型
	 * @param modelItemType
	 * @return
	 */
	public static String getJavaType(String modelItemType){
		for (String type : map.keySet()) {
			if(type.equalsIgnoreCase(modelItemType)){
				return map.get(type);
			}
		}
		return "java.lang.String";
	}

}
