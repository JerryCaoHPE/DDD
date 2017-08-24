package ddd.base.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import ddd.base.exception.DDDException;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.util.json.DDDJSON;
import ddd.base.util.json.JSONReferenceSerializer;
import ddd.base.util.json.JSONResult;



public class JSONUtil {
	public  static void main(String[] args)
	{
		Student map = fromJSON("{name:'111',date:'1990-08-15'}",Student.class);
		System.out.println(map);
	}
	
	private static SerializeConfig serializeConfig = new SerializeConfig(); 
	static {
		SimpleDateFormatSerializer dateFormatSerializer = new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss");
	 
		serializeConfig.put(java.util.Date.class, dateFormatSerializer);  
		serializeConfig.put(java.sql.Date.class, dateFormatSerializer);
		serializeConfig.put(java.sql.Timestamp.class,dateFormatSerializer); 
		serializeConfig.put(java.sql.Time.class, dateFormatSerializer); 
	} 
    // 将 Java 对象转为 JSON 字符串
    public static <T> JSONResult toJSONFormat(T obj) {
    	JSONResult jsonResult ;
        try {
        	jsonResult = DDDJSON.toJSONString(obj,serializeConfig,
            		SerializerFeature.SkipTransientField,
            		SerializerFeature.PrettyFormat,
            		SerializerFeature.BrowserCompatible,
            		SerializerFeature.WriteNullListAsEmpty,
            		SerializerFeature.WriteNullStringAsEmpty);
        } catch (Exception e) {
            DDDException dddException = new DDDException("toJSONFormatError", "将对象转换成JSON出错，原因是："+e.getMessage(), e);
            dddException.putExtendedData("Class", obj.getClass().getName());
            throw dddException;
        }
        return jsonResult;
    }
    // 将 Java 对象转为 JSON 字符串
    public static <T> JSONResult toJSON(T obj) {
    	JSONResult jsonResult ;
        try {
        	jsonResult = DDDJSON.toJSONString(obj,serializeConfig,
            		SerializerFeature.SkipTransientField,
            		SerializerFeature.BrowserCompatible,
            		SerializerFeature.WriteNullListAsEmpty,
            		SerializerFeature.WriteNullStringAsEmpty);
        } catch (Exception e) {
            DDDException dddException = new DDDException("toJSONError", "将对象转换成JSON出错，原因是："+e.getMessage(), e);
            dddException.putExtendedData("Class", obj.getClass().getName());
            throw dddException;
        }
        return jsonResult;
    }
    public static JSONResult  entityToJSON(Object obj,boolean format){
    	JSONResult jsonResult ;
    	Entity.setLazyLoad(false);
    	if(format){
    		jsonResult = toJSONFormat(obj);
    	}else{
    		jsonResult = toJSON(obj);
    	}
    	Entity.setLazyLoad(true);
    	return jsonResult;
    }
    
    
    // 将 JSON 字符串转为 Java 对象
    public static <T> T fromJSON(String json, Class<T> type) {
        T obj = null;
        try {
            obj = JSON.parseObject(json, type);
        } catch (Exception e) {
           DDDException dddException = new DDDException("fromJSONError", "从JSON中解析字符串出错，原因是："+e.getMessage(), e);
           dddException.putExtendedData("JSON", json);
           dddException.putExtendedData("Class", type.getName());
           throw dddException;
        }
        return obj;
    }
    
    public static String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
        	JSONReferenceSerializer serializer = new JSONReferenceSerializer(out);
        
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }
            
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }
}
