package ddd.simple.service.log;
import java.util.Set;

import ddd.simple.entity.log.Log;


public interface LogService {
	public Log saveLog(Log Log) ;
	
	/*public void deleteLog(Long LogId);
	
	public Log updateLog(Log Log);*/
	
	public Log findLogById(Long LogId);

	public Set<Log> findLogs(String where);
}