package degreesapp.repositories;

import degreesapp.models.RequirementGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementGroupRepository extends JpaRepository<RequirementGroup, String> {
}
