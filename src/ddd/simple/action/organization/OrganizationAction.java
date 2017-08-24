package ddd.simple.action.organization;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.util.EntityUtil;
import ddd.simple.entity.organization.Organization;
import ddd.simple.service.organization.OrganizationService;

@Action
@RequestMapping("/Organization")
@Controller
public class OrganizationAction
{
	@Resource(name="organizationServiceBean")
	private OrganizationService organizationService;
	
	public Organization saveOrganization(Organization organization)
	{
		try {
			Organization saveOrganization = this.organizationService.saveOrganization(organization);
			return saveOrganization;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteOrganization(Long organizationId){
		
		try {
			return this.organizationService.deleteOrganization(organizationId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public Organization updateOrganization(Organization organization) {
		try {
			Organization updateOrganization = this.organizationService.updateOrganization(organization);
			return updateOrganization;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Organization findOrganizationById(Long organizationId){
		try {
			Organization findOrganization = this.organizationService.findOrganizationById(organizationId);
			Organization parent = findOrganization.getParent();
			if(parent != null){
				parent.getName();
			}
//			EntitySet<Organization> child = findOrganization.getChildren();
//			Iterator<Organization> ite = child.iterator();
//			while(ite.hasNext()){
//				Organization temp = ite.next();
//				if(temp != null){
//					temp.getName();
//				}
//			}
			EntityUtil.loadLazyProperty(findOrganization, "children");
			try{
				SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				
				Long time=findOrganization.getCreatTime().getTime();
				
				String d = format.format(time);

				Date date=format.parse(d);
				
				findOrganization.setCreatTime(date);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			EntityUtil.clearProperty(findOrganization, "children.parent");
			return  findOrganization;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Organization> findAllOrganization(){
		try{
			EntitySet<Organization> allOrganization = this.organizationService.findAllOrganization();
			return allOrganization;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public String submitApply(Organization organization,String operate){
		return this.organizationService.submitApply(organization,operate);
	} 

}