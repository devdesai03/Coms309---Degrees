package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@ApiModel ( description = "An advisor object. " +
        "It contains fields for advisor-specific information, " +
        "such as their department, along with a field containing the " +
        "associated ISURegistration object." )
@ToString
@EqualsAndHashCode
@Setter
@Getter
public class Advisor {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @ApiModelProperty ( "The advisor's university ID." )
    private Long advisorId;

    @OneToOne ( cascade = { CascadeType.PERSIST } )
    @MapsId
    @JoinColumn ( name = "universityId" )
    @JsonIgnoreProperties ( value = { "advisor" }, allowSetters = true )
    @ApiModelProperty ( "The advisor's ISU Registration data." )
    @OnDelete ( action = OnDeleteAction.CASCADE )
    private IsuRegistration isuRegistration;

    @ApiModelProperty ( "The advisor's department." )
    @ManyToOne
    @JoinColumn ( name = "advisorDepartmentId" )
    private Department advisorDepartment;

    public Advisor(Department advisorDepartment) {
        this.advisorDepartment = advisorDepartment;
    }

    public Advisor() {
    }


    public IsuRegistration getIsuRegistration() {
        return isuRegistration;
    }

    public void setIsuRegistration(IsuRegistration isuRegistration) {
        this.isuRegistration = isuRegistration;
        this.advisorId = isuRegistration.getUniversityId();
    }

    public Long getAdvisorId() {
        return advisorId;
    }

    public Department getAdvisorDepartment() {
        return advisorDepartment;
    }

    public void setAdvisorDepartment(Department advisorDepartment) {
        this.advisorDepartment = advisorDepartment;
    }
}
