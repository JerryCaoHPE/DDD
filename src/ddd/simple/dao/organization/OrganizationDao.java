package ddd.simple.dao.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.Organization;

public interface OrganizationDao extends BaseDaoInterface
{
	public Organization saveOrganization(Organization organization) throws Exception;
	
	public int deleteOrganization(Long organizationId) throws Exception;
	
	public Organization updateOrganization(Organization organization) throws Exception;
	
	public Organization findOrganizationById(Long organizationId) throws Exception;
	
	public EntitySet<Organization> findAllOrganization() throws Exception;
}
