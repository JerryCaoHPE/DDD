package ddd.base.exporter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import ddd.base.util.FileUtil;
import ddd.simple.entity.organization.Department;

public class Test {

	public static void main(String[] args) {
		Config config  = new Config();
		
		config.setEntityName("department");
		config.setPkFieldName("code");
		//config.setEntityFields(entityFields);
		Map<String, Config> entityFields = new HashMap<String, Config>();
		
		
		Config config1  = new Config();
		config1.setEntityName("organization");
		config1.setPkFieldName("fullCode");
		entityFields.put("organization", config1);
		
		Config config2  = new Config();
		config2.setEntityName("employee");
		config2.setPkFieldName("code");
		entityFields.put("employees", config2);
		
		config.setEntityFields(entityFields);
		System.out.println(JSON.toJSONString(config, SerializerFeature.PrettyFormat));
	
		test2();
		
	}
	
	public static void test1(){
		String configString = null;
		try {
			configString = FileUtil.readeString(new File("C:\\Users\\Administrator\\Desktop\\config.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Config config = JSON.parseObject(configString, Config.class);
		System.out.println(config.getEntityName());
	}
	
	public static void test2(){
		String dataString = null;
		try {
			dataString = FileUtil.readeString(new File("C:\\Users\\Administrator\\Desktop\\config.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Department> departments = JSON.parseArray(dataString, Department.class);
		System.out.println(departments.size());
	}

}
