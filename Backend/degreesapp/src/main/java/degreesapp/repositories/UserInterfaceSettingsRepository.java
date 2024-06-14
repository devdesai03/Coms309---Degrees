package degreesapp.repositories;

import degreesapp.models.UserInterfaceSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterfaceSettingsRepository extends JpaRepository<UserInterfaceSettings, Long> {

}
