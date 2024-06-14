package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class RequirementFulfillment {
    @Embeddable
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Value
    public static class Key implements Serializable {
        private String groupId;
        private Long courseId;
    }

    @EmbeddedId
    private Key id = new Key();
    public void setId(Key key) {
        if (key.getGroupId() == null) {
            this.group = null;
        } else {
            this.group = new RequirementGroup();
            this.group.setId(key.getGroupId());
        }
        if (key.getCourseId() == null) {
            this.course = null;
        } else {
            this.course = new Course();
            this.course.setCourseId(key.getCourseId());
        }
        this.id = key;
    }

    @ManyToOne
    @MapsId("groupId")
    @JsonIgnoreProperties(value = {"fulfillments"}, allowSetters = true)
    private RequirementGroup group;
    public void setGroup(RequirementGroup group) {
        id = new Key(group.getId(), id.getCourseId());
        this.group = group;
    }

    @ManyToOne
    @MapsId("courseId")
    private Course course;
    public void setCourse(Course course) {
        id = new Key(id.getGroupId(), course.getId());
        this.course = course;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal minimumGrade;
}
