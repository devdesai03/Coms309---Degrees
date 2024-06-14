package degreesapp.services;
import degreesapp.models.MajorRegistration;
import degreesapp.models.MinorRegistration;
import degreesapp.models.Student;
import degreesapp.models.User;
import degreesapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Mohammed Abdalgader
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsuRegistrationRepository isuRegistrationRepository;

    @Autowired
    private DegreeRepository degreeRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    @Override
    public User fetchUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public User updateUser(Long userId, User user) {
        User userDB =  userRepository.findById(userId).get();
//        user.setUserPassword(userDB.getUserPassword());
//        return userRepository.save(user);

        if (Objects.nonNull(user.getUserName())){
            userDB.setUserName(user.getUserName());
        }

        if (Objects.nonNull(user.getUserAddress())){
            userDB.setUserAddress(user.getUserAddress());
        }

        if (Objects.nonNull(user.getUserEmail())) {
            userDB.setUserEmail(user.getUserEmail());

        }

        if (Objects.nonNull(user.getPhoneNumber())) {
            userDB.setPhoneNumber(user.getPhoneNumber());

        }

        if (Objects.nonNull(user.getUserPassword())) {
            userDB.setUserPassword(user.getUserPassword());
        }

        // Some dirty hacks to get the Manage Users screen to work, and avoid weird Hibernate/JPA issues
        // involving entity lifecycles. Can be improved later.

        if (Objects.nonNull(user.getIsuRegistration())) {
            user.getIsuRegistration().setUser(userDB);
            if (Objects.nonNull(user.getIsuRegistration().getStudent())) {
                Student student = user.getIsuRegistration().getStudent();
                if (Objects.nonNull(student.getMajorRegistrations())) {
                    student.setMajorRegistrations(student.getMajorRegistrations()
                            .stream().map(reg -> reg.getDegree())
                            .map(deg -> degreeRepository.findById(deg.getId()).orElse(deg))
                            .map(deg -> new MajorRegistration(new MajorRegistration.Key(student.getUniversityId(), deg.getId()), student, deg))
//                            .map(reg -> majorRegistrationRepository.save(reg))
                            .collect(Collectors.toList()));
                }
                if (Objects.nonNull(student.getMinorRegistrations())) {
                    student.setMinorRegistrations(student.getMinorRegistrations()
                            .stream().map(reg -> reg.getDegree())
                            .map(deg -> degreeRepository.findById(deg.getId()).orElse(deg))
                            .map(deg -> new MinorRegistration(new MinorRegistration.Key(student.getUniversityId(), deg.getId()), student, deg))
//                            .map(reg -> minorRegistrationRepository.save(reg))
                            .collect(Collectors.toList()));
                }
                if (Objects.nonNull(userDB.getIsuRegistration())) {
                    user.getIsuRegistration().setUniversityId(userDB.getIsuRegistration().getUniversityId());
                }
            }
        } else if (Objects.nonNull(userDB.getIsuRegistration())) {
            isuRegistrationRepository.delete(userDB.getIsuRegistration());
        }
        userDB.setIsuRegistration(user.getIsuRegistration());
        return userRepository.save(userDB);
    }

    public User login(String userEmail, String userPassword) {
        User user = userRepository.findByUserEmail(userEmail);

        if (user != null && user.getUserPassword().equals(userPassword)) {
            return user;
        }

        return null; // Authentication failed
    }

    @Override
    public String getStoredVerificationCode(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null) {
            return user.getVerificationCode();
        } else {
            return null; // User not found
        }
    }

    @Override
    public User fetchUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    @Override
    public User fetchUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
