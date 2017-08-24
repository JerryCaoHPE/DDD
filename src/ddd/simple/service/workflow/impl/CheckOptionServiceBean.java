package ddd.simple.service.workflow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddd.base.exception.DDDException;
import ddd.simple.dao.workflow.CheckOptionDao;
import ddd.simple.entity.workflow.CheckOption;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.workflow.CheckOptionService;

@Service
@Transactional
public class CheckOptionServiceBean extends BaseService implements CheckOptionService
{
	final Logger logger = LoggerFactory.getLogger(CheckOptionServiceBean.class);

	@Resource(name="checkOptionDaoBean")
	private CheckOptionDao checkOptionDao;
	 
	 //缓存对象
//	@Resource(name = "camCache")
//	private Ehcache cache;
	
	public  void  deleteCheckOptionById( Long checkOptionId) 
	{
		try
		{
		 	this.checkOptionDao.deleteById(checkOptionId, CheckOption.class);
	   	}
		catch(Exception e)
		{
			DDDException ex= new DDDException("deleteCheckOptionByIdError",e.getMessage(),e);
	        throw ex;			
		}
	}
	
	public  CheckOption findCheckOptionById( Long checkOptionId)
	{
		try
		{	
			CheckOption foundCheckOption = this.checkOptionDao.query(checkOptionId,CheckOption.class);
		 	return foundCheckOption;
	   	}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByIdError",e.getMessage(),e);
	        throw ex;			
		}	
	} 
	
  
	public CheckOption saveCheckOption(CheckOption checkOption) {
		try
		{		
			CheckOption savedCheckOption = this.checkOptionDao.save(checkOption);
		 	return savedCheckOption;			
	   	}
		catch(Exception e)
		{
			DDDException ex= new DDDException("saveCheckOptionError",e.getMessage(),e);
	        throw ex;			
		}			
	}

	public CheckOption updateCheckOption(CheckOption checkOption) {
		try
		{		
			CheckOption updatedCheckOption = this.checkOptionDao.update(checkOption);
		 	return updatedCheckOption;			
	   	}
		catch(Exception e)
		{
			DDDException ex= new DDDException("updateCheckOptionError",e.getMessage(),e);
	        throw ex;			
		}			
	}

	public  void deleteCheckOption(CheckOption checkOption) 
	{
		try
		{
		   //this.logging("deleteCheckOption","deleteCheckOption",checkOption);
		   this.checkOptionDao.delete(checkOption);
	   	}
		catch(Exception e)
		{
			DDDException ex= new DDDException("deleteCheckOptionError",e.getMessage(),e);
	        throw ex;			
		}	
	}
	
	
	public Set<Map<String, Object>> findCheckOptionsByCheckOptionIdAndFormId(Long checkOptionId,Long formId)
	{
		try
		{
			String sql = "select * from checkOption co where co.checkOptionId=" + checkOptionId + " and co.formId=" + formId;
			return  this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionsByCheckOptionIdAndFormIdError",e.getMessage(),e);
	        throw ex;			
		}
	}
	
	public Set<Map<String, Object>> findCheckOptionsByFormTypeAndAuditStep(String formType,String taskName)
	{
		try
		{
			String sql = "select * from checkOption co where co.formType = '"+formType+"' and co.taskName= '"+taskName+"' ";
			return  this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionsByFormTypeAndAuditStepError",e.getMessage(),e);
	        throw ex;			
		}
	}


	public Set<Map<String, Object>> findCheckOptionByFormIdAndFormTypeAndProcessId(Long formId,
			String formType,Long processInstanceId) {
		
		try
		{
			String sql = "select * from checkOption o where o.formId =" + formId + " and o.formType = '" + formType + "' and o.processInstanceId='" + processInstanceId + "' order by o.checkTime asc";
			return this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByFormIdAndFormTypeAndProcessIdError",e.getMessage(),e);
	        throw ex;			
		}
	}
	
	
	public Set<Map<String, Object>> findCheckOptionByFormIdAndFormType(Long formId,String formType) 
	{
		try
		{
			String sql = "select checkTime,checkPeople,checkResult,description from checkOption o where o.formId =" + formId + " and o.formType = '" + formType + "' order by o.checkTime asc";
			return this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByFormIdAndFormTypeError",e.getMessage(),e);
	        throw ex;			
		}
	}

	
	public Set<Map<String, Object>> findCheckOptionByProcessInstanceId(
			Long processInstanceId) {
		try
		{
			String sql = "select * from checkOption o where o.processInstanceId='" + processInstanceId + "' order by o.checkTime asc";
			return this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByProcessInstanceIdError",e.getMessage(),e);
	        throw ex;			
		}
	}
	
	
	public Set<Map<String, Object>> findCheckOptionByCheckPeople(String checkPeople)
	{
		try
		{
			String sql = "select * from checkOption o where o.checkPeople = '" + checkPeople+"' order by o.checkOptionId desc";
			return this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByCheckPeopleError",e.getMessage(),e);
	        throw ex;			
		}
	}

	
	
	public Set<Map<String, Object>> findCheckOptionsByFormId(Long formId) {
		try
		{
			String sql = "slect * from checkOption co where co.formId=" + formId;
			return (Set<Map<String, Object>>) this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionsByFormIdError",e.getMessage(),e);
	        throw ex;			
		}
	}

	
	public Set<Map<String, Object>> findCheckOptionByParameters(String checkPeople,
			Date startDate, Date endDate, String organizationName,
			String managementItemName) {
		try
		{
			String sql = "select * from CheckOption o";
			List<String> strs = new ArrayList<String>();
			if(checkPeople!=null){
				strs.add("o.checkPeople = '"+checkPeople+"'");
			}
			if(startDate!=null){
				strs.add("o.checkTime >= "+toDate(startDate,"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH24:mi:ss")+"");
			}
			if(endDate!=null){
				strs.add("o.checkTime <= "+toDate(endDate,"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH24:mi:ss")+"");
			}
			if(organizationName!=null){
				strs.add("o.organizationName = '"+organizationName+"'");
			}
			if(managementItemName!=null){
				strs.add("o.managementItemName = '"+managementItemName+"'");
			}
			
			if(strs.size()!=0){
				sql = sql +" where ";
				for(int i=0;i<strs.size();i++){
					if(i==strs.size()-1){
						sql += strs.get(i) ;
					}else{
						sql += strs.get(i) +" and ";
					}
				}
				sql +=" and o.checkResult like '启动%成功'";
			}
			return this.checkOptionDao.query(sql);
		}
		catch(Exception e)
		{
			DDDException ex= new DDDException("findCheckOptionByParametersError",e.getMessage(),e);
	        throw ex;			
		}
	}
	
    private String toDate(Date d, String format, String sqlFormat) {  
        StringBuffer bf = new StringBuffer();  
        bf.append("to_date('");  
        bf.append(dateFormat(d, format));  
        bf.append("','");  
        bf.append(sqlFormat);  
        bf.append("')");  
        return bf.toString();  
	}
	 
	private String dateFormat(Date d, String formatStr) {  
        return (new java.text.SimpleDateFormat(formatStr).format(d));  
	}  

}
