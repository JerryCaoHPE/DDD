package ddd.simple.dao.organization;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.organization.Employee;

public interface EmployeeDao extends BaseDaoInterface
{
	public Employee saveEmployee(Employee employee) throws Exception;
	
	public int deleteEmployee(Long employeeId) throws Exception;
	
	public Employee updateEmployee(Employee employee) throws Exception;
	
	public Employee findEmployeeById(Long employeeId) throws Exception;
	
	public EntitySet<Employee> findAllEmployee() throws Exception;

	public Employee findEmployeeByCode(String code) throws Exception;
}
