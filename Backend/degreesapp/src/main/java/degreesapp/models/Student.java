package degreesapp.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@ApiModel ( description = "Stores information about a student. " +
        "This is modeled as an object containing data specific to a student, " +
        "combined with a reference to an ISU Registration object that contains " +
        "non-student-specific information." )
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Student {
    @Id
    @ApiModelProperty ( "The university ID; this is the primary key." )
    private Long universityId;

    @OneToOne ( cascade = { CascadeType.PERSIST , CascadeType.MERGE } )
    @MapsId
    @JoinColumn ( name = "universityId" )
    @JsonIgnoreProperties ( value = { "student" }, allowSetters = true )
    @ApiModelProperty ( "The ISU registration information associated with this student." )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private IsuRegistration isuRegistration;

    @ManyToOne
    @Getter
    @Setter
    private Advisor studentAdvisor;

    // Back references
    @OneToMany ( mappedBy = "student", cascade = { CascadeType.PERSIST , CascadeType.REMOVE } )
    @JsonIgnoreProperties ( value = { "student" } )
    @ApiModelProperty ( "The student's registered majors." )
    private List<MajorRegistration> majorRegistrations = new ArrayList<>();

    @OneToMany ( mappedBy = "student", cascade = { CascadeType.PERSIST , CascadeType.REMOVE } )
    @JsonIgnoreProperties ( value = { "student" } )
    @ApiModelProperty ( "The student's registered minors." )
    private List<MinorRegistration> minorRegistrations = new ArrayList<>();

    // Methods

    public Student() {
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

    public List<MajorRegistration> getMajorRegistrations() {
        return this.majorRegistrations;
    }

    public void setMajorRegistrations(List<MajorRegistration> majorRegistrations) {
        this.majorRegistrations = majorRegistrations;
    }

    public List<Degree> getMajors() {
        return this.majorRegistrations.stream().map(MajorRegistration::getDegree).collect(Collectors.toList());
    }

    public List<MinorRegistration> getMinorRegistrations() {
        return this.minorRegistrations;
    }

    public void setMinorRegistrations(List<MinorRegistration> minorRegistrations) {
        this.minorRegistrations = minorRegistrations;
    }

    public List<Degree> getMinors() {
        return this.minorRegistrations.stream().map(MinorRegistration::getDegree).collect(Collectors.toList());
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;

    }

    public Advisor getStudentAdvisor() {
        return studentAdvisor;
    }

}
