package degreesapp.services;

import degreesapp.models.DepartmentHead;
import degreesapp.models.IsuRegistration;

import java.util.List;

public interface DepartmentHeadService {
    public DepartmentHead saveDepartmentHead(DepartmentHead departmentHead);

    public DepartmentHead fetchDepartmentHeadById(Long id);

    public DepartmentHead fetchDepartmentHeadByIsuRegistration(IsuRegistration isuRegistration);

    public List<DepartmentHead> fetchDepartmentHeadList();

    public void deleteDepartmentHeadById(Long id);

    public void deleteDepartmentHeadByIsuRegistration(IsuRegistration isuRegistration);

    public DepartmentHead updateDepartmentHead(Long id, DepartmentHead departmentHead);

    public DepartmentHead updateDepartmentHead(IsuRegistration isuRegistration, DepartmentHead departmentHead);
}
