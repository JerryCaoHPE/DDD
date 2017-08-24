package ddd.simple.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DispatcherProperty {
static Properties props=new Properties();
	
	static {
		InputStream in = PropertyUtil.class.getResourceAsStream("/dispatcherFilter.properties");  
		try {
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<String> getDispatcherProperties(){
		List<String> lists = new ArrayList<String>();
		for(Object key:props.keySet())
		{
			lists.add((String) props.get(key));
		}
		return lists;
	}
	public static void main(String[] args) {
		List<String> list=getDispatcherProperties();
		for(int i=0;i<list.size();i++)
		{
			System.err.println(list.get(i));
		}
	}
}
