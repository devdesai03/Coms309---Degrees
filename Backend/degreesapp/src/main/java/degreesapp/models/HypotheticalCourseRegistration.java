package degreesapp.models;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class HypotheticalCourseRegistration {
    @EmbeddedId
    private HypotheticalCourseRegistrationKey id;

    private int semesterNumber;

}
