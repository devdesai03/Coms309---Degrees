package degreesapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import degreesapp.models.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
     List<Message> findByUserName(String requestingUsername);
}