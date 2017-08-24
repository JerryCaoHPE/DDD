package ddd.simple.dao.log;

import java.util.List;
import java.util.Set;

import ddd.simple.entity.log.Log;

/*
	@author yp
 */
public interface LogDao {
	
	public Log saveLog(Log Log) throws Exception;
	
	public void deleteLog(Long LogId) throws Exception;
	
	public Log updateLog(Log Log) throws Exception;
	
	public Log findLogById(Long LogId) throws Exception;

	public Set<Log> findLogs(String where) throws Exception;
	
}