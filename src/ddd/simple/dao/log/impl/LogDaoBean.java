package ddd.simple.dao.log.impl;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.log.LogDao;
import ddd.simple.entity.log.Log;

@Controller
@Service
public class LogDaoBean extends BaseDao implements LogDao {

	@Override
	public Log saveLog(Log Log)  throws Exception{
		return this.save(Log);
	}

	@Override
	public void deleteLog(Long LogId)  throws Exception{
		 this.deleteById(LogId,Log.class);
	}

	@Override
	public Log updateLog(Log Log)  throws Exception{
		return this.update(Log);
	}

	@Override
	public Log findLogById(Long LogId)  throws Exception{
		return this.query(LogId, Log.class);
	}
	
	@Override
	public Set<Log> findLogs(String where) throws Exception {
		return this.query(where,Log.class);
	}
	
}
