package degreesapp.repositories;

import degreesapp.models.RequirementFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementFulfillmentRepository extends JpaRepository<RequirementFulfillment, RequirementFulfillment.Key> {
    List<RequirementFulfillment> findByGroupId(String groupId);
}
