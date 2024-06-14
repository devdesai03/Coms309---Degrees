package degreesapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrerequisitesKey implements Serializable {
    @Column ( name = "pre_course_id" )
    private Long preCourseId;

    @Column ( name = "post_course_id" )
    private Long postCourseId;


    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        PrerequisitesKey that = ( PrerequisitesKey ) o;
        return Objects.equals(preCourseId , that.preCourseId) &&
                Objects.equals(postCourseId , that.postCourseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preCourseId , postCourseId);
    }

}