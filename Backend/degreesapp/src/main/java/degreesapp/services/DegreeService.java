package degreesapp.services;

import degreesapp.models.Degree;

import java.util.List;

public interface DegreeService {
    Degree saveDegree(Degree degree);

    List<Degree> fetchDegreeList();

    Degree fetchDegreeById(Long degreeId);

    Degree fetchDegreeByNameAndSuffix(String degreeName, String degreeSuffix);

    void deleteDegreeById(Long degreeId);

    Degree updateDegree(Long degreeId, Degree degree);
}
