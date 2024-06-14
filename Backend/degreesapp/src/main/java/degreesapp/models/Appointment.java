package degreesapp.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn ( name = "student_id" )
    private Student student;

    @ManyToOne
    @JoinColumn ( name = "advisor_id" )
    private Advisor advisor;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

}
