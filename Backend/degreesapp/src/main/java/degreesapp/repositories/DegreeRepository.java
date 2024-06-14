package degreesapp.repositories;

import degreesapp.models.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {
    Degree findByNameAndSuffix(String name, String suffix);
}
