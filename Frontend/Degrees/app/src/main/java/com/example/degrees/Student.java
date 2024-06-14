package com.example.degrees;

public class Student {
    private final String netId;
    private final String fullName;
    private String grade;
    private final long universityId;


    public Student(long universityId, String netId, String fullName, String grade) {
        this.netId = netId;
        this.fullName = fullName;
        this.grade = grade;
        this.universityId = universityId;
    }

    public String netId() {
        return netId;
    }

    public long universityId() {
        return universityId;
    }
    public String getFullName() {
        return fullName;
    }

    public String getGrade() {
        return grade;
    }

    // Getters and setters
}