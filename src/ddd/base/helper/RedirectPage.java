package ddd.base.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RedirectPage {
	
	private String path;              // 页面路径
    private Map<String, Object> data; // 相关数据
    
    
    //转发请求的时候的默认方法是get，也可以自己指定
    private String method = "get";
    
    public RedirectPage(){
    	data = new HashMap<String, Object>();
    }
    
    public RedirectPage(String path) {
        this.path = path;
        data = new HashMap<String, Object>();
    }
    
    
    public String getMethod() {
		return method;
	}

	public RedirectPage setMethod(String method) {
		this.method = method;
		return this;
	}

	public RedirectPage(String path,String type) {
        this.path = path;
        data = new HashMap<String, Object>();
    }

    public RedirectPage data(String key, Object value) {
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

    public RedirectPage setPath(String path) {
        this.path = path;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public RedirectPage setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
    
    public String getAtionPath(){
    	return this.method+":"+this.path;
    }
    public String genURL()
    {
    	StringBuffer url= new StringBuffer();
    	url.append(this.path).append("?");
        if (data!=null&&data.size()>0) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                try
				{
					url.append(URLEncoder.encode(entry.getKey().toString(),"UTF-8")).append("=").append(URLEncoder.encode(entry.getValue().toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }   
        return url.toString();
        
    }
    
}
