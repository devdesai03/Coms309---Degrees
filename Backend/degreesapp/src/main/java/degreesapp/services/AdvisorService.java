package degreesapp.services;



import degreesapp.models.Advisor;
import degreesapp.models.IsuRegistration;


import java.util.List;

public interface AdvisorService {
    public Advisor saveAdvisor(Advisor advisor);

    public List<Advisor> fetchAdvisorList();

    public Advisor fetchAdvisorByIsuRegistration(IsuRegistration isuRegistration);
    public Advisor fetchAdvisorById(Long advisorId);

    public void deleteAdvisorById(Long advisorId);

    public void deleteAdvisorByIsuRegistration(IsuRegistration isuRegistration);

    public Advisor updateAdvisor(Long advisorId, Advisor advisor);

    public Advisor updateAdvisor(IsuRegistration isuRegistration, Advisor advisor);

    public Advisor fetchAdvisorByEmail(String advisorEmail);
}
