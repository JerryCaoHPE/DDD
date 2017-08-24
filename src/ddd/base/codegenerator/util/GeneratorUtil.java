package ddd.base.codegenerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ddd.base.codegenerator.generator.GenerationParameter;

public class GeneratorUtil {
	
	public static List<String> visitModifiers = new ArrayList<String>(
			Arrays.asList("public", "protected", "private"));
	
	public static List<String> attributeModifiers = new ArrayList<String>(
			Arrays.asList("final"));
	
	public static String subLowToUp(String low, int beginIndex, int endIndex) {
		if(beginIndex > endIndex || beginIndex < 0 || endIndex > low.length()) {
			return low;
		}
		String temp = low.substring(beginIndex, endIndex).toUpperCase();
		StringBuffer upper = new StringBuffer(low);
		upper.replace(beginIndex, endIndex, temp);
		return upper.toString();
	}
	
	public static String subUptoLow(String up, int beginIndex, int endIndex) {
		if(beginIndex > endIndex || beginIndex < 0 || endIndex > up.length()) {
			return up;
		}
		String temp = up.substring(beginIndex, endIndex).toLowerCase();
		StringBuffer low = new StringBuffer(up);
		low.replace(beginIndex, endIndex, temp);
		return low.toString();
	}
	
	public static void generateFromTemplate(String template, String target, Map<String, String> replacement,Boolean coverWrite) {
		String temp = readFile(template);
		for(String key : replacement.keySet()) {
			temp = temp.replace(key, replacement.get(key));
		}
		GenerationParameter.javaCode=temp;
		if(coverWrite==null){
			
		}
		else if(coverWrite==true){
			writeData(target, temp,coverWrite);
		}else if(coverWrite==false){
			writeData(target, temp, coverWrite);
		}
	}
	
	public static String readFile(String path) {
		
		FileInputStream reader = null;
		StringBuffer sb ;

		try {
			path = URLDecoder.decode(path, "UTF-8");
			File file = new File(path);
			reader = new FileInputStream(file);
			byte[] data= new byte[reader.available()];
			reader.read(data, 0, reader.available());
			reader.close();
			sb=new StringBuffer( new String(data,"UTF-8"));
			return sb.toString();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (reader != null) 
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e1) 
				{
				}
			}
		}
		return null;
	}
	
	private static void writeData(String filePath, String data,boolean coverWrite) {
		try 
		{
			filePath = URLDecoder.decode(filePath, "UTF-8");
			File file = new File(filePath);
			if(file.exists()){
				if(!coverWrite){
					System.err.println("文件已存在:("+filePath);
					return;
				}
			}
			File pDirec = new File(file.getParent());
			if(!pDirec.exists()) pDirec.mkdirs();
			FileOutputStream fileWriter;
			fileWriter = new FileOutputStream(file);
			fileWriter.write(data.getBytes("UTF-8"));
	 
			fileWriter.flush();
			fileWriter.close(); 
			System.out.println("成功生成:("+filePath);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
	private static String[] javaTypes={"String","Integer","Byte","Long","Boolean",
			"Double","Float","Date","Timestamp","BigDecimal","byte[]"};
	
	public static String isBaseDataType(String javaType){
		
		for (String type : javaTypes) {
			if( type.equalsIgnoreCase(javaType)){
				return type;
			}
		}
		return null;
	} 
	
	public static String getImport(File dir,String type){
		File[] list =  dir.listFiles();
		for (File file : list) {
			if(file.isDirectory()){
			 	String result = getImport(file,type);
			 	if(!result.equals("")){
			 		return result;
			 	}
			}else{
				String fileName =  file.getName();
				int endIndex = fileName.lastIndexOf(".");
				if(endIndex>0&&type.equals(fileName.substring(0, endIndex))){
					String parent = file.getParent().replaceAll("\\\\", ".");
					String packageName = parent.substring(parent.lastIndexOf(GenerationParameter.basePackageName));
					return "import "+packageName+"."+type+";";
				}
			}
		}
		return "";
	}

}
