package com.example.degrees;

public class CourseSection {
    private long id;
    private String courseShortName;
    private String courseLongName;
    private String semester;
    private String sectionIdentifier;
    private String location;
    private String startDate;
    private String endDate;
    private String daysOfWeek;
    private String startTime;
    private String endTime;

    public CourseSection(long id, String courseShortName, String courseLongName, String semester, String sectionIdentifier, String location, String startDate, String endDate, String daysOfWeek, String startTime, String endTime) {
        this.id = id;
        this.courseShortName = courseShortName;
        this.courseLongName = courseLongName;
        this.semester = semester;
        this.sectionIdentifier = sectionIdentifier;
        this.location = location;
        this.startTime = startTime;
        this.startDate = startDate;
        this.endTime = endTime;
        this.endDate = endDate;
        this.daysOfWeek = daysOfWeek;
    }

    public String getCourseShortName() {
        return courseShortName;
    }

    public String getCourseLongName() {
        return courseLongName;
    }

    public String getSectionIdentifier() {
        return sectionIdentifier;
    }

    public Object getId() {
        return id;
    }
}
