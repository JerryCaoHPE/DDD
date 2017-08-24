package ddd.simple.dao.organization.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.organization.Organization;
import ddd.simple.dao.organization.OrganizationDao;

@Service
public class OrganizationDaoBean extends BaseDao implements OrganizationDao
{
	@Override
	public Organization saveOrganization(Organization organization)  throws Exception{
		return this.save(organization);
	}

	@Override
	public int deleteOrganization(Long organizationId)  throws Exception{
		return this.deleteById(organizationId,Organization.class);
	}

	@Override
	public Organization updateOrganization(Organization organization)  throws Exception{
		return this.update(organization);
	}

	@Override
	public Organization findOrganizationById(Long organizationId)  throws Exception{
		return this.query(organizationId, Organization.class);
	}
	
	@Override
	public EntitySet<Organization> findAllOrganization() throws Exception {
		return this.query("",Organization.class);
	}
}
