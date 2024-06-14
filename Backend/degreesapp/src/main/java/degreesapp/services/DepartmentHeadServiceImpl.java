package degreesapp.services;

import degreesapp.models.DepartmentHead;
import degreesapp.models.IsuRegistration;
import degreesapp.repositories.DepartmentHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentHeadServiceImpl implements DepartmentHeadService {
    @Autowired DepartmentHeadRepository departmentHeadRepository;
    @Autowired IsuRegistrationService isuRegistrationService;

    @Override
    public DepartmentHead saveDepartmentHead(DepartmentHead departmentHead) {
        return departmentHeadRepository.save(departmentHead);
    }

    @Override
    public DepartmentHead fetchDepartmentHeadById(Long id) {
        return departmentHeadRepository.findById(id).orElse(null);
    }

    @Override
    public DepartmentHead fetchDepartmentHeadByIsuRegistration(IsuRegistration isuRegistration) {
        return departmentHeadRepository.findById(isuRegistration.getUniversityId()).orElse(null);
    }

    @Override
    public List<DepartmentHead> fetchDepartmentHeadList() {
        return departmentHeadRepository.findAll();
    }

    @Override
    public void deleteDepartmentHeadById(Long id) {
        var isuRegistration = isuRegistrationService.fetchIsuRegistrationUserById(id);
        if (isuRegistration != null) {
            isuRegistration.setDepartmentHead(null);
        }
        departmentHeadRepository.deleteById(id);
    }

    @Override
    public void deleteDepartmentHeadByIsuRegistration(IsuRegistration isuRegistration) {
        departmentHeadRepository.deleteById(isuRegistration.getUniversityId());
    }

    @Override
    public DepartmentHead updateDepartmentHead(Long id, DepartmentHead departmentHead) {
        IsuRegistration isuRegistration = isuRegistrationService.fetchIsuRegistrationUserById(id);
        departmentHead.setIsuRegistration(isuRegistration);
        return departmentHeadRepository.save(departmentHead);
    }

    @Override
    public DepartmentHead updateDepartmentHead(IsuRegistration isuRegistration, DepartmentHead departmentHead) {
        departmentHead.setIsuRegistration(isuRegistration);
        return departmentHeadRepository.save(departmentHead);
    }
}
