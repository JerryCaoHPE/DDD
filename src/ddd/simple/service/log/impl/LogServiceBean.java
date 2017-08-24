package ddd.simple.service.log.impl;
import javax.annotation.Resource;

import ddd.simple.dao.log.LogDao;
import ddd.simple.entity.log.Log;
import ddd.simple.service.log.LogService;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;

import ddd.base.exception.DDDException;

@Controller
public class LogServiceBean implements LogService {
	
	@Resource(name="logDaoBean")
	private LogDao LogDao ;
	
	@Override
	public Log saveLog(Log log)
	{
		try {
			return this.LogDao.saveLog(log);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveLog", e.getMessage(), e);
		}
	}

	/*@Override
	public void deleteLog(Long LogId){
		
		try {
			this.LogDao.deleteLog(LogId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteLog", e.getMessage(), e);
		}
	}

	@Override
	public Log updateLog(Log Log) {
		try {
			return this.LogDao.updateLog(Log);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateLog", e.getMessage(), e);
		}
	}*/

	@Override
	public Log findLogById(Long LogId){
		try {
			return this.LogDao.findLogById(LogId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findLogById", e.getMessage(), e);
		}
	}

	@Override
	public Set<Log> findLogs(String where) {
		try{
			return this.LogDao.findLogs(where);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findLogs", e.getMessage(), e);
		}
	}
	
}
