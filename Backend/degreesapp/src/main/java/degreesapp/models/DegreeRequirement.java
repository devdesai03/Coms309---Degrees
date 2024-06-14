package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(description = "A relationship that links a requirement group to a degree, " +
        "indicating that the requirement group needs to be fulfilled to get the degree.")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DegreeRequirement {
    @Embeddable
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Value
    @ApiModel(description = "The unique ID of a degree requirement. " +
            "Consists of a requirement group ID and degree ID.")
    public static class Key implements Serializable {
        @ApiModelProperty("The requirement group ID.")
        private String groupId;
        @ApiModelProperty("The degree ID.")
        private Long degreeId;
    }

    @EmbeddedId
    @ApiModelProperty("The unique ID.")
    private Key id = new Key();
    public void setId(Key key) {
        if (key.getGroupId() == null) {
            this.group = null;
        } else {
            this.group = new RequirementGroup();
            this.group.setId(key.getGroupId());
        }
        if (key.getDegreeId() == null) {
            this.degree = null;
        } else {
            this.degree = new Degree();
            this.degree.setId(key.getDegreeId());
        }
        this.id = key;
    }

    @MapsId("groupId")
    @ManyToOne
    @ApiModelProperty("The ID of the requirement group.")
    RequirementGroup group;
    public void setGroup(RequirementGroup group) {
        id = new Key(group.getId(), id.getDegreeId());
        this.group = group;
    }

    @MapsId("degreeId")
    @ManyToOne
    @JsonIgnoreProperties(value = {"requirements"}, allowSetters = true)
    @ApiModelProperty("The degree.")
    Degree degree;
    public void setDegree(Degree degree) {
        id = new Key(id.getGroupId(), degree.getId());
        this.degree = degree;
    }

    @Setter
    @ApiModelProperty("If non-null, then the requirement group will appear " +
            "as a box on the flowchart in the corresponding semester row.")
    private Integer recommendedSemester;
}
