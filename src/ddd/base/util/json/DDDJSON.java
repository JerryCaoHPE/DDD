/**
* @Title: JSON.java
* @Package ddd.base.util.json
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月26日 下午2:22:58
* @version V1.0
*/
package ddd.base.util.json;

import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.LabelFilter;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 项目名称：DDD3
 * 类名称：JSON
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月26日 下午2:22:58
 * 修改人：DDD
 * 修改时间：2015年11月26日 下午2:22:58
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public abstract class DDDJSON
{
	  public static JSONResult toJSONString(Object object, SerializeConfig config, SerializerFeature... features) {
	        return toJSONString(object, config, (SerializeFilter) null, features);
	    }

	    public static JSONResult toJSONString(Object object, SerializeConfig config, SerializeFilter filter,
	                                            SerializerFeature... features) {
	    	
	        SerializeWriter out = new SerializeWriter();

	        try {
	            JSONReferenceSerializer serializer = new JSONReferenceSerializer(out, config);
	            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
	                serializer.config(feature, true);
	            }

	            setFilter(serializer, filter);

	            serializer.write(object);

	            JSONResult jsonResult = new JSONResult(serializer.isHasReference(),  out.toString());
	            return jsonResult;
	        } finally {
	            out.close();
	        }
	    }
 

	    private static void setFilter(JSONSerializer serializer, SerializeFilter filter) {
	        if (filter == null) {
	            return;
	        }
	        
	        if (filter instanceof PropertyPreFilter) {
	            serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
	        }

	        if (filter instanceof NameFilter) {
	            serializer.getNameFilters().add((NameFilter) filter);
	        }

	        if (filter instanceof ValueFilter) {
	            serializer.getValueFilters().add((ValueFilter) filter);
	        }

	        if (filter instanceof PropertyFilter) {
	            serializer.getPropertyFilters().add((PropertyFilter) filter);
	        }

	        if (filter instanceof BeforeFilter) {
	            serializer.getBeforeFilters().add((BeforeFilter) filter);
	        }

	        if (filter instanceof AfterFilter) {
	            serializer.getAfterFilters().add((AfterFilter) filter);
	        }
	        
	        if (filter instanceof LabelFilter) {
	            serializer.getLabelFilters().add((LabelFilter) filter);
	        }
	    }
}
