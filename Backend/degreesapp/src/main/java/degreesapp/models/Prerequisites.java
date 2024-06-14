package degreesapp.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Prerequisites {
    @EmbeddedId
    private PrerequisitesKey id;

    @ManyToOne
    @MapsId ( "preCourseId" ) // Map to preCourseId in the composite key
    private Course preCourse;

    @ManyToOne
    @MapsId ( "postCourseId" ) // Map to postCourseId in the composite key
    private Course postCourse;

    private String description;

    private double minimumGrade;

}
