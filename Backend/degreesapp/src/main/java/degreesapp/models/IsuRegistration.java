package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @author Mohammed Abdalgader
 */

@Entity
public class IsuRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long universityId;

    @OneToOne // (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties(value = {"isuRegistration"}, allowSetters = true)
    private User user;
    private String netId;

    private String givenName;
    private String middleName;
    private String surname;

    /* Back references */
    @OneToOne(mappedBy = "isuRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"isuRegistration"}, allowSetters = true)
    private Student student;

    @OneToOne(mappedBy = "isuRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"isuRegistration"}, allowSetters = true)
    private Instructor instructor;

    @OneToOne(mappedBy = "isuRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"isuRegistration"}, allowSetters = true)
    private Advisor advisor;

    @OneToOne(mappedBy = "isuRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"isuRegistration"}, allowSetters = true)
    private DepartmentHead departmentHead;


    /* Methods */
    public IsuRegistration() {
    }

    public IsuRegistration(Long universityId,User user, String netId) {
        this.universityId = universityId;
        this.netId = netId;
        this.user = user;

    }


    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long userId) {
        this.universityId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNetId() {
        return netId;
    }


    public void setNetId(String userName) {
        this.netId = userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullNameFriendly() {
        if (surname == null)
            return null;
        if (givenName == null)
            return null;
        return givenName + " " + surname;
//        if (middleName == null) {
//            return givenName + " " + surname;
//        } else {
//            return givenName + " " + middleName + " " + surname;
//        }
    }

    public String getFullName() {
        if (surname == null)
            return null;
        if (givenName == null)
            return surname;
        if (middleName == null) {
            return surname + ", " + givenName;
        } else {
            return surname + ", " + givenName + " " + middleName;
        }
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        if (student != null)
            student.setIsuRegistration(this);
        this.student = student;
    }

    public Instructor getInstructor() {
        return this.instructor;
    }

    public void setInstructor(Instructor instructor) {
        if (instructor != null)
            instructor.setIsuRegistration(this);
        this.instructor = instructor;
    }

    public Advisor getAdvisor() {
        return this.advisor;
    }

    public void setAdvisor(Advisor advisor) {
        if (advisor != null)
            advisor.setIsuRegistration(this);
        this.advisor = advisor;
    }

    public DepartmentHead getDepartmentHead() {
        return this.departmentHead;
    }

    public void setDepartmentHead(DepartmentHead departmentHead) {
        if (departmentHead != null)
            departmentHead.setIsuRegistration(this);
        this.departmentHead = departmentHead;
    }
}
