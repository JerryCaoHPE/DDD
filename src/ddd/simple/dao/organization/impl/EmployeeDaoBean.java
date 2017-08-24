package ddd.simple.dao.organization.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.organization.Employee;
import ddd.simple.dao.organization.EmployeeDao;

@Service
public class EmployeeDaoBean extends BaseDao implements EmployeeDao
{
	@Override
	public Employee saveEmployee(Employee employee)  throws Exception{
		return this.save(employee);
	}

	@Override
	public int deleteEmployee(Long employeeId)  throws Exception{
		return this.deleteById(employeeId,Employee.class);
	}

	@Override
	public Employee updateEmployee(Employee employee)  throws Exception{
		return this.update(employee);
	}

	@Override
	public Employee findEmployeeById(Long employeeId)  throws Exception{
		return this.query(employeeId, Employee.class);
	}
	
	@Override
	public EntitySet<Employee> findAllEmployee() throws Exception {
		return this.query("",Employee.class);
	}
	
	@Override
	public Employee findEmployeeByCode(String code) throws Exception {
		String sql="code = '"+code+"' ";
		return this.queryOne(sql, Employee.class);
	}
}
