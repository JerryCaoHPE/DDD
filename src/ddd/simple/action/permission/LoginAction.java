package ddd.simple.action.permission;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.simple.entity.organization.Organization;
import ddd.simple.service.permission.OperatorService;
import ddd.simple.service.taskManageModel.TaskManageService;


@Action
@RequestMapping("/Login")
@Controller
public class LoginAction {
	
	@Autowired 
	private HttpServletRequest request;
	
	@Resource(name="operatorServiceBean")
	private OperatorService operatorService;
	
	@Resource(name="taskManageServiceBean")
	private TaskManageService taskManageService;
	
	public Map<String, Object> checkLoginUser(String operatorCode,String operatorPassword,Organization organization)throws Exception
	{
		try {
				Map<String, Object> loginMap = this.operatorService.checkLoginUser(operatorCode, operatorPassword,organization);
				return loginMap;
		} catch (DDDException e) {
			throw e;
		}
	}
	public ArrayList<Organization> searchOrganization(String operatorCode) throws Exception{
		try {
			return this.operatorService.searchOrganization(operatorCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public void loginOut(HttpServletRequest request)
	{
		try {
			request.getSession().removeAttribute("loginUser");
		} catch (DDDException e) {
			throw e;
		}
	}
	
}
