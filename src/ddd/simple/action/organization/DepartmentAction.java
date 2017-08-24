package ddd.simple.action.organization;

import javax.annotation.Resource;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.organization.Department;
import ddd.simple.service.organization.DepartmentService;

@Action
@RequestMapping("/Department")
@Controller
public class DepartmentAction
{
	@Resource(name="departmentServiceBean")
	private DepartmentService departmentService;
	
	public Department saveDepartment(Department department)
	{
		try {
			Department saveDepartment = this.departmentService.saveDepartment(department);
			return saveDepartment;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteDepartment(Long departmentId){
		
		try {
			return this.departmentService.deleteDepartment(departmentId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public Department updateDepartment(Department department) {
		try {
			Department updateDepartment = this.departmentService.updateDepartment(department);
			return updateDepartment;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Department findDepartmentById(Long departmentId){
		try {
			Department findDepartment = this.departmentService.findDepartmentById(departmentId);
			findDepartment.getOrganization().getName();
			return  findDepartment;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Department> findAllDepartment(){
		try{
			EntitySet<Department> allDepartment = this.departmentService.findAllDepartment();
			return allDepartment;
		} catch (DDDException e) {
			throw e;
		}
	}

}