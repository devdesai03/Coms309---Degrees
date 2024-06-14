package degreesapp.services;

import degreesapp.models.Degree;
import degreesapp.models.MajorRegistration;
import degreesapp.models.RegistrationId;
import degreesapp.models.Student;
import degreesapp.repositories.MajorRegistrationRepository;

import java.util.List;

public interface MajorRegistrationService {
    List<MajorRegistration> fetchMajorRegistrationList();

    List<MajorRegistration> fetchMajorRegistrationListForStudent(Long studentId);

    MajorRegistration fetchMajorRegistrationById(MajorRegistration.Key id);

    MajorRegistration saveMajorRegistration(MajorRegistration majorRegistration);

    MajorRegistration updateMajorRegistration(MajorRegistration.Key id, MajorRegistration majorRegistration);

    MajorRegistration addMajorRegistrationForStudent(Long studentId, MajorRegistration majorRegistration);

    MajorRegistration addMajorRegistrationForStudent(Long studentId, Degree degree);

    void deleteMajorRegistration(MajorRegistration.Key id);

    MajorRegistration setMajorForStudent(Long studentId, Degree degree);
}
