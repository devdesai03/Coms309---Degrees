package degreesapp.services;

import degreesapp.models.Department;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

public interface DepartmentService {
    public Department saveDepartment(Department department);

    public List<Department> fetchDepartmentList();

    public Department fetchDepartmentById(Long departmentId);

    public void deleteDepartmentById(Long departmentById);

    public Department updateDepartment(Long departmentId , Department department);

    public Long getDepartmentIdByDepartmentName(String departName);
}
