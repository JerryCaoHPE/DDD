package ddd.simple.service.organization.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.service.base.BaseService;
import ddd.simple.entity.organization.Employee;
import ddd.simple.dao.organization.EmployeeDao;
import ddd.simple.service.organization.EmployeeService;

@Service
public class EmployeeServiceBean extends BaseService implements EmployeeService
{

	@Resource(name="employeeDaoBean")
	private EmployeeDao employeeDao;
	
	@Override
	public Employee saveEmployee(Employee employee) 
	{
		try {
			return this.employeeDao.saveEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveEmployee", e.getMessage(), e);
		}
	}

	@Override
	public int deleteEmployee(Long employeeId) {
		try {
			return this.employeeDao.deleteEmployee(employeeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteEmployee", e.getMessage(), e);
		}
		
	}

	@Override
	public Employee updateEmployee(Employee employee) {
		try {
			return this.employeeDao.updateEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateEmployee", e.getMessage(), e);
		}
	}

	@Override
	public Employee findEmployeeById(Long employeeId) {
		try {
			Employee employee = this.employeeDao.findEmployeeById(employeeId);
			if(employee!=null){
				return this.addEmployeeReference(employee);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findEmployeeById", e.getMessage(), e);
		}
	}
	
	private Employee addEmployeeReference(Employee employee) {
		if(employee.getDepartment()!=null)
		{
			employee.getDepartment().getName();
		}
		if(employee.getOrganization() !=null)
		{
			employee.getOrganization().getName();
			if(employee.getDepartment() != null){
				employee.getDepartment().setOrganization(null);	
			}else{
				//不处理
			}
			
		}
		return employee;
	}

	@Override
	public EntitySet<Employee> findAllEmployee() {
		try{
			return this.employeeDao.findAllEmployee();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllEmployee", e.getMessage(), e);
		}
	}

}
