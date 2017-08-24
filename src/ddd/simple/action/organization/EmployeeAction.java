package ddd.simple.action.organization;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.organization.Employee;
import ddd.simple.service.organization.EmployeeService;

@Action
@RequestMapping("/Employee")
@Controller
public class EmployeeAction
{
	@Resource(name="employeeServiceBean")
	private EmployeeService employeeService;
	
	public Employee saveEmployee(Employee employee)
	{
		try {
			Employee saveEmployee = this.employeeService.saveEmployee(employee);
			return saveEmployee;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteEmployee(Long employeeId){
		
		try {
			return this.employeeService.deleteEmployee(employeeId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public Employee updateEmployee(Employee employee) {
		try {
			Employee updateEmployee = this.employeeService.updateEmployee(employee);
			return updateEmployee;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Employee findEmployeeById(Long employeeId){
		try {
			Employee findEmployee = this.employeeService.findEmployeeById(employeeId);
			return  findEmployee;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Employee> findAllEmployee(){
		try{
			EntitySet<Employee> allEmployee = this.employeeService.findAllEmployee();
			return allEmployee;
		} catch (DDDException e) {
			throw e;
		}
	}

}