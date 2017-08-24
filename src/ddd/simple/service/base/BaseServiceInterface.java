package ddd.simple.service.base;

import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.log.Log;
import ddd.simple.entity.permission.LoginUser;

public interface BaseServiceInterface {
	public LoginUser getLoginUser();
	//public Boolean setLoginUser(LoginUser loginUser);
	public String getLoginUserIp();
	public void setLoginUser(LoginUser loginUser);
	public void doLog(Log log);
	public void doLog(String logType,String operationName,
			Class[] entityClasses,String operationContent,String remark);
	public void doLog(String logType,String operationName,
			Class entityClass,String operationContent,String remark);
	public void initEntity(Entity entity);
	
	public void setCurrentOperateInfo(Entity entity);
	
	public void removeLoginUser();
}
