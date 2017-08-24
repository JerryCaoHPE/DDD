package ddd.simple.service.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.organization.Organization;

public interface OrganizationService extends BaseServiceInterface
{
	public Organization saveOrganization(Organization organization) ;
	
	public int deleteOrganization(Long organizationId) ;
	
	public Organization updateOrganization(Organization organization) ;
	
	public Organization findOrganizationById(Long organizationId) ;
	
	public EntitySet<Organization> findAllOrganization() ;
	
	public String submitApply(Organization organization,String operate);
 
}