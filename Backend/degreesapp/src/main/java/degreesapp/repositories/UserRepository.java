package degreesapp.repositories;

import degreesapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Mohammed Abdalgader
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select u from User u where u.userEmail = ?1")
    User findByUserEmail(String userEmail);

    @Query(value = "select u from User u where u.userName = :userName")
    User findByUserName(String userName);
}
