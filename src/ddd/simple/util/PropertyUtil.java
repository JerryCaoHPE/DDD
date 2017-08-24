package ddd.simple.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class PropertyUtil {
	static Properties props=new Properties();
	
	static {
		InputStream in = PropertyUtil.class.getResourceAsStream("/db.properties");  
		try {
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		
		return props.getProperty(key); 
	}

}
