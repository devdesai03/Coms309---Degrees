package degreesapp.services;

import degreesapp.models.Advisor;
import degreesapp.models.IsuRegistration;
import degreesapp.repositories.AdvisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class AdvisorServiceImpl implements AdvisorService {


    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    IsuRegistrationService isuRegistrationService;


    @Override
    public Advisor saveAdvisor(Advisor advisor) {
        return advisorRepository.save(advisor);
    }

    @Override
    public List<Advisor> fetchAdvisorList() {
        return advisorRepository.findAll();
    }

    @Override
    public Advisor fetchAdvisorByIsuRegistration(IsuRegistration isuRegistration) {
        return fetchAdvisorById(isuRegistration.getUniversityId());
    }

    @Override
    public Advisor fetchAdvisorById(Long advisorId) {
        return advisorRepository.findAdvisorById(advisorId);
    }

    @Override
    public void deleteAdvisorById(Long advisorId) {
        var isuRegistration = isuRegistrationService.fetchIsuRegistrationUserById(advisorId);
        if (isuRegistration != null) {
            isuRegistration.setAdvisor(null);
        }
        advisorRepository.deleteById(advisorId);
    }

    @Override
    public void deleteAdvisorByIsuRegistration(IsuRegistration isuRegistration) {

    }

    @Override
    @Transactional
    public Advisor updateAdvisor(Long advisorId , Advisor advisor) {
        advisor.setIsuRegistration(isuRegistrationService.fetchIsuRegistrationUserById(advisorId));
        return advisorRepository.save(advisor);
    }

    @Override
    public Advisor updateAdvisor(IsuRegistration isuRegistration , Advisor advisor) {
        advisor.setIsuRegistration(isuRegistration);

        return advisorRepository.save(advisor);
    }

    @Override
    public Advisor fetchAdvisorByEmail(String advisorEmail) {
        return advisorRepository.findAdvisorByEmail(advisorEmail);
    }
}
