package ddd.simple.dao.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.Department;

public interface DepartmentDao extends BaseDaoInterface
{
	public Department saveDepartment(Department department) throws Exception;
	
	public int deleteDepartment(Long departmentId) throws Exception;
	
	public Department updateDepartment(Department department) throws Exception;
	
	public Department findDepartmentById(Long departmentId) throws Exception;
	
	public EntitySet<Department> findAllDepartment() throws Exception;
}
