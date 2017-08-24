package ddd.simple.service.base;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.log.Log;
import ddd.simple.entity.organization.Department;
import ddd.simple.entity.organization.Organization;
import ddd.simple.entity.permission.LoginUser;
import ddd.simple.service.log.LogService;

/**
 * @author Administrator
 *
 */
@Service
public class BaseService implements BaseServiceInterface{
	@Autowired 
	private HttpServletRequest request;
	@Resource(name="logServiceBean")
	private LogService logService;
	
	/**
	 * 获取登录用户
	 */
	public LoginUser getLoginUser(){
		
		try
		{
			if (request != null && request.getSession() != null) {
				@SuppressWarnings("rawtypes")
				LoginUser  loginUser = (LoginUser) request.getSession().getAttribute("loginUser");
				return loginUser; 
			}
		}catch(IllegalStateException e)
		{
			//如果此方法不是由前台请求，而是在启动时，或者定时服务，就没有登陆信息
			return null; 
		}
		return null;
	}
	/**
	 * 获取登录用户的单位
	 */
	public Organization getOrganization(){
		LoginUser loginUser = this.getLoginUser();
		if(loginUser != null )
		{
			return loginUser.getCurrentOrganization();
		}
		return null;
	}
	/**
	 * 获取登录用户的部门
	 */
	public Department getDepartment(){
		Department department = null;
		if (request != null && request.getSession() != null) {
			department =(Department) ((HashMap)request.getSession().getAttribute("loginUser")).get("dep");
		}
		return department;
	}

	/*public Boolean setLoginUser(LoginUser loginUser) {
		request.getSession().setAttribute("loginUser", loginUser);
		String userIp =request.getRemoteAddr();
		request.getSession().setAttribute("userIp", userIp);
	}*/
	
	/**
	 * 获取登录用户的IP
	 */
	public String getLoginUserIp(){
		return request.getRemoteAddr();
	}
	/**
	 * 设置登录用户
	 */
	public void setLoginUser(LoginUser loginUser){
		HttpSession session = request.getSession();
		session.setAttribute("loginUser", loginUser);
	}
	/**
	 * 设置登录会员
	 */
	public void setVipLogin(Object vipLogin){
		HttpSession session = request.getSession();
		session.setAttribute("vipLogin", vipLogin);
	}
	/**
	 * 写日志
	 */
	public void doLog(Log log){
		 logService.saveLog(log);
	}
	/**
	 * @param logType 日志类型
	 * @param operationName 操作名称
	 * @param entityClasses 操作中用到的实体
	 * @param operationContent 操作内容
	 * @param remark 操作备注
	 * @return 保存后的日志记录
	 */
	public void doLog(String logType,String operationName,
			Class[] entityClasses,String operationContent,String remark){
		Log log = new Log();
		log.setLogDate(new Date());
		LoginUser loginUser = (LoginUser) this.getLoginUser();
		if(loginUser != null && loginUser.getLoginOperator() != null)
		{
			log.setOperator(loginUser.getLoginOperator().getName());
			log.setOperatorCode(loginUser.getLoginOperator().getCode());
			log.setIpAddress(this.getLoginUserIp());
		}
		
		log.setLogType(logType);
		log.setOperationName(operationName);
		StringBuffer entityClassNames = new StringBuffer();
		for (Class entityClass : entityClasses) {
			entityClassNames.append(entityClass.getName());
			entityClassNames.append("  ");
		}
		log.setEntityClassName(entityClassNames.toString());
		log.setOperationContent(operationContent);
		log.setRemark(remark);
		this.logService.saveLog(log);
	}
	
	/**
	 * @param logType 日志类型
	 * @param operationName 操作名称
	 * @param entityClass 操作中用到的实体
	 * @param operationContent 操作内容
	 * @param remark 操作备注
	 * @return 保存后的日志记录
	 */
	public void doLog(String logType,String operationName,
			Class entityClass,String operationContent,String remark){
		this.doLog(logType, operationName, new Class[]{entityClass}, operationContent, remark);
	}
	
	/**
	 * 设置当前操作的人员和日期
	 */
	public void initEntity(Entity entity){
		if(entity.getOperatorCode()==null){
			entity.setOperateDate(new Date());
			if(this.getOrganization() != null){
				entity.setOrgId(this.getOrganization().getEId());
				if(getLoginUser() != null  && getLoginUser().getLoginOperator() != null){
					entity.setOperatorCode(getLoginUser().getLoginOperator().getCode());
				}
			}
		}
	}
 
	/* (非 Javadoc) 
	* <p>Title: setCurrentOperateInfo</p> 
	* <p>Description: </p> 
	* @param entity 
	* @see ddd.simple.service.base.BaseServiceInterface#setCurrentOperateInfo(ddd.base.persistence.baseEntity.Entity) 
	*/
	@Override
	public void setCurrentOperateInfo(Entity entity)
	{
		// TODO Auto-generated method stub
		
	}
	/* (非 Javadoc) 
	* <p>Title: removeLoginUser</p> 
	* <p>Description: </p>  
	* @see ddd.simple.service.base.BaseServiceInterface#removeLoginUser() 
	*/
	@Override
	public void removeLoginUser()
	{
		// TODO Auto-generated method stub
		request.getSession().removeAttribute("loginUser");
	}

}