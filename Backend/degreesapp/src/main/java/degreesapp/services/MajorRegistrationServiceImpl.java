package degreesapp.services;

import degreesapp.models.Degree;
import degreesapp.models.MajorRegistration;
import degreesapp.models.Student;
import degreesapp.repositories.MajorRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MajorRegistrationServiceImpl implements MajorRegistrationService {
    @Autowired
    MajorRegistrationRepository majorRegistrationRepository;

    @Override
    public List<MajorRegistration> fetchMajorRegistrationList() {
        return majorRegistrationRepository.findAll();
    }

    @Override
    public List<MajorRegistration> fetchMajorRegistrationListForStudent(Long studentId) {
        return majorRegistrationRepository.findByStudentId(studentId);
    }

    @Override
    public MajorRegistration fetchMajorRegistrationById(MajorRegistration.Key id) {
        return majorRegistrationRepository.findById(id).orElse(null);
    }

    @Override
    public MajorRegistration saveMajorRegistration(MajorRegistration majorRegistration) {
        return majorRegistrationRepository.save(majorRegistration);
    }

    @Override
    public MajorRegistration updateMajorRegistration(MajorRegistration.Key id, MajorRegistration majorRegistration) {
        majorRegistration.setMajorRegistrationId(id);
        return majorRegistrationRepository.save(majorRegistration);
    }

    @Override
    public MajorRegistration addMajorRegistrationForStudent(Long studentId, MajorRegistration majorRegistration) {
        MajorRegistration.Key oldId = majorRegistration.getMajorRegistrationId();
        majorRegistration.setMajorRegistrationId(new MajorRegistration.Key(studentId, oldId.getDegreeId()));
        return majorRegistrationRepository.save(majorRegistration);
    }

    @Override
    public MajorRegistration addMajorRegistrationForStudent(Long studentId, Degree degree) {
        MajorRegistration majorRegistration = new MajorRegistration();
        majorRegistration.setMajorRegistrationId(new MajorRegistration.Key(studentId, degree.getId()));
        System.out.println(majorRegistration);
        return majorRegistrationRepository.save(majorRegistration);
    }

    @Override
    public void deleteMajorRegistration(MajorRegistration.Key id) {
        majorRegistrationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MajorRegistration setMajorForStudent(Long studentId, Degree degree) {
        majorRegistrationRepository.deleteByStudentId(studentId);
        MajorRegistration majorRegistration = new MajorRegistration();
        majorRegistration.setMajorRegistrationId(new MajorRegistration.Key(studentId, degree.getId()));
        return majorRegistrationRepository.save(majorRegistration);
    }
}
