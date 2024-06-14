package degreesapp.services;

import degreesapp.models.IsuRegistration;
import degreesapp.models.Student;

import java.util.List;

public interface StudentService {
    public Student saveStudent(Student student);

    public Student fetchStudentById(Long id);

    public Student fetchStudentByIsuRegistration(IsuRegistration isuRegistration);

    public List<Student> fetchStudentList();

    public void deleteStudentById(Long id);

    public void deleteStudentByIsuRegistration(IsuRegistration isuRegistration);

    public Student updateStudent(Long id, Student student);

    public Student updateStudent(IsuRegistration isuRegistration, Student student);
}
