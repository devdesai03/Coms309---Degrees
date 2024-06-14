package degreesapp.services;

import degreesapp.models.IsuRegistration;
import degreesapp.models.Student;
import degreesapp.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    IsuRegistrationService isuRegistrationService;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student fetchStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student fetchStudentByIsuRegistration(IsuRegistration isuRegistration) {
        return fetchStudentById(isuRegistration.getUniversityId());
    }

    @Override
    public List<Student> fetchStudentList() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudentById(Long id) {
        var isuRegistration = isuRegistrationService.fetchIsuRegistrationUserById(id);
        if (isuRegistration != null) {
            isuRegistration.setStudent(null);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public void deleteStudentByIsuRegistration(IsuRegistration isuRegistration) {
        deleteStudentById(isuRegistration.getUniversityId());
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        student.setIsuRegistration(
                isuRegistrationService.fetchIsuRegistrationUserById(id));
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(IsuRegistration isuRegistration, Student student) {
        student.setIsuRegistration(isuRegistration);
        return studentRepository.save(student);
    }
}