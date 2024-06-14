package degreesapp.services;


import degreesapp.models.IsuRegistration;
import degreesapp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

@Service
public interface IsuRegistrationService {
    public IsuRegistration saveIsuRegistration(IsuRegistration isuRegistration);

    public IsuRegistration fetchIsuRegistrationUserById(Long isuRegistrationId);

    public List<IsuRegistration> fetchIsuRegistrationList();

    public void deleteIsuRegistrationUserById(Long isuRegistrationId);

    public IsuRegistration updateIsuRegistrationUser(Long userId, IsuRegistration isuRegistration);
}
