package degreesapp.services;

import degreesapp.models.Degree;
import degreesapp.models.MinorRegistration;
import degreesapp.models.Student;

import java.util.List;

public interface MinorRegistrationService {
    List<MinorRegistration> fetchMinorRegistrationList();

    List<MinorRegistration> fetchMinorRegistrationListForStudent(Long studentId);

    MinorRegistration fetchMinorRegistrationById(MinorRegistration.Key id);

    MinorRegistration saveMinorRegistration(MinorRegistration minorRegistration);

    MinorRegistration updateMinorRegistration(MinorRegistration.Key id, MinorRegistration minorRegistration);

    MinorRegistration addMinorRegistrationForStudent(Long studentId, MinorRegistration minorRegistration);

    MinorRegistration addMinorRegistrationForStudent(Long studentId, Degree degree);

    void deleteMinorRegistration(MinorRegistration.Key id);
}
