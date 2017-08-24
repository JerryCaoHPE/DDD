package ddd.simple.service.organization.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.simple.dao.organization.OrganizationDao;
import ddd.simple.entity.organization.Organization;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.organization.OrganizationService;
import ddd.simple.service.workflow.WorkFlowEngineService;

@Service
public class OrganizationServiceBean extends BaseService implements OrganizationService
{

	@Resource(name="organizationDaoBean")
	private OrganizationDao organizationDao;
	
	@Resource(name="workFlowEngineService")
	private WorkFlowEngineService workFlowEngineService;
	
	@Override
	public Organization saveOrganization(Organization organization) 
	{
		try {
			return this.organizationDao.saveOrganization(organization);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveOrganization", e.getMessage(), e);
		}
	}

	@Override
	public int deleteOrganization(Long organizationId) {
		try {
			return this.organizationDao.deleteOrganization(organizationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteOrganization", e.getMessage(), e);
		}
		
	}

	@Override
	public Organization updateOrganization(Organization organization) {
		try {
			return this.organizationDao.updateOrganization(organization);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateOrganization", e.getMessage(), e);
		}
	}

	@Override
	public Organization findOrganizationById(Long organizationId) {
		try {
			return this.organizationDao.findOrganizationById(organizationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findOrganizationById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<Organization> findAllOrganization() {
		try{
			return this.organizationDao.findAllOrganization();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllOrganization", e.getMessage(), e);
		}
	}
	
	@Override
	public String submitApply(Organization organization, String operate) {
		try {
			Long entityId = null;
			Map<String, Object> params = new HashMap<String, Object>();

			if (operate.equals("add")) {
				entityId = SessionFactory.getNewPrimarykey("Organization");
				organization.setEId(entityId);
				this.organizationDao.saveOrganization(organization);
			} else {
				entityId = organization.getEId();
				organization = this.findOrganizationById(entityId);
				organization.setAuditState("正在审核中");
				this.organizationDao.updateOrganization(organization);
			}
			return this.workFlowEngineService.startProcess("单位审批流程", entityId,
					"Organization", "", params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("submitApply", e.getMessage(), e);
		}
	}

}
