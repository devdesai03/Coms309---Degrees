package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MajorRegistration {
    @Embeddable
    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Key implements Serializable {
        private Long universityId;
        private Long degreeId;
    }

    @EmbeddedId
    @Getter
    private Key majorRegistrationId = new Key();
    public void setMajorRegistrationId(Key majorRegistrationId) {
        this.majorRegistrationId = majorRegistrationId;
        if (majorRegistrationId == null) {
            if (this.student != null && this.student.getUniversityId() != null) {
                this.student = null;
            }
            if (this.degree != null && this.degree.getId() != null) {
                this.degree = null;
            }
        } else {
            if (this.student == null || !Objects.equals(this.student.getUniversityId(), majorRegistrationId.getUniversityId())) {
                if (majorRegistrationId.getUniversityId() != null) {
                    Student student = new Student();
                    student.setUniversityId(majorRegistrationId.getUniversityId());
                    this.student = student;
                } else {
                    this.student = null;
                }
            }
            if (this.degree == null || !Objects.equals(this.degree.getId(), majorRegistrationId.getDegreeId())) {
                if (majorRegistrationId.getDegreeId() != null) {
                    Degree degree = new Degree();
                    degree.setId(majorRegistrationId.getDegreeId());
                    this.degree = degree;
                } else {
                    this.degree = null;
                }
            }
        }
        System.out.println(this);
    }

    @ManyToOne
    @MapsId("universityId")
    @JoinColumn(name = "universityId")
    @JsonIgnoreProperties(value = {"majors"}, allowSetters = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Student student;
    public void setStudent(Student student) {
        this.student = student;
        if (student == null || student.getUniversityId() == null) {
            if (majorRegistrationId == null) {
                return;
            } else {
                this.majorRegistrationId = new MajorRegistration.Key(null, this.majorRegistrationId.getDegreeId());
            }
        } else {
            if (majorRegistrationId == null) {
                this.majorRegistrationId = new MajorRegistration.Key(student.getUniversityId(), null);
            } else {
                this.majorRegistrationId = new MajorRegistration.Key(student.getUniversityId(), majorRegistrationId.getDegreeId());
            }
        }
    }

    @ManyToOne
    @MapsId("degreeId")
    @JoinColumn(name = "degreeId")
    @Getter
    @Setter
    private Degree degree;
    public void setDegree(Degree degree) {
        this.degree = degree;
        if (degree == null || degree.getId() == null) {
            if (majorRegistrationId == null) {
                return;
            } else {
                this.majorRegistrationId = new MajorRegistration.Key(this.majorRegistrationId.getUniversityId(), null);
            }
        } else {
            if (majorRegistrationId == null) {
                this.majorRegistrationId = new MajorRegistration.Key(null, degree.getId());
            } else {
                this.majorRegistrationId = new MajorRegistration.Key(majorRegistrationId.getUniversityId(), degree.getId());
            }
        }
    }
}
