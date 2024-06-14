package degreesapp.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseRatingKey implements Serializable {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        CourseRatingKey that = ( CourseRatingKey ) o;
        return Objects.equals(student , that.student) &&
                Objects.equals(course , that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student , course);
    }
}
