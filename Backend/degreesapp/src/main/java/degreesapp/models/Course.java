package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Entity
@ApiModel(description = "A course, like COM S 309. Not to be confused with `CourseSection`.")
public class Course {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @ApiModelProperty("A unique ID representing the course. The ID should not be shown to" +
            " users because it has no meaning.")
    private Long courseId;
    @ApiModelProperty("The course's name, such as \"Software Development Practices\".")
    private String courseName;
    @ManyToOne
    @JoinColumn ( name = "courseDepartmentId" )
    @ApiModelProperty("A Department object representing the course's department, such as " +
            "the Department of Computer Science. This object contains the course's " +
            "department code, like the \"COM S\" in \"COM S 309\".")
    private Department courseDepartment;
    @ApiModelProperty("A string that, combined with the course's department code, uniquely " +
            "identifies the course. For example, the \"309\" in \"COM S 309\" is the course's number. " +
            "The course number is not necessarily a valid integer string; for example, \"WISE 201X\" " +
            "has a course number of \"201X\".")
    private String courseNumber;
    @ApiModelProperty("A detailed description of the course.")
    private String courseDescription;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @ApiModelProperty("A decimal number representing the number of credit hours earned by " +
            "taking this course. This is always a valid decimal number.")
    private BigDecimal courseCreditHours;

    public Course() {
    }

    public Course(Long courseId,
                  String courseName,
                  Department courseDepartment,
                  String courseNumber,
                  String courseDescription,
                  BigDecimal courseCreditHours) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDepartment = courseDepartment;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.courseCreditHours = courseCreditHours;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Department getCourseDepartment() {
        return this.courseDepartment;
    }

    public void setCourseDepartment(Department courseDepartment) {
        this.courseDepartment = courseDepartment;
    }

    public String getCourseNumber() {
        return this.courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public BigDecimal getCourseCreditHours() {
        return this.courseCreditHours;
    }

    public void setCourseCreditHours(BigDecimal courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public Long getId() {
        return this.courseId;
    }

    public static Comparator<? super String> alphanumericComparator() {
        return new AlphanumericComparator();
    }

    public static Comparator<? super Course> courseComparator() {
        return Comparator
                .comparing((Course c) -> c.getCourseDepartment().getDepartmentCode())
                .thenComparing(Course::getCourseNumber , alphanumericComparator());
    }
}

/**
 * An alphanumeric comparator.
 * This is to simultaneously order course numbers as numbers,
 * so "42" goes before "100", for example,
 * while simultaneously allowing letters,
 * like "201A" goes before "201B".
 */
class AlphanumericComparator implements Comparator<String> {
    @Override
    public int compare(String lhs , String rhs) {
        for (
                int lhsOffset = 0, rhsOffset = 0;
                lhsOffset < lhs.length() && rhsOffset < rhs.length();
        ) {
            int lhsCodePoint = lhs.codePointAt(lhsOffset);
            int rhsCodePoint = rhs.codePointAt(rhsOffset);
            System.out.println(new String(Character.toChars(lhsCodePoint)) + " " + new String(Character.toChars(rhsCodePoint)));

            int lhsDigit = Character.digit(lhsCodePoint , 10);
            int rhsDigit = Character.digit(rhsCodePoint , 10);

            if ( lhsDigit == -1 || rhsDigit == -1 ) {
                int compareResult = Integer.compare(lhsCodePoint , rhsCodePoint);
                if ( compareResult != 0 ) {
                    return compareResult;
                }
                lhsOffset += Character.charCount(lhsCodePoint);
                rhsOffset += Character.charCount(rhsCodePoint);
            } else {
                BigInteger lhsValue = BigInteger.valueOf(0);
                while ( lhsDigit != -1 ) {
                    lhsValue = lhsValue.multiply(BigInteger.valueOf(10)).add(BigInteger.valueOf(lhsDigit));
                    lhsOffset += Character.charCount(lhsCodePoint);
                    if ( lhsOffset >= lhs.length() ) {
                        break;
                    }
                    lhsCodePoint = lhs.codePointAt(lhsOffset);
                    lhsDigit = Character.digit(lhsCodePoint , 10);
                }
                BigInteger rhsValue = BigInteger.valueOf(0);
                while ( rhsDigit != -1 ) {
                    rhsValue = rhsValue.multiply(BigInteger.valueOf(10)).add(BigInteger.valueOf(rhsDigit));
                    rhsOffset += Character.charCount(rhsCodePoint);
                    if ( rhsOffset >= rhs.length() ) {
                        break;
                    }
                    rhsCodePoint = rhs.codePointAt(rhsOffset);
                    rhsDigit = Character.digit(rhsCodePoint , 10);
                }
                System.out.println(lhsValue + " AND " + rhsValue);
                int compareResult = lhsValue.compareTo(rhsValue);
                if ( compareResult != 0 ) {
                    return compareResult;
                }
            }
        }
        return lhs.compareTo(rhs);
    }
}