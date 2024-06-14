package degreesapp.repositories;

import degreesapp.models.IsuRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Mohammed Abdalgader
 */

@Repository
public interface IsuRegistrationRepository extends JpaRepository<IsuRegistration,Long> {

}
