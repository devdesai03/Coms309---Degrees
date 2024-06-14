package degreesapp.services;


import degreesapp.models.IsuRegistration;
import degreesapp.repositories.IsuRegistrationRepository;
import degreesapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Mohammed Abdalgader
 */
@Service
public class IsuRegistrationServiceImp implements IsuRegistrationService {

    @Autowired
    private IsuRegistrationRepository isuRegistrationRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public IsuRegistration saveIsuRegistration(IsuRegistration isuRegistration) {
        return isuRegistrationRepository.save(isuRegistration);
    }

    @Override
    public IsuRegistration fetchIsuRegistrationUserById(Long isuRegistrationId) {
        if(isuRegistrationRepository.findById(isuRegistrationId).isPresent()) {
            return isuRegistrationRepository.findById(isuRegistrationId).get();
        }else {
            return null;
        }
    }

    @Override
    public List<IsuRegistration> fetchIsuRegistrationList() {
        return isuRegistrationRepository.findAll();
    }

    @Override
    public void deleteIsuRegistrationUserById(Long isuRegistrationId) {
        var reg = isuRegistrationRepository.findById(isuRegistrationId);
        reg.ifPresent(registration -> {
            if (registration.getUser() != null) {
                registration.getUser().setIsuRegistration(null);
            }
        });
        isuRegistrationRepository.deleteById(isuRegistrationId);
    }

    @Override
    public IsuRegistration updateIsuRegistrationUser(Long userId, IsuRegistration isuRegistration) {

        IsuRegistration isuRegistrationDB = fetchIsuRegistrationUserById(userId);

       if(Objects.nonNull(isuRegistration.getNetId())
                && !"".equalsIgnoreCase(isuRegistration.getNetId())){
            isuRegistrationDB.setNetId(isuRegistration.getNetId());
        }
        if (Objects.nonNull(isuRegistration.getUniversityId())){
            isuRegistrationDB.setUniversityId(isuRegistration.getUniversityId());
        }
        return isuRegistrationRepository.save(isuRegistrationDB);
    }
}
