package ddd.base.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ddd.base.Config;

public class VMPage {
	//模板文件
	private File vmFile;
	//模板
	private String vmTemplate;
	//模板路径
	private String vmPath;
	
	private Map<String,Object> data;

	public String getVmPath() {
		return vmPath;
	}

	public void setVmPath(String vmPath) {
//		this.vmPath =Config.tomcatWebContentPath+"/"+vmPath;
		this.vmPath = vmPath;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void putData(String key,Object value) {
		if(this.data==null){
			this.data = new HashMap<String, Object>();
		}
		this.data.put(key, value);
	}

	public File getVmFile() {
		return vmFile;
	}

	public void setVmFile(File vmFile) {
		this.vmFile = vmFile;
	}

	public String getVmTemplate() {
		return vmTemplate;
	}

	public void setVmTemplate(String vmTemplate) {
		this.vmTemplate = vmTemplate;
	}
	
}
