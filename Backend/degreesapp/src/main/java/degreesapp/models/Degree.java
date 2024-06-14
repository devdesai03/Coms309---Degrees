package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a degree, such as "Computer science BA".
 *
 * @author Ellie Chen
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "suffix"}))
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ApiModel(description = "A possible degree. A degree can generally serve as " +
        "either a major or minor for a student.\n" +
        "A degree is uniquely identified by its name combined with its suffix, such as \"Computer Science, BA\".")
public class Degree {
    @Id
    @GeneratedValue
    @Setter
    /**
     * The unique ID of the degree.
     */
    @ApiModelProperty("The unique ID of the degree.")
    private Long id;

    /**
     * The degree name, such as "Computer Science".
     */
    @NotNull
    @Setter
    @ApiModelProperty("The degree name, such as \"Computer Science\".")
    private String name;

    /**
     * The degree suffix, such as "MA" or "BA" or "MD".
     */
    @NotNull
    @Setter
    @ApiModelProperty("The degree suffix, such as \"MA\" or \"BA\" or \"MD\".")
    private String suffix;

    /**
     * The department offering the degree.
     */
    @ManyToOne
    @Setter
    @ApiModelProperty("The department offering the degree.")
    private Department department;

    public String toString() {
        return name + ", " + suffix;
    }

    // Back-references
    @OneToMany(mappedBy = "degree")
    @JsonIgnoreProperties(value = "degree")
    @ApiModelProperty("A list of requirements of the degree.")
    private List<DegreeRequirement> requirements;
}
