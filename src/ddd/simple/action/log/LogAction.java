package ddd.simple.action.log;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSONObject;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.simple.entity.log.Log;
import ddd.simple.service.log.LogService;

@Action
@RequestMapping("/Log")
@Controller
public class LogAction {
	
	@Resource(name="logServiceBean")
    private LogService LogService;
	
	public String findLogById(Long logId){
		try {
			Log findLog = this.LogService.findLogById(logId);
			return  JSONObject.toJSONString(findLog);
		} catch (DDDException e) {
			return JSONObject.toJSONString(e);
		}
	}
	
	public Log saveLog(Log log){
		return this.LogService.saveLog(log);
	}
	

}
