package degreesapp.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlannedCourse {
    private Course course;
    private List<Prerequisites> missingPrerequisites;
    private int semesterNumber;
}