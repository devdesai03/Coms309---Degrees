package degreesapp.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ApiModel(description = "A course rating.")
public class CourseRating {

    @EmbeddedId
    @ApiModelProperty("The compound primary key of this course rating.")
    private CourseRatingKey id;

    @ManyToOne
    @Nullable
    @ApiModelProperty("The instructor who taught the course section that the student was in.")
    @OnDelete(action = OnDeleteAction.CASCADE) // TODO: make SET_NULL
    private Instructor instructor;

    @Temporal ( TemporalType.TIMESTAMP )
    @ApiModelProperty("The date the review was written.")
    private Date dateWritten;

    @ApiModelProperty("The rating of the course, from 1-5.")
    private int rating;

    @Lob
    @ApiModelProperty("The rating's text contents, if the user wrote text in their review. " +
            "Only reviews with text should be individually displayed.")
    private String reviewText;

}
