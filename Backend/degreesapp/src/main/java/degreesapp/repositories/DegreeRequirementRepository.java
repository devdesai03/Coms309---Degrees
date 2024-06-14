package degreesapp.repositories;

import degreesapp.models.DegreeRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeRequirementRepository extends JpaRepository<DegreeRequirement, DegreeRequirement.Key> {
    List<DegreeRequirement> findByDegreeId(Long degreeId);
}
