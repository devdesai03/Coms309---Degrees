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
public class MinorRegistration {
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
    private Key minorRegistrationId = new Key();
    public void setMinorRegistrationId(MinorRegistration.Key minorRegistrationId) {
        this.minorRegistrationId = minorRegistrationId;
        if (minorRegistrationId == null) {
            if (this.student != null && this.student.getUniversityId() != null) {
                this.student = null;
            }
            if (this.degree != null && this.degree.getId() != null) {
                this.degree = null;
            }
        } else {
            if (this.student == null || !Objects.equals(this.student.getUniversityId(), minorRegistrationId.getUniversityId())) {
                if (minorRegistrationId.getUniversityId() != null) {
                    Student student = new Student();
                    student.setUniversityId(minorRegistrationId.getUniversityId());
                    this.student = student;
                } else {
                    this.student = null;
                }
            }
            if (this.degree == null || !Objects.equals(this.degree.getId(), minorRegistrationId.getDegreeId())) {
                if (minorRegistrationId.getDegreeId() != null) {
                    Degree degree = new Degree();
                    degree.setId(minorRegistrationId.getDegreeId());
                    this.degree = degree;
                } else {
                    this.degree = null;
                }
            }
        }
    }

    @ManyToOne
    @MapsId("universityId")
    @JoinColumn(name = "universityId")
    @JsonIgnoreProperties(value = {"minors"}, allowSetters = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Student student;
    public void setStudent(Student student) {
        this.student = student;
        if (student == null || student.getUniversityId() == null) {
            if (minorRegistrationId == null) {
                return;
            } else {
                this.minorRegistrationId = new MinorRegistration.Key(null, this.minorRegistrationId.getDegreeId());
            }
        } else {
            if (minorRegistrationId == null) {
                this.minorRegistrationId = new MinorRegistration.Key(student.getUniversityId(), null);
            } else {
                this.minorRegistrationId = new MinorRegistration.Key(student.getUniversityId(), minorRegistrationId.getDegreeId());
            }
        }
    }

    @ManyToOne
    @MapsId("degreeId")
    @JoinColumn(name = "degreeId")
    @Getter
    private Degree degree;
    public void setDegree(Degree degree) {
        this.degree = degree;
        if (degree == null || degree.getId() == null) {
            if (minorRegistrationId == null) {
                return;
            } else {
                this.minorRegistrationId = new MinorRegistration.Key(this.minorRegistrationId.getUniversityId(), null);
            }
        } else {
            if (minorRegistrationId == null) {
                this.minorRegistrationId = new MinorRegistration.Key(null, degree.getId());
            } else {
                this.minorRegistrationId = new MinorRegistration.Key(minorRegistrationId.getUniversityId(), degree.getId());
            }
        }
    }
}
