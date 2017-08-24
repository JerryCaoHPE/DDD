package ddd.simple.service.externalInterface;

import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import ddd.base.exception.DDDException;
import ddd.base.util.SpringContextUtil;
import ddd.simple.entity.externalInterface.ExternalInterface;

public class ExternalInterfaceEngine
{
	private static ExternalInterfaceService externalInterfaceService = (ExternalInterfaceService) SpringContextUtil
			.getBean("externalInterfaceServiceBean");
			
	public static void execute(String key)
	{
		try
		{
			ExternalInterface externalInterface = externalInterfaceService.findExternalInterfaceByKey(key);
			String configJson = externalInterface.getConfigJson();
			String executeClass = externalInterface.getExecuteClass();
			Map<String, Object> config = (Map<String, Object>) JSON.parse(externalInterface.getConfigJson());
			if (executeClass != null)
			{
				EIInterface ei;
				try
				{
					Class<? extends EIInterface> clazz = (Class<? extends EIInterface>) Class.forName(executeClass);
					ei = clazz.newInstance();
				} catch (Exception e)
				{
					throw new DDDException("无法实例化，实现类路径错误:" + executeClass);
				}
				ei.execute(config);
			} else
			{
				throw new DDDException("未指定实现类");
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("ExternalInterfaceEngine.execute()", e.getMessage(), e);
		}
	}
}
