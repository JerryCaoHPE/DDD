package ddd.simple.service.outInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.util.SpringContextUtil;
import ddd.simple.entity.outInterface.OutInterfaceConfig;

@Service
public class OutInterfaceScheduleApp 
{
	@Resource(name="outInterfaceConfigServiceBean")
	OutInterfaceConfigService outInterfaceConfigService;
	public void execute(String configName)
	{
		OutInterfaceConfig outInterfaceConfig = outInterfaceConfigService.findOutInterfaceConfigByName(configName);
		if(outInterfaceConfig ==  null)
		{
			throw new DDDException("没有找到名字为:"+configName+"的外部同步接口配置");
		}
		String excuteBeanName = outInterfaceConfig.getExcuteInterfaceBean();
		if(excuteBeanName == null || excuteBeanName.length() == 0)
		{
			excuteBeanName = "outInterfaceCommonServiceBean";
		}
		OutInterfaceBaseService outInterfaceBaseService = (OutInterfaceBaseService)SpringContextUtil.getBean(excuteBeanName);
		outInterfaceBaseService.excute(outInterfaceConfig);
	}
	
	
	public void executeByParam(String configName,String param)
	{
		OutInterfaceConfig outInterfaceConfig = outInterfaceConfigService.findOutInterfaceConfigByName(configName);
		if(outInterfaceConfig ==  null)
		{
			throw new DDDException("没有找到名字为:"+configName+"的外部同步接口配置");
		}
		String excuteBeanName = outInterfaceConfig.getExcuteInterfaceBean();
		if(excuteBeanName == null || excuteBeanName.length() == 0)
		{
			excuteBeanName = "outInterfaceCommonServiceBean";
		}
		OutInterfaceBaseService outInterfaceBaseService = (OutInterfaceBaseService)SpringContextUtil.getBean(excuteBeanName);
		outInterfaceBaseService.excuteByParam(outInterfaceConfig, param);
	}
	public void excuteEveryDayTask(String configName,String param)
	{
		OutInterfaceConfig outInterfaceConfig = outInterfaceConfigService.findOutInterfaceConfigByName(configName);
		if(outInterfaceConfig ==  null)
		{
			throw new DDDException("没有找到名字为:"+configName+"的外部同步接口配置");
		}
		String excuteBeanName = outInterfaceConfig.getExcuteInterfaceBean();
		if(excuteBeanName == null || excuteBeanName.length() == 0)
		{
			excuteBeanName = "outInterfaceCommonServiceBean";
		}
		OutInterfaceBaseService outInterfaceBaseService = (OutInterfaceBaseService)SpringContextUtil.getBean(excuteBeanName);
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-mm-dd");
		String todayString = sFormat.format(new Date());
		String dataParam = "STARTDATE_VALUE=="+todayString+";;ENDDATE_VALUE=="+todayString;
		if(param == null)
		{
			param = dataParam;
		}
		else
		{
			param = param + dataParam;
		}
		outInterfaceBaseService.excuteByParam(outInterfaceConfig, param);
	}
}
