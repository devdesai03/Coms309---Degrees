package degreesapp.repositories;

import degreesapp.models.Advisor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    @Query ( value = "select * from user u where u.user_email = ?1", nativeQuery = true )
    Advisor findAdvisorByEmail(String advisorEmail);

    @Query ( "select a from Advisor a where a.advisorId = :advisorId" )
    Advisor findAdvisorById(Long advisorId);
}
