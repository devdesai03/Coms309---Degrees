package com.example.degrees;

/**
 * Represents a course rating entity, containing information about a course's rating,
 * including the course name, instructor's name, rating value, and review text.
 */
public class CourseRating {
    private String courseName;
    private String instructorName;
    private float rating;
    private String reviewText;

    /**
     * Constructs a CourseRating object with specified attributes.
     * @param courseName The name of the course
     * @param instructorName The name of the instructor
     * @param rating The numerical rating value given to the course
     * @param reviewText The text containing the review of the course
     */
    public CourseRating(String courseName, String instructorName, float rating, String reviewText) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    /**
     * Retrieves the name of the course.
     * @return The name of the course
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Retrieves the name of the instructor.
     * @return The name of the instructor
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Retrieves the rating value of the course.
     * @return The numerical rating value of the course
     */
    public float getRating() {
        return rating;
    }

    /**
     * Retrieves the review text of the course.
     * @return The text containing the review of the course
     */
    public String getReviewText() {
        return reviewText;
    }
}
