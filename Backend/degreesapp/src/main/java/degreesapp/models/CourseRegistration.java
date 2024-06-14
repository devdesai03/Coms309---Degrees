package degreesapp.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CourseRegistration {
    @EmbeddedId
    private RegistrationId id;

    @ManyToOne
    @MapsId ( "universityId" )
    @JsonIgnoreProperties ( value = { "courseRegistrations" } )
    @OnDelete ( action = OnDeleteAction.CASCADE )
    private Student student;

    @ManyToOne
    @MapsId ( "sectionId" )
    @JsonIgnoreProperties ( value = { "registrations" } )
    @OnDelete ( action = OnDeleteAction.CASCADE )
    private CourseSection section;

    private BigDecimal grade; // Grade as a number (you can change the data type if needed)

    // Additional field for credit hours if needed
    private Double creditHours;
}
