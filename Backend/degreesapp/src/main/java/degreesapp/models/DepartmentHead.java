package degreesapp.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@ApiModel(description = "The department head.")
public class DepartmentHead {
    @Id
    @ApiModelProperty("The department head's university ID.")
    private Long universityId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = "universityId")
    @ApiModelProperty("The department head's ISU registration information.")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private IsuRegistration isuRegistration;

    public DepartmentHead() {
    }

    /*
    There is no setUniversityId() method because
    setting the universityId field directly will cause
    a malformed object.
    Always use the setIsuRegistration() method instead.
     */
    public Long getUniversityId() {
        return this.universityId;
    }

    public IsuRegistration getIsuRegistration() {
        return this.isuRegistration;
    }

    public void setIsuRegistration(IsuRegistration isuRegistration) {
        this.isuRegistration = isuRegistration;
        this.universityId = isuRegistration.getUniversityId();
    }
}
