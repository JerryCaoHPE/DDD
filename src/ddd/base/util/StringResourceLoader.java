package ddd.base.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import ddd.base.Config;

public class StringResourceLoader extends ResourceLoader{

	@Override
	public long getLastModified(Resource arg0) {
		return 0;
	}

	@Override
	public InputStream getResourceStream(String stringTemplate)
			throws ResourceNotFoundException {
		InputStream result = null;
		if(stringTemplate.endsWith(".vm") ||  stringTemplate.endsWith(".html"))
		{
			return null;
		}
		if (stringTemplate == null || stringTemplate.length() == 0) {  
            throw new ResourceNotFoundException("模板没有被定义~！");  
        }  
        try {
			result = new ByteArrayInputStream(stringTemplate.getBytes(Config.CHARSET_UTF8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
     
        return result; 
	}

	@Override
	public void init(ExtendedProperties arg0) {
		
	}

	@Override
	public boolean isSourceModified(Resource arg0) {
		return false;
	}

}
