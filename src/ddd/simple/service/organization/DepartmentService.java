package ddd.simple.service.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.organization.Department;

public interface DepartmentService extends BaseServiceInterface
{
	public Department saveDepartment(Department department) ;
	
	public int deleteDepartment(Long departmentId) ;
	
	public Department updateDepartment(Department department) ;
	
	public Department findDepartmentById(Long departmentId) ;
	
	public EntitySet<Department> findAllDepartment() ;
 
}