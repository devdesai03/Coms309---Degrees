package degreesapp.services;

import degreesapp.models.Degree;
import degreesapp.repositories.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeServiceImpl implements DegreeService {
    @Autowired
    DegreeRepository degreeRepository;

    @Override
    public Degree saveDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    @Override
    public List<Degree> fetchDegreeList() {
        return degreeRepository.findAll();
    }

    @Override
    public Degree fetchDegreeById(Long degreeId) {
        return degreeRepository.findById(degreeId).orElse(null);
    }

    @Override
    public Degree fetchDegreeByNameAndSuffix(String degreeName, String degreeSuffix) {
        return degreeRepository.findByNameAndSuffix(degreeName, degreeSuffix);
    }

    @Override
    public void deleteDegreeById(Long degreeId) {
        degreeRepository.deleteById(degreeId);
    }

    @Override
    public Degree updateDegree(Long degreeId, Degree degree) {
        degree.setId(degreeId);
        return degreeRepository.save(degree);
    }
}
