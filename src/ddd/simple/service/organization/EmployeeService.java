package ddd.simple.service.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseServiceInterface;
import ddd.simple.entity.organization.Employee;

public interface EmployeeService extends BaseServiceInterface
{
	public Employee saveEmployee(Employee employee) ;
	
	public int deleteEmployee(Long employeeId) ;
	
	public Employee updateEmployee(Employee employee) ;
	
	public Employee findEmployeeById(Long employeeId) ;
	
	public EntitySet<Employee> findAllEmployee() ;
 
}