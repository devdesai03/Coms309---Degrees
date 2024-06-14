package degreesapp.services;

import degreesapp.models.IsuRegistration;
import degreesapp.models.Instructor;
import degreesapp.repositories.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {
    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    IsuRegistrationService isuRegistrationService;

    @Override
    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor fetchInstructorById(Long id) {
        return instructorRepository.findById(id).orElse(null);
    }

    @Override
    public Instructor fetchInstructorByIsuRegistration(IsuRegistration isuRegistration) {
        return fetchInstructorById(isuRegistration.getUniversityId());
    }

    @Override
    public List<Instructor> fetchInstructorList() {
        return instructorRepository.findAll();
    }

    @Override
    public void deleteInstructorById(Long id) {
        var isuRegistration = isuRegistrationService.fetchIsuRegistrationUserById(id);
        if (isuRegistration != null) {
            isuRegistration.setInstructor(null);
        }
        instructorRepository.deleteById(id);
    }

    @Override
    public void deleteInstructorByIsuRegistration(IsuRegistration isuRegistration) {
        deleteInstructorById(isuRegistration.getUniversityId());
    }

    @Override
    public Instructor updateInstructor(Long id, Instructor instructor) {
        System.out.println("Test...");
        System.out.println(isuRegistrationService.fetchIsuRegistrationUserById(id));
        instructor.setIsuRegistration(
                isuRegistrationService.fetchIsuRegistrationUserById(id));
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor updateInstructor(IsuRegistration isuRegistration, Instructor instructor) {
        instructor.setIsuRegistration(isuRegistration);
        return instructorRepository.save(instructor);
    }
}
