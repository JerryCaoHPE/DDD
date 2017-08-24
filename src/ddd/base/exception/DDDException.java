package ddd.base.exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ddd.base.Config;
import ddd.base.util.DateUtil;

public class DDDException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	protected String code;
    protected Map<String,Object> extendedData = new HashMap<String, Object>();
  
    
    static
    {
    	File file = new File(Config.logPath);
    	if(! file.exists())
    	{
    		file.mkdirs();
    	}
    }
    public static void logInFile(Exception exception,String errorId)
    {
    	if(Config.logPath == null) return ;
    	
    	String  logFileName = Config.logPath + "\\"+ DateUtil.getNowDateTimeAsFileName()+"_"+exception.getClass().getName()+"_"+errorId+".txt";
    
    	PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logFileName)),true);
	    	printWriter.println(logFileName);
	    	printWriter.println(exception.getMessage());
	    	if(exception.getCause() != null)
	    	{
	    		exception.getCause().printStackTrace(printWriter);
	    	}
	    	if( exception instanceof DDDException )
	    	{
	    		DDDException dddException = (DDDException)exception;
	    		printWriter.println("DDDException编码是："+dddException.getCode());
	    		printWriter.println("DDDException扩展数据是：");
		    	if(dddException.extendedData != null)
		    	{
		    		Iterator<String> extendedDataIterator = dddException.extendedData.keySet().iterator();
		    		while(extendedDataIterator.hasNext())
		    		{
		    			String key = extendedDataIterator.next();
		    			printWriter.print(key);
		    			printWriter.print("=");
		    			printWriter.println(dddException.extendedData.get(key));
		    		}
		    	}
	    	}
	    	printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
    	 
    }
    public  static void printException(Exception exception)
    {
    	if(Config.isDeveloping)
    	{
    		if(exception != null)
    		{
    			exception.printStackTrace();
    		}
    	}
    }
	public DDDException(String code,String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		if(cause instanceof DDDException)
		{
			this.extendedData.putAll(((DDDException)cause).getExtendedData());
		}		
	}

	public DDDException( String code,String message) {
		super(message);
		this.code = code;
	}

	public DDDException(String message, Throwable cause) {
		super(message, cause);
		if(cause instanceof DDDException)
		{
			this.extendedData.putAll(((DDDException)cause).getExtendedData());
		}		
	}

	private String getRooCuauseMessage(Throwable cause)
	{
		String rootMessage = "";
		while(cause != null)
		{
			rootMessage+=cause.getMessage()+"\n";
			cause = cause.getCause();
		}
		return rootMessage;
	}
	public DDDException(Throwable cause) {
		super(cause); 
		if(cause instanceof DDDException)
		{
			this.extendedData.putAll(((DDDException)cause).getExtendedData());
		}
	}

	public DDDException(String message) {
		super(message);
	}
	/**
	 * @return the extendedData
	 */
	public Map<String,Object> getExtendedData() {
		return extendedData;
		 
	}

	/**
	 * @param extendedData the extendedData to set
	 */
	public void setExtendedData(Map<String,Object> extendedData) {
		this.extendedData = extendedData;
	}
	/*
	 * 增加扩展数据
	 */
	public void putExtendedData(String key,Object value)
	{
 
		this.extendedData.put(key, value);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
}
