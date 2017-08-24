package ddd.base.helper;

import java.util.HashMap;
import java.util.Map;

public class ForwardPage {
	private String path;              // 页面路径
    private Map<String, Object> data; // 相关数据
    
    
    //转发请求的时候的默认方法是get，也可以自己指定
    private String method = "get";
    
    public ForwardPage(){
    	data = new HashMap<String, Object>();
    }
    
    public ForwardPage(String path) {
        this.path = path;
        data = new HashMap<String, Object>();
    }
    
    
    public String getMethod() {
		return method;
	}

	public ForwardPage setMethod(String method) {
		this.method = method;
		return this;
	}

    public ForwardPage data(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public boolean isRedirect() {
    	System.err.println(path+":"+!path.contains("."));
    	return !path.contains(".");
       
    }

	public String getPath() {
        return path;
    }

    public ForwardPage setPath(String path) {
        this.path = path;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ForwardPage setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
    
    public String getAtionPath(){
    	return this.method+":"+this.path;
    }
    
}
