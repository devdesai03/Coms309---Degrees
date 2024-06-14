package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Instructor {
    @Id
    private Long universityId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = "universityId")
    @JsonIgnoreProperties(value = {"instructor"}, allowSetters = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private IsuRegistration isuRegistration;

    @ManyToOne
    @JoinColumn(name = "instructorDepartmentId")
    private Department instructorDepartment;

    public Instructor() {
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
