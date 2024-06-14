package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequirementGroup {
    @Id
    @Setter
    private String id;

    @Setter
    private String name;

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties(value = {"group"}, allowSetters = true)
    private List<RequirementFulfillment> fulfillments;
}
