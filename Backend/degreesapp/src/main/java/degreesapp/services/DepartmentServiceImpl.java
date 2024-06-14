package degreesapp.services;

import degreesapp.models.Department;
import degreesapp.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Mohammed Abdalgader
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> fetchDepartmentList() {
        return departmentRepository.findAll();
    }

    @Override
    public Department fetchDepartmentById(Long departmentId) {
        return departmentRepository.findByDepartmentId(departmentId);
    }

    @Override
    public void deleteDepartmentById(Long departmentById) {

        departmentRepository.deleteById(departmentById);
    }

    @Override
    public Department updateDepartment(Long departmentId , Department department) {
        Department departmentDB = departmentRepository.findById(departmentId).get();

        if ( Objects.nonNull(department.getDepartmentName())
                && !"".equalsIgnoreCase(department.getDepartmentName()) ) {
            departmentDB.setDepartmentName(department.getDepartmentName());
        }
        if ( Objects.nonNull(department.getDepartmentAddress())
                && !"".equalsIgnoreCase(department.getDepartmentAddress()) ) {
            departmentDB.setDepartmentAddress(department.getDepartmentAddress());
        }
        if ( Objects.nonNull(department.getDepartmentCode())
                && !"".equalsIgnoreCase(department.getDepartmentCode()) ) {
            departmentDB.setDepartmentCode(department.getDepartmentCode());
        }
        return departmentRepository.save(departmentDB);
    }

    @Override
    public Long getDepartmentIdByDepartmentName(String departName) {
        Department department = departmentRepository.findByDepartmentName(departName);
        if ( department != null ) {
            return department.getDepartmentId();
        } else {
            return 1000000000L;
        }
    }
}
