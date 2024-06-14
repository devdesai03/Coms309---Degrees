package degreesapp.services;

import degreesapp.models.IsuRegistration;
import degreesapp.models.Instructor;

import java.util.List;

public interface InstructorService {
    public Instructor saveInstructor(Instructor instructor);

    public Instructor fetchInstructorById(Long id);

    public Instructor fetchInstructorByIsuRegistration(IsuRegistration isuRegistration);

    public List<Instructor> fetchInstructorList();

    public void deleteInstructorById(Long id);

    public void deleteInstructorByIsuRegistration(IsuRegistration isuRegistration);

    public Instructor updateInstructor(Long id, Instructor instructor);

    public Instructor updateInstructor(IsuRegistration isuRegistration, Instructor instructor);
}
