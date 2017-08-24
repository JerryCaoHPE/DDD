package ddd.simple.dao.organization.impl;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import org.springframework.stereotype.Service;
import ddd.simple.entity.organization.Department;
import ddd.simple.dao.organization.DepartmentDao;

@Service
public class DepartmentDaoBean extends BaseDao implements DepartmentDao
{
	@Override
	public Department saveDepartment(Department department)  throws Exception{
		return this.save(department);
	}

	@Override
	public int deleteDepartment(Long departmentId)  throws Exception{
		return this.deleteById(departmentId,Department.class);
	}

	@Override
	public Department updateDepartment(Department department)  throws Exception{
		return this.update(department);
	}

	@Override
	public Department findDepartmentById(Long departmentId)  throws Exception{
		return this.query(departmentId, Department.class);
	}
	
	@Override
	public EntitySet<Department> findAllDepartment() throws Exception {
		return this.query("",Department.class);
	}
}
