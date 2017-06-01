package com.snowsea.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lecturer {

    public class AssignedSubject {

        @SerializedName("course_number")
        @Expose
        private Integer courseNumber;
        @SerializedName("level_number")
        @Expose
        private Integer levelNumber;
        @SerializedName("subject_number")
        @Expose
        private Integer subjectNumber;

        public Integer getCourseNumber() {
            return courseNumber;
        }

        public void setCourseNumber(Integer courseNumber) {
            this.courseNumber = courseNumber;
        }

        public Integer getLevelNumber() {
            return levelNumber;
        }

        public void setLevelNumber(Integer levelNumber) {
            this.levelNumber = levelNumber;
        }

        public Integer getSubjectNumber() {
            return subjectNumber;
        }

        public void setSubjectNumber(Integer subjectNumber) {
            this.subjectNumber = subjectNumber;
        }

    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("subjects")
    @Expose
    private ArrayList<Subject> assignedSubjects = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }

    public void setPaidSubjects(ArrayList<Subject> assignedSubjects) {
        this.assignedSubjects = assignedSubjects;
    }

}