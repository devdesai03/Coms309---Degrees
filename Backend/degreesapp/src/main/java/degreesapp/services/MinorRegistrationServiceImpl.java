package degreesapp.services;

import degreesapp.models.Degree;
import degreesapp.models.MinorRegistration;
import degreesapp.models.Student;
import degreesapp.repositories.MinorRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MinorRegistrationServiceImpl implements MinorRegistrationService {
    @Autowired
    MinorRegistrationRepository minorRegistrationRepository;

    @Override
    public List<MinorRegistration> fetchMinorRegistrationList() {
        return minorRegistrationRepository.findAll();
    }

    @Override
    public List<MinorRegistration> fetchMinorRegistrationListForStudent(Long studentId) {
        return minorRegistrationRepository.findByStudentId(studentId);
    }

    @Override
    public MinorRegistration fetchMinorRegistrationById(MinorRegistration.Key id) {
        return minorRegistrationRepository.findById(id).orElse(null);
    }

    @Override
    public MinorRegistration saveMinorRegistration(MinorRegistration minorRegistration) {
        return minorRegistrationRepository.save(minorRegistration);
    }

    @Override
    public MinorRegistration updateMinorRegistration(MinorRegistration.Key id, MinorRegistration minorRegistration) {
        minorRegistration.setMinorRegistrationId(id);
        return minorRegistrationRepository.save(minorRegistration);
    }

    @Override
    public MinorRegistration addMinorRegistrationForStudent(Long studentId, MinorRegistration minorRegistration) {
        MinorRegistration.Key oldId = minorRegistration.getMinorRegistrationId();
        minorRegistration.setMinorRegistrationId(new MinorRegistration.Key(studentId, oldId.getDegreeId()));
        return minorRegistrationRepository.save(minorRegistration);
    }

    @Override
    public MinorRegistration addMinorRegistrationForStudent(Long studentId, Degree degree) {
        MinorRegistration minorRegistration = new MinorRegistration();
        minorRegistration.setMinorRegistrationId(new MinorRegistration.Key(studentId, degree.getId()));
        return minorRegistrationRepository.save(minorRegistration);
    }

    @Override
    public void deleteMinorRegistration(MinorRegistration.Key id) {
        minorRegistrationRepository.deleteById(id);
    }
}
