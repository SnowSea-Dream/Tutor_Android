package com.snowsea.accountingtutors.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Student {

    public class PaidSubject {

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

    PaidSubject getPaidSubjectObject() {
        return new PaidSubject();
    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("paid_subjects")
    @Expose
    private ArrayList<PaidSubject> paidSubjects = null;

    public void addPaidSubject(Subject subject) {
        PaidSubject newPaid = new PaidSubject();
        newPaid.setCourseNumber(subject.getCourseNumber());
        newPaid.setLevelNumber(subject.getLevelNumber());
        newPaid.setSubjectNumber(subject.getNumber());
        paidSubjects.add(newPaid);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<PaidSubject> getPaidSubjects() {
        return paidSubjects;
    }

    public void setPaidSubjects(ArrayList<PaidSubject> paidSubjects) {
        this.paidSubjects = paidSubjects;
    }

}