package degreesapp.services;

import degreesapp.models.*;
import degreesapp.models.notifications.CourseRegistrationNotification;
import degreesapp.repositories.CourseRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<CourseSection> getRegisteredSectionsForStudent(Long studentId) {
        // Use the repository method to find course registrations by studentId
        List<CourseRegistration> courseRegistrations = courseRegistrationRepository.findByStudentId(studentId);

        // Extract the course sections from the course registrations
        List<CourseSection> registeredSections = new ArrayList<>();
        for ( CourseRegistration courseRegistration : courseRegistrations ) {
            registeredSections.add(courseRegistration.getSection());
        }

        return registeredSections;
    }

    @Override
    public List<CourseRegistration> getRegistrationsForStudent(Long studentid) {
        return courseRegistrationRepository.findByStudentId(studentid);
    }

    @Transactional
    public CourseRegistration saveCourseSection(CourseRegistration courseRegistration) {
        CourseRegistration newCourseRegistration = courseRegistrationRepository.save(courseRegistration);
        sendNotification:
        {
            Student student = newCourseRegistration.getStudent();
            if ( student == null )
                break sendNotification;
            Advisor studentAdvisor = student.getStudentAdvisor();
            if ( studentAdvisor == null )
                break sendNotification;
            User studentAdvisorUser = studentAdvisor.getIsuRegistration().getUser();
            if ( studentAdvisorUser == null )
                break sendNotification;
            notificationService.sendNotificationToUser(
                    CourseRegistrationNotification.adding(newCourseRegistration) ,
                    studentAdvisorUser.getUserId());
        }
        return newCourseRegistration;
    }

    @Override
    public void deleteById(RegistrationId id) {
        CourseRegistration deletedCourseRegistration = courseRegistrationRepository.findById(id).orElse(null);
        courseRegistrationRepository.deleteById(id);
        sendNotification: {
            if (deletedCourseRegistration == null)
                break sendNotification;
            Student student = deletedCourseRegistration.getStudent();
            if (student == null)
                break sendNotification;
            Advisor studentAdvisor = student.getStudentAdvisor();
            if (studentAdvisor == null)
                break sendNotification;
            User studentAdvisorUser = studentAdvisor.getIsuRegistration().getUser();
            if (studentAdvisorUser == null)
                break sendNotification;
            notificationService.sendNotificationToUser(
                    CourseRegistrationNotification.dropping(deletedCourseRegistration),
                    studentAdvisorUser.getUserId());
        }
    }

    @Override
    public Optional<CourseRegistration> findById(RegistrationId id) {
        return courseRegistrationRepository.findById(id);
    }

    @Override
    public List<CourseRegistration> getRegistrationsByInstructorId(Long instructorId) {
        return courseRegistrationRepository.findByInstructorId(instructorId);
    }

    @Override
    @Transactional
    public void updateGrade(RegistrationId id , BigDecimal newGrade) {
        // Check if the student with the specified universityId is registered in the given sectionId
        if ( !isStudentRegistered(id.getUniversityId() , id.getSectionId()) ) {
            throw new IllegalStateException("Student is not registered in the specified CourseSection");
        }
        Optional<CourseRegistration> optionalCourseRegistration = courseRegistrationRepository.findById(id);
        if ( optionalCourseRegistration.isPresent() ) {
            CourseRegistration courseRegistration = optionalCourseRegistration.get();
            courseRegistration.setGrade(newGrade);
            courseRegistrationRepository.save(courseRegistration);
        } else {
            throw new EntityNotFoundException("CourseRegistration with ID " + id + " not found");
        }
    }

    @Override
    public List<CourseRegistration> getRegistrationsBySectionId(Long sectionId) {
        return courseRegistrationRepository.findBySectionId(sectionId);
    }


    private boolean isStudentRegistered(Long universityId , Long sectionId) {
        return courseRegistrationRepository.findByUniversityIdAndSectionId(universityId , sectionId).isPresent();
    }


}
